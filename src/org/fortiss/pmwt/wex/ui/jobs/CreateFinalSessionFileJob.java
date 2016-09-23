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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBException;

import org.fortiss.pmwt.wex.ui.io.session.SessionReader;
import org.fortiss.pmwt.wex.ui.io.session.SessionWriter;
import org.fortiss.pmwt.wex.ui.io.session.StateFilter;
import org.fortiss.pmwt.wex.ui.io.session.model.Session;
import org.fortiss.pmwt.wex.ui.io.session.model.State;
import org.fortiss.pmwt.wex.ui.io.synoptic.SynopticLogWriter;
import org.fortiss.pmwt.wex.ui.persistence.Project;
import org.fortiss.pmwt.wex.ui.persistence.TimeRangeFilter;
import org.fortiss.pmwt.wex.ui.utils.DateUtils;
import org.fortiss.pmwt.wex.ui.utils.NumberUtils;
import org.fortiss.pmwt.wex.ui.utils.SerializationUtils;
import org.fortiss.pmwt.wex.ui.utils.StringUtils;

/**
 * Creates the final session log.
 */

public class CreateFinalSessionFileJob implements IJob<Project> {
	/**
	 * Input/Output/Configuration files.
	 */

	private File m_fInputInitialSessionFile = null;
	private File m_fInputStateFilterFile = null;
	private File m_fInputTimeRangeFilterFile = null;
	private File m_fOutputFinalSessionFile = null;
	private File m_fOutputSynopticLogFile = null;

	/**
	 * State filter reference.
	 */

	private StateFilter m_stateFilter = null;

	/**
	 * Time range filter reference.
	 */

	private TimeRangeFilter m_timeRangeFilter = null;

	/**
	 * Ignored session count by state name.
	 */

	private int m_nIgnoredByStateName = 0;

	/**
	 * Ignored session count by missing mandatory state name.
	 */

	private int m_nIgnoredByMissingMandatoryName = 0;

	/**
	 * Ignored session count by time range.
	 */

	private int m_nIgnoredByTimeRange = 0;

	/**
	 * Ignored session count by missing start/end states.
	 */

	private int m_nIgnoredByStartAndEndStateName = 0;

	/**
	 * Amount of processed sessions.
	 */

	private int m_nSessionCount = 0;

	/**
	 * Start timestamp.
	 */

	private long m_lRangeFrom = 0;

	/**
	 * Stop timestamp.
	 */

	private long m_lRangeTo = 0;

	/**
	 * State names to ignore.
	 */

	private String[] m_strStatesToIgnoreArray = null;

	/**
	 * Mandatory state names.
	 */

	private String[] m_strMandatoryStateNameArray = null;

	/**
	 * State name a session must start with.
	 */

	private String m_strSessionMustStartWithStateName = null;

	/**
	 * State name a session must end with.
	 */

	private String m_strSessionMustEndWithStateName = null;

	/**
	 * Amount of splitted sessions.
	 */

	private int m_nAmountOfSplittedSessions = 0;

	/**
	 * Current project.
	 */

	private Project m_project = null;

	/**
	 * Flag to indicate if the transition threshold time should be considered.
	 */

	private boolean m_bConsiderTransitionThresholdTime = false;

	/**
	 * Transition threshold time.
	 */

	private long m_lTransitionThresholdTime = 0;

	/**
	 * Main entry.
	 */

	@Override
	public void run(Project project) throws Exception {
		this.m_project = project;

		// -- Set input and output files
		initFiles();

		// -- Configuration
		initConfiguration();

		// -- Create final session file
		createFinalSessionFile();
	}

	/**
	 * Initalize files.
	 */

	private void initFiles() {
		// -- Input files
		this.m_fInputInitialSessionFile = this.m_project
				.getFile(Project.TAG_FILE_INITIAL_SESSION);
		this.m_fInputStateFilterFile = this.m_project
				.getFile(Project.TAG_FILE_STATE_FILTER_CONFIGURATION);
		this.m_fInputTimeRangeFilterFile = this.m_project
				.getFile(Project.TAG_FILE_TIME_RANGE_FILTER_CONFIGURATION);

		// -- Output files
		this.m_fOutputFinalSessionFile = this.m_project.addFile(
				Project.TAG_FILE_FINAL_SESSION, Project.FILENAME_FINAL_SESSION);
		this.m_fOutputSynopticLogFile = this.m_project.addFile(
				Project.TAG_FILE_SYNOPTIC_LOG_FILE,
				Project.FILENAME_SYNOPTIC_LOG);
	}

	/**
	 * Initalize configurations.
	 * 
	 * @throws JAXBException
	 *             Occurs, if something unexpected happens.
	 */

