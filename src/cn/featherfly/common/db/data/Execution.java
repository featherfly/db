
package cn.featherfly.common.db.data;

/**
 * <p>
 * Statement
 * </p>
 * 
 * @author 钟冀
 */
public interface Execution {
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
