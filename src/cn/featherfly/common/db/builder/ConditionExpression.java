
package cn.featherfly.common.db.builder;

import java.lang.reflect.Array;
import java.util.Collection;

import cn.featherfly.common.db.JdbcException;
import cn.featherfly.common.db.operator.QueryOperator;
import cn.featherfly.common.lang.LangUtils;

/**
 * <p>
 * 条件表达式
 * </p>
 *
 * @author 钟冀
 */
public class ConditionExpression implements ParamedExpression{

	private String name;

	private Object value;

	private String queryAlias;

	private Object paramValue;

	private QueryOperator queryOperator;

	/**
	 * @param name 名称
	 * @param value 值
	 * @param queryOperator 查询运算符（查询类型）
	 */
	ConditionExpression(String name, Object value, QueryOperator queryOperator) {
		this(name, null, value , queryOperator);
	}
	/**
	 * @param name 名称
	 * @param queryAlias 查询别名
	 * @param value 值
	 * @param queryOperator 查询运算符（查询类型）
	 */
	ConditionExpression(String name, String queryAlias, Object value, QueryOperator queryOperator) {
		if (queryOperator == null) {
			throw new JdbcException("#query.operator.null");
//			throw new JdbcException("queryOperator不能为空");
		}
		this.name = name;
		this.value = value;
		this.queryAlias = queryAlias;
		this.queryOperator = queryOperator;
		if (LangUtils.isNotEmpty(value)) {
			switch (queryOperator) {
				case sw:
					paramValue = value + "%"; break;
				case co:
					paramValue = "%" + value + "%"; break;
				case ew:
					paramValue = "%" + value; break;
				default:
					paramValue = value;
			}
		}
	}

	/**
	 * 返回name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 返回queryOperator
	 * @return queryOperator
	 */
	public QueryOperator getQueryOperator() {
		return queryOperator;
	}

	/**
	 * 返回alias
	 * @return alias
	 */
	public String getQueryAlias() {
		return queryAlias;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getParamValue() {
		return paramValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String build() {
		StringBuilder condition = new StringBuilder();
		if (LangUtils.isNotEmpty(queryAlias)) {
			 condition.append(queryAlias).append(".");
		}
		if (LangUtils.isEmpty(name)) {
			return "";
		} else if (QueryOperator.isn == queryOperator || QueryOperator.inn == queryOperator) {
			condition.append(name)
					.append(" ")
					.append(queryOperator.toOperator());
		} else if (LangUtils.isNotEmpty(value)) {
			if (QueryOperator.in == queryOperator || QueryOperator.nin == queryOperator) {
				int length = 1;
				if (value instanceof Collection) {
					length = ((Collection<?>) value).size();
				} else if (value.getClass().isArray()){
					length = Array.getLength(value);
				}
				condition.append(name)
					.append(" ")
					.append(queryOperator.toOperator())
					.append(" (");
				for (int i = 0; i < length; i++) {
					if (i > 0) {
						condition.append(",");
					}
					condition.append("?");
				}
				condition.append(")");
			} else {
				condition.append(name)
					.append(" ")
					.append(queryOperator.toOperator())
					.append(" ?");;
			}
		} else {
			return "";
		}
		return condition.toString();
	}
}
