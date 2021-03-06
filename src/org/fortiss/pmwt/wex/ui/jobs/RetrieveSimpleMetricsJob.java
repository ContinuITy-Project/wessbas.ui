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

import org.fortiss.pmwt.wex.ui.io.session.SessionReader;
import org.fortiss.pmwt.wex.ui.io.session.model.Session;
import org.fortiss.pmwt.wex.ui.metrics.SimpleMetrics;
import org.fortiss.pmwt.wex.ui.persistence.Project;

/**
 * Retrieves simple metrics from a session log.
 */

public class RetrieveSimpleMetricsJob implements IJob<Project> {
	/**
	 * Simple metrics instance.
	 */

	private SimpleMetrics m_simpleMetrics = null;

	/**
	 * Constructor.
	 */

	public RetrieveSimpleMetricsJob() {
		this.m_simpleMetrics = new SimpleMetrics();
	}

	/**
	 * Main entry.
	 */

	@Override
	public void run(Project project) throws Exception {
		File file = project.getFile(Project.TAG_FILE_FINAL_SESSION);

		try (SessionReader reader = new SessionReader();) {
			reader.open(file);

			Session session = null;
			while ((session = reader.readSession()) != null) {
				this.m_simpleMetrics.handleSession(session);
			}
		}
	}

	/**
	 * @return Simple metrics instance.
	 */

	public SimpleMetrics getSimpleMetrics() {
		return this.m_simpleMetrics;
	}
}