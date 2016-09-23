package org.fortiss.pmwt.wex.ui.io.synoptic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.fortiss.pmwt.wex.ui.io.session.model.Session;
import org.fortiss.pmwt.wex.ui.io.session.model.State;

/**
 * Creates a synoptic file (guards and actions).
 */

public class SynopticLogWriter implements AutoCloseable
{
	/**
	 * Separator.
	 */

	public static final String	SEPARATOR	= "---";

	/**
	 * FileWriter instance that refers to the output file.
	 */

	private FileWriter			m_fwOutput	= null;

	/**
	 * Opens the output file.
	 * 
	 * @param fOutputLogFile
	 *            Output file.
	 * @throws IOException
	 *             Occurs, if something unexpected happens.
	 */

	public void open( File fOutputLogFile ) throws IOException
	{
		fOutputLogFile.createNewFile();

		this.m_fwOutput = new FileWriter( fOutputLogFile );
	}

	/**
	 * Writes a session in the synoptic format to the file.
	 * 
	 * @param session
	 *            Current session.
	 * @throws IOException
	 *             Occurs, if something unexpected happens.
	 */

	public void writeSession( Session session ) throws IOException
	{
		for( State state : session.getStateList() )
		{
			this.m_fwOutput.write( state.getName() + "\n" );
		}

		this.m_fwOutput.write( SEPARATOR + "\n" );
	}

	/**
	 * Closes all open handles to the session output files.
	 */

	@Override
	public void close()
	{
		try
		{
			this.m_fwOutput.close();
		}
		catch( IOException ioe )
		{
		}
	}
}
