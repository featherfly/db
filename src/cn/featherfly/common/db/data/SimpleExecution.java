
package cn.featherfly.common.db.data;

import cn.featherfly.common.lang.AssertIllegalArgument;


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
public class SimpleExecution implements Execution{
	
	private String sql;

	private Object[] params;

	/**
	 * @param sql sql
	 * @param params params
	 */
	public SimpleExecution(String sql, Object...params) {
	    AssertIllegalArgument.isNotEmpty(sql, "sql不能为空");
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
}
