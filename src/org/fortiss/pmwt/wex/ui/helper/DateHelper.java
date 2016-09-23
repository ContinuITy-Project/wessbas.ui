package org.fortiss.pmwt.wex.ui.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Helper to parse dates in the UI.
 */

public class DateHelper {
	/**
	 * String that contains a simple date format pattern.
	 */

	private String m_strFormat = null;

	/**
	 * Holds the parsed date.
	 */

	private Date m_date = null;

	/**
	 * Constructor.
	 * 
	 * @param strFormat
	 *            String that contains a simple date format pattern.
	 */

	public DateHelper(String strFormat) {
		this.m_strFormat = strFormat;
	}

	/**
	 * Constructor.
	 */

	public DateHelper() {
		// For use with parseNS and parseMS only!
	}

	/**
	 * Parses a date string.
	 * 
	 * @param strValue
	 *            String value that contains the date.
	 * @return Reference to class for chain access in scripts.
	 * @throws ParseException
	 *             Thrown if parsing goes wrong.
	 */

	public DateHelper parse(String strValue) throws ParseException {
		Date date = new SimpleDateFormat(this.m_strFormat).parse(strValue);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		this.m_date = calendar.getTime();

		return this;
	}

	/**
	 * Parses nanoseconds.
	 * 
	 * @param lValue
	 *            Long value that represents a timestamp as nanoseconds.
	 * @return Reference to the class for chain access in scripts.
	 */

	public DateHelper parseNS(long lValue) {
		long lNew = TimeUnit.NANOSECONDS.toMillis(lValue);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(lNew);

		this.m_date = calendar.getTime();

		return this;
	}

	/**
	 * Parses milliseconds.
	 * 
	 * @param lValue
	 *            Long value that represents a timestamp as milliseconds.
	 * @return Reference to the class for chain access in scripts.
	 */

	public DateHelper parseMS(long lValue) {
		long lNew = lValue;

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(lNew);

		this.m_date = calendar.getTime();

		return this;
	}

	/**
	 * @return Parsed time in milliseconds.
	 */

	public long getTimeMS() {
		return this.m_date.getTime();
	}

	/**
	 * @return Parsed time in milliseconds.
	 */

	public long getTimeNS() {
		return this.m_date.getTime() * 1000000;
	}
}
