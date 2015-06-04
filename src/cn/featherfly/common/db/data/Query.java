
package cn.featherfly.common.db.data;


/**
 * <p>
 * Query
 * </p>
 * 
 * @author 钟冀
 */
public interface Query extends Execution{
	/**
	 * 返回查询名称.
	 * @return name
	 */
	String getName();
}
