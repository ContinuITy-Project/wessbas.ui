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
 * Utility functions related to arrays.
 */

public class ArrayUtils {
	/**
	 * Converts an Integer-array to an int-array.
	 * 
	 * @param arrIn
	 *            Integer-array to be converted.
	 * @return int-array representing the Integer-array.
	 */

	public static int[] toPrimitive(Integer[] arrIn) {
		int[] arrOut = new int[arrIn.length];

		for (int i = 0; i < arrIn.length; i++) {
			arrOut[i] = arrIn[i].intValue();
		}

		return arrOut;
	}

	/**
	 * Converts a Long-array to a long-array.
	 * 
	 * @param arrIn
	 *            Long-array to be converted.
	 * @return long-array representing the Long-array.
	 */

	public static long[] toPrimitive(Long[] arrIn) {
		long[] arrOut = new long[arrIn.length];

		for (int i = 0; i < arrIn.length; i++) {
			arrIn[i] = arrIn[i].longValue();
		}

		return arrOut;
	}
}
