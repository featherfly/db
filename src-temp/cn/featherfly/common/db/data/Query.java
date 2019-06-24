
package cn.featherfly.common.db.data;

/**
 * <p>
 * Query
 * </p>
 * 
 * @author zhongj
 */
public interface Query {
	/**
	 * 返回查询名称.
	 * @return name
	 */
	String getName();
	/**
	 * 返回sql
	 * @return sql
	 */
	String getSql();
	/**
	 * 返回params
	 * @return params
	 */
	Object[] getParams();
}
