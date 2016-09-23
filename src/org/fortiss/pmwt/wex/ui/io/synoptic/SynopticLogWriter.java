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

package org.fortiss.pmwt.wex.ui.io.synoptic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.fortiss.pmwt.wex.ui.io.session.model.Session;
import org.fortiss.pmwt.wex.ui.io.session.model.State;

/**
 * Creates a synoptic file (guards and actions).
 */

public class SynopticLogWriter implements AutoCloseable {
	/**
	 * Separator.
	 */

	public static final String SEPARATOR = "---";

	/**
	 * FileWriter instance that refers to the output file.
	 */

	private FileWriter m_fwOutput = null;

	/**
	 * Opens the output file.
	 * 
	 * @param fOutputLogFile
	 *            Output file.
	 * @throws IOException
	 *             Occurs, if something unexpected happens.
	 */

	public void open(File fOutputLogFile) throws IOException {
		fOutputLogFile.createNewFile();

		this.m_fwOutput = new FileWriter(fOutputLogFile);
	}

	/**
	 * Writes a session in the synoptic format to the file.
	 * 
	 * @param session
	 *            Current session.
	 * @throws IOException
	 *             Occurs, if something unexpected happens.
	 */

	public void writeSession(Session session) throws IOException {
		for (State state : session.getStateList()) {
			this.m_fwOutput.write(state.getName() + "\n");
		}

		this.m_fwOutput.write(SEPARATOR + "\n");
	}

	/**
	 * Closes all open handles to the session output files.
	 */

	@Override
	public void close() {
		try {
			this.m_fwOutput.close();
		} catch (IOException ioe) {
		}
	}
}
