
package cn.featherfly.common.db.builder;


/**
 * <p>
 * 逻辑建造者
 * </p>
 *
 * @author 钟冀
 */
public interface LogicBuilder{
	/**
	 * <p>
	 * 逻辑与
	 * </p>
	 * @return ExpressionBuilder
	 */
	ExpressionBuilder and();
	/**
	 * <p>
	 * 逻辑或
	 * </p>
	 * @return ExpressionBuilder
	 */
	ExpressionBuilder or();

	/**
	 * 返回上级表达式组
	 * @return parent
	 */
	LogicBuilder getParent();
//	/**
//	 * <p>
//	 * 条件逻辑组
//	 * </p>
//	 * @return 新条件逻辑组
//	 */
//	ExpressionBuilder group();
}
