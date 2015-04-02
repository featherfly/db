
package cn.featherfly.common.db.data.query;

import cn.featherfly.common.db.builder.ConditionBuilder;
import cn.featherfly.common.db.data.Query;

/**
 * <p>
 * TableQuery
 * </p>
 * 
 * @author 钟冀
 */
public class TableQuery implements Query{

	/**
	 * @param tableName tableName
	 */
	public TableQuery(String tableName) {
		this(tableName, null);
	}
	/**
	 * @param tableName tableName
	 * @param conditionBuilder ConditionBuilder
	 */
	public TableQuery(String tableName, ConditionBuilder conditionBuilder){
		//FIXME 用断言判断tableName
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(tableName);
		if (conditionBuilder != null) {
			conditionBuilder.setBuildWithWhere(true);
			String condition = conditionBuilder.build();
			if (condition != null) {
				sql.append(" ").append(condition);
			}
		}
		this.sql = sql.toString();
		this.tableName = tableName;
	}
	
	private String tableName;
	
	private String sql;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return tableName;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSql() {
		return sql;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] getParams() {
		return null;
	}
}
