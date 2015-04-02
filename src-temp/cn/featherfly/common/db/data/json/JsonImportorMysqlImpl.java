
/**
 * @author 钟冀 - yufei
 *		 	Mar 12, 2009
 */
package cn.featherfly.common.db.data.json;

import java.io.Reader;

import cn.featherfly.common.db.dialect.MySqlDialect;

/**
 * <p>
 * 数据导出为xml
 * </p>
 *
 * @author 钟冀
 *
 */
public class JsonImportorMysqlImpl extends JsonImportor{

	/**
	 */
	public JsonImportorMysqlImpl() {
		setDialect(new MySqlDialect());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void imp(Reader reader) {
		if (!isFkCheck()) {
			addPrepareSql("SET FOREIGN_KEY_CHECKS=0");
		}
		super.imp(reader);
	}
}