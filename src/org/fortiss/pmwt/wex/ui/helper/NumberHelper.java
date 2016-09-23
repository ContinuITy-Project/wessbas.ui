package org.fortiss.pmwt.wex.ui.helper;

/**
 * Helper to parse numbers in the UI.
 */

public class NumberHelper
{
	/**
	 * Parsed number.
	 */

	private Number	m_number	= null;

	/**
	 * Constructor.
	 * 
	 * @param strFormat
	 *            String that contains a simple date format pattern.
	 */

	public NumberHelper()
	{
	}

	/**
	 * Constructor.
	 * 
	 * @param dblValue
	 *            Double value for initialization.
	 */

	public NumberHelper( double dblValue )
	{
		this.m_number = new Double( dblValue );
	}

	/**
	 * Constructor.
	 * 
	 * @param lValue
	 *            Long value for initialization.
	 */

	public NumberHelper( long lValue )
	{
		this.m_number = new Long( lValue );
	}

	/**
	 * Constructor.
	 * 
	 * @param nValue
	 *            int value for initialization.
	 */

	public NumberHelper( int nValue )
	{
		this.m_number = new Integer( nValue );
	}

	/**
	 * @return Integer value representation of the initialization value.
	 */

	public int toInteger()
	{
		return this.m_number.intValue();
	}

	/**
	 * @return Long value representation of the initialization value.
	 */

	public long toLong()
	{
		return this.m_number.longValue();
	}

	/**
	 * @return Double value representation of the initialization value.
	 */

	public double toDouble()
	{
		return this.m_number.doubleValue();
	}
}
