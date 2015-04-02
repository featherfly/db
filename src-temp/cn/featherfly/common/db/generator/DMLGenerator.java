
package cn.featherfly.common.db.generator;

/**
 * <p>
 * DMLGenerator
 * </p>
 * 
 * @author 钟冀
 */
public abstract class DMLGenerator extends AbstractGenerator{
	protected String getKeywordInsert() {
		return getKeyword("insert");
	}
	protected String getKeywordInto() {
		return getKeyword("into");
	}
	protected String getKeywordUpdate() {
		return getKeyword("update");
	}
	protected String getKeywordDelete() {
		return getKeyword("delete");
	}
	protected String getKeywordFrom() {
		return getKeyword("from");
	}
	protected String getKeywordValues() {
		return getKeyword("values");
	}
}
