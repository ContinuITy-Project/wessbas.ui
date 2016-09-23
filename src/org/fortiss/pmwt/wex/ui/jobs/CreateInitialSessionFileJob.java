package org.fortiss.pmwt.wex.ui.jobs;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.fortiss.pmwt.wex.ui.io.generic.GenericReader;
import org.fortiss.pmwt.wex.ui.io.generic.GenericReaderConfiguration;
import org.fortiss.pmwt.wex.ui.io.generic.exception.GenericReaderException;
import org.fortiss.pmwt.wex.ui.io.generic.exception.MissingFieldException;
import org.fortiss.pmwt.wex.ui.io.generic.exception.PreConditionFailedException;
import org.fortiss.pmwt.wex.ui.io.generic.model.CustomLogEntry;
import org.fortiss.pmwt.wex.ui.io.session.SessionBuilder;
import org.fortiss.pmwt.wex.ui.io.session.SessionWriter;
import org.fortiss.pmwt.wex.ui.io.session.model.Session;
import org.fortiss.pmwt.wex.ui.persistence.Project;
import org.fortiss.pmwt.wex.ui.utils.DateUtils;
import org.fortiss.pmwt.wex.ui.utils.SerializationUtils;

/**
 * Creates an initial session log.
 */

public class CreateInitialSessionFileJob implements IJob<Project> {
	/**
	 * Input/Output/Configuration files.
	 */

	private File m_fOutputInitialSessionFile = null;
	private File m_fInputCSVFile = null;
	private File m_fInputGenericReaderConfigurationFile = null;
	private GenericReaderConfiguration m_readerConfiguration = null;
	private String m_strCSVDelimiter = null;
	private Project m_project = null;

	/**
	 * First timestamp in session.
	 */

	private long m_lFirstSessionTime = 0;

	/**
	 * Last timestamp in session.
	 */

	private long m_lLastSessionTime = 0;

	/**
	 * Constructor.
	 */

	public CreateInitialSessionFileJob() {
		this.m_lFirstSessionTime = Long.MAX_VALUE;
		this.m_lLastSessionTime = Long.MIN_VALUE;
	}

	/**
	 * Main entry.
	 */

	@Override
	public void run(Project project) throws Exception {
		this.m_project = project;

		// -- Set input and output files
		initFiles();

		// -- Reader configuration
		initReaderConfiguration();

		// -- Create session file
		createSessionFile();
	}

	/**
	 * Creates the initial session file.
	 * 
	 * @throws IOException
	 *             Occurs, if something unexpected happens.
	 * @throws GenericReaderException
	 *             Occurs, if something unexpected happens.
	 */

	private void createSessionFile() throws IOException, GenericReaderException {
		SessionBuilder sessionBuilder = new SessionBuilder();

		// -- Reader for generic CSV-based content
		try (GenericReader genericReader = new GenericReader(
				this.m_readerConfiguration);) {
			genericReader.open(this.m_fInputCSVFile, this.m_strCSVDelimiter); // GenericReaderException

			// -- Convert CSV-based line to CustomLogEntry and add it to a
			// session
			CustomLogEntry customLogEntry = null;
			while (true) {
				try {
					if ((customLogEntry = genericReader.nextEntry()) == null) // GenericReaderException
					{
						break;
					}
					// TODO: make session threshold configurable
					sessionBuilder.addCustomLogEntry(customLogEntry);
				} catch (MissingFieldException e) {
					System.out.println("MissingFieldException: "
							+ e.getMessage());
				} catch (PreConditionFailedException e) {
					System.out.println("PreConditionFailed: " + e.getMessage());
				} catch (GenericReaderException e) {
					System.out.println(e.getMessage());
				}
			}
		}

		// -- Step #2: Write sessions to file
		// -- Prepare the collector for iteration over the stored sessions
		sessionBuilder.initializeIterator();

		// -- No rewriting of state names (= use cases) at this point
		try (SessionWriter writer = new SessionWriter()) {
			writer.open(this.m_fOutputInitialSessionFile); // IOException

			// -- Iterate over sessions
			Session session = null;
			while ((session = sessionBuilder.getSession()) != null) {
				writer.writeSession(session); // IOException

				handleSession(session);
			}
		}

		// -- Debug
		System.out.println("- First timestamp over all sessions: "
				+ this.m_lFirstSessionTime + " -> "
				+ DateUtils.FORMAT_LONG.format(this.m_lFirstSessionTime));
		System.out.println("- Last timestamp over all sessions: "
				+ this.m_lLastSessionTime + " -> "
				+ DateUtils.FORMAT_LONG.format(this.m_lLastSessionTime));

		// -- Needed to calculate time intervalls for time range filter
		this.m_project.addValue(Project.TAG_VALUE_FIRST_TIMESTAMP, new Long(
				this.m_lFirstSessionTime));
		this.m_project.addValue(Project.TAG_VALUE_LAST_TIMESTAMP, new Long(
				this.m_lLastSessionTime));
	}

	/**
	 * Helper method for handling session timestampsl.
	 * 
	 * @param session
	 *            Current session.
	 */

	private void handleSession(Session session) {
		// -- First timestamp in data
		if (session.getFirstTimestampInSession() < this.m_lFirstSessionTime) {
			this.m_lFirstSessionTime = session.getFirstTimestampInSession();
		}

		// -- Last timestamp in data
		if (session.getLastTimestampInSession() > this.m_lLastSessionTime) {
			this.m_lLastSessionTime = session.getLastTimestampInSession();
		}
	}

	/**
	 * Initalize files.
	 */

	private void initFiles() {
		// -- Output files
		this.m_fOutputInitialSessionFile = this.m_project.addFile(
				Project.TAG_FILE_INITIAL_SESSION,
				Project.FILENAME_INITIAL_SESSION);

		// -- Input files
		this.m_fInputCSVFile = this.m_project.getFile(Project.TAG_FILE_CSV);
		this.m_fInputGenericReaderConfigurationFile = this.m_project
				.getFile(Project.TAG_FILE_GENERIC_READER_CONFIGURATION);
	}

	/**
	 * Initalize reader configuration.
	 * 
	 * @throws JAXBException
	 *             Occurs, if something unexpected happens.
	 */

	private void initReaderConfiguration() throws JAXBException {
		this.m_readerConfiguration = SerializationUtils.deserializeFromXML(
				this.m_fInputGenericReaderConfigurationFile,
				GenericReaderConfiguration.class); // JAXBException
		this.m_strCSVDelimiter = this.m_readerConfiguration.getDelimiter();
	}
}