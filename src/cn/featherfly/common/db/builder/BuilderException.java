package cn.featherfly.common.db.builder;

import java.util.Locale;

import cn.featherfly.common.exception.StandardResourceBundleException;


/**
 * <p>
 * JDBC操作包装异常
 * </p>
 *
 * @author 钟冀
 */
public class BuilderException extends StandardResourceBundleException {

	private static final long serialVersionUID = -7034897190745766939L;

	/**
	 * 
	 */
	public BuilderException() {
		super();
	}

	/**
	 * @param message
	 * @param locale
	 * @param ex
	 */
	public BuilderException(String message, Locale locale, Throwable ex) {
		super(message, locale, ex);
	}

	/**
	 * @param message
	 * @param locale
	 */
	public BuilderException(String message, Locale locale) {
		super(message, locale);
	}

	/**
	 * @param message
	 * @param argus
	 * @param locale
	 * @param ex
	 */
	public BuilderException(String message, Object[] argus, Locale locale,
			Throwable ex) {
		super(message, argus, locale, ex);
	}

	/**
	 * @param message
	 * @param argus
	 * @param locale
	 */
	public BuilderException(String message, Object[] argus, Locale locale) {
		super(message, argus, locale);
	}

	/**
	 * @param message
	 * @param argus
	 * @param ex
	 */
	public BuilderException(String message, Object[] argus, Throwable ex) {
		super(message, argus, ex);
	}

	/**
	 * @param message
	 * @param argus
	 */
	public BuilderException(String message, Object[] argus) {
		super(message, argus);
	}

	/**
	 * @param message
	 * @param ex
	 */
	public BuilderException(String message, Throwable ex) {
		super(message, ex);
	}

	/**
	 * @param message
	 */
	public BuilderException(String message) {
		super(message);
	}

	/**
	 * @param ex
	 */
	public BuilderException(Throwable ex) {
		super(ex);
	}


}
