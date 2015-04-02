
package cn.featherfly.common.db.builder;



/**
 * <p>
 * 条件建造者
 * </p>
 * @author 钟冀
 */
public interface Builder {
	/**
	 * <p>
	 * 创建条件语句
	 * </p>
	 * @return 条件语句
	 */
	String build();
}
