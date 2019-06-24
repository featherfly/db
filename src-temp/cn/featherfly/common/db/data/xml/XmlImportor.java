
/**
 * @author zhongj - yufei
 *		 	Mar 12, 2009
 */
package cn.featherfly.common.db.data.xml;

import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.io.SAXReader;

import cn.featherfly.common.constant.Charset;
import cn.featherfly.common.db.JdbcUtils;
import cn.featherfly.common.db.data.AbstractDataImportor;
import cn.featherfly.common.db.data.ImportException;
import cn.featherfly.common.db.data.RecordModel;
import cn.featherfly.common.db.data.RecordModel.ValueModel;
import cn.featherfly.common.lang.LangUtils;

/**
 * <p>
 * 数据导出为xml
 * </p>
 *
 * @author zhongj
 *
 */
public abstract class XmlImportor extends AbstractDataImportor {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void imp(Reader reader) {
		Connection conn = null;
		try {
			SAXReader saxr = new SAXReader();
//			saxr.setDefaultHandler(new AutoClearElementHandler());
			Document doc = saxr.read(reader);

			Element root = doc.getRootElement();
			saxr.addHandler("/tables/table", new AutoClearElementHandler());
			@SuppressWarnings("unchecked")
			List<Element> tableElements = root.elements();

			conn = getDataSource().getConnection();

			conn.setAutoCommit(false);
			if (LangUtils.isNotEmpty(getPrepareSqls())) {
				for (String prepareSql : getPrepareSqls()) {
					logger.debug("prepareSql: {}", prepareSql);
					conn.createStatement().execute(prepareSql);
				}
			}

			// 表
			for (Element tableElement : tableElements) {
				String tableName = tableElement.attributeValue("name");
				Statement statement = conn.createStatement();
				int num = 0;
				if (ExistPolicy.exception == getExistPolicy()) {
					@SuppressWarnings("unchecked")
					List<Element> rowElements = tableElement.elements();
					// 行记录
					for (Element rowElement : rowElements) {
						@SuppressWarnings("unchecked")
						List<Element> columnElements = rowElement.elements();
						insert(columnElements, tableName, statement);
					}
				} else {
					String pkMapping = tableElement.attributeValue("pkMapping");
					String[] pkMappings = pkMapping.split(",");

					StringBuilder selectSql = new StringBuilder();
					selectSql.append("select count(*) as num from ")
							.append(tableName)
							.append(" where ");
					for (int i = 0; i < pkMappings.length; i++) {
						String pkName = pkMappings[i];
						if (i > 0) {
							selectSql.append(" and ");
						}
						selectSql.append(pkName).append(" = ? ");
					}

					logger.debug("selectSql : {}", selectSql.toString());

					String msg = "table " + tableName + " {}, {}";

					PreparedStatement prep = conn.prepareStatement(selectSql.toString());
					@SuppressWarnings("unchecked")
					List<Element> rowElements = tableElement.elements();
					// 行记录
//					int max = 1000000;
//					int count = 0;
					for (Element rowElement : rowElements) {
						// 处理主键
						for (int i = 0; i < pkMappings.length; i++) {
							String pkName = pkMappings[i];
							Element pkElement = rowElement.element(pkName);
							String value = pkElement.getText();
							JdbcUtils.setParameter(prep, i + 1, value);
						}
						@SuppressWarnings("unchecked")
						List<Element> columnElements = rowElement.elements();
						ResultSet res = prep.executeQuery();
						if (res.next()) {
							if (res.getLong(1) > 0) {
								logger.debug(msg, "exits", num++);
								if (ExistPolicy.update == getExistPolicy()) {
									update(columnElements, tableName, statement, pkMappings);
								}
							} else {
								logger.debug(msg, "not exits", num++);
								insert(columnElements, tableName, statement);
							}
						}
//						count++;
//						if (max == count) {
//							logger.debug("max == count execute");
//							statement.executeBatch();
//							statement.clearBatch();
//							count = 0;
//						}
					}
				}
				statement.executeBatch();
				statement.close();
				if (getTransactionPolicy() == TransactionPolicy.table) {
					conn.commit();
				}
			}
			if (getTransactionPolicy() == TransactionPolicy.all) {
				conn.commit();
			}
			JdbcUtils.closeQuietly(conn);
		} catch (Exception e) {
			JdbcUtils.rollbackAndCloseQuietly(conn);
			throw new ImportException(e);
		}
	}

	private void update(List<Element> columnElements, String tableName, Statement statement, String[] pkMappings) throws SQLException {
		StringBuilder updateSql = new StringBuilder();
		StringBuilder condition = new StringBuilder();
		StringBuilder values = new StringBuilder();
		if (columnElements.size() == pkMappings.length) {
			logger.trace("列数量与主键列数量一致，不进行更新");
			return;
		}

		// 准备 recordModel
		RecordModel recordModel = new RecordModel(tableName);
		for (Element columnElement : columnElements) {
			ValueModel valueModel = new ValueModel(columnElement.getName(), getType(columnElement), columnElement.getText());
			recordModel.add(valueModel);
		}

		// 过滤数据
		if (filtdate(recordModel, statement.getConnection())) {
			return;
		}

		// 数据处理
		recordModel = transform(recordModel);

		// 列值
		for (Element columnElement : columnElements) {
			String columnName = columnElement.getName();
//			int type = Types.VARCHAR;
//			try {
//				type = Integer.parseInt(columnElement.attributeValue("type"));
//			} catch (Exception e) {
//				logger.warn("转换sql类型失败，目标类型 -> " + columnElement.attributeValue("type"));
//			}
//			String value = columnElement.getText();
			ValueModel vm = recordModel.getValueMode(columnName);
			int type = vm.getType();
			String value = vm.getValue();

			boolean isPkColumn = false;
			for (int i = 0; i < pkMappings.length; i++) {
				String pkName = pkMappings[i];
				if (columnName.equals(pkName)) {
					if (condition.length() > 0) {
						condition.append(" and ");
					}
					condition.append(getDialect().wrapName(columnName.toUpperCase()))
							.append(" = ")
							.append(getValueToSql(value, type, columnElement.attributeValue("null")));
					isPkColumn = true;
				}
			}

			// 列名大写
			if (!isPkColumn) {
				values.append(" ")
					.append(getDialect().wrapName(columnName.toUpperCase()))
					.append(" = ")
					.append(getValueToSql(value, type, columnElement.attributeValue("null")))
					.append(",");
			}
		}
		if (values.length() > 0 && values.lastIndexOf(",")+1 == values.length()) {
			values.deleteCharAt(values.length()-1);
		}
		if (condition.length() > 0 && condition.lastIndexOf(",") +1 == condition.length()) {
			condition.deleteCharAt(condition.length()-1);
		}
		// 表名大写
		updateSql.append("UPDATE ")
			.append(getDialect().wrapName(tableName.toUpperCase()))
			.append(" SET ")
			.append(values)
			.append(" WHERE ")
			.append(condition)
			;
		logger.trace("update sql : {}" , updateSql.toString());
		statement.addBatch(updateSql.toString());
	}

	private void insert(List<Element> columnElements, String tableName, Statement statement) throws SQLException {
		// 准备 recordModel
		RecordModel recordModel = new RecordModel(tableName);
		for (Element columnElement : columnElements) {
			ValueModel valueModel = new ValueModel(columnElement.getName(), getType(columnElement), columnElement.getText());
			recordModel.add(valueModel);
		}

		// 过滤数据
		if (filtdate(recordModel, statement.getConnection())) {
			return;
		}

		// 数据处理
		recordModel = transform(recordModel);


		StringBuilder insertSql = new StringBuilder();
		StringBuilder columns = new StringBuilder();
		StringBuilder values = new StringBuilder();
		values.append(" VALUES ( ");
		columns.append(" ( ");
		// 列值
		for (Element columnElement : columnElements) {
			String columnName = columnElement.getName();
//			int type = Types.VARCHAR;
//			try {
//				type = Integer.parseInt(columnElement.attributeValue("type"));
//			} catch (Exception e) {
//				logger.warn("转换sql类型失败，目标类型 -> " + columnElement.attributeValue("type"));
//			}
//			String value = columnElement.getText();
			ValueModel vm = recordModel.getValueMode(columnName);
			int type = vm.getType();
			String value = vm.getValue();

			// 列名大写
			columns.append(getDialect().wrapName(columnName.toUpperCase())).append(",");
			values.append(getValueToSql(value, type, columnElement.attributeValue("null"))).append(",");
//			row.put(columnName, column);
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
		// 表名大写
		insertSql.append("INSERT INTO ")
				.append(getDialect().wrapName(tableName.toUpperCase()))
				.append(columns)
				.append(";");
		logger.trace("insert sql : {}" , insertSql.toString());
		statement.addBatch(insertSql.toString());
	}

	// ********************************************************************
	//	property
	// ********************************************************************

	/**
	 * <p>
	 * 把字符串表示的type转换为int表示的type
	 * </p>
	 * @param strType type
	 * @return javax.sql.Types定义的type
	 */
	protected int getType(Element columnElement) {
		return super.getType(columnElement.attributeValue("type"));
	}

	private String encoding = Charset.UTF_8;

	/**
	 * 返回encoding
	 * @return encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * 设置encoding
	 * @param encoding encoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
// TODO 大数据 内存溢出还没解决，要解决需要修改XML格式，暂时不动了，先用JSON的
class AutoClearElementHandler implements ElementHandler {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStart(ElementPath elementPath) {
//        elementPath.getCurrent().detach();
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
    public void onEnd(ElementPath elementPath) {
		Element row = elementPath.getCurrent();
//        Element table = row.getParent();
//        Document document = row.getDocument();
//        Element root = document.getRootElement();
//		Iterator<Element> it = root.elementIterator();
//		while(it.hasNext()){
//			Element element = (Element)it.next();
//			System.out.println(" id : " + element.elementText("id") + " name : " + element.elementText("name"));
//		}
		row.detach();
		row = null;
    }
}