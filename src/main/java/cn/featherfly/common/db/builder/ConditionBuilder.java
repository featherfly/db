
package cn.featherfly.common.db.builder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.featherfly.common.db.dialect.Dialect;
import cn.featherfly.common.db.operator.QueryOperator;
import cn.featherfly.common.lang.CollectionUtils;
import cn.featherfly.common.lang.StringUtils;
import cn.featherfly.common.structure.page.Limit;
import cn.featherfly.common.structure.page.Pagination;

/**
 * <p>
 * 带参数条件查询建造者，例如（ name like ?）
 * </p>
 *
 * @author zhongj
 */
public class ConditionBuilder implements ExpressionBuilder, OrderBuilder {

    private ConditionGroup conditionGroup;

    private OrderBuilderImpl orderBuilder;

    /**
     */
    public ConditionBuilder() {
        this(null);
    }

    /**
     * @param queryAlias 查询对象别名
     */
    public ConditionBuilder(String queryAlias) {
        conditionGroup = new ConditionGroup(queryAlias);
        orderBuilder = new OrderBuilderImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String build() {
        StringBuilder result = new StringBuilder();
        String condition = conditionGroup.build();
        if (StringUtils.isNotBlank(condition)) {
            if (buildWithWhere) {
                ConditionBuildUtils.appendCondition(result, "where");
            }
            ConditionBuildUtils.appendCondition(result, condition);
        }
        ConditionBuildUtils.appendCondition(result, orderBuilder.build());

        if (pagination != null) {
            if (dialect == null) {
                throw new BuilderException("需要分页时，dialect不能为空");
            }
            Limit limit = new Limit(pagination);
            return dialect.getPaginationSql(result.toString(), limit.getOffset(), limit.getLimit()).trim();
        } else {
            return result.toString().trim();
        }
    }
    //	/**
    //	 * <p>
    //	 * 创建查询对象
    //	 * </p>
    //	 * @return 查询对象
    //	 */
    //	public Query buildQuery() {
    //		StringBuilder result = new StringBuilder();
    //		String condition = conditionGroup.build();
    //		if (StringUtils.isNotBlank(condition)) {
    //			if (buildWithWhere) {
    //				ConditionBuildUtils.appendCondition(result, "where");
    //			}
    //			ConditionBuildUtils.appendCondition(result, condition);
    //		}
    //		ConditionBuildUtils.appendCondition(result, orderBuilder.build());
    //
    //		if (this.pagination != null) {
    //			AssertStandardSys.isNotEmpty(this.dialect, "需要分页时，dialect不能为空");
    //			PaginationWrapper<Object> pw = new PaginationWrapper<Object>(pagination);
    //			int start = pw.getStart();
    //			int limit = pw.getLimit();
    //			Object[] params = dialect.getPaginationSqlParameter(getParams().toArray(), start, limit);
    //			getParams().clear();
    //			List<Object> resultParams = getParams();
    //			CollectionUtils.addAll(resultParams, params);
    //			dialect.get
    //			return new SimpleQuery(dialect.getPaginationSql(result.toString(), start, limit), getParams().toArray());
    //		} else {
    //			return  result.toString();
    //		}
    //
    //		if (this.pagination != null) {
    //			AssertStandardSys.isNotEmpty(this.dialect, "需要分页时，dialect不能为空");
    //			PaginationWrapper<Object> pw = new PaginationWrapper<Object>(pagination);
    //			int start = pw.getStart();
    //			int limit = pw.getLimit();
    //			Object[] params = dialect.getPaginationSqlParameter(getParams().toArray(), start, limit);
    //			getParams().clear();
    //			CollectionUtils.addAll(getParams(), params);
    //			return new SimpleQuery(build(), getParams().toArray());
    //		} else {
    //			return new SimpleQuery(build(), getParams().toArray());
    //		}
    //	}

    //	/**
    //	 * {@inheritDoc}
    //	 */
    //	@Override
    //	public LogicBuilder getParent() {
    //		return conditionGroup.getParent();
    //	}

    /**
     * {@inheritDoc}
     */
    @Override
    public ExpressionBuilder group() {
        return conditionGroup.group();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogicBuilder lt(String name, Object value) {
        return conditionGroup.lt(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogicBuilder le(String name, Object value) {
        return conditionGroup.le(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogicBuilder eq(String name, Object value) {
        return conditionGroup.eq(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogicBuilder ne(String name, Object value) {
        return conditionGroup.ne(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogicBuilder ge(String name, Object value) {
        return conditionGroup.ge(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogicBuilder gt(String name, Object value) {
        return conditionGroup.gt(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogicBuilder sw(String name, Object value) {
        return conditionGroup.sw(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogicBuilder co(String name, Object value) {
        return conditionGroup.co(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogicBuilder ew(String name, Object value) {
        return conditionGroup.ew(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogicBuilder in(String name, Object value) {
        return conditionGroup.in(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogicBuilder nin(String name, Object value) {
        return conditionGroup.nin(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogicBuilder isn(String name) {
        return conditionGroup.isn(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogicBuilder inn(String name) {
        return conditionGroup.inn(name);
    }

    /**
     * <p>
     * 获取查询参数
     * </p>
     *
     * @return 查询参数
     */
    @SuppressWarnings("unchecked")
    public List<Object> getParams() {
        List<Object> result = new ArrayList<>();
        Object param = conditionGroup.getParamValue();
        if (param == null) {
            result.add(param);
        } else if (param instanceof Collection) {
            result.addAll((Collection<Object>) param);
        } else if (param.getClass().isArray()) {
            int length = Array.getLength(param);
            for (int i = 0; i < length; i++) {
                result.add(Array.get(param, i));
            }
        }
        if (pagination != null) {
            if (dialect == null) {
                throw new BuilderException("需要分页时，dialect不能为空");
            }
            Limit limit = new Limit(pagination);
            Object[] params = dialect.getPaginationSqlParameter(result.toArray(), limit.getOffset(), limit.getLimit());
            result.clear();
            CollectionUtils.addAll(result, params);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderBuilder asc(String... names) {
        return orderBuilder.asc(names);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderBuilder desc(String... names) {
        return orderBuilder.desc(names);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderBuilder ascFirst(String... names) {
        return orderBuilder.ascFirst(names);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderBuilder descFirst(String... names) {
        return orderBuilder.descFirst(names);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderBuilder clearOrders() {
        return orderBuilder.clearOrders();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogicBuilder add(String name, Object value, QueryOperator queryOperator) {
        return conditionGroup.add(name, value, queryOperator);
    }

    // ********************************************************************
    //	property
    // ********************************************************************

    private boolean buildWithWhere;

    private Dialect dialect;

    private Pagination pagination;

    /**
     * 返回dialect
     *
     * @return dialect
     */
    public Dialect getDialect() {
        return dialect;
    }

    /**
     * 设置dialect
     *
     * @param dialect dialect
     */
    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    /**
     * 返回buildWithWhere
     *
     * @return buildWithWhere
     */
    public boolean isBuildWithWhere() {
        return buildWithWhere;
    }

    /**
     * 设置buildWithWhere
     *
     * @param buildWithWhere buildWithWhere
     */
    public void setBuildWithWhere(boolean buildWithWhere) {
        this.buildWithWhere = buildWithWhere;
    }

    /**
     * 返回pagination
     *
     * @return pagination
     */
    public Pagination getPagination() {
        return pagination;
    }

    /**
     * 设置pagination
     *
     * @param pagination pagination
     */
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
