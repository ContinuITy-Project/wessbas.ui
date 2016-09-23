package org.fortiss.pmwt.wex.ui.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper to parse URL parameters in the UI.
 */

public class ParameterHelper
{
	/**
	 * Map with all parameters and corresponding values.
	 */

	private Map< String, String >	m_map	= null;

	/**
	 * Constructor.
	 * 
	 * @param strQueryString
	 *            Query string to parse.
	 */

	public ParameterHelper( String strQueryString )
	{
		if( strQueryString != null && strQueryString.startsWith( "?" ) )
		{
			strQueryString = strQueryString.substring( 1 );
		}

		this.m_map = queryStringToMap( strQueryString );
	}

	/**
	 * @param strKey
	 *            Key of the corresponding value.
	 * @return Corresponding value of the key if found, null otherwise.
	 */

	public String get( String strKey )
	{
		String strValue = this.m_map.get( strKey );
		return strValue;
	}

	/**
	 * Parses the query string and returns the result as map.
	 * 
	 * @param strQueryString
	 *            The query string to parse.
	 * @return Map with all key/value pairs of the query string.
	 */

	private static Map< String, String > queryStringToMap( String strQueryString )
	{
		// Missing: Decode; Multi-Parameter

		Map< String, String > map = new HashMap< String, String >();

		if( strQueryString == null || strQueryString.length() == 0 )
		{
			return map;
		}

		String[] strValueArray0 = strQueryString.split( "&" );
		for( String strValue0 : strValueArray0 )
		{
			String[] strValueArray1 = strValue0.split( "=" );
			String strKey = strValueArray1[ 0 ];
			String strValue = strValueArray1[ 1 ];
			map.put( strKey, strValue );
		}

		return map;
	}
}
