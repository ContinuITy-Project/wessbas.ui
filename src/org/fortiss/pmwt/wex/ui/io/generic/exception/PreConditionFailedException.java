package org.fortiss.pmwt.wex.ui.io.generic.exception;

/**
 * Thrown, if the pre-condition failed to execute.
 */

public class PreConditionFailedException extends GenericReaderException
{
	private static final long	serialVersionUID	= 1L;

	public PreConditionFailedException( Throwable throwable )
	{
		super( throwable );
	}

	public PreConditionFailedException( String message )
	{
		super( message );
	}
}
