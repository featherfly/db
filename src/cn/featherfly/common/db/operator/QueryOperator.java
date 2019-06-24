
package cn.featherfly.common.db.operator;

/**
 * <p>
 * 操作符号
 * </p>
 *
 * @author zhongj
 */
public enum QueryOperator {
    
	/**
	 * 小于，符号：<
	 */
	lt("<"),
	/**
	 * 小于等于，符号：<=
	 */
	le("<="),
	/**
	 * 等于，符号：=
	 */
	eq("="),
	/**
	 * 不等于，符号：!=
	 */
	ne("!="),
	/**
	 * 大于等于，符号：>=
	 */
	ge(">="),
	/**
	 * 大于，符号：>
	 */
	gt(">"),
	/**
	 * 以XX开始，符号：like
	 */
	sw("like"),
	/**
	 * 包含，符号：like
	 */
	co("like"),
	/**
	 * 以XX结尾，符号：like
	 */
	ew("like"),
	/**
	 * 为null，符号：is null
	 */
	isn("is null"),
	/**
	 * 不为null, 符号：is not null
	 */
	inn("is not null"),
	/**
	 * in
	 */
	in("in"),
	/**
	 * not in
	 */
	nin("not in"),
//	/**
//	 * 按位与 ： &
//	 */
//	ba("&"),
//	/**
//	 * 按位或：|
//	 */
//	bo("|"),
//	/**
//	 * 按位异或:^
//	 */
//	bx("^"),
//	/**
//	 * 按位取反:~
//	 */
//	bn("~"),
//	/**
//	 * 按位左移:<<
//	 */
//	bl("<<"),
//	/**
//	 * 按位右移：>>
//	 */
//	br(">>")
	;
	
	private String operator;
	
	private QueryOperator(String operator) {
	    this.operator = operator;
	}

	/**
	 * <p>
	 * 返回操作符号的字符串表示
	 * </p>
	 * @return 操作符号的字符串表示
	 */
	public String toOperator() {
	    return this.operator;
//		String sign = "like";
//		switch (this) {
//			case lt:
//				sign = "<"; break;
//			case le:
//				sign = "<="; break;
//			case eq:
//				sign = "="; break;
//			case ne:
//				sign = "!="; break;
//			case ge:
//				sign = ">="; break;
//			case gt:
//				sign = ">"; break;
//			case sw:
//				sign = "like"; break;
//			case co:
//				sign = "like"; break;
//			case ew:
//				sign = "like"; break;
//			case isn:
//				sign = "is null"; break;
//			case inn:
//				sign = "is not null"; break;
//			case in:
//				sign = "in"; break;
//			case nin:
//				sign = "not in"; break;
//			default:
//				sign = "like";
//		}
//		return sign;
	}
}
