package org.fortiss.pmwt.wex.ui.io.generic.exception;

/**
 * Container exception to represent different exceptions.
 */

public class GenericReaderException extends Exception
{
	private static final long	serialVersionUID	= 1L;

	public GenericReaderException( Throwable throwable )
	{
		super( throwable );
	}

	public GenericReaderException( String message )
	{
		super( message );
	}
}