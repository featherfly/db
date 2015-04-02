
package cn.featherfly.common.db.operator;

/**
 * <p>
 * 操作符号
 * </p>
 *
 * @author 钟冀
 */
public enum QueryOperator {
	/**
	 * 小于，符号：<
	 */
	lt,
	/**
	 * 小于等于，符号：<=
	 */
	le,
	/**
	 * 等于，符号：=
	 */
	eq,
	/**
	 * 不等于，符号：!=
	 */
	ne,
	/**
	 * 大于等于，符号：>=
	 */
	ge,
	/**
	 * 大于，符号：>
	 */
	gt,
	/**
	 * 以XX开始，符号：like
	 */
	sw,
	/**
	 * 包含，符号：like
	 */
	co,
	/**
	 * 以XX结尾，符号：like
	 */
	ew,
	/**
	 * 为null，符号：is null
	 */
	isn,
	/**
	 * 不为null, 符号：is not null
	 */
	inn,
	/**
	 * in
	 */
	in,
	/**
	 * not in
	 */
	nin;

	/**
	 * <p>
	 * 返回操作符号的字符串表示
	 * </p>
	 * @return 操作符号的字符串表示
	 */
	public String toOperator() {
		String sign = "like";
		switch (this) {
			case lt:
				sign = "<"; break;
			case le:
				sign = "<="; break;
			case eq:
				sign = "="; break;
			case ne:
				sign = "!="; break;
			case ge:
				sign = ">="; break;
			case gt:
				sign = ">"; break;
			case sw:
				sign = "like"; break;
			case co:
				sign = "like"; break;
			case ew:
				sign = "like"; break;
			case isn:
				sign = "is null"; break;
			case inn:
				sign = "is not null"; break;
			case in:
				sign = "in"; break;
			case nin:
				sign = "not in"; break;
			default:
				sign = "like";
		}
		return sign;
	}
}
