
package cn.featherfly.common.db.generator.dml;

import java.util.Map;

import cn.featherfly.common.db.generator.DMLGenerator;
import cn.featherfly.common.db.metadata.ColumnMetadata;
import cn.featherfly.common.db.metadata.TableMetadata;
import cn.featherfly.common.lang.AssertIllegalArgument;

/**
 * <p>
 * InsertDDL
 * </p>
 * 
 * @author 钟冀
 */
public class InsertDML extends DMLGenerator{
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generate() {
		StringBuilder insertSql = new StringBuilder();
		AssertIllegalArgument.isNotNull(tableMetadata, "tableMetadata is not set");
		AssertIllegalArgument.isNotNull(values, "values is not set");
		
		String tableName = tableMetadata.getName();		
		StringBuilder columns = new StringBuilder();
		StringBuilder values = new StringBuilder();

		values.append(" ")
			.append(getKeywordValues())
			.append(" ( ");;
		columns.append(" ( ");
		for (ColumnMetadata columnMetadata : tableMetadata.getColumns()) {
			String columnName = columnMetadata.getName();
			Object value = this.values.get(columnName);
			int type = columnMetadata.getType();
			columns.append(getDialect().wrapName(getColumnName(columnName))).append(",");
			values.append(getDialect().valueToSql(value, type)).append(",");
		}
		if(columns.lastIndexOf(",")+1==columns.length()){
			columns.deleteCharAt(columns.length()-1);
		}
		columns.append(" )");
		if(values.lastIndexOf(",")+1==values.length()){
			values.deleteCharAt(values.length()-1);
		}
		values.append(" )");
		columns.append(values.toString());
		insertSql.append(getKeywordInsert())
				.append(" ")
				.append(getKeywordInto())
				.append(getDialect().wrapName(getTableName(tableName)))
				.append(columns);
		return insertSql.toString();
	}
	
	private Map<String, Object> values;
	
	/**
	 * 返回values
	 * @return values
	 */
	public Map<String, Object> getValues() {
		return values;
	}

	/**
	 * 设置values
	 * @param values values
	 */
	public void setValues(Map<String, Object> values) {
		this.values = values;
	}

	private TableMetadata tableMetadata;

	/**
	 * 返回tableMetadata
	 * @return tableMetadata
	 */
	public TableMetadata getTableMetadata() {
		return tableMetadata;
	}

	/**
	 * 设置tableMetadata
	 * @param tableMetadata tableMetadata
	 */
	public void setTableMetadata(TableMetadata tableMetadata) {
		this.tableMetadata = tableMetadata;
	}
}