	private void initConfiguration() throws JAXBException {
		// -- State filter
		this.m_stateFilter = SerializationUtils.deserializeFromXML(
				this.m_fInputStateFilterFile, StateFilter.class);

		// -- State filter / Transition threshold time
		Long lTransitionThresholdTime = this.m_stateFilter
				.getTransitionThresholdTime();
		if (lTransitionThresholdTime != null
				&& lTransitionThresholdTime.longValue() > 0) {
			this.m_bConsiderTransitionThresholdTime = true;
			this.m_lTransitionThresholdTime = lTransitionThresholdTime
					.longValue();
		}

		// -- Retrieve array of to-be ignored states
		this.m_strStatesToIgnoreArray = getStateToIgnoreArray(this.m_stateFilter);

		// -- Retrieve array of must-be-in-session states
		this.m_strMandatoryStateNameArray = getMandatoryStateArray(this.m_stateFilter);

		// -- Session must start/end with use case
		this.m_strSessionMustStartWithStateName = StringUtils
				.toNull(StringUtils.toEmpty(
						this.m_stateFilter.getSessionMustStartWithUseCase())
						.toLowerCase());
		this.m_strSessionMustEndWithStateName = StringUtils.toNull(StringUtils
				.toEmpty(this.m_stateFilter.getSessionMustEndWithUseCase())
				.toLowerCase());

		// -- Time Range filter
		this.m_timeRangeFilter = SerializationUtils.deserializeFromXML(
				this.m_fInputTimeRangeFilterFile, TimeRangeFilter.class);

		// -- Retrieve time range
		Long lngDataStartTime = (Long) this.m_project
				.getValue(Project.TAG_VALUE_FIRST_TIMESTAMP);
		Long lngDataStopTime = (Long) this.m_project
				.getValue(Project.TAG_VALUE_LAST_TIMESTAMP);

		this.m_lRangeFrom = lngDataStartTime.longValue()
				+ TimeUnit.MINUTES.toMillis(this.m_timeRangeFilter
						.getCropMinutesFromTheLeft());
		this.m_lRangeTo = lngDataStopTime.longValue()
				- TimeUnit.MINUTES.toMillis(this.m_timeRangeFilter
						.getCropMinutesFromTheRight());

		System.out.println("- Crop minutes from the left: "
				+ this.m_timeRangeFilter.getCropMinutesFromTheLeft());
		System.out.println("- Crop minutes from the right: "
				+ this.m_timeRangeFilter.getCropMinutesFromTheRight());
		System.out.println("- Left timestamp: " + lngDataStartTime.longValue()
				+ " -> "
				+ DateUtils.FORMAT_LONG.format(lngDataStartTime.longValue()));
		System.out.println("- Right timestamp: " + lngDataStopTime.longValue()
				+ " -> "
				+ DateUtils.FORMAT_LONG.format(lngDataStopTime.longValue()));
		System.out.println("- Left result: " + this.m_lRangeFrom + " -> "
				+ DateUtils.FORMAT_LONG.format(this.m_lRangeFrom));
		System.out.println("- Right result: " + this.m_lRangeTo + " -> "
				+ DateUtils.FORMAT_LONG.format(this.m_lRangeTo));
	}

	/**
	 * Rewrites the initial session file. Rewriting includes filtering by state
	 * name, rewriting of state names, and filtering sessions by time range.
	 * 
	 * @param fInputSessionFile
	 *            Initial session file to be rewritten.
	 * @param fOutputNewSessionFile
	 *            New session file.
	 * @param stateFilter
	 *            Instance of a state filter.
	 * @param timeRangeFilter
	 *            Instance of a time range filter.
	 * @throws IOException
	 *             Thrown, if there are problems with
	 *             accessing/reading/writing/... files.
	 */

	private void createFinalSessionFile() throws IOException {
		// -- Rewrite using a modified use case mapping
		try (SessionReader reader = new SessionReader();
				SessionWriter sessionWriter = new SessionWriter();
				SynopticLogWriter synopticLogWriter = new SynopticLogWriter()) {
			// -- Synoptic log file
			synopticLogWriter.open(this.m_fOutputSynopticLogFile);

			// -- Session file
			reader.open(this.m_fInputInitialSessionFile); // IOException
			sessionWriter.open(this.m_fOutputFinalSessionFile); // IOException

			Session session = null;
			while ((session = reader.readSession()) != null) // IOException
			{
				this.m_nSessionCount++;

				//
				// -- Replace state names in session
				//

				this.m_stateFilter.replaceStateNamesInSession(session);

				//
				// -- Ignore session by first and last state name
				//

				if (!containsFirstAndLastStateName(session)) {
					this.m_nIgnoredByStartAndEndStateName++;
					continue;
				}

				//
				// -- Ignore session by state name
				//

				if (this.m_strStatesToIgnoreArray != null
						&& containsStateToIgnore(session)) {
					this.m_nIgnoredByStateName++;
					continue;
				}

				//
				// -- Ignore session if not all mandatory state names are in
				//

				if (this.m_strMandatoryStateNameArray != null
						&& !containsMandatoryStates(session)) {
					this.m_nIgnoredByMissingMandatoryName++;
					continue;
				}

				//
				// -- Ignore session by time range
				//

				long lSessionStart = session.getFirstTimestampInSession();
				long lSessionStop = session.getLastTimestampInSession();

				if (lSessionStart >= this.m_lRangeFrom
						&& lSessionStop <= this.m_lRangeTo) {
					// -- Desired clause
				} else {
					boolean b1 = lSessionStart < this.m_lRangeFrom;
					boolean b2 = lSessionStop > this.m_lRangeTo;
					if (b1 || b2) {
						System.out
								.println("[Core] Session ignored due to timerange: ");
						if (b1) {
							System.out.println("TR.First < TR.From: "
									+ DateUtils.toString(lSessionStart) + " ("
									+ lSessionStart + ") < "
									+ DateUtils.toString(this.m_lRangeFrom)
									+ " (" + this.m_lRangeFrom + ")");
						}
						if (b2) {
							System.out.println("TR.Last > TR.To: "
									+ DateUtils.toString(lSessionStop) + " > "
									+ DateUtils.toString(this.m_lRangeTo));
						}

						System.out.println();

						this.m_nIgnoredByTimeRange++;
					}

					// -- Next session
					continue;
				}

				//
				// -- Handle transition threshold times
				//

				Session[] sessionArray = null;
				if (this.m_bConsiderTransitionThresholdTime) {
					sessionArray = splitSessionByTransitionThresholdTime(
							session, this.m_lTransitionThresholdTime);
					this.m_nAmountOfSplittedSessions++;
				} else {
					sessionArray = new Session[] { session };
				}

				//
				// -- Write
				//

				for (Session session0 : sessionArray) {
					// -- Write final session
					sessionWriter.writeSession(session0); // IOException

					// -- Write synoptic session log
					synopticLogWriter.writeSession(session0); // IOException
				}
			}
		}

		System.out.println("Session count: " + this.m_nSessionCount
				+ " (splitted: " + this.m_nAmountOfSplittedSessions + ")");
		System.out.println("Ignored (state name to ignore): "
				+ this.m_nIgnoredByStateName
				+ " ("
				+ NumberUtils.toPercent(this.m_nIgnoredByStateName,
						this.m_nSessionCount) + ")");
		System.out.println("Ignored (missing state name): "
				+ this.m_nIgnoredByMissingMandatoryName
				+ " ("
				+ NumberUtils.toPercent(this.m_nIgnoredByMissingMandatoryName,
						this.m_nSessionCount) + ")");
		System.out.println("Ignored (time range): "
				+ this.m_nIgnoredByTimeRange
				+ " ("
				+ NumberUtils.toPercent(this.m_nIgnoredByTimeRange,
						this.m_nSessionCount) + ")");
		System.out.println("Ignored (start and stop state name): "
				+ this.m_nIgnoredByStartAndEndStateName
				+ " ("
				+ NumberUtils.toPercent(this.m_nIgnoredByStartAndEndStateName,
						this.m_nSessionCount) + ")\n");
	}

	/**
	 * Helper method that extracts all states to be ignored from the state
	 * filter.
	 * 
	 * @param stateFilter
	 *            Instance of a state filter.
	 * @return String-array that contains all state names that should be
	 *         ignored.
	 */

	private String[] getStateToIgnoreArray(StateFilter stateFilter) {
		List<String> lstRemoveStateName = new ArrayList<String>();

		Map<String, StateFilter.CState> mapState = stateFilter.getStateMap();
		Iterator<Map.Entry<String, StateFilter.CState>> iterator = mapState
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, StateFilter.CState> mapEntry = iterator.next();
			StateFilter.CState state = mapEntry.getValue();

			if (state.isRemoveState()) {
				lstRemoveStateName.add(state.getOldStateName());
			}
		}

		return lstRemoveStateName.size() > 0 ? lstRemoveStateName
				.toArray(new String[0]) : null;
	}

	/**
	 * Retrieves mandatory state names.
	 * 
	 * @param stateFilter
	 *            State filter reference.
	 * @return Array with mandatory state names.
	 */

	private String[] getMandatoryStateArray(StateFilter stateFilter) {
		List<String> lstMandatoryState = new ArrayList<String>();

		Map<String, StateFilter.CState> mapState = stateFilter.getStateMap();
		Iterator<Map.Entry<String, StateFilter.CState>> iterator = mapState
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, StateFilter.CState> mapEntry = iterator.next();
			StateFilter.CState state = mapEntry.getValue();

			if (state.isMandatoryState()) {
				lstMandatoryState.add(state.getOldStateName());
			}
		}

		return lstMandatoryState.size() > 0 ? lstMandatoryState
				.toArray(new String[0]) : null;
	}

	/**
	 * Checks if the session should be ignored according to included states.
	 * 
	 * @param strIgnoreStateArray
	 *            Array that contains all states that should mark the session as
	 *            ignorable.
	 * @param session
	 *            Session that includes multiple states.
	 * @return true, if a state was found in the session that should be ignored,
	 *         false otherwise.
	 */

	private boolean containsStateToIgnore(Session session) {
		for (String strIgnoreState : this.m_strStatesToIgnoreArray) {
			for (State state : session.getStateList()) {
				if (state.getName().equals(strIgnoreState)) {
					System.out.println("[Core] Session '"
							+ session.getSessionID()
							+ "' ignored due to state name '" + strIgnoreState
							+ "'.");
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Checks if session contains first and last state names.
	 * 
	 * @param session
	 *            Current session.
	 * @return true if the session contains both state names, null otherweise.
	 */

	private boolean containsFirstAndLastStateName(Session session) {
		if (this.m_strSessionMustStartWithStateName != null) {
			String strStartUseCase = session.getStateList().get(0).getName()
					.toLowerCase(); // Provoke NullPointerException
			if (!this.m_strSessionMustStartWithStateName
					.equals(strStartUseCase)) {
				return false;
			}
		}

		if (this.m_strSessionMustEndWithStateName != null) {
			String strEndUseCase = session.getStateList()
					.get(session.getStateCount() - 1).getName().toLowerCase(); // Provoke
																				// NullPointerException
			if (!this.m_strSessionMustEndWithStateName.equals(strEndUseCase)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks if session contains mandatory state names.
	 * 
	 * @param session
	 *            Current session.
	 * @return true if the session contains all mandator state names, false
	 *         otherwise.
	 */

	private boolean containsMandatoryStates(Session session) {
		String[] strMandatoryNameArray = this.m_strMandatoryStateNameArray;

		boolean[] bMandatoryNameArray = new boolean[strMandatoryNameArray.length];
		Arrays.fill(bMandatoryNameArray, false);

		for (State state : session.getStateList()) {
			for (int i = 0; i < strMandatoryNameArray.length; i++) {
				String strMandatoryName = strMandatoryNameArray[i];
				String strStateName = state.getName();
				if (strMandatoryName.equals(strStateName)) {
					bMandatoryNameArray[i] = true;
				}
			}
		}

		for (boolean bValue : bMandatoryNameArray) {
			if (!bValue) {
				return false;
			}
		}

		return true;
	}

	private Session[] splitSessionByTransitionThresholdTime(Session session,
			long lTransitionThresholdTime) {
		State[] arrState = session.getStateList().toArray(new State[0]);

		// -- Split indices
		int[] nSplitIndexArray = new int[arrState.length + 1]; // +2 !!!
		Arrays.fill(nSplitIndexArray, -1);
		nSplitIndexArray[0] = 0;
		int nSplitCounter = 1;

		for (int i = 0; i < arrState.length - 1; i++) {
			State state0 = arrState[i];
			State state1 = arrState[i + 1];

			long lTimestamp0 = state0.getTimeOut().longValue();
			long lTimestamp1 = state1.getTimeIn().longValue();

			long lDiff = lTimestamp1 - lTimestamp0;
			if (lDiff > lTransitionThresholdTime) {
				nSplitIndexArray[nSplitCounter++] = i + 1;
			}
		}

		// -- No splitting necessary
		if (nSplitCounter == 1) {
			return new Session[] { session };
		}

		// -- Splitting necessary
		List<Session> lstSession = new ArrayList<Session>();
		nSplitIndexArray[nSplitCounter] = arrState.length;
		for (int i = 0; i < nSplitCounter; i++) {
			int nSplitIndex0 = nSplitIndexArray[i];
			int nSplitIndex1 = nSplitIndexArray[i + 1];

			State[] arrSubState = Arrays.copyOfRange(arrState, nSplitIndex0,
					nSplitIndex1);

			Session newSession = new Session(session.getSessionID() + "_split_"
					+ nSplitIndex0 + "->" + nSplitIndex1);
			newSession.addStates(arrSubState);

			lstSession.add(newSession);
		}

		return lstSession.toArray(new Session[0]);
	}
}