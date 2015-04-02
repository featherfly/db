
/**
 * @author 钟冀 - yufei
 *		 	Mar 12, 2009
 */
package cn.featherfly.common.db.data.sql;

import cn.featherfly.common.db.dialect.OracleDialect;



/**
 * @author 钟冀
 *
 */
public class SqlExportorOracleImpl extends SqlExportor2 {

	private String database;

	/**
	 * 使用指定表空间名称创建SQL导出器
	 * @param database 表空间名称
	 */
	public SqlExportorOracleImpl(String database) {
		setDialect(new OracleDialect());
		this.database = database;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getDatabase() {
		return database;
	}
}