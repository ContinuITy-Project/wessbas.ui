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
 * Utility function related to strings.
 */

public class StringUtils {
	/**
	 * Converts a Long to its string representation, or if null, to an empty
	 * string.
	 * 
	 * @param lValue
	 *            Long value to be converted to a string.
	 * @return String representation of the long value, or if null, an empty
	 *         string.
	 */

	public static String toEmptyString(Long lValue) {
		return lValue != null ? lValue.toString() : "";
	}

	/**
	 * Checks if a string is empty (null or length of 0).
	 * 
	 * @param strValue
	 *            Value to be checked.
	 * @return true if string is empty, false otherwise.
	 */

	public static boolean isEmpty(String strValue) {
		return strValue == null || strValue.trim().length() == 0;
	}

	/**
	 * Checks if a string is not empty (not null or length greater than 0).
	 * 
	 * @param strValue
	 *            Value to be checked.
	 * @return true if string is not empty, false otherwise.
	 */

	public static boolean isNotEmpty(String strValue) {
		return !isEmpty(strValue);
	}

	/**
	 * Checks if two string are equal (case sensitive!).
	 * 
	 * @param str0
	 *            First string.
	 * @param str1
	 *            Second string.
	 * @return true if both strings are equal, false otherwise.
	 */

	public static boolean equals(String str0, String str1) {
		if (str0 == null && str1 == null) {
			return true;
		} else if ((str0 == null && str1 != null)
				|| (str0 != null && str1 == null)) {
			return false;
		} else {
			return str0.equals(str1);
		}
	}

	/**
	 * Converts an integer to a string with fallback default value.
	 * 
	 * @param iValue
	 *            Integer-value to be converted.
	 * @param strDefaultValue
	 *            Default string value if integer cannot be converted (e.g.,
	 *            null).
	 * @return String-value representing the integer-value.
	 */

	public static String toString(Integer iValue, String strDefaultValue) {
		return iValue != null ? iValue.toString() : strDefaultValue;
	}

	/**
	 * Converts a string to null if it is empty.
	 * 
	 * @param strValue
	 *            String-value to be nulled.
	 * @return null if the string is empty, string-value of strValue otherwise.
	 */

	public static String toNull(String strValue) {
		return strValue != null && strValue.length() == 0 ? null : strValue;
	}

	/**
	 * Converts a null string to an empty string.
	 * 
	 * @param strValue
	 *            String-value to be emptied.
	 * @return An empty string if strValue is null, string-value of strValue
	 *         otherwise.
	 */

	public static String toEmpty(String strValue) {
		return strValue != null && strValue.length() > 0 ? strValue : "";
	}
}
