package cn.featherfly.common.db.builder;

import java.util.ArrayList;
import java.util.List;

import cn.featherfly.common.db.operator.OrderOperator;

/**
 * <p>
 * 排序建造者
 * </p>
 *
 * @author 钟冀
 */
public class OrderBuilderImpl implements OrderBuilder {

	/**
	 */
	public OrderBuilderImpl() {
	}

	/*
	 * 排序参数
	 */
	private List<Order> orderParams = new ArrayList<Order>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderBuilder asc(String...names) {
		if (names != null) {
			for (String name : names) {
				addOrderBy(name, OrderOperator.ASC, false);
			}
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderBuilder desc(String...names) {
		if (names != null) {
			for (String name : names) {
				addOrderBy(name, OrderOperator.DESC, false);
			}
		}
		return this;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderBuilder ascFirst(String...names) {
		if (names != null) {
			for (String name : names) {
				addOrderBy(name, OrderOperator.ASC, true);
			}
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderBuilder descFirst(String...names) {
		if (names != null) {
			for (String name : names) {
				addOrderBy(name, OrderOperator.DESC, true);
			}
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderBuilder clearOrders() {
		orderParams.clear();
		return this;
	}

	/**
	 * <p>
	 * 构建排序字符串
	 * </p>
	 * @return 排序字符串
	 */
	public String build() {
		StringBuilder sb = new StringBuilder();
		if (orderParams.size() > 0) {
			sb.append(" ORDER BY ");
		}
		for (Order orderParam : orderParams) {
			sb.append(orderParam).append(",");
		}
		if (orderParams.size() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.getClass().getName() + " : " + build();
	}

	// ********************************************************************
	//	private method
	// ********************************************************************

	private void addOrderBy(String propertyName, OrderOperator orderOperator, boolean first) {
		Order orderParam = null;
		if (orderParams.size() > 0) {
			orderParam = orderParams.get(orderParams.size() - 1);
			if (!orderParam.isOrderOperator(orderOperator)) {
				orderParam = new Order(orderOperator);
				if (first) {
					orderParams.add(0, orderParam);
				} else {
					orderParams.add(orderParam);
				}
			}
		} else {
			orderParam = new Order(orderOperator);
			if (first) {
				orderParams.add(0, orderParam);
			} else {
				orderParams.add(orderParam);
			}
		}
		orderParam.addParam(propertyName);
	}

	// ********************************************************************
	//	内部类
	// ********************************************************************

	/**
	 * <p>
	 * 排序参数辅助对象
	 * </p>
	 *
	 * @author 钟冀
	 */
	public static class Order {

		/**
		 * @param orderOperator orderOperator
		 */
		public Order(OrderOperator orderOperator) {
			this.orderOperator = orderOperator;
		}

		private OrderOperator orderOperator;

		private List<String> params = new ArrayList<String>();

		/**
		 * <p>
		 * 添加排序参数
		 * </p>
		 * @param param 排序参数
		 */
		public void addParam(String param) {
			params.add(param);
		}

		/**
		 * 返回是否是传入的操作
		 * @param orderOperator orderOperator
		 * @return 是否是传入的操作
		 */
		public boolean isOrderOperator(OrderOperator orderOperator) {
			return this.orderOperator == orderOperator;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (String param : params) {
				sb.append(" ").append(param).append(",");
			}
			if (params.size() > 0) {
				sb.deleteCharAt(sb.length() - 1);
				sb.append(" ").append(orderOperator.toString());
			}
			return sb.toString();
		}
	}

}