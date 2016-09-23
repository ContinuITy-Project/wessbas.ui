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

package org.fortiss.pmwt.wex.ui.helper;

/**
 * Helper to parse numbers in the UI.
 */

public class NumberHelper {
	/**
	 * Parsed number.
	 */

	private Number m_number = null;

	/**
	 * Constructor.
	 * 
	 * @param strFormat
	 *            String that contains a simple date format pattern.
	 */

	public NumberHelper() {
	}

	/**
	 * Constructor.
	 * 
	 * @param dblValue
	 *            Double value for initialization.
	 */

	public NumberHelper(double dblValue) {
		this.m_number = new Double(dblValue);
	}

	/**
	 * Constructor.
	 * 
	 * @param lValue
	 *            Long value for initialization.
	 */

	public NumberHelper(long lValue) {
		this.m_number = new Long(lValue);
	}

	/**
	 * Constructor.
	 * 
	 * @param nValue
	 *            int value for initialization.
	 */

	public NumberHelper(int nValue) {
		this.m_number = new Integer(nValue);
	}

	/**
	 * @return Integer value representation of the initialization value.
	 */

	public int toInteger() {
		return this.m_number.intValue();
	}

	/**
	 * @return Long value representation of the initialization value.
	 */

	public long toLong() {
		return this.m_number.longValue();
	}

	/**
	 * @return Double value representation of the initialization value.
	 */

	public double toDouble() {
		return this.m_number.doubleValue();
	}
}
