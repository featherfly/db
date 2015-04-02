package cn.featherfly.common.db.metadata;

import java.sql.SQLException;

import cn.featherfly.common.db.JdbcException;

/**
 * <p>
 * DatabaseMetadata异常
 * </p>
 *
 * @author 钟冀
 */
public class DatabaseMetadataException extends JdbcException {
	
	private static final long serialVersionUID = -7034897190745766939L;

	/**
	 */
	public DatabaseMetadataException() {
		super();
	}

	/**
	 * @param message 描述信息 (之后可以使用 {@link #getMessage()} 方法获得).
	 */
	public DatabaseMetadataException(String message) {
		super(message);
	}

	/**
	 * @param message 描述信息 (之后可以使用 {@link #getMessage()} 方法获得).
	 * @param e SQL异常
	 */
	public DatabaseMetadataException(String message, SQLException e) {
		super(message, e);
	}

	/**
	 * @param e SQL异常
	 */
	public DatabaseMetadataException(SQLException e) {
		super(e);
	}
}
