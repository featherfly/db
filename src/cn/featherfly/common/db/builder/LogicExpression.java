
package cn.featherfly.common.db.builder;

import cn.featherfly.common.db.operator.LogicOperator;

/**
 * <p>
 * 条件逻辑
 * </p>
 *
 * @author zhongj
 */
public class LogicExpression implements Expression{

	private LogicOperator logicOperator = LogicOperator.AND;

	public LogicExpression() {
	}

	/**
	 * @param logicOperator 逻辑运算符
	 */
	public LogicExpression(LogicOperator logicOperator) {
		if (logicOperator != null) {
			this.logicOperator = logicOperator;
		}
	}

	/**
	 * 返回logicOperator
	 * @return logicOperator
	 */
	public LogicOperator getLogicOperator() {
		return logicOperator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String build() {
		return logicOperator.toString();
	}

}
