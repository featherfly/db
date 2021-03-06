
package cn.featherfly.common.db.builder;

import cn.featherfly.common.db.operator.QueryOperator;


/**
 * <p>
 * 表达式建造者
 * </p>
 *
 * @author zhongj
 */
public interface ExpressionBuilder extends Builder{
	/**
	 * 添加条件
	 * @param name 参数名称
	 * @param value 参数值
	 * @param queryOperator 运算符
	 * @return LogicBuilder
	 */
	LogicBuilder add(String name, Object value, QueryOperator queryOperator);
	/**
	 * 小于
	 * @param name 参数名称
	 * @param value 参数值
	 * @return LogicBuilder
	 */
	LogicBuilder lt(String name, Object value) ;
	/**
	 * 小于等于
	 * @param name 参数名称
	 * @param value 参数值
	 * @return LogicBuilder
	 */
	LogicBuilder le(String name, Object value) ;
	/**
	 * 等于
	 * @param name 参数名称
	 * @param value 参数值
	 * @return LogicBuilder
	 */
	LogicBuilder eq(String name, Object value) ;
	/**
	 * 不等于
	 * @param name 参数名称
	 * @param value 参数值
	 * @return LogicBuilder
	 */
	LogicBuilder ne(String name, Object value) ;
	/**
	 * 大于等于
	 * @param name 参数名称
	 * @param value 参数值
	 * @return LogicBuilder
	 */
	LogicBuilder ge(String name, Object value) ;
	/**
	 * 大于
	 * @param name 参数名称
	 * @param value 参数值
	 * @return LogicBuilder
	 */
	LogicBuilder gt(String name, Object value) ;
	/**
	 * 以XX开始
	 * @param name 参数名称
	 * @param value 参数值
	 * @return LogicBuilder
	 */
	LogicBuilder sw(String name, Object value) ;
	/**
	 * 包含
	 * @param name 参数名称
	 * @param value 参数值
	 * @return LogicBuilder
	 */
	LogicBuilder co(String name, Object value) ;
	/**
	 * 以XX结尾
	 * @param name 参数名称
	 * @param value 参数值
	 * @return LogicBuilder
	 */
	LogicBuilder ew(String name, Object value) ;
	/**
	 * 包含指定，sql中的in
	 * @param name 参数名称
	 * @param value 参数值
	 * @return LogicBuilder
	 */
	LogicBuilder in(String name, Object value);
	/**
	 * 不包含指定，sql中的not in
	 * @param name 参数名称
	 * @param value 参数值
	 * @return LogicBuilder
	 */
	LogicBuilder nin(String name, Object value);
	/**
	 * 为null的
	 * @param name 参数名称
	 * @return LogicBuilder
	 */
	LogicBuilder isn(String name) ;
	/**
	 * 不为null的
	 * @param name 参数名称
	 * @return LogicBuilder
	 */
	LogicBuilder inn(String name) ;
//	/**
//	 * between xx and yy
//	 * @param name 参数名称
//	 * @param minValue between第一个参数
//	 * @param maxValue between第二个参数
//	 * @return LogicBuilder
//	 */
//	LogicBuilder bt(String name, Object minValue, Object maxValue);

//	/**
//	 * 返回上级表达式组
//	 * @return parent
//	 */
//	LogicBuilder getParent();
	/**
	 * <p>
	 * 条件逻辑组
	 * </p>
	 * @return 新条件逻辑组
	 */
	ExpressionBuilder group();
}
