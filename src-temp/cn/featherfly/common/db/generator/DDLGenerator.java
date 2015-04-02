
package cn.featherfly.common.db.generator;

/**
 * <p>
 * DDLGenerator
 * </p>
 * 
 * @author 钟冀
 */
public abstract class DDLGenerator extends AbstractGenerator{
	protected String getKeywordCreate() {
		return getKeyword("create");
	}
	protected String getKeywordDrop() {
		return getKeyword("drop");
	}
	protected String getKeywordTable() {
		return getKeyword("table");
	}
	protected String getKeywordColumn() {
		return getKeyword("column");
	}
	protected String getKeywordIndex() {
		return getKeyword("index");
	}
}
