
package cn.featherfly.common.db.builder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.featherfly.common.db.operator.LogicOperator;
import cn.featherfly.common.db.operator.QueryOperator;
import cn.featherfly.common.lang.LangUtils;
import cn.featherfly.common.lang.StringUtils;

/**
 * <p>
 * 条件逻辑组建造者
 * </p>
 *
 * @author zhongj
 */
public class ConditionGroup implements ExpressionBuilder, LogicBuilder , ParamedExpression{

	/**
	 * @param parent 上级组
	 */
	ConditionGroup(ConditionGroup parent) {
		this.parent = parent;
		this.queryAlias = parent.queryAlias;
	}
	/**
	 * @param parent
	 * @param queryAlias
	 */
	ConditionGroup(String queryAlias) {
		this.queryAlias = queryAlias;
	}

	/**
	 * <p>
	 * and 条件
	 * </p>
	 * @return this
	 */
	@Override
	public ExpressionBuilder and() {
		addCondition(new LogicExpression(LogicOperator.AND));
		return this;
	}
	/**
	 * <p>
	 * or 条件
	 * </p>
	 * @return this
	 */
	@Override
	public ExpressionBuilder or() {
		addCondition(new LogicExpression(LogicOperator.OR));
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LogicBuilder add(String name, Object value,
			QueryOperator queryOperator) {
		addCondition(new ConditionExpression(name, queryAlias, value, queryOperator));
		return this;
	}

	/**
	 * <p>
	 * 小于
	 * </p>
	 * @param name 参数名称
	 * @param value 参数值
	 * @return this
	 */
	@Override
	public LogicBuilder lt(String name, Object value) {
		addCondition(new ConditionExpression(name, queryAlias, value, QueryOperator.lt));
		return this;
	}
	/**
	 * 小于等于
	 * @param name 参数名称
	 * @param value 参数值
	 * @return this
	 */
	@Override
	public LogicBuilder le(String name, Object value) {
		addCondition(new ConditionExpression(name, queryAlias, value, QueryOperator.le));
		return this;
	}
	/**
	 * 等于
	 * @param name 参数名称
	 * @param value 参数值
	 * @return this
	 */
	@Override
	public LogicBuilder eq(String name, Object value) {
		addCondition(new ConditionExpression(name, queryAlias, value, QueryOperator.eq));
		return this;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LogicBuilder ne(String name, Object value) {
		addCondition(new ConditionExpression(name, queryAlias, value, QueryOperator.ne));
		return this;
	}
	/**
	 * 大于等于
	 * @param name 参数名称
	 * @param value 参数值
	 * @return this
	 */
	@Override
	public LogicBuilder ge(String name, Object value) {
		addCondition(new ConditionExpression(name, queryAlias, value, QueryOperator.ge));
		return this;
	}
	/**
	 * 大于
	 * @param name 参数名称
	 * @param value 参数值
	 * @return this
	 */
	@Override
	public LogicBuilder gt(String name, Object value) {
		addCondition(new ConditionExpression(name, queryAlias, value, QueryOperator.gt));
		return this;
	}
	/**
	 * 以XX开始
	 * @param name 参数名称
	 * @param value 参数值
	 * @return this
	 */
	@Override
	public LogicBuilder sw(String name, Object value) {
		addCondition(new ConditionExpression(name, queryAlias, value, QueryOperator.sw));
		return this;
	}
	/**
	 * 包含
	 * @param name 参数名称
	 * @param value 参数值
	 * @return this
	 */
	@Override
	public LogicBuilder co(String name, Object value) {
		addCondition(new ConditionExpression(name, queryAlias, value, QueryOperator.co));
		return this;
	}
	/**
	 * 以XX结尾
	 * @param name 参数名称
	 * @param value 参数值
	 * @return this
	 */
	@Override
	public LogicBuilder ew(String name, Object value) {
		addCondition(new ConditionExpression(name, queryAlias, value, QueryOperator.ew));
		return this;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LogicBuilder in(String name, Object value) {
		addCondition(new ConditionExpression(name, queryAlias, value, QueryOperator.in));
		return this;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LogicBuilder nin(String name, Object value) {
		addCondition(new ConditionExpression(name, queryAlias, value, QueryOperator.nin));
		return this;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LogicBuilder isn(String name) {
		addCondition(new ConditionExpression(name, queryAlias, null, QueryOperator.isn));
		return this;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LogicBuilder inn(String name) {
		addCondition(new ConditionExpression(name, queryAlias, null, QueryOperator.inn));
		return this;
	}

	/**
	 * <p>
	 * 条件逻辑组
	 * </p>
	 * @return 新条件逻辑组
	 */
	@Override
	public ExpressionBuilder group() {
		ConditionGroup group = new ConditionGroup(this);
		addCondition(group);
		return group;
	}

	/**
	 * 返回parent
	 * @return parent
	 */
	@Override
	public LogicBuilder getParent() {
		return parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String build() {
		StringBuilder result = new StringBuilder();
		if (conditions.size() > 0) {
			Expression last = conditions.get(conditions.size() - 1);
			if (last instanceof LogicExpression) {
				throw new BuilderException(((LogicExpression) last).getLogicOperator() + " 后没有跟条件表达式");
			}
		}

		List<String> availableConditions = new ArrayList<String>();
		List<Expression> availableExpressions = new ArrayList<Expression>();
		for (Expression expression : conditions) {
			String condition = expression.build();
			if (StringUtils.isNotBlank(condition)) {
				availableConditions.add(condition);
				availableExpressions.add(expression);
			} else {
				if (availableExpressions.size() > 0) {
					Expression pre = availableExpressions.get(availableExpressions.size() - 1);
					if (pre instanceof LogicExpression) {
						availableExpressions.remove(availableExpressions.size() - 1);
						availableConditions.remove(availableConditions.size() - 1);
					}
				}
			}
		}

		if (availableExpressions.size() > 0) {
			if (availableExpressions.get(0) instanceof LogicExpression) {
				availableExpressions.remove(0);
				availableConditions.remove(0);
			}
		}

		for (String condition : availableConditions) {
			ConditionBuildUtils.appendCondition(result, condition);
		}
		if (result.length() > 0 && parent != null) {
			return " ( " + result.toString() + " ) ";
		} else {
			return result.toString();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getParamValue() {
		List<Object> params = new ArrayList<Object>();
		for (Expression condition : conditions) {
			if (condition instanceof ParamedExpression) {
				Object param = ((ParamedExpression) condition).getParamValue();
				if (LangUtils.isNotEmpty(param)) {
					if (param instanceof Collection) {
						params.addAll((Collection<Object>) param);
					} else if (param.getClass().isArray()){
						int length = Array.getLength(param);
						for (int i = 0; i < length; i++) {
							params.add(Array.get(param, i));
						}
					} else {
						params.add(param);
					}
				}
			}
		}
		return params;
	}

	// ********************************************************************
	//	private method
	// ********************************************************************

	private void addCondition(Expression condition) {
		if (previousCondition != null) {
			if (previousCondition.getClass().isInstance(condition)) {
				throw new BuilderException("语法错误，连续相同类型的表达式：" + condition.getClass().getName());
			}
		}
		previousCondition = condition;
		this.conditions.add(condition);
	}

	// ********************************************************************
	//	property
	// ********************************************************************

	private List<Expression> conditions = new ArrayList<Expression>();

	private ConditionGroup parent;

	private Expression previousCondition;

	private String queryAlias;

	/*
	 * 忽略空值
	 */
	private boolean ignoreEmpty = true;

	/**
	 * 返回ignoreEmpty
	 * @return ignoreEmpty
	 */
	public boolean isIgnoreEmpty() {
		return ignoreEmpty;
	}
	/**
	 * 设置ignoreEmpty
	 * @param ignoreEmpty ignoreEmpty
	 */
	public void setIgnoreEmpty(boolean ignoreEmpty) {
		this.ignoreEmpty = ignoreEmpty;
	}

	/**
	 * 返回conditions
	 * @return conditions
	 */
	public List<Expression> getConditions() {
		return conditions;
	}
}
