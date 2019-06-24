
/**
 * @author zhongj - yufei
 *		 	Mar 12, 2009
 */
package cn.featherfly.common.db.data.sql;

import cn.featherfly.common.db.dialect.MySqlDialect;



/**
 * @author zhongj
 *
 */
public class SqlExportorMysqlImpl extends SqlExportor2 {

	/**
	 */
	public SqlExportorMysqlImpl() {
		setDialect(new MySqlDialect());
	}
}