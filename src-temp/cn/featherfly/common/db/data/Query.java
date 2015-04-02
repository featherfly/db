
package cn.featherfly.common.db.data;

/**
 * <p>
 * Query
 * </p>
 * 
 * @author 钟冀
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
