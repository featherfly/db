
package cn.featherfly.common.db.data.query;

import cn.featherfly.common.db.data.Query;


/**
 * <p>
 * 简单查询
 * </p>
 * <p>
 * copyright cdthgk 2010-2020, all rights reserved.
 * </p>
 *
 * @author 钟冀
 */
public class SimpleQuery implements Query{
	
	private String name;

	private String sql;

	private Object[] params;

	/**
	 * @param sql sql
	 * @param params params
	 */
	public SimpleQuery(String sql, Object...params) {
		this(null, sql, params);
	}
	/**
	 * @param name name
	 * @param sql sql
	 * @param params params
	 */
	public SimpleQuery(String name, String sql, Object...params) {
		// FIXME 断言判断
		this.name = name;
		this.sql = sql;
		this.params = params;
	}

	/**
	 * 返回sql
	 * @return sql
	 */
	@Override
	public String getSql() {
		return sql;
	}

	/**
	 * 返回params
	 * @return params
	 */
	@Override
	public Object[] getParams() {
		return params;
	}

	/**
	 * 返回name
	 * @return name
	 */
	@Override
	public String getName() {
		return name;
	}
}
