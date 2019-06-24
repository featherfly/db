
package cn.featherfly.common.db.generator;

import cn.featherfly.common.db.dialect.Dialect;

/**
 * <p>
 * AbstractGenerator
 * </p>
 * 
 * @author zhongj
 */
public abstract class AbstractGenerator implements Generator{
	
	/**
	 * <p>
	 * 返回tableName
	 * </p>
	 * @param tableName tableName
	 * @return tableName
	 */
	protected String getTableName(String tableName) {
		if (upperCaseTtableName) {
			return tableName.toUpperCase();
		}
		return tableName;
	}
	/**
	 * <p>
	 * 返回columnName
	 * </p>
	 * @param columnName columnName
	 * @return columnName
	 */
	protected String getColumnName(String columnName) {
		if (upperCaseColumnName) {
			return columnName.toUpperCase();
		}
		return columnName;
	}
	/**
	 * <p>
	 * 返回keyword
	 * </p>
	 * @param keyword keyword
	 * @return keyword
	 */
	protected String getKeyword(String keyword) {
		if (upperCaseKeyword) {
			return keyword.toUpperCase();
		}
		return keyword;
	}
	
	// ********************************************************************
	//	property
	// ********************************************************************
	
	private Dialect dialect;
	
	private boolean upperCaseTtableName;
	
	private boolean upperCaseColumnName;
	
	private boolean upperCaseKeyword;
	
	/**
	 * 返回dialect
	 * @return dialect
	 */
	public Dialect getDialect() {	
		return dialect;
	}

	/**
	 * 设置dialect
	 * @param dialect dialect
	 */
	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	/**
	 * 返回upperCaseTtableName
	 * @return upperCaseTtableName
	 */
	public boolean isUpperCaseTtableName() {
		return upperCaseTtableName;
	}

	/**
	 * 设置upperCaseTtableName
	 * @param upperCaseTtableName upperCaseTtableName
	 */
	public void setUpperCaseTtableName(boolean upperCaseTtableName) {
		this.upperCaseTtableName = upperCaseTtableName;
	}

	/**
	 * 返回upperCaseColumnName
	 * @return upperCaseColumnName
	 */
	public boolean isUpperCaseColumnName() {
		return upperCaseColumnName;
	}

	/**
	 * 设置upperCaseColumnName
	 * @param upperCaseColumnName upperCaseColumnName
	 */
	public void setUpperCaseColumnName(boolean upperCaseColumnName) {
		this.upperCaseColumnName = upperCaseColumnName;
	}

	/**
	 * 返回upperCaseKeyword
	 * @return upperCaseKeyword
	 */
	public boolean isUpperCaseKeyword() {
		return upperCaseKeyword;
	}

	/**
	 * 设置upperCaseKeyword
	 * @param upperCaseKeyword upperCaseKeyword
	 */
	public void setUpperCaseKeyword(boolean upperCaseKeyword) {
		this.upperCaseKeyword = upperCaseKeyword;
	}
}
