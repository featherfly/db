
/**
 * @author 钟冀 - yufei
 *		 	Mar 12, 2009
 */
package cn.featherfly.common.db.data.json;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.featherfly.common.db.JdbcUtils;
import cn.featherfly.common.db.data.AbstractDataExportor;
import cn.featherfly.common.db.data.ExportException;
import cn.featherfly.common.db.data.Query;
import cn.featherfly.common.lang.DateUtils;
import cn.featherfly.common.lang.LangUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

/**
 * <p>
 * 数据导出为xml
 * </p>
 *
 * @author 钟冀
 *
 */
public class JsonExportor2 extends AbstractDataExportor {

	private JsonFactory jsonFactory;

	/**
	 */
	public JsonExportor2() {
		jsonFactory = new JsonFactory();
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportData(Writer writer,
			Collection<Query> querys) {
		try {
			JsonGenerator generator = createJsonGenerator(writer);
			generator.writeStartArray();
			for (Query query : querys) {
				exportData(query.getSql(), null, generator, query.getParams());
			}
			generator.writeEndArray();
			generator.close();
		} catch (Exception e) {
			throw new ExportException(e);
		}
	}

	private void exportData(String querySql, String tableName, JsonGenerator generator, Object...params) throws Exception{
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

		generator.writeStartObject();

		generator.writeStringField("name", tableName);
		generator.writeStringField("pkMapping", pkMapping.toString());

		generator.writeArrayFieldStart("rows");
		int columnTotal = rsmd.getColumnCount();
		while(res.next()){
			generator.writeStartObject();
			for (int i = 1; i <= columnTotal; i++) {
				String columnName = rsmd.getColumnName(i);
				int type = rsmd.getColumnType(i);
				Object value = res.getObject(columnName);

				generator.writeObjectFieldStart(columnName);

				generator.writeStringField("type", type + "");

				if (value != null) {
					generator.writeStringField("null", "0");
					switch (type) {
					case Types.TIMESTAMP:
						generator.writeStringField("value", DateUtils.formartTime(res.getTimestamp(columnName)));
						break;
					default:
						generator.writeStringField("value", value.toString());
						break;
					}
				} else {
					generator.writeStringField("null", "1");
				}
				generator.writeEndObject();
			}
			generator.writeEndObject();
		}
		generator.writeEndArray();

		generator.writeEndObject();

		JdbcUtils.closeQuietly(conn, prep, res);
	}

	private JsonGenerator createJsonGenerator(Writer writer) {
		JsonGenerator generator;
		try {
			generator = jsonFactory.createGenerator(writer);
			return generator;
		} catch (IOException e) {
			throw new ExportException(e);
		}
	}	

	// ********************************************************************
	//	property
	// ********************************************************************
}