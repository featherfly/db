
package cn.featherfly.common.db.builder;
import static org.testng.Assert.*;

import org.testng.annotations.Test;

/**
 * <p>
 * SqlBuilderTest
 * </p>
 * 
 * @author 钟冀
 */
public class SqlBuilderTest {

    @Test
    public void test1() {
        SqlBuilder builder = new SqlBuilder("user");
        builder.select("id", "name", "sex");
        String select = "select id,name,sex from user";
        assertEquals(select, builder.build());
    }
    @Test
    public void test2() {
        SqlBuilder builder = new SqlBuilder("user", "u");
        builder.select("id", "name", "sex");
        String select = "select u.id,u.name,u.sex from user u";
        assertEquals(select, builder.build());
    }
    @Test
    public void test3() {
        SqlBuilder builder = new SqlBuilder("user", "u");
        String select = "select u.id,u.name,u.sex from user u where u.sex = ?";
        builder.select("id", "name", "sex")
                .eq("sex", "male");
        assertEquals(select, builder.build());
    }
}
