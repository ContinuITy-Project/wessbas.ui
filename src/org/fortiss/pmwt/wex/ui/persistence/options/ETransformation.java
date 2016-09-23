/***************************************************************************
 * Copyright (c) 2016 the WESSBAS project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

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