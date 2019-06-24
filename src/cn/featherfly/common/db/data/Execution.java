
package cn.featherfly.common.db.data;

/**
 * <p>
 * Statement
 * </p>
 * 
 * @author zhongj
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
