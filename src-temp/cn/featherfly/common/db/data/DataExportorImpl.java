
/**
 * @author 钟冀 - yufei
 *		 	Mar 12, 2009
 */
package cn.featherfly.common.db.data;

import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import cn.featherfly.common.bean.BeanUtils;
import cn.featherfly.common.db.JdbcUtils;
import cn.featherfly.common.db.builder.ConditionBuilder;
import cn.featherfly.common.db.data.query.TableQuery;
import cn.featherfly.common.db.metadata.DatabaseMetadata;
import cn.featherfly.common.db.metadata.TableMetadata;
import cn.featherfly.common.lang.AssertIllegalArgument;
import cn.featherfly.common.lang.ClassUtils;
import cn.featherfly.common.lang.LangUtils;

/**
 * <p>
 * 数据导出为xml
 * </p>
 *
 * @author 钟冀
 *
 */
public class DataExportorImpl extends AbstractDataImpExp implements DataExportor{
	
	private Class<?> formatType;
	
	/**
	 * 
	 * @param formatType formatType
	 */
	public DataExportorImpl(Class<?> formatType) {
		AssertIllegalArgument.isNotNull(formatType, "formatType不能为空");
		AssertIllegalArgument.isTrue(ClassUtils.isParent(DataFormat.class, formatType)
				, "formatType必须是DataFormat子类或子接口");
		this.formatType = formatType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportDatabase(Writer writer) {
		Collection<String> tableNames = new ArrayList<String>();
		DatabaseMetadata databaseMetadata = getDatabaseMetadata();
		Collection<TableMetadata> tableMetadatas = databaseMetadata.getTables();
		for (TableMetadata tableMetadata : tableMetadatas) {
			tableNames.add(tableMetadata.getName());
		}
		exportTable(writer, tableNames);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportTable(String tableName, Writer writer) {
		exportTable(new TableQuery(tableName), writer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportTable(Writer writer, Collection<String> tableNames) {
		if (LangUtils.isEmpty(tableNames)) {
			tableNames = new ArrayList<String>();
		}
		Query[] querySqls = new Query[tableNames.size()];
		int i = 0;
		for (String tableName : tableNames) {
			querySqls[i] = new TableQuery(tableName);
			i++;
		}
		exportData(writer, querySqls);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportTable(Writer writer, String...tableNames) {
		exportTable(writer, Arrays.asList(tableNames));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportTable(TableQuery tableQuery, Writer writer) {
		exportData(tableQuery, writer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportTables(Writer writer, TableQuery...querys) {
		exportData(writer, querys);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportTables(Writer writer, Collection<TableQuery> querys) {
		exportTables(writer, querys.toArray(new TableQuery[] {}));
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
		try {
			DatabaseMetadata databaseMetadata = getDatabaseMetadata();
			DataFormat dataFormat = createDataFormat();
			dataFormat.writeDataStart(databaseMetadata);
			for (String querySql : querySqls) {
				exportData(querySql, null);
			}
			dataFormat.writeDataEnd(databaseMetadata);
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
		try {
			DatabaseMetadata databaseMetadata = getDatabaseMetadata();
			DataFormat dataFormat = createDataFormat();
			dataFormat.writeDataStart(databaseMetadata);
			for (Query query : querys) {
				exportData(query, dataFormat);
			}
			dataFormat.writeDataEnd(databaseMetadata);
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
	
	private void exportData(Query query, DataFormat dataFormat) throws Exception{
		//得到字段信息
		Connection conn = getDataSource().getConnection();
		PreparedStatement prep = conn.prepareStatement(query.getSql());
		if (LangUtils.isNotEmpty(query.getParams())) {
			JdbcUtils.setParameters(prep, query.getParams());
		}
		ResultSet res = prep.executeQuery();
		ResultSetMetaData rsmd = res.getMetaData();
		String name = query.getName();
		if (LangUtils.isEmpty(name)) {
			name = rsmd.getTableName(1);
			if (LangUtils.isEmpty(name)) {
				throw new ExportException("自动获取表名称失败，当前数据库驱动不支持从结果集获取表名称！");
			} else {
				logger.debug("自动从结果集第一列获取表名称：{}", name);
			}
		}
		TableMetadata tableMetadata = getDatabaseMetadata().getTable(name);
		
		dataFormat.writeTableStart(tableMetadata);
		while(res.next()){
			dataFormat.writeRow(tableMetadata, res);			
		}
		
		dataFormat.writeTableEnd(tableMetadata);

		JdbcUtils.closeQuietly(conn, prep, res);
	}

	// ********************************************************************
	//	property
	// ********************************************************************
	
	private DataFormat createDataFormat() {
		return (DataFormat) BeanUtils.instantiateClass(formatType);
	}
}