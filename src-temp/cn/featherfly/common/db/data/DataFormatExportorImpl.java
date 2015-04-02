
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

import cn.featherfly.common.db.JdbcUtils;
import cn.featherfly.common.db.builder.ConditionBuilder;
import cn.featherfly.common.db.data.query.TableQuery;
import cn.featherfly.common.db.metadata.DatabaseMetadata;
import cn.featherfly.common.db.metadata.TableMetadata;
import cn.featherfly.common.lang.LangUtils;

/**
 * <p>
 * 数据导出为xml
 * </p>
 *
 * @author 钟冀
 *
 */
public class DataFormatExportorImpl extends AbstractDataImpExp implements DataFormatExportor{
	/**
	 */
	public DataFormatExportorImpl() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportDatabase(Writer writer, DataFormat dataFormat) {
		Collection<String> tableNames = new ArrayList<String>();
		DatabaseMetadata databaseMetadata = getDatabaseMetadata();
		Collection<TableMetadata> tableMetadatas = databaseMetadata.getTables();
		for (TableMetadata tableMetadata : tableMetadatas) {
			tableNames.add(tableMetadata.getName());
		}
		exportTable(tableNames, writer, dataFormat);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportTable(String tableName, Writer writer, DataFormat dataFormat) {
		exportData(new TableQuery(tableName), writer, dataFormat);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportTable(Collection<String> tableNames, Writer writer, DataFormat dataFormat) {
		if (LangUtils.isEmpty(tableNames)) {
			tableNames = new ArrayList<String>();
		}
		Query[] querySqls = new Query[tableNames.size()];
		int i = 0;
		for (String tableName : tableNames) {
			querySqls[i] = new TableQuery(tableName);
			i++;
		}
		exportData(writer, dataFormat, querySqls);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportData(String querySql, Writer writer, DataFormat dataFormat) {
		exportData(writer, dataFormat, new String[]{querySql});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportData(Collection<String> querySqls, Writer writer, DataFormat dataFormat) {
		exportData(writer, dataFormat, querySqls.toArray(new String[]{}));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportData(Writer writer, DataFormat dataFormat, String... querySqls) {
		try {
			DatabaseMetadata databaseMetadata = getDatabaseMetadata();
			dataFormat.writeDataStart(databaseMetadata);
			for (String querySql : querySqls) {
				exportData(querySql, null, dataFormat);
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
	public void exportData(String tableName, ConditionBuilder conditionBuilder, Writer os, DataFormat dataFormat) {
		String querySql = getTableQuerySql(tableName);
		if (conditionBuilder != null) {
			querySql+= " " + conditionBuilder.build();
		}
		exportData(querySql, os, dataFormat);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportData(Writer writer, DataFormat dataFormat, Collection<Query> querys) {
		try {
			DatabaseMetadata databaseMetadata = getDatabaseMetadata();
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
	public void exportData(Writer writer, DataFormat dataFormat, Query...querys) {
		exportData(writer, dataFormat, Arrays.asList(querys));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportData(Query query, Writer writer, DataFormat dataFormat) {
		exportData(writer, dataFormat, query);
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
		
		dataFormat.writeRow(tableMetadata, res);
		
		dataFormat.writeTableEnd(tableMetadata);

		JdbcUtils.closeQuietly(conn, prep, res);
	}

	// ********************************************************************
	//	property
	// ********************************************************************
}