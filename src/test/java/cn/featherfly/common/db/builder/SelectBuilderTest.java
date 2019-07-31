
package cn.featherfly.common.db.builder;
import static org.testng.Assert.*;

import org.testng.annotations.Test;

/**
 * <p>
 * SelectBuilderTest
 * </p>
 * 
 * @author 钟冀
 */
public class SelectBuilderTest {

    @Test
    public void test1() {
        SelectBuilder builder = new SelectBuilder();
        builder.select("id", "name", "sex");
        builder.setBuildWithFrom(false);
        String select = "select id,name,sex";
        assertEquals(select, builder.build());
    }
    @Test
    public void test2() {
        SelectBuilder builder = new SelectBuilder();
        builder.select("id", "name", "sex");
        builder.setBuildWithFrom(false);
        builder.setAlias("u");
        String select = "select u.id,u.name,u.sex";
        assertEquals(select, builder.build());
    }
    @Test
    public void test3() {
        SelectBuilder builder = new SelectBuilder("user");
        builder.select("id", "name", "sex");
        String select = "select id,name,sex from user";
        assertEquals(select, builder.build());
    }
    @Test
    public void test4() {
        SelectBuilder builder = new SelectBuilder("user", "u");
        builder.select("id", "name", "sex");
        String select = "select u.id,u.name,u.sex from user u";
        assertEquals(select, builder.build());
    }
}
