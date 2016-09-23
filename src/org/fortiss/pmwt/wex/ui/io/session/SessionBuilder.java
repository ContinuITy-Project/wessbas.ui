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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.fortiss.pmwt.wex.ui.io.generic.model.CustomLogEntry;
import org.fortiss.pmwt.wex.ui.io.session.model.Session;
import org.fortiss.pmwt.wex.ui.io.session.model.State;

/**
 * Builds a session log.
 */

public class SessionBuilder {
	/**
	 * Map of format: <Session_ID,<CValue instance>>. The CValue instance acts
	 * as a storage for log entries.
	 */

	private Map<String, CValue> m_mapSessionStore = null;

	/**
	 * Iterator to iterate over the session map.
	 */

	private Iterator<Map.Entry<String, CValue>> m_iterator = null;

	/**
	 * Constructor.
	 */

	public SessionBuilder() {
		this.m_mapSessionStore = new HashMap<String, CValue>();
	}

	/**
	 * Adds an instance of
	 * {@link org.fortiss.pmwt.wex.ui.io.generic.model.CustomLogEntry} the the
	 * corresponding session. If the session does not exist, the session will be
	 * created automatically.
	 * 
	 * @param customLogEntry
	 *            Log entry to be added to a session.
	 * @param threshold
	 *            threshold when a new session is created.
	 */
	public void addCustomLogEntry(CustomLogEntry customLogEntry) {
		CValue value = this.m_mapSessionStore
				.get(customLogEntry.getSessionID());
		if (value == null) {
			value = new CValue();
			this.m_mapSessionStore.put(customLogEntry.getSessionID(), value);
		}

		value.lstCustomLogEntry.add(customLogEntry);
	}

	/**
	 * Initializes the iterator used to iterate over all sessions.
	 */

	public void initializeIterator() {
		this.m_iterator = this.m_mapSessionStore.entrySet().iterator();
	}

	/**
	 * @return Current session.
	 */

	public Session getSession() {
		if (!this.m_iterator.hasNext()) {
			return null;
		}

		// -- Create junction
		Map.Entry<String, CValue> mapEntry = this.m_iterator.next();
		String strSessionID = mapEntry.getKey();
		CValue value = mapEntry.getValue();

		// -- Create session
		Session session = toSession(strSessionID, value);
		return session;
	}

	/**
	 * Creates an instance of
	 * {@link org.fortiss.pmwt.wex.ui.io.session.model.Session} by a session id
	 * and all log entries that belong to the provided session id.
	 * 
	 * @param strSessionID
	 *            Session id.
	 * @param value
	 *            Instance of
	 *            {@link org.fortiss.pmwt.wex.ui.io.session.SessionBuilder.CValue}
	 *            that stores all log entries that belong to the provided
	 *            session id.
	 * @return An instance of
	 *         {@link org.fortiss.pmwt.wex.ui.io.session.model.Session}.
	 */

	private Session toSession(String strSessionID, CValue value) {
		Session session = new Session(strSessionID);

		Collections.sort(value.lstCustomLogEntry);

		for (CustomLogEntry customLogEntry : value.lstCustomLogEntry) {
			State state = new State(customLogEntry.getStateName(),
					customLogEntry.getRequestStartTime(),
					customLogEntry.getRequestEndTime(),
					customLogEntry.getRequestURL(),
					customLogEntry.getLocalPort(), customLogEntry.getHostIP(),
					customLogEntry.getEncoding(), customLogEntry.getMethod(),
					customLogEntry.getParameterNames(),
					customLogEntry.getEncoding());
			session.addState(state);
		}

		return session;
	}

	/**
	 * Stores instances of
	 * {@link org.fortiss.pmwt.wex.ui.io.generic.model.CustomLogEntry}
	 */

	private static final class CValue {
		public LinkedList<CustomLogEntry> lstCustomLogEntry = new LinkedList<CustomLogEntry>();
	}
}
