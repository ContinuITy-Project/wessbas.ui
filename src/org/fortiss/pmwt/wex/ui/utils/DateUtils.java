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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility functions related to dates.
 */

public class DateUtils {
	/**
	 * Format used within then WESSBAS UI.
	 */

	public static final SimpleDateFormat FORMAT_LONG = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.sss");

	/**
	 * Convert a timestamp in ms to a string.
	 * 
	 * @param lTimestampInMilliseconds
	 *            Milliseconds to convert.
	 * @return String-based representation of the milliseconds.
	 */

	public static String toString(Long lTimestampInMilliseconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(lTimestampInMilliseconds);

		return toString(calendar.getTime());
	}

	/**
	 * Converts a date object to a string-value.
	 * 
	 * @param date
	 *            Date to be converted.
	 * @return String-based representation of the date object.
	 */

	public static String toString(Date date) {
		return FORMAT_LONG.format(date);
	}
}
