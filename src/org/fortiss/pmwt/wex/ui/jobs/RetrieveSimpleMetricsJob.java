package org.fortiss.pmwt.wex.ui.jobs;

import java.io.File;

import org.fortiss.pmwt.wex.ui.io.session.SessionReader;
import org.fortiss.pmwt.wex.ui.io.session.model.Session;
import org.fortiss.pmwt.wex.ui.metrics.SimpleMetrics;
import org.fortiss.pmwt.wex.ui.persistence.Project;

/**
 * Retrieves simple metrics from a session log.
 */

public class RetrieveSimpleMetricsJob implements IJob< Project >
{
	/**
	 * Simple metrics instance.
	 */

	private SimpleMetrics	m_simpleMetrics	= null;

	/**
	 * Constructor.
	 */

	public RetrieveSimpleMetricsJob()
	{
		this.m_simpleMetrics = new SimpleMetrics();
	}

	/**
	 * Main entry.
	 */

	@Override
	public void run( Project project ) throws Exception
	{
		File file = project.getFile( Project.TAG_FILE_FINAL_SESSION );

		try( SessionReader reader = new SessionReader(); )
		{
			reader.open( file );

			Session session = null;
			while( ( session = reader.readSession() ) != null )
			{
				this.m_simpleMetrics.handleSession( session );
			}
		}
	}

	/**
	 * @return Simple metrics instance.
	 */

	public SimpleMetrics getSimpleMetrics()
	{
		return this.m_simpleMetrics;
	}
}