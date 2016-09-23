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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.fortiss.pmwt.wex.ui.io.session.model.Session;
import org.fortiss.pmwt.wex.ui.io.session.model.State;
import org.fortiss.pmwt.wex.ui.utils.NumberUtils;

/**
 * Reader for session logs.
 */

public class SessionReader implements AutoCloseable {
	/**
	 * FileReader instance that refers to the input file.
	 */

	private FileReader m_frInput = null;

	/**
	 * BufferedReader instance that refers to the input data.
	 */

	private BufferedReader m_brInput = null;

	/**
	 * Set to collect state names found in the session file.
	 */

	private Set<String> m_stateNameSet = null;

	/**
	 * Amount of session read.
	 */

	private long m_lSessionCount = -1;

	/**
	 * Amount of states over all sessions read.
	 */

	private long m_lVisitedStateCount = -1;

	/**
	 * Constructor.
	 */

	public SessionReader() {
		this.m_stateNameSet = new HashSet<String>();
		this.m_lSessionCount = 0;
		this.m_lVisitedStateCount = 0;
	}

	/**
	 * Opens the session log.
	 * 
	 * @param fInputFile
	 *            File to be opened.
	 * @throws IOException
	 *             Thrown, if there are problems with
	 *             accessing/reading/writing/... files.
	 */

	public void open(File fInputFile) throws IOException {
		this.m_frInput = new FileReader(fInputFile);
		this.m_brInput = new BufferedReader(this.m_frInput);
	}

	/**
	 * Reads a single session from the session file.
	 * 
	 * @return Session that represents the read line.
	 * @throws IOException
	 *             Thrown, if there are problems with
	 *             accessing/reading/writing/... files.
	 */

	public Session readSession() throws IOException {
		String strLine = this.m_brInput.readLine();
		if (strLine == null) {
			return null;
		}

		// --
		this.m_lSessionCount++;

		// --
		Session session = parseLine(strLine);
		return session;
	}

	/**
	 * @return Count of all sessions read.
	 */

	public long getSessionCount() {
		return this.m_lSessionCount;
	}

	/**
	 * @return Sum of all states over all sessions read.
	 */

	public long getVisitedStateCount() {
		return this.m_lVisitedStateCount;
	}

	/**
	 * @return Set with all states over all sessions read.
	 */

	public Set<String> getStateNameSet() {
		return this.m_stateNameSet;
	}

	/**
	 * Closes all open handles to the session input file.
	 */

	@Override
	public void close() {
		try {
			this.m_brInput.close();
			this.m_frInput.close();
		} catch (IOException ioe) {
		}
	}

	/**
	 * Parses a string and creates a session from it.
	 * 
	 * @param strLine
	 *            String to be processed.
	 * @return Session that represents the provided string.
	 */

	private Session parseLine(String strLine) {
		String[] strValueArray_0 = strLine.split(";");

		int nIndex = 0;
		int nLen = strValueArray_0.length;

		Session session = new Session(strValueArray_0[nIndex++]);

		for (; nIndex < nLen; nIndex++) {
			String[] s = strValueArray_0[nIndex].split(":");

			// -- State name
			String strStateName = s[0];
			if (strStateName.startsWith("\"") && strStateName.endsWith("\"")) {
				strStateName = strStateName.substring(1,
						strStateName.length() - 1);
			}

			State state = new State(strStateName, NumberUtils.toLong(V(s, 1),
					null), NumberUtils.toLong(V(s, 2), null), V(s, 3),
					NumberUtils.toInteger(V(s, 4), null), V(s, 5), V(s, 6), V(
							s, 7), V(s, 8), V(s, 9));
			session.addState(state);

			// -- Store state name
			this.m_stateNameSet.add(state.getName());

			// -- Visited state count
			this.m_lVisitedStateCount++;
		}

		return session;
	}

	/**
	 * Convenient method to retrieve a certain value by index from a given
	 * array.
	 * 
	 * @param arr
	 *            Array.
	 * @param nIndex
	 *            Index of the desired element in the given array.
	 * @return Array element at given index or null.
	 */

	private static <T> T V(T[] arr, int nIndex) {
		return nIndex < arr.length ? arr[nIndex] : null;
	}
}
