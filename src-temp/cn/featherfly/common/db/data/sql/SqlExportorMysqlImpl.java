
/**
 * @author 钟冀 - yufei
 *		 	Mar 12, 2009
 */
package cn.featherfly.common.db.data.sql;

import cn.featherfly.common.db.dialect.MySqlDialect;



/**
 * @author 钟冀
 *
 */
public class SqlExportorMysqlImpl extends SqlExportor2 {

	/**
	 */
	public SqlExportorMysqlImpl() {
		setDialect(new MySqlDialect());
	}
}