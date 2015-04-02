//
///**
// * @author 钟冀 - yufei
// *		 	Mar 12, 2009
// */
//package cn.featherfly.common.db.data.sql;
//
//import java.io.Writer;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import cn.featherfly.common.db.JdbcUtils;
//import cn.featherfly.common.db.builder.ConditionBuilder;
//import cn.featherfly.common.db.data.AbstractDataExportor;
//import cn.featherfly.common.db.data.ExportException;
//import cn.featherfly.common.db.data.Query;
//import cn.featherfly.common.db.metadata.DatabaseMetadata;
//import cn.featherfly.common.db.metadata.DatabaseMetadataManager;
//import cn.featherfly.common.db.metadata.TableMetadata;
//import cn.featherfly.common.lang.LangUtils;
//
///**
// * <p>
// * 数据导出为sql
// * </p>
// *
// * @author 钟冀
// *
// */
//public abstract class SqlExportor extends AbstractDataExportor {
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void exportDatabase(Writer writer) {
//		DatabaseMetadata databaseMetadata = DatabaseMetadataManager.getDefaultManager().create(getDataSource(), getCheckedDatabase());
//		Collection<TableMetadata> tableMetadatas = databaseMetadata.getTables();
//		try {
//			for (TableMetadata tableMetadata : tableMetadatas) {
//				writer.write(exportTable(tableMetadata.getName()));
//				writer.flush();
//			}
//			writer.close();
//		} catch (Exception e) {
//			throw new ExportException(e);
//		}
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void exportTable(String tableName, Writer writer) {
//		exportData(getTableQuerySql(tableName), writer);
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void exportTable(Collection<String> tableNames, Writer writer) {
//		if (LangUtils.isEmpty(tableNames)) {
//			tableNames = new ArrayList<String>();
//		}
//		String[] querySqls = new String[tableNames.size()];
//		int i = 0;
//		for (String tableName : tableNames) {
//			querySqls[i] = getTableQuerySql(tableName);
//			i++;
//		}
//		exportData(writer, querySqls);
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void exportData(String querySql, Writer writer) {
//		exportData(writer, new String[]{querySql});
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void exportData(Collection<String> querySqls, Writer writer) {
//		exportData(writer, querySqls.toArray(new String[]{}));
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void exportData(Writer writer, String...querySqls) {
//		try {
//			for (String querySql : querySqls) {
//				writer.write(exportSql(querySql, null));
//				writer.flush();
//			}
//			writer.close();
//		} catch (Exception e) {
//			throw new ExportException(e);
//		}
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void exportData(String tableName, ConditionBuilder conditionBuilder, Writer writer) {
//		String querySql = "select * from " + tableName;
//		if (conditionBuilder != null) {
//			querySql+= " " + conditionBuilder.build();
//		}
//		exportData(querySql, writer);
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void exportData(Writer writer, Collection<Query> querys) {
//		try {
//			for (Query query : querys) {
//				writer.write(exportSql(query.getSql(), null, query.getParams()));
//				writer.flush();
//			}
//			writer.close();
//		} catch (Exception e) {
//			throw new ExportException(e);
//		}
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void exportData(Writer writer, Query...querys) {
//		exportData(writer, Arrays.asList(querys));
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void exportData(Query query, Writer writer) {
//		exportData(writer, query);
//	}
//
//	/**
//	 * <p>
//	 * 返回具体的库名称
//	 * </p>
//	 * @return 具体的库名称
//	 */
//	protected String getDatabase() {
//		return JdbcUtils.getCatalog(getDataSource());
//	}
//
//	private String exportTable(String tableName) throws SQLException{
//		String querySql = "select * from " + tableName;
//		StringBuilder tableSql = new StringBuilder();
//		tableSql
//			.append("/*==============================================================*/\n")
//			.append("/*\t").append(tableName).append("\t*/\n")
//			.append("/*==============================================================*/\n")
//			.append(exportSql(querySql, tableName))
//			.append("\n");
//		return tableSql.toString();
//	}
//
//	private String exportSql(String querySql, String tableName, Object...params) throws SQLException{
//		Connection conn = getDataSource().getConnection();
//		PreparedStatement prep = conn.prepareStatement(querySql);
//		if (LangUtils.isNotEmpty(params)) {
//			JdbcUtils.setParameters(prep, params);
//		}
//		ResultSet res = prep.executeQuery();
//		ResultSetMetaData rsmd = res.getMetaData();
//		if (LangUtils.isEmpty(tableName)) {
//			tableName = rsmd.getTableName(1);
//			if (LangUtils.isEmpty(tableName)) {
//				throw new ExportException("自动获取表名称失败，当前数据库驱动不支持从结果集获取表名称！");
//			} else {
//				logger.debug("自动从结果集第一列获取表名称：{}", tableName);
//			}
//		}
//
//		StringBuilder sql = new StringBuilder();
//		sql
//		.append("/*==============================================================*/\n")
//		.append("/*\t").append(tableName).append("\t*/\n")
//		.append("/*\t").append(querySql).append("\t*/\n")
//		.append("/*==============================================================*/\n")
//		;
//
//
//		int columnTotal = rsmd.getColumnCount();
//		List<Map<String,Map<String, Object>>> rows = new ArrayList<Map<String,Map<String, Object>>>();
//		Map<String,Map<String, Object>> row ;
//		StringBuilder insertSql = new StringBuilder();
//		while(res.next()){
//			row = new HashMap<String, Map<String, Object>>();
//			StringBuilder columns = new StringBuilder();
//			StringBuilder values = new StringBuilder();
//			values.append(" VALUES ( ");
//			columns.append(" ( ");
//			for(int i=1;i<=columnTotal;i++){
//				Map<String, Object> column = new HashMap<String, Object>();
//				String columnName = rsmd.getColumnName(i);
//				Object value = res.getObject(columnName);
//				int type = rsmd.getColumnType(i);
//				// 列名大写
//				columns.append(getDialect().wrapName(columnName.toUpperCase())).append(",");
//				values.append(getDialect().valueToSql(value, type)).append(",");
//				column.put("name", columnName);
//				column.put("value", value);
//				column.put("type", type);
//				row.put(columnName,column);
//			}
//			rows.add(row);
//			if(columns.lastIndexOf(",")+1==columns.length()){
//				columns.deleteCharAt(columns.length()-1);
//			}
//			columns.append(" )");
//			if(values.lastIndexOf(",")+1==values.length()){
//				values.deleteCharAt(values.length()-1);
//			}
//			values.append(" )");
//			columns.append(values.toString());
//			// 表名大写
//			insertSql.append("INSERT INTO "+getDialect().wrapName(tableName.toUpperCase())+columns+";\n");
//		}
//
////		JdbcUtils.closeQuietly(conn, prep, res);
//		res.close();
//		prep.close();
//		conn.close();
//
//		sql.append(insertSql.toString())
//		.append("\n");
//		return sql.toString();
//	}
//
//	private String getCheckedDatabase() {
//		String database = getDatabase();
//		if (LangUtils.isEmpty(database)) {
//			throw new ExportException("数据库连接的具体库名称不能为空！");
//		}
//		return database;
//	}
//}