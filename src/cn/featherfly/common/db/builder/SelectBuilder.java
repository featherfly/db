
package cn.featherfly.common.db.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.featherfly.common.constant.Chars;
import cn.featherfly.common.lang.AssertIllegalArgument;
import cn.featherfly.common.lang.LangUtils;

/**
 * <p>
 * SelectBuilder
 * </p>
 * 
 * @author 钟冀
 */
public class SelectBuilder implements Builder {

    private String alias;
    
    private String tableName;
    
    private boolean buildWithFrom = true;
    
    private List<String> columns = new ArrayList<>(0);
    
    /**
     */
    public SelectBuilder() {
        this(null);
    }
    
    /**
     * @param tableName tableName
     */
    public SelectBuilder(String tableName) {
        this(tableName, null);
    }
    
    /**
     * @param tableName tableName
     * @param alias alias
     */
    public SelectBuilder(String tableName, String alias) {
        this.alias = alias;
        this.tableName = tableName;
    }

    /**
     * <p>
     * 添加select的列
     * </p>
     * @param columnName columnName
     * @return this
     */
    public SelectBuilder select(String columnName) {
        columns.add(columnName);
        return this;
    }
    /**
     * <p>
     * 批量添加select的列
     * </p>
     * @param columnNames columnNames
     * @return this
     */
    public SelectBuilder select(String...columnNames) {
        if (LangUtils.isNotEmpty(columnNames)) {
            for (String columnName : columnNames) {
                select(columnName);
            }
        }
        return this;
    }
    /**
     * <p>
     * 批量添加select的列
     * </p>
     * @param columnNames columnNames
     * @return this
     */
    public SelectBuilder select(Collection<String> columnNames) {
        if (LangUtils.isNotEmpty(columnNames)) {
            for (String columnName : columnNames) {
                select(columnName);
            }
        }
        return this;
    }

    /**
     * 返回alias
     * @return alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 设置alias
     * @param alias alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * 返回tableName
     * @return tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 设置tableName
     * @param tableName tableName
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 返回buildWithFrom
     * @return buildWithFrom
     */
    public boolean isBuildWithFrom() {
        return buildWithFrom;
    }

    /**
     * 设置buildWithFrom
     * @param buildWithFrom buildWithFrom
     */
    public void setBuildWithFrom(boolean buildWithFrom) {
        this.buildWithFrom = buildWithFrom;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String build() {
        StringBuilder select = new StringBuilder();
        select.append("select ");
        for (String column : columns) {
            if (LangUtils.isNotEmpty(alias)) {
                select.append(alias).append(Chars.DOT);
            }
            select.append(column).append(",");
        }
        if (!columns.isEmpty()) {
            select.deleteCharAt(select.length() - 1);
        }
        if (buildWithFrom) {
            AssertIllegalArgument.isNotEmpty(tableName, "buildWithFrom=true时，tableName不能为空");
            select.append(" from ").append(tableName);
            if (LangUtils.isNotEmpty(alias)) {
                select.append(" ").append(alias);
            }
        }
        return select.toString();
    }
}
