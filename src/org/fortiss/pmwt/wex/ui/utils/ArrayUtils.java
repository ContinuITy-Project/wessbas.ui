package org.fortiss.pmwt.wex.ui.utils;

/**
 * Utility functions related to arrays.
 */

public class ArrayUtils
{
	/**
	 * Converts an Integer-array to an int-array.
	 * 
	 * @param arrIn
	 *            Integer-array to be converted.
	 * @return int-array representing the Integer-array.
	 */

	public static int[] toPrimitive( Integer[] arrIn )
	{
		int[] arrOut = new int[ arrIn.length ];

		for( int i = 0; i < arrIn.length; i++ )
		{
			arrOut[ i ] = arrIn[ i ].intValue();
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

	public static long[] toPrimitive( Long[] arrIn )
	{
		long[] arrOut = new long[ arrIn.length ];

		for( int i = 0; i < arrIn.length; i++ )
		{
			arrIn[ i ] = arrIn[ i ].longValue();
		}

		return arrOut;
	}
}
