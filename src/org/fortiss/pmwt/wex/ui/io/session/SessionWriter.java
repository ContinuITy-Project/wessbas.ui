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

package org.fortiss.pmwt.wex.ui.io.session;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.fortiss.pmwt.wex.ui.io.session.model.Session;
import org.fortiss.pmwt.wex.ui.io.session.model.State;
import org.fortiss.pmwt.wex.ui.utils.NumberUtils;

/**
 * Writer for writing session files.
 */

public class SessionWriter implements AutoCloseable {
	/**
	 * FileWriter instance that refers to the output file.
	 */

	private FileWriter m_fwOutput = null;

	/**
	 * Opens the session output file.
	 * 
	 * @param fOutputSessionFile
	 *            Target output file.
	 * @throws IOException
	 *             Thrown, if there are problems with
	 *             accessing/reading/writing/... files.
	 */

	public void open(File fOutputSessionFile) throws IOException {
		fOutputSessionFile.createNewFile();

		this.m_fwOutput = new FileWriter(fOutputSessionFile);
	}

	/**
	 * Writes a single session to the session output file.
	 * 
	 * @param session
	 *            Session to be written.
	 * @throws IOException
	 *             Thrown, if there are problems with
	 *             accessing/reading/writing/... files.
	 */

	public void writeSession(Session session) throws IOException {
		this.m_fwOutput.write(session.getSessionID() + ";");

		for (State state : session.getStateList()) {
			String strNewName = state.getName();
			String strTimeIn = Long.toString(state.getTimeIn()); // Provoke
																	// NullPointerException
																	// on
																	// wrong
																	// data
			String strTimeOut = Long.toString(state.getTimeOut()); // Provoke
																	// NullPointerException
																	// on
																	// wrong
																	// data

			String[] strContentArray = new String[] { "\"" + strNewName + "\"",
					strTimeIn, strTimeOut, state.getURL(),
					NumberUtils.toString(state.getPort(), null),
					state.getHostIP(), state.getProtocol(), state.getAction(),
					state.getQueryString(), state.getEncoding() };

			int nSize = strContentArray.length;
			for (int i = 0; i < nSize; i++) {
				String strValue = strContentArray[i];
				if (strValue == null) {
					break;
				}

				if (i > 0) {
					this.m_fwOutput.write(':');
				}

				this.m_fwOutput.write(strValue);
			}

			this.m_fwOutput.write(";");
		}

		this.m_fwOutput.write("\n");
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
