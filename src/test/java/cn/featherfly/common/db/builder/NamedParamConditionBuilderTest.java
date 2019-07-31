//
//package cn.featherfly.common.db.builder;
//
//import cn.featherfly.common.db.operator.QueryOperator;
//
///**
// * <p>
// * 类的说明放这里
// * </p>
// * <p>
// * copyright cdthgk 2010-2020, all rights reserved.
// * </p>
// *
// * @author 钟冀
// */
//public class NamedParamConditionBuilderTest {
//	public static void main(String[] args) {
//		NamedParamConditionBuilder builder = new NamedParamConditionBuilder("User", "u");
//		builder.and("name", QueryOperator.eq, "name", "yufei")
//			.and("pwd", QueryOperator.eq, "pwd", "123")
//			.andGroup().and("sex", QueryOperator.eq, "sex", "m");
//
//		System.out.println(builder);
//		System.out.println(builder.getParams());
//
//		builder = new NamedParamConditionBuilder("Account", "acc");
//		builder.andGroup();
//		builder.andGroup();
//
//		System.out.println(builder);
//		System.out.println(builder.getParams());
//
//		builder = new NamedParamConditionBuilder("Account", "acc");
//		builder.andGroup();
//		builder.andGroup().and("sex", QueryOperator.eq, "sex", "m");
//
//		System.out.println(builder);
//		System.out.println(builder.getParams());
//
//		builder = new NamedParamConditionBuilder("Account", "acc");
//		builder.andGroup().and("sex", QueryOperator.eq, "sex", "m");
//		builder.andGroup();
//
//		System.out.println(builder);
//		System.out.println(builder.getParams());
//
//		builder = new NamedParamConditionBuilder("Account", "acc");
//		builder.andGroup().and("sex", QueryOperator.eq, "sex", "m");
//		builder.andGroup().and("sex", QueryOperator.eq, "sex", "m");
//
//		System.out.println(builder);
//		System.out.println(builder.getParams());
//
//		builder = new NamedParamConditionBuilder("User", "u");
//		builder.and("name", QueryOperator.eq, "name", "yufei")
//			.andGroup().and("sex", QueryOperator.eq, "sex", "m")
//			.orGroup().and("school", QueryOperator.co, "school", "学校");
//		builder.or("pwd", QueryOperator.eq, "pwd", "123");
//
//		System.out.println(builder);
//		System.out.println(builder.getParams());
//	}
//}
