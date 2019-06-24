
package cn.featherfly.common.db.data;

import java.io.Writer;
import java.util.Collection;

import cn.featherfly.common.db.builder.ConditionBuilder;

/**
 * <p>
 * 数据导出
 * </p>
 *
 * @author zhongj
 */
public interface DataFormatExportor {
	/**
	 * <p>
	 * 导出整库
	 * </p>
	 * @param writer writer
	 */
	void exportDatabase(Writer writer, DataFormat dataFormat);
	/**
	 * <p>
	 * 导出某张表
	 * </p>
	 * @param tableName  表名称
	 * @param writer writer
	 */
	void exportTable(String tableName, Writer writer, DataFormat dataFormat);
	/**
	 * <p>
	 * 导出某几张表
	 * </p>
	 * @param tableNames  表名称集合
	 * @param writer writer
	 */
	void exportTable(Collection<String> tableNames, Writer writer, DataFormat dataFormat);
	/**
	 * <p>
	 * 导出结果集
	 * </p>
	 * @param querySql 查询sql
	 * @param tableName  表名称
	 * @param writer writer
	 */
	void exportData(String querySql, Writer writer, DataFormat dataFormat);
	/**
	 * <p>
	 * 导出结果集
	 * </p>
	 * @param querySqls 查询sql集合
	 * @param writer writer
	 */
	void exportData(Collection<String> querySqls, Writer writer, DataFormat dataFormat);
	/**
	 * <p>
	 * 导出结果集
	 * </p>
	 * @param writer writer
	 * @param querySqls 查询sql可变参数
	 */
	void exportData(Writer writer, DataFormat dataFormat, String...querySqls);
	/**
	 * <p>
	 * 导出结果集
	 * </p>
	 * @param conditionBuilder 查询条件
	 * @param tableName  表名称
	 * @param writer writer
	 */
	void exportData(String tableName, ConditionBuilder conditionBuilder, Writer writer, DataFormat dataFormat);
	/**
	 * <p>
	 * 导出结果集
	 * </p>
	 * @param writer writer
	 * @param querys 查询对象可变参数
	 */
	void exportData(Writer writer, DataFormat dataFormat, Query...querys);
	/**
	 * <p>
	 * 导出结果集
	 * </p>
	 * @param writer writer
	 * @param querys 查询对象集合
	 */
	void exportData(Writer writer, DataFormat dataFormat, Collection<Query> querys);
	/**
	 * <p>
	 * 导出结果集
	 * </p>
	 * @param query 查询
	 * @param writer writer
	 */
	void exportData(Query query, Writer writer, DataFormat dataFormat);
}
