package org.fortiss.pmwt.wex.ui.persistence;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.fortiss.pmwt.wex.ui.utils.FileUtils;

/**
 * Properties for wessbas2pcm extended.
 */

public class WESSBAS2PCMProperties extends AbstractProperties
{
	private static final long	serialVersionUID					= 1L;

	public static final String	USE_CONSOLE_OUT						= "useConsoleOut";
	public static final String	MODEL_DIRECTORY_SOURCE				= "model.directory.source";
	public static final String	MODEL_RESOURCE_ENVIRONMENT			= "model.infrastructure.name";
	public static final String	MODEL_REPOSITORY					= "model.repository.name";
	public static final String	MODEL_SYSTEM						= "model.system.name";
	public static final String	MODEL_SYSTEM_PROVIDED_ROLE			= "model.system.providedRole";
	public static final String	MODEL_USAGE							= "model.usage.name";
	public static final String	MODEL_ALLOCATION					= "model.allocation.name";
	public static final String	GUARDS_AND_ACTIONS					= "guardsAndActions";
	public static final String	MODEL_DIRECTORY_TARGET				= "model.directory.target";
	public static final String	PROBABILITY_FILTER_USE				= "probabilityFilter.use";
	public static final String	PROBABILITY_FILTER_AMOUNT_OF_DIGITS	= "probabilityFilter.amountOfDigits";

	/**
	 * Constructor.
	 */

	public WESSBAS2PCMProperties()
	{
	}

	/**
	 * Constructor.
	 * 
	 * @param properties
	 *            Untyped properties to read from.
	 */

	private WESSBAS2PCMProperties( Properties properties )
	{
		copy( properties, this );
	}

	/**
	 * Read property file of certain type.
	 * 
	 * @param fInputFile
	 *            Property file to be read.
	 * @return A wessbas2pcm extended property file instance.
	 * @throws IOException
	 *             Occurs, when something unexpected happens.
	 */

	public static WESSBAS2PCMProperties read( File fInputFile ) throws IOException
	{
		return new WESSBAS2PCMProperties( FileUtils.restoreProperties( fInputFile ) );
	}
}
