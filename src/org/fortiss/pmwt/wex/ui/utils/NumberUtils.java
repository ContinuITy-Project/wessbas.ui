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

package org.fortiss.pmwt.wex.ui.utils;

/**
 * Utility functions related to numbers.
 */

public class NumberUtils {
	/**
	 * Converts a string value to an integer.
	 * 
	 * @param strValue
	 *            Value to be converted.
	 * @param iDefaultValue
	 *            Default value if conversion fails.
	 * @return Instance of Integer that represents the string value or the
	 *         provided default value on error.
	 */

	public static Integer toInteger(String strValue, Integer iDefaultValue) {
		Integer iRetVal = null;

		try {
			iRetVal = new Integer(strValue);
			return iRetVal;
		} catch (Exception e) {
		}

		return iDefaultValue;
	}

	/**
	 * Converts a string value to an integer.
	 * 
	 * @param strValue
	 *            Value to be converted.
	 * @param lDefaultValue
	 *            Default value if conversion fails.
	 * @return Instance of Long that represents the string value or the provided
	 *         default value on error.
	 */

	public static Long toLong(String strValue, Long lDefaultValue) {
		Long lRetVal = null;

		try {
			lRetVal = new Long(strValue);
			return lRetVal;
		} catch (Exception e) {
		}

		return lRetVal;
	}

	/**
	 * Converts an integer value to a string.
	 * 
	 * @param iValue
	 *            Integer value to be converted.
	 * @param strDefaultValue
	 *            Default string value if conversion fails.
	 * @return String value that represents the integer value or the default
	 *         value on error.
	 */

	public static String toString(Integer iValue, String strDefaultValue) {
		return iValue != null ? iValue.toString() : strDefaultValue;
	}

	/**
	 * Returns the length of a long value.
	 * 
	 * @param lValue
	 *            Long value to be "measured".
	 * @return "Length" of the provided long value.
	 */

	public static int length(long lValue) {
		return lValue > 0 ? (int) (Math.log10(lValue) + 1) : -1;
	}

	/**
	 * Helper method to format percentage values.
	 * 
	 * @param nValue
	 *            Represents the percentage (ger., PW).
	 * @param nMaxValue
	 *            Represents the basic value (ger., GW).
	 * @return A string that represents a percentage value.
	 */

	public static String toPercent(int nValue, int nMaxValue) {
		return (int) ((double) nValue / (double) nMaxValue * 100.0) + "%";
	}

	public static int stringToInt(String strValue, int nDefaultValue) {
		try {
			return new Integer(strValue).intValue();
		} catch (Exception e) {
			return nDefaultValue;
		}
	}
}
