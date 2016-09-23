package org.fortiss.pmwt.wex.ui.persistence;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.fortiss.pmwt.wex.ui.utils.FileUtils;
import org.fortiss.pmwt.wex.ui.utils.NumberUtils;

/**
 * Common functionality for properties.
 */

public abstract class AbstractProperties extends Properties
{
	/**
	 * Serialization.
	 */

	private static final long	serialVersionUID	= 1L;

	/**
	 * Adds a string to the properties.
	 * 
	 * @param strProperty
	 *            Name of the property.
	 * @param strValue
	 *            Value of the property.
	 * @param strDefaultValue
	 *            Default value for the property if property value is null.
	 */

	public void putString( String strProperty, String strValue, String strDefaultValue )
	{
		put( strProperty, strValue != null ? strValue : strDefaultValue );
	}

	/**
	 * Adds a boolean to the properties.
	 * 
	 * @param strKey
	 *            Name of the property.
	 * @param bValue
	 *            Value of the property.
	 */

	public void putBoolean( String strKey, boolean bValue )
	{
		put( strKey, new Boolean( bValue ).toString() );
	}

	/**
	 * Adds a string to the properties.
	 * 
	 * @param strKey
	 *            Name of the property.
	 * @param strValue
	 *            Value of the property.
	 */

	public void putString( String strKey, String strValue )
	{
		put( strKey, strValue != null ? strValue : "" );
	}

	/**
	 * @param strKey
	 *            Name of the property.
	 * @param bDefaultValue
	 *            Default value if the property cannot be retrieved.
	 * @return Boolean value refering to the key if exists, the default value otherwise.
	 */

	public boolean getBoolean( String strKey, boolean bDefaultValue )
	{
		String strValue = (String)get( strKey );
		Boolean bValue = new Boolean( strValue );
		return bValue != null ? bValue.booleanValue() : bDefaultValue;
	}

	/**
	 * @param strKey
	 *            Name of the property.
	 * @param strDefaultValue
	 *            Default value if the property cannot be retrieved.
	 * @return String value refering to the key if exists, the default value otherwise.
	 */

	public String getString( String strKey, String strDefaultValue )
	{
		String strValue = (String)get( strKey );
		return strValue != null ? strValue : strDefaultValue;
	}

	/**
	 * @param strKey
	 *            Name of the property.
	 * @param nDefaultValue
	 *            Default value if the property cannot be retrieved.
	 * @return int value referting to the key if exists, the default value otherwise.
	 */

	public int getInt( String strKey, int nDefaultValue )
	{
		return NumberUtils.stringToInt( (String)get( strKey ), nDefaultValue );
	}

	/**
	 * Serializes the property instance to a file.
	 * 
	 * @param fOutputFile
	 *            Output file.
	 * @throws IOException
	 *             Occurs, if something unexpected happens.
	 */

	public void write( File fOutputFile ) throws IOException
	{
		FileUtils.storeProperties( fOutputFile, this );
	}

	/**
	 * Copies one properties instance to another.
	 * 
	 * @param pFrom
	 *            Source properties.
	 * @param pTo
	 *            Target properties.
	 */

	public static void copy( Properties pFrom, Properties pTo )
	{
		Iterator< Map.Entry< Object, Object >> iterator = pFrom.entrySet().iterator();
		while( iterator.hasNext() )
		{
			Map.Entry< Object, Object > mapEntry = iterator.next();
			pTo.put( mapEntry.getKey(), mapEntry.getValue() );
		}
	}
}
