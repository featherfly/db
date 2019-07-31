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
//public class ParamConditionBuilderTest {
//	public static void main(String[] args) {
//		ConditionBuilder builder = null;
//		ConditionBuilder sub = null;
//
//		builder = new ConditionBuilder("User", "u");
//		builder.and("name", QueryOperator.eq, "yufei")
//			.and("pwd", QueryOperator.eq, "123")
//			.andGroup().and("sex", QueryOperator.eq, "m");
//
//		System.out.println(builder);
//		System.out.println(builder.getParams());
//
//		builder = new ConditionBuilder("User", "u");
//		builder.andGroup().and("sex", QueryOperator.eq, "m");
//		builder.and("name", QueryOperator.eq, "yufei")
//			.and("pwd", QueryOperator.eq, "123");
//
//		System.out.println(builder);
//		System.out.println(builder.getParams());
//
//		builder = new ConditionBuilder("Account", "acc");
//		builder.andGroup();
//		builder.andGroup();
//
//		System.out.println(builder);
//		System.out.println(builder.getParams());
//		builder = new ConditionBuilder("Account", "acc");
//
//		builder.andGroup().orGroup().andGroup();
//		builder.andGroup();
//
//		System.out.println(builder);
//		System.out.println(builder.getParams());
//
//		builder = new ConditionBuilder("Account", "acc");
//		builder.andGroup();
//		builder.andGroup().and("sex", QueryOperator.eq, "m");
//
//		System.out.println(builder);
//		System.out.println(builder.getParams());
//
//		builder = new ConditionBuilder("Account", "acc");
//		builder.andGroup().and("sex", QueryOperator.eq, "m");
//		builder.andGroup();
//
//		System.out.println(builder);
//		System.out.println(builder.getParams());
//
//		builder = new ConditionBuilder("Account", "acc");
//		builder.andGroup().and("sex", QueryOperator.eq, "m");
//		builder.andGroup().and("sex", QueryOperator.eq, "m");
//
//		System.out.println(builder);
//		System.out.println(builder.getParams());
//
//		builder = new ConditionBuilder("User", "u");
//		builder.and("name", QueryOperator.eq, "yufei")
//			.andGroup().and("sex", QueryOperator.eq, "m")
//			.orGroup().and("school", QueryOperator.co, "学校");
//		builder.or("pwd", QueryOperator.eq, "123");
//
//		System.out.println(builder);
//		System.out.println(builder.getParams());
//
//		builder = new ConditionBuilder("Account", "acc");
//
//		sub = builder.andGroup();
//		sub.andGroup().and("sex", QueryOperator.eq, "m");
//		sub.andGroup().and("sex", QueryOperator.eq, "m");
//		builder.or("pwd", QueryOperator.eq, "123");
//
//		System.out.println(builder);
//		System.out.println(builder.getParams());
//
//		builder = new ConditionBuilder("Account", "acc");
//
//		builder.andGroup();
//		sub = builder.andGroup();
//		sub.andGroup().and("sex", QueryOperator.eq, "m");
//		sub.orGroup();
//		sub.andGroup().and("sex2", QueryOperator.eq, "w")
//			.orGroup().or("name", QueryOperator.eq, "yufei");
//		builder.or("pwd", QueryOperator.eq, "123456");
//		builder.or("qq", QueryOperator.eq, "11");
//
//		System.out.println(builder.build());
//		System.out.println(builder.getParams());
//	}
//}
