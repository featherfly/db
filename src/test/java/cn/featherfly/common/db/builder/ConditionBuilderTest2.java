
package cn.featherfly.common.db.builder;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

/**
 * <p>
 * 类的说明放这里
 * </p>
 * <p>
 * copyright cdthgk 2010-2020, all rights reserved.
 * </p>
 *
 * @author 钟冀
 */
public class ConditionBuilderTest2 {

	ConditionBuilder builder = null;
	ConditionBuilder sub = null;

	@Test
	public void test() {
		String sql = "u.name = ? AND u.pwd = ? AND ( u.sex = ? )";
		List<Object> params = new ArrayList<Object>();

		builder = new ConditionBuilder("u");
						
		String name = "yufei";
		String pwd = "123";
		String sex = "m";
		builder.eq("name", name)
			.and()
			.eq("pwd", pwd)
			.and()
			.group()
				.eq("sex", sex);
		params.add(name);
		params.add(pwd);
		params.add(sex);

		System.out.println(builder.build());
		System.out.println(builder.getParams());
		assertEquals(builder.build().trim(), sql);
		assertEquals(builder.getParams(), params);
	}
	@Test
	public void test1() {
		String sql = "u.pwd = ? AND ( u.sex = ? )";
		List<Object> params = new ArrayList<Object>();

		builder = new ConditionBuilder("u");
		String name = null;
		String pwd = "123";
		String sex = "m";
		builder.eq("name", name)
		.and()
		.eq("pwd", pwd)
		.and()
		.group()
		.eq("sex", sex);
//		params.add(name);
		params.add(pwd);
		params.add(sex);

		System.out.println(builder.build());
		System.out.println(builder.getParams());
		assertEquals(builder.build(), sql);
		assertEquals(builder.getParams(), params);
	}
	@Test
	public void test2() {
		String sql = "";
		List<Object> params = new ArrayList<Object>();

		builder = new ConditionBuilder("acc");
		builder.group();

		System.out.println(builder.build());
		System.out.println(builder.getParams());
		assertEquals(builder.build().trim(), sql);
		assertEquals(builder.getParams(), params);
	}
}
