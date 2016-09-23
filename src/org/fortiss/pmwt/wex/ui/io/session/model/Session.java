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

package org.fortiss.pmwt.wex.ui.io.session.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model that represents a single session.
 */

public class Session {
	private String m_strSessionID = null;
	private List<State> m_lstState = null;
	private long m_lFirstTimestamp = Long.MAX_VALUE;
	private long m_lLastTimestamp = Long.MIN_VALUE;

	@SuppressWarnings("unused")
	private Session() {
	}

	public Session(String strSessionID) {
		this.m_strSessionID = strSessionID;
		this.m_lstState = new ArrayList<State>();
	}

	public String getSessionID() {
		return this.m_strSessionID;
	}

	public long getFirstTimestampInSession() {
		return this.m_lFirstTimestamp;
	}

	public long getLastTimestampInSession() {
		return this.m_lLastTimestamp;
	}

	public void addState(State state) {
		this.m_lstState.add(state);

		this.m_lFirstTimestamp = Math.min(this.m_lFirstTimestamp, state
				.getTimeIn().longValue());
		this.m_lLastTimestamp = Math.max(this.m_lLastTimestamp, state
				.getTimeOut().longValue());
	}

	public void addStates(State[] arrState) {
		for (State state : arrState) {
			addState(state);
		}
	}

	public int getStateCount() {
		return this.m_lstState.size();
	}

	public List<State> getStateList() {
		return this.m_lstState;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof Session)) {
			return false;
		}

		return ((Session) object).getSessionID().equals(this.m_strSessionID);
	}

	@Override
	public int hashCode() {
		return this.m_strSessionID.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sbOut = new StringBuilder();

		sbOut.append("SessionID = " + this.m_strSessionID + "\n");

		int i = 0;
		for (State state : this.m_lstState) {
			sbOut.append(">>>>> (" + (++i) + ")\n");
			sbOut.append(state.toString());
			sbOut.append("<<<<<\n");
		}

		return sbOut.toString();
	}
}