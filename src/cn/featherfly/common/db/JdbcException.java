package cn.featherfly.common.db;

import java.util.Locale;

import cn.featherfly.common.exception.StandardResourceBundleException;

/**
 * <p>
 * JDBC操作包装异常
 * </p>
 *
 * @author 钟冀
 */
public class JdbcException extends StandardResourceBundleException {
	
	private static final long serialVersionUID = -7034897190745766939L;

	/**
	 * 
	 */
	public JdbcException() {
		super();
	}

	/**
	 * @param message
	 * @param locale
	 * @param ex
	 */
	public JdbcException(String message, Locale locale, Throwable ex) {
		super(message, locale, ex);
	}

	/**
	 * @param message
	 * @param locale
	 */
	public JdbcException(String message, Locale locale) {
		super(message, locale);
	}

	/**
	 * @param message
	 * @param argus
	 * @param locale
	 * @param ex
	 */
	public JdbcException(String message, Object[] argus, Locale locale,
			Throwable ex) {
		super(message, argus, locale, ex);
	}

	/**
	 * @param message
	 * @param argus
	 * @param locale
	 */
	public JdbcException(String message, Object[] argus, Locale locale) {
		super(message, argus, locale);
	}

	/**
	 * @param message
	 * @param argus
	 * @param ex
	 */
	public JdbcException(String message, Object[] argus, Throwable ex) {
		super(message, argus, ex);
	}

	/**
	 * @param message
	 * @param argus
	 */
	public JdbcException(String message, Object[] argus) {
		super(message, argus);
	}

	/**
	 * @param message
	 * @param ex
	 */
	public JdbcException(String message, Throwable ex) {
		super(message, ex);
	}

	/**
	 * @param message
	 */
	public JdbcException(String message) {
		super(message);
	}

	/**
	 * @param ex
	 */
	public JdbcException(Throwable ex) {
		super(ex);
	}


}
