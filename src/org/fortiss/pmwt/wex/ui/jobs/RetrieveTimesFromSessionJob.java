/***************************************************************************
 * Copyright (c) 2016 the WESSBAS project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package org.fortiss.pmwt.wex.ui.jobs;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.fortiss.pmwt.wex.ui.io.session.SessionReader;
import org.fortiss.pmwt.wex.ui.io.session.model.Session;
import org.fortiss.pmwt.wex.ui.persistence.Project;

/**
 * Retrieves timing information from a session log.
 */

public class RetrieveTimesFromSessionJob implements IJob<Project> {
	/**
	 * Input session log.
	 */

	private File m_fInputInitialSessionFile = null;

	/**
	 * First timestamp in session.
	 */

	private long m_lFirstSessionStartTime = 0;

	/**
	 * Last timestamp in session.
	 */

	private long m_lLastSessionStopTime = 0;

	/**
	 * Constructor.
	 */

	public RetrieveTimesFromSessionJob() {
		this.m_lFirstSessionStartTime = Long.MAX_VALUE;
		this.m_lLastSessionStopTime = Long.MIN_VALUE;
	}

	/**
	 * Retrieve files.
	 * 
	 * @param project
	 *            Current project.
	 */

	private void initFiles(Project project) {
		// -- Input files
		this.m_fInputInitialSessionFile = project
				.getFile(Project.TAG_FILE_INITIAL_SESSION);
	}

	/**
	 * Main entry.
	 */

	@Override
	public void run(Project project) throws Exception {
		// -- Set input and output files
		initFiles(project);

		// -- Create initial time range filter
		createTimeRangeFilter(); // IOException, JAXBException
	}

	/**
	 * Creates a time range filter instance based on the session log.
	 * 
	 * @throws IOException
	 *             Occurs, if something unexpected happens.
	 * @throws JAXBException
	 *             Occurs, if something unexpected happens.
	 */

	private void createTimeRangeFilter() throws IOException, JAXBException {
		// -- Read states of sessions
		try (SessionReader reader = new SessionReader();) {
			reader.open(this.m_fInputInitialSessionFile);
			Session session = null;
			while ((session = reader.readSession()) != null) {
				long lSessionStartTime = session.getFirstTimestampInSession();
				if (lSessionStartTime < this.m_lFirstSessionStartTime) {
					this.m_lFirstSessionStartTime = lSessionStartTime;
				}

				long lSessionStopTime = session.getLastTimestampInSession();
				if (lSessionStopTime > this.m_lLastSessionStopTime) {
					this.m_lLastSessionStopTime = lSessionStopTime;
				}
			}
		}
	}

	/**
	 * @return First timestamp in the session log.
	 */

	public long getFirstSessionStartTime() {
		return this.m_lFirstSessionStartTime;
	}

	/**
	 * @return Last timestamp in the session log.
	 */

	public long getLastSessionStopTime() {
		return this.m_lLastSessionStopTime;
	}
}