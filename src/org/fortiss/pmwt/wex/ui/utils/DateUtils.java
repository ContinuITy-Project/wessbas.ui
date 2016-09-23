package org.fortiss.pmwt.wex.ui.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility functions related to dates.
 */

public class DateUtils
{
	/**
	 * Format used within then WESSBAS UI.
	 */

	public static final SimpleDateFormat	FORMAT_LONG	= new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.sss" );

	/**
	 * Convert a timestamp in ms to a string.
	 * 
	 * @param lTimestampInMilliseconds
	 *            Milliseconds to convert.
	 * @return String-based representation of the milliseconds.
	 */

	public static String toString( Long lTimestampInMilliseconds )
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis( lTimestampInMilliseconds );

		return toString( calendar.getTime() );
	}

	/**
	 * Converts a date object to a string-value.
	 * 
	 * @param date
	 *            Date to be converted.
	 * @return String-based representation of the date object.
	 */

	public static String toString( Date date )
	{
		return FORMAT_LONG.format( date );
	}
}
