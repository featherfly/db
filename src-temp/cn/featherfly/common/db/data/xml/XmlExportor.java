
/**
 * @author zhongj - yufei
 *		 	Mar 12, 2009
 */
package cn.featherfly.common.db.data.xml;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.dom.DOMCDATA;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.featherfly.common.constant.Charset;
import cn.featherfly.common.db.JdbcUtils;
import cn.featherfly.common.db.builder.ConditionBuilder;
import cn.featherfly.common.db.data.AbstractDataExportor;
import cn.featherfly.common.db.data.ExportException;
import cn.featherfly.common.db.data.Query;
import cn.featherfly.common.db.metadata.DatabaseMetadata;
import cn.featherfly.common.db.metadata.DatabaseMetadataManager;
import cn.featherfly.common.db.metadata.TableMetadata;
import cn.featherfly.common.lang.DateUtils;
import cn.featherfly.common.lang.LangUtils;

/**
 * <p>
 * 数据导出为xml
 * </p>
 *
 * @author zhongj
 *
 */
public class XmlExportor extends AbstractDataExportor {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportDatabase(Writer writer) {
		Collection<String> tableNames = new ArrayList<String>();
		DatabaseMetadata databaseMetadata = DatabaseMetadataManager.getDefaultManager().create(getDataSource(), getCheckedDatabase());
		Collection<TableMetadata> tableMetadatas = databaseMetadata.getTables();
		for (TableMetadata tableMetadata : tableMetadatas) {
			tableNames.add(tableMetadata.getName());
		}
		exportTable(tableNames, writer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportTable(String tableName, Writer writer) {
		exportData(getTableQuerySql(tableName), writer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportTable(Collection<String> tableNames, Writer writer) {
		if (LangUtils.isEmpty(tableNames)) {
			tableNames = new ArrayList<String>();
		}
		String[] querySqls = new String[tableNames.size()];
		int i = 0;
		for (String tableName : tableNames) {
			querySqls[i] = getTableQuerySql(tableName);
			i++;
		}
		exportData(writer, querySqls);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportData(String querySql, Writer writer) {
		exportData(writer, new String[]{querySql});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportData(Collection<String> querySqls, Writer writer) {
		exportData(writer, querySqls.toArray(new String[]{}));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportData(Writer writer, String... querySqls) {
		Document document = DocumentHelper.createDocument();
		Element tablesElement =  document.addElement("tables");
		document.setRootElement(tablesElement);
		try {
			for (String querySql : querySqls) {
				exportData(querySql, null, tablesElement);
			}
			write(document, writer);
		} catch (Exception e) {
			throw new ExportException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportData(String tableName, ConditionBuilder conditionBuilder, Writer os) {
		String querySql = getTableQuerySql(tableName);
		if (conditionBuilder != null) {
			querySql+= " " + conditionBuilder.build();
		}
		exportData(querySql, os);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportData(Writer writer, Collection<Query> querys) {
		Document document = DocumentHelper.createDocument();
		Element tablesElement =  document.addElement("tables");
		document.setRootElement(tablesElement);
		try {
			for (Query query : querys) {
				exportData(query.getSql(), null, tablesElement, query.getParams());
			}
			write(document, writer);
		} catch (Exception e) {
			throw new ExportException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportData(Writer writer, Query...querys) {
		exportData(writer, Arrays.asList(querys));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportData(Query query, Writer writer) {
		exportData(writer, query);
	}

	/**
	 * <p>
	 * 返回具体的库名称
	 * </p>
	 * @return 具体的库名称
	 */
	protected String getDatabase() {
		return JdbcUtils.getCatalog(getDataSource());
	}

	private void exportData(String querySql, String tableName, Element tablesElement, Object...params) throws SQLException{
		//得到字段信息
		Connection conn = getDataSource().getConnection();

		PreparedStatement prep = conn.prepareStatement(querySql);
		if (LangUtils.isNotEmpty(params)) {
			JdbcUtils.setParameters(prep, params);
		}
		ResultSet res = prep.executeQuery();
		ResultSetMetaData rsmd = res.getMetaData();

		if (LangUtils.isEmpty(tableName)) {
			tableName = rsmd.getTableName(1);
			if (LangUtils.isEmpty(tableName)) {
				throw new ExportException("自动获取表名称失败，当前数据库驱动不支持从结果集获取表名称！");
			} else {
				logger.debug("自动从结果集第一列获取表名称：{}", tableName);
			}
		}

		DatabaseMetaData dbmd = conn.getMetaData();
		ResultSet pkRs = dbmd.getPrimaryKeys(null, null, tableName);
		List<String> pkColumns = new ArrayList<String>();
		while(pkRs.next()){
			pkColumns.add(pkRs.getString("COLUMN_NAME"));
		}
		if (pkColumns.size() < 1) {
			throw new ExportException(tableName + "没有设置主键");
		}
		StringBuilder pkMapping = new StringBuilder();
		for (String pkColumn : pkColumns) {
			pkMapping.append(pkColumn).append(",");
		}
		if (pkMapping.length() > 0) {
			pkMapping.deleteCharAt(pkMapping.length() - 1);
		}

//		Element tableElement = tablesElement.addElement(tableName);
		Element tableElement = tablesElement.addElement("table");
		tableElement.addAttribute("name", tableName);
		tableElement.addAttribute("pkMapping", pkMapping.toString());

		int columnTotal = rsmd.getColumnCount();
		while(res.next()){
			Element row = tableElement.addElement("row");
			for (int i = 1; i <= columnTotal; i++) {
				String columnName = rsmd.getColumnName(i);
				int type = rsmd.getColumnType(i);
				Object value = res.getObject(columnName);
				Element columnElement = row.addElement(columnName);
				columnElement.addAttribute("type", type + "");
				if (value != null) {
					columnElement.addAttribute("null", "0");
					switch (type) {
					case Types.TIMESTAMP:
//						columnElement.add(new DOMCDATA(stripNonValidXMLChars(DateUtils.formartTime(res.getTimestamp(columnName)))));
						columnElement.setText(DateUtils.formartTime(res.getTimestamp(columnName)));
//						columnElement.setText(
//								getDialect().valueToSql(DateUtils.formartTime(res.getTimestamp(columnName)), type));
						break;
					default:
						columnElement.add(new DOMCDATA(stripNonValidXMLChars(value.toString())));
//						columnElement.add(new DOMCDATA(
//								getDialect().valueToSql(stripNonValidXMLChars(value.toString()), type)
//								));
//						columnElement.setText(value.toString());
						break;
					}
				} else {
					// 如何为空，则不放入xml
//					columnElement.add(new DOMCDATA(""));
					columnElement.addAttribute("null", "1");
					columnElement.setText("");
				}
			}
		}
		res.close();
		res = null;
		prep.close();
		prep = null;
		conn.close();
		conn = null;
		JdbcUtils.closeQuietly(conn, prep, res);
	}

	private String stripNonValidXMLChars(String str) {
		if (LangUtils.isEmpty(str)) {
			return str;
		}
		return str.replaceAll("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]", "");
	}

	private String getCheckedDatabase() {
		String database = getDatabase();
		if (LangUtils.isEmpty(database)) {
			database = JdbcUtils.getCatalog(getDataSource());
			if (database == null) {
				throw new ExportException("数据库连接的具体库名称不能为空！");
			}
		}
		return database;
	}


	private void write(Document document, Writer os) throws IOException {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(encoding);
		XMLWriter output = new XMLWriter(os, format);
		output.write(document);
		output.close();
	}

	// ********************************************************************
	//	property
	// ********************************************************************

	private String encoding = Charset.UTF_8;

	/**
	 * 返回encoding
	 * @return encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * 设置encoding
	 * @param encoding encoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}