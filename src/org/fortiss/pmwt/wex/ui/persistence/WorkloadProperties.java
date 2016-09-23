package org.fortiss.pmwt.wex.ui.persistence;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.fortiss.pmwt.wex.ui.utils.FileUtils;

/**
 * Properties for workload intensity.
 */

public class WorkloadProperties extends AbstractProperties
{
	private static final long	serialVersionUID		= 1L;

	public static final String	PROPERTY_TYPE			= "workloadIntensity.type";
	public static final String	PROPERTY_FORMULA		= "workloadIntensity.formula";
	public static final String	PROPERTY_WL_TYPE		= "wl.type";
	public static final String	PROPERTY_WL_TYPE_VALUE	= "wl.type.value";

	public static final String	VALUE_OPEN				= "open";
	public static final String	VALUE_CLOSED			= "closed";
	public static final String	VALUE_CONSTANT			= "constant";

	/**
	 * Constructor.
	 */

	public WorkloadProperties()
	{
		put( PROPERTY_TYPE, VALUE_CONSTANT );
		put( PROPERTY_FORMULA, "1" );
		put( PROPERTY_WL_TYPE, VALUE_CLOSED );
		put( PROPERTY_WL_TYPE_VALUE, "1" );
	}

	/**
	 * Constructor.
	 * 
	 * @param properties
	 *            Untyped properties to read from.
	 */

	private WorkloadProperties( Properties properties )
	{
		putString( PROPERTY_FORMULA, (String)properties.get( PROPERTY_FORMULA ), "1" );
		putString( PROPERTY_TYPE, (String)properties.get( PROPERTY_TYPE ), VALUE_CONSTANT );
		putString( PROPERTY_WL_TYPE, (String)properties.get( PROPERTY_WL_TYPE ), VALUE_CLOSED );
		putString( PROPERTY_WL_TYPE_VALUE, (String)properties.get( PROPERTY_WL_TYPE_VALUE ), "1" );
	}

	/**
	 * Read property file of certain type.
	 * 
	 * @param fInputFile
	 *            Property file to be read.
	 * @return A workload property file instance.
	 * @throws IOException
	 *             Occurs, when something unexpected happens.
	 */

	public static WorkloadProperties read( File fInputFile ) throws IOException
	{
		return new WorkloadProperties( FileUtils.restoreProperties( fInputFile ) );
	}
}
