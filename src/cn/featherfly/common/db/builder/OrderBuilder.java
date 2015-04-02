package cn.featherfly.common.db.builder;

/**
 * <p>
 * 排序构建接口
 * </p>
 *
 * @author 钟冀
 */
public interface OrderBuilder {
	/**
	 * <p>
	 * 添加升序条件
	 * </p>
	 * @param names 名称
	 * @return this
	 */
	OrderBuilder asc(String... names);

	/**
	 * <p>
	 * 添加降序条件
	 * </p>
	 * @param names 名称
	 * @return this
	 */
	OrderBuilder desc(String... names);
	/**
	 * <p>
	 * 添加升序条件到最前面
	 * </p>
	 * @param names 名称
	 * @return this
	 */
	OrderBuilder ascFirst(String... names);
	/**
	 * <p>
	 * 添加降序条件到最前面
	 * </p>
	 * @param names 名称
	 * @return this
	 */
	OrderBuilder descFirst(String... names);
	/**
	 * <p>
	 * 删除所有排序条件
	 * </p>
	 * @return this
	 */
	OrderBuilder clearOrders();

}