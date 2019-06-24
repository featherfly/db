
/**
 * @author zhongj - yufei
 *		 	Mar 12, 2009
 */
package cn.featherfly.common.db.data.sql;

import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.featherfly.common.db.JdbcUtils;
import cn.featherfly.common.db.data.AbstractDataExportor;
import cn.featherfly.common.db.data.ExportException;
import cn.featherfly.common.db.data.Query;
import cn.featherfly.common.lang.LangUtils;

/**
 * <p>
 * 数据导出为sql
 * </p>
 *
 * @author zhongj
 *
 */
public abstract class SqlExportor2 extends AbstractDataExportor {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportData(Writer writer, Collection<Query> querys) {
		try {
			for (Query query : querys) {
				if (query == null) continue;
				StringBuilder tableSql = new StringBuilder();
				if (LangUtils.isNotEmpty(query.getName())) {
					tableSql.append("/*==============================================================*/\n")
						.append("/*\t").append(query.getName()).append("\t*/\n")
						.append("/*==============================================================*/\n");
				}
				tableSql.append(exportSql(query.getSql(), null, query.getParams()))
						.append("\n");
				writer.write(tableSql.toString());
				writer.flush();
			}
			writer.close();
		} catch (Exception e) {
			throw new ExportException(e);
		}
	}
	
	private String exportSql(String querySql, String tableName, Object...params) throws SQLException{
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

		StringBuilder sql = new StringBuilder();
		sql
		.append("/*==============================================================*/\n")
		.append("/*\t").append(tableName).append("\t*/\n")
		.append("/*\t").append(querySql).append("\t*/\n")
		.append("/*==============================================================*/\n")
		;


		int columnTotal = rsmd.getColumnCount();
		List<Map<String,Map<String, Object>>> rows = new ArrayList<Map<String,Map<String, Object>>>();
		Map<String,Map<String, Object>> row ;
		StringBuilder insertSql = new StringBuilder();
		while(res.next()){
			row = new HashMap<String, Map<String, Object>>();
			StringBuilder columns = new StringBuilder();
			StringBuilder values = new StringBuilder();
			values.append(" VALUES ( ");
			columns.append(" ( ");
			for(int i=1;i<=columnTotal;i++){
				Map<String, Object> column = new HashMap<String, Object>();
				String columnName = rsmd.getColumnName(i);
				Object value = res.getObject(columnName);
				int type = rsmd.getColumnType(i);
				// 列名大写
				columns.append(getDialect().wrapName(columnName.toUpperCase())).append(",");
				values.append(getDialect().valueToSql(value, type)).append(",");
				column.put("name", columnName);
				column.put("value", value);
				column.put("type", type);
				row.put(columnName,column);
			}
			rows.add(row);
			if(columns.lastIndexOf(",")+1==columns.length()){
				columns.deleteCharAt(columns.length()-1);
			}
			columns.append(" )");
			if(values.lastIndexOf(",")+1==values.length()){
				values.deleteCharAt(values.length()-1);
			}
			values.append(" )");
			columns.append(values.toString());
			// 表名大写
			insertSql.append("INSERT INTO "+getDialect().wrapName(tableName.toUpperCase())+columns+";\n");
		}

//		JdbcUtils.closeQuietly(conn, prep, res);
		res.close();
		prep.close();
		conn.close();

		sql.append(insertSql.toString())
		.append("\n");
		return sql.toString();
	}	
}