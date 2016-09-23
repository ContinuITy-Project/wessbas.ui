package org.fortiss.pmwt.wex.ui.io.generic.exception;

/**
 * Thrown, when a mandatory field was evaluated to null.
 */

public class MissingFieldException extends GenericReaderException
{
	private static final long	serialVersionUID	= 1L;

	public MissingFieldException( Throwable throwable )
	{
		super( throwable );
	}

	public MissingFieldException( String message )
	{
		super( message );
	}
}
