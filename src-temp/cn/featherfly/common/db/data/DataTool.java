
package cn.featherfly.common.db.data;

import java.io.Writer;
import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.featherfly.common.db.JdbcUtils;
import cn.featherfly.common.db.data.DataImportor.ExistPolicy;
import cn.featherfly.common.db.data.DataImportor.TransactionPolicy;
import cn.featherfly.common.db.data.query.TableQuery;
import cn.featherfly.common.db.dialect.Dialect;
import cn.featherfly.common.db.metadata.DatabaseMetadata;
import cn.featherfly.common.db.metadata.DatabaseMetadataManager;
import cn.featherfly.common.lang.LangUtils;

/**
 * <p>
 * DataTool
 * </p>
 * 
 * @author 钟冀
 */
public class DataTool {
	/**
	 * logger
	 */
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	/**
	 * <p>
	 * 导出整库
	 * </p>
	 * @param writer writer
	 */
	public void exportDatabase(Writer writer, DataFormat dataFormat) {
		
	}
	/**
	 * <p>
	 * 导出某张表
	 * </p>
	 * @param tableName  表名称
	 * @param writer writer
	 */
	public void exportTable(String tableName, Writer writer, DataFormat dataFormat) {
		
	}
	/**
	 * <p>
	 * 导出某几张表
	 * </p>
	 * @param tableNames  表名称集合
	 * @param writer writer
	 */
	public void exportTable(Collection<String> tableNames, Writer writer, DataFormat dataFormat) { 
		
	}
	/**
	 * <p>
	 * 导出某张表
	 * </p>
	 * @param tableName  表名称
	 * @param writer writer
	 */
	public void exportTable(TableQuery query, Writer writer, DataFormat dataFormat) {
		
	}
	/**
	 * <p>
	 * 导出某几张表
	 * </p>
	 * @param tableNames  表名称集合
	 * @param writer writer
	 */
	public void exportTables(Collection<TableQuery> tableQuerys, Writer writer, DataFormat dataFormat) {
		
	}
	
	// ********************************************************************
	//	property
	// ********************************************************************
	
	/**
	 * <p>
	 * 添加预执行sql
	 * </p>
	 * @param prepareSql prepareSql
	 */
	public void addPrepareSql(String prepareSql) {
		if (LangUtils.isNotEmpty(prepareSql)) {
			this.prepareSqls.add(prepareSql);
		}
	}
	/**
	 * <p>
	 * 添加预执行sql
	 * </p>
	 * @param prepareSqls prepareSqls
	 */
	public void addPrepareSql(String...prepareSqls) {
		if (LangUtils.isNotEmpty(prepareSqls)) {
			for (String prepareSql : prepareSqls) {
				this.prepareSqls.add(prepareSql);
			}
		}
	}

	/**
	 * <p>
	 * 添加过滤器
	 * </p>
	 * @param filter filter
	 */
	public void addFilter(DataFilter filter) {
		if (LangUtils.isNotEmpty(filter)) {
			this.filters.add(filter);
		}
	}
	/**
	 * <p>
	 * 添加过滤器
	 * </p>
	 * @param dataFilter dataFilter
	 */
	public void addFilter(DataFilter...filters) {
		if (LangUtils.isNotEmpty(filters)) {
			for (DataFilter filter : filters) {
				this.filters.add(filter);
			}
		}
	}

	/**
	 * <p>
	 * 添加数据变换器
	 * </p>
	 * @param transformer transformer
	 */
	public void addTransformers(DataTransformer transformer) {
		if (LangUtils.isNotEmpty(transformer)) {
			this.transformers.add(transformer);
		}
	}
	/**
	 * <p>
	 * 添加数据变换器
	 * </p>
	 * @param transformers transformer
	 */
	public void addTransformers(DataTransformer...transformers) {
		if (LangUtils.isNotEmpty(filters)) {
			for (DataTransformer transformer : transformers) {
				this.transformers.add(transformer);
			}
		}
	}

