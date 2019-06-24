package cn.featherfly.common.db.data;

import java.util.Locale;

import cn.featherfly.common.exception.StandardResourceBundleException;


/**
 * <p>
 * export异常
 * </p>
 *
 * @author zhongj
 */
public class ExportException extends StandardResourceBundleException {
	
	private static final long serialVersionUID = -7034897190745766939L;

	/**
	 * 
	 */
	public ExportException() {
		super();
	}

	/**
	 * @param message
	 * @param locale
	 * @param ex
	 */
	public ExportException(String message, Locale locale, Throwable ex) {
		super(message, locale, ex);
	}

	/**
	 * @param message
	 * @param locale
	 */
	public ExportException(String message, Locale locale) {
		super(message, locale);
	}

	/**
	 * @param message
	 * @param argus
	 * @param locale
	 * @param ex
	 */
	public ExportException(String message, Object[] argus, Locale locale,
			Throwable ex) {
		super(message, argus, locale, ex);
	}

	/**
	 * @param message
	 * @param argus
	 * @param locale
	 */
	public ExportException(String message, Object[] argus, Locale locale) {
		super(message, argus, locale);
	}

	/**
	 * @param message
	 * @param argus
	 * @param ex
	 */
	public ExportException(String message, Object[] argus, Throwable ex) {
		super(message, argus, ex);
	}

	/**
	 * @param message
	 * @param argus
	 */
	public ExportException(String message, Object[] argus) {
		super(message, argus);
	}

	/**
	 * @param message
	 * @param ex
	 */
	public ExportException(String message, Throwable ex) {
		super(message, ex);
	}

	/**
	 * @param message
	 */
	public ExportException(String message) {
		super(message);
	}

	/**
	 * @param ex
	 */
	public ExportException(Throwable ex) {
		super(ex);
	}

	
}
