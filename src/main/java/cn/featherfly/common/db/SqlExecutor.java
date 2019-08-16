
package cn.featherfly.common.db;

import java.io.File;
import java.io.IOException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.featherfly.common.constant.Charset;
import cn.featherfly.common.db.wrapper.ConnectionWrapper;
import cn.featherfly.common.db.wrapper.StatementWrapper;
import cn.featherfly.common.lang.AssertIllegalArgument;
import cn.featherfly.common.lang.LangUtils;

/**
 * <p>
 * SqlExecutor
 * </p>
 * <p>
 * 2019-08-16
 * </p>
 *
 * @author zhongj
 */
public class SqlExecutor {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String END_SQL_SIGN = ";";

    private DataSource dataSource;

    /**
     * @param dataSource
     */
    public SqlExecutor(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
    }

    /**
     * read sql from file with UTF-8 and execute
     *
     * @param sqlFile
     * @throws IOException
     */
    public void execute(File sqlFile) throws IOException {
        execute(sqlFile, Charset.UTF_8);
    }

    /**
     * read sql from file with assgin encoding and execute
     *
     * @param sqlFile
     * @param encoding
     * @throws IOException
     */
    public void execute(File sqlFile, String encoding) throws IOException {
        AssertIllegalArgument.isExists(sqlFile, "sqlFile");
        String content = org.apache.commons.io.FileUtils.readFileToString(sqlFile, encoding);
        String[] sqls = content.split(END_SQL_SIGN);
        execute(sqls);
    }

    /**
     * read sql from string sqlContent
     *
     * @param sqlContent
     */
    public void execute(String sqlContent) {
        String[] sqls = sqlContent.split(END_SQL_SIGN);
        execute(sqls);
    }

    /**
     * execute sqls
     *
     * @param sqls
     */
    public void execute(String[] sqls) {
        try (ConnectionWrapper connection = JdbcUtils.getConnectionWrapper(dataSource);
                StatementWrapper statement = new StatementWrapper(connection.createStatement(), connection)) {
            for (String sql : sqls) {
                sql = sql.trim();
                if (LangUtils.isNotEmpty(sql)) {
                    logger.debug("add sql -> " + sql);
                    statement.addBatch(sql);
                }
            }
            statement.executeBatch();
        }
    }

}