	/**
	 * <p>
	 * 数据过滤
	 * </p>
	 * @param recordModel recordModel
	 * @param conn conn
	 * @return RecordModel
	 */
	protected boolean filtdate(RecordModel recordModel, Connection conn) {
		// 过滤
		if (LangUtils.isNotEmpty(filters)) {
			for (DataFilter filter : filters) {
				if (filter.filter(recordModel, conn)) {
					logger.debug("过滤该条数据， {}", recordModel);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * <p>
	 * 数据变换
	 * </p>
	 * @param recordModel recordModel
	 * @return RecordModel
	 */
	protected RecordModel transform(RecordModel recordModel) {
		// 数据变换
		if (LangUtils.isNotEmpty(transformers)) {
			for (DataTransformer transformer : transformers) {
				recordModel = transformer.transform(recordModel);
			}
		}
		return recordModel;
	}

	// ********************************************************************
	//	peroperty
	// ********************************************************************

	private boolean fkCheck;

	private ExistPolicy existPolicy = ExistPolicy.exception;

	private TransactionPolicy transactionPolicy = TransactionPolicy.all;

	private List<String> prepareSqls = new ArrayList<String>();

	private List<DataFilter> filters = new ArrayList<DataFilter>();

	private List<DataTransformer> transformers = new ArrayList<DataTransformer>();

	/**
	 * 返回prepareSqls
	 * @return prepareSqls
	 */
	public List<String> getPrepareSqls() {
		return prepareSqls;
	}
	/**
	 * 设置prepareSqls
	 * @param prepareSqls prepareSqls
	 */
	public void setPrepareSqls(List<String> prepareSqls) {
		this.prepareSqls = prepareSqls;
	}
	/**
	 * 返回transformers
	 * @return transformers
	 */
	public List<DataTransformer> getTransformers() {
		return transformers;
	}
	/**
	 * 设置transformers
	 * @param transformers transformers
	 */
	public void setTransformers(List<DataTransformer> transformers) {
		this.transformers = transformers;
	}

	/**
	 * 返回existPolicy
	 * @return existPolicy
	 */
	public ExistPolicy getExistPolicy() {
		return existPolicy;
	}
	/**
	 * 设置existPolicy
	 * @param existPolicy existPolicy
	 */
	public void setExistPolicy(ExistPolicy existPolicy) {
		this.existPolicy = existPolicy;
	}
	/**
	 * 返回transactionPolicy
	 * @return transactionPolicy
	 */
	public TransactionPolicy getTransactionPolicy() {
		return transactionPolicy;
	}
	/**
	 * 设置transactionPolicy
	 * @param transactionPolicy transactionPolicy
	 */
	public void setTransactionPolicy(TransactionPolicy transactionPolicy) {
		this.transactionPolicy = transactionPolicy;
	}

	/**
	 * 返回fkCheck
	 * @return fkCheck
	 */
	public boolean isFkCheck() {
		return fkCheck;
	}

	/**
	 * 设置fkCheck
	 * @param fkCheck fkCheck
	 */
	public void setFkCheck(boolean fkCheck) {
		this.fkCheck = fkCheck;
	}
	
	/**
	 * <p>
	 * 根据表名称获取查询对象
	 * </p>
	 * @param tableName 表名称
	 * @return 查询对象
	 */
	protected Query getTableQuery(String tableName) {
		return new Query(tableName, getTableQuerySql(tableName), new Object[]{});
	}

	/*
	 * 数据源
	 */
	private DataSource dataSource;

	// 数据库方言
	private Dialect dialect;

	/**
	 * 返回dialect
	 * @return dialect
	 */
	public Dialect getDialect() {
		if (dialect == null) {
			throw new ImportException("dialect 未设置");
		}
		return dialect;
	}

	/**
	 * 设置dialect
	 * @param dialect dialect
	 */
	protected void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	/**
	 * <p>
	 * 设置数据源
	 * </p>
	 * @param dataSource 数据源
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 返回dataSource
	 * @return dataSource
	 */
	protected DataSource getDataSource() {
		if (dataSource == null) {
			throw new ExportException("dataSource未设置");
		}
		return dataSource;
	}
	
	/**
	 * <p>
	 * 返回DatabaseMetadata
	 * </p>
	 * @return DatabaseMetadata
	 */
	protected DatabaseMetadata getDatabaseMetadata() {
		return DatabaseMetadataManager.getDefaultManager().create(getDataSource(), getCheckedDatabase());
	}
	
	private String getCheckedDatabase() {
		String database = JdbcUtils.getCatalog(getDataSource());
		if (LangUtils.isEmpty(database)) {
			database = JdbcUtils.getCatalog(getDataSource());
			if (database == null) {
				throw new ExportException("数据库连接的具体库名称不能为空！");
			}
		}
		return database;
	}

	/**
	 * <p>
	 * 把字符串转换为指定类型的sql语句参数
	 * </p>
	 * @param value 值
	 * @param type javax.sql.Types定义的type
	 * @param isNull 是否为null
	 * @return sql语句参数
	 */
	protected String getValueToSql(String value, int type, String isNull) {
		String v =  null;
		// "1"代表isNull属性的值为真
		if ("1".equals(isNull) && LangUtils.isEmpty(value)) {
			v = "null";
		} else {
			v = getDialect().valueToSql(value, type);
		}
		return v;
	}

	/**
	 * <p>
	 * 把字符串表示的type转换为int表示的type
	 * </p>
	 * @param strType type
	 * @return javax.sql.Types定义的type
	 */
	protected int getType(String strType) {
		int type = Types.VARCHAR;
		try {
			type = Integer.parseInt(strType);
		} catch (Exception e) {
			logger.warn("转换sql类型失败，目标类型 -> " + strType);
		}
		return type;
	}
}
