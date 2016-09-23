package org.fortiss.pmwt.wex.ui.jobs;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.fortiss.pmwt.wex.ui.io.session.SessionReader;
import org.fortiss.pmwt.wex.ui.io.session.StateFilter;
import org.fortiss.pmwt.wex.ui.io.session.model.Session;
import org.fortiss.pmwt.wex.ui.io.session.model.State;
import org.fortiss.pmwt.wex.ui.persistence.Project;
import org.fortiss.pmwt.wex.ui.utils.SerializationUtils;

/**
 * Retrieves state information from a session log.
 */

public class RetrieveStatesFromSessionJob implements IJob< Project >
{
	/**
	 * Input session log.
	 */

	private File		m_fInputInitialSessionFile	= null;

	/**
	 * Output state filter configuration file.
	 */

	private File		m_fOutputStateFilterFile	= null;

	/**
	 * State filter instance.
	 */

	private StateFilter	m_stateFilter				= null;

	/**
	 * Constructor.
	 */

	public RetrieveStatesFromSessionJob()
	{
		this.m_stateFilter = new StateFilter();
	}

	/**
	 * Initalize files.
	 * 
	 * @param project
	 *            Current project.
	 */

	private void initFiles( Project project )
	{
		// -- Output files
		this.m_fOutputStateFilterFile = project.addFile( Project.TAG_FILE_STATE_FILTER_CONFIGURATION, Project.FILENAME_STATE_FILTER );

		// -- Input files
		this.m_fInputInitialSessionFile = project.getFile( Project.TAG_FILE_INITIAL_SESSION );
	}

	/**
	 * Main entry.
	 */

	@Override
	public void run( Project project ) throws Exception
	{
		// -- Set input and output files
		initFiles( project );

		// -- Create initial state set
		createStateFilter(); // IOException, JAXBException
	}

	/**
	 * @return State filter instance.
	 */

	public StateFilter getStateFilter()
	{
		return this.m_stateFilter;
	}

	/**
	 * Creates the state filter.
	 * 
	 * @throws IOException
	 *             Occurs, if something unexpected happens.
	 * @throws JAXBException
	 *             Occurs, if something unexpected happens.
	 */

	private void createStateFilter() throws IOException, JAXBException
	{
		// -- Read states of sessions
		try( SessionReader reader = new SessionReader(); )
		{
			reader.open( this.m_fInputInitialSessionFile );
			Session session = null;
			while( ( session = reader.readSession() ) != null )
			{
				// Set< State > stateSet = session.getStateSet();
				List< State > stateList = session.getStateList();
				for( State state : stateList )
				// for( State state : stateSet )
				{
					this.m_stateFilter.addInitialStateName( state.getName(), false );
				}
			}
		}

		// -- Serialize state filter
		SerializationUtils.serializeToXML( this.m_fOutputStateFilterFile, this.m_stateFilter ); // JAXBException
	}
}