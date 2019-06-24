
package cn.featherfly.common.db.data;


/**
 * <p>
 * Query
 * </p>
 * 
 * @author zhongj
 */
public interface Query extends Execution{
	/**
	 * 返回查询名称.
	 * @return name
	 */
	String getName();
}
