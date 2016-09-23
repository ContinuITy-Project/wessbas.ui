package org.fortiss.pmwt.wex.ui.persistence.options;

/**
 * Represents possible options for the transformation properties.
 */

public enum ETransformation {
	// @formatter:off
	XMEANS_MIN("xmeans.min", new Integer(2)), XMEANS_MAX("xmeans.max",
			new Integer(3)), XMEANS_SEED("xmeans.seed", new Integer(3)), USEXMEANS(
			"use.xmeans", new Boolean(true)), GENERATE_MODIFIED_PCM_USAGE_MODEL(
			"generate.modifiedpcmusagemodel", new Boolean(true)), GENERATE_APACHE_JMETER_TESTPLAN(
			"generate.apachejmetertestplan", new Boolean(true)); // @formatter:on

	/**
	 * Key of the option.
	 */

	private String m_strKey = null;

	/**
	 * Default value of the option.
	 */

	private Object m_objDefaultValue = null;

	/**
	 * Constructor.
	 * 
	 * @param strKey
	 *            Key of the option.
	 * @param objDefaultValue
	 *            Default value of the option.
	 */

	ETransformation(String strKey, Object objDefaultValue) {
		this.m_strKey = strKey;
		this.m_objDefaultValue = objDefaultValue;
	}

	/**
	 * @return Key of the option.
	 */

	public String getKey() {
		return this.m_strKey;
	}

	/**
	 * @return Default value of the option.
	 */

	public Object getDefaultValue() {
		return this.m_objDefaultValue;
	}
}