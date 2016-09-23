package org.fortiss.pmwt.wex.ui.metrics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.fortiss.pmwt.wex.ui.io.session.model.Session;

/**
 * Allows to extract some simple metrics from a session log.
 */

public class SimpleMetrics {
	/**
	 * Amount of sessions in the session log.
	 */

	private int m_nSessionCount = -1;

	/**
	 * Minimum event count in a single session over all sessions.
	 */

	private int m_nMinEventCountInSession = -1;

	/**
	 * Maximum event count in a single session over all sessions.
	 */
	private int m_nMaxEventCountInSession = -1;

	/**
	 * Amount of events over all sessions.
	 */

	private int m_nEventCountOverAllSessions = -1;

	/**
	 * Start time of session log.
	 */

	private long m_lObservationStartTime = -1;

	/**
	 * Stop time of session log.
	 */

	private long m_lObservationStopTime = -1;

	/**
	 * Start time for filtering.
	 */

	private long m_lFilterStartTime = -1;

	/**
	 * Stop time for filtering.
	 */

	private long m_lFilterStopTime = -1;

	/**
	 * Active session count grouped by time segment.
	 */

	private Map<String, Integer> m_mapActiveSessionCount = null;

	/**
	 * Session arrival rate grouped by time segment.
	 */

	private Map<String, Integer> m_mapArrivalRate = null;

	/**
	 * Key for time segment.
	 */

	private static final SimpleDateFormat S_SDF_KEY_SECOND = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * Constructor.
	 */

	public SimpleMetrics() {
		this.m_mapActiveSessionCount = new HashMap<String, Integer>(); // <key:yyyy_mm_dd_hh_mm_ss><val:counter>
		this.m_mapArrivalRate = new HashMap<String, Integer>(); // <key:yyyy_mm_dd_hh_mm_ss><val:counter>

		this.m_nMinEventCountInSession = Integer.MAX_VALUE;
		this.m_nMaxEventCountInSession = Integer.MIN_VALUE;
		this.m_nSessionCount = 0;
		this.m_nEventCountOverAllSessions = 0;

		this.m_lFilterStartTime = Long.MIN_VALUE;
		this.m_lFilterStopTime = Long.MAX_VALUE;
		this.m_lObservationStartTime = Long.MAX_VALUE;
		this.m_lObservationStopTime = Long.MIN_VALUE;
	}

	/**
	 * Sets the start time for filtering.
	 * 
	 * @param lTimestamp
	 *            Start time for filtering.
	 */

	public void setFilterStartTime(long lTimestamp) {
		this.m_lFilterStartTime = lTimestamp;
	}

	/**
	 * Sets the stop time for filtering.
	 * 
	 * @param lTimestamp
	 *            Stop time for filtering.
	 */

	public void setFilterStopTime(long lTimestamp) {
		this.m_lFilterStopTime = lTimestamp;
	}

	/**
	 * Creates a segment array (keys).
	 * 
	 * @return An array with time segments according to the observed time period
	 *         in the session log.
	 */

	public String[] createSegmentArray() {
		List<String> lstKey = new ArrayList<String>();

		long lTimestamp = this.m_lObservationStartTime;
		long lTimestampStop = this.m_lObservationStopTime;

		do {
			lstKey.add(createKeyBySeconds(lTimestamp));
			lTimestamp += 1000;
		} while (lTimestamp < lTimestampStop);

		return (String[]) lstKey.toArray(new String[0]);
	}

	/**
	 * Processes a session and acquires statistics.
	 * 
	 * @param session
	 *            The current session.
	 */

	public void handleSession(Session session) {
		boolean bFilter = session.getFirstTimestampInSession() >= this.m_lFilterStartTime
				&& session.getLastTimestampInSession() <= this.m_lFilterStopTime;
		if (!bFilter) {
			return;
		}

		// -- Active session count
		processActiveSessionCount(session);

		// -- Arrival rate
		processArrivalRate(session);

		// -- Simple statistics
		this.m_lObservationStartTime = Math.min(this.m_lObservationStartTime,
				session.getFirstTimestampInSession());
		this.m_lObservationStopTime = Math.max(this.m_lObservationStopTime,
				session.getLastTimestampInSession());
		this.m_nSessionCount++;
		this.m_nMinEventCountInSession = Math.min(
				this.m_nMinEventCountInSession, session.getStateCount());
		this.m_nMaxEventCountInSession = Math.max(
				this.m_nMaxEventCountInSession, session.getStateCount());
		this.m_nEventCountOverAllSessions += session.getStateCount();

	}

	/**
	 * Extract the active session count.
	 * 
	 * @param session
	 *            The current session.
	 */

	private void processActiveSessionCount(Session session) {
		long lValue = session.getFirstTimestampInSession();

		do {
			String strSegment = createKeyBySeconds(lValue);
			addOne(this.m_mapActiveSessionCount, strSegment);
			lValue += 1000;
		} while (lValue < session.getLastTimestampInSession());
	}

	/**
	 * Extract the arrival rate.
	 * 
	 * @param session
	 *            The current session.
	 */

	private void processArrivalRate(Session session) {
		String strSegment = createKeyBySeconds(session
				.getFirstTimestampInSession());
		addOne(this.m_mapArrivalRate, strSegment);
	}

	/**
	 * @return Average session length according to event counts.
	 */

	public double getAverageSessionLengthByEventCount() {
		double dblResult = ((double) this.m_nEventCountOverAllSessions)
				/ ((double) this.m_nSessionCount);
		return Math.round(dblResult * 100.0) / 100.0;
	}

	/**
	 * @return Average active session count.
	 */

	public double getAverageActiveSessionCount() {
		int nActiveSessionCountOverAllSegments = 0;
		int nSegmentCount = 0;

		Iterator<Map.Entry<String, Integer>> iterator = this.m_mapActiveSessionCount
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Integer> mapEntry = iterator.next();
			nActiveSessionCountOverAllSegments += mapEntry.getValue()
					.intValue();

			nSegmentCount++;
		}

		double dblValue = ((double) nActiveSessionCountOverAllSegments)
				/ ((double) nSegmentCount);
		return Math.round(dblValue * 100.0) / 100.0;
	}

	/**
	 * @return Average session arrival rate.
	 */

	public double getAverageArrivalRate() {
		int nArrivalRate = 0;
		int nSegmentCount = 0;

		Iterator<Map.Entry<String, Integer>> iterator = this.m_mapArrivalRate
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Integer> mapEntry = iterator.next();
			nArrivalRate += mapEntry.getValue().intValue();
			nSegmentCount++;
		}

		double dblValue = ((double) nArrivalRate) / ((double) nSegmentCount);
		return Math.round(dblValue * 100.0) / 100.0;
	}

	/**
	 * @return Overall session count.
	 */

	public int getSessionCount() {
		return this.m_nSessionCount;
	}

	/**
	 * @return Minimum event count in a single session over all sessions.
	 */

	public int getMinEventCountInSession() {
		return this.m_nMinEventCountInSession;
	}

	/**
	 * @return Maximum event count in a single session over all sessions.
	 */

	public int getMaxEventCountInSession() {
		return this.m_nMaxEventCountInSession;
	}

	/**
	 * @return Overall event count.
	 */

	public int getEventCountOverAllSessions() {
		return this.m_nEventCountOverAllSessions;
	}

	public Map<String, Integer> getActiveSessionMap() {
		return this.m_mapActiveSessionCount;
	}

	/**
	 * @return Map with arrival rates grouped by time segment.
	 */

	public Map<String, Integer> getArrivalRateMap() {
		return this.m_mapArrivalRate;
	}

	/**
	 * Generic method that adds one to a integer map value.
	 * 
	 * @param map
	 *            Map to handle.
	 * @param strKey
	 *            Key of the value.
	 */

	private void addOne(Map<String, Integer> map, String strKey) {
		Integer iValue = map.get(strKey);
		if (iValue != null) {
			map.put(strKey, new Integer(iValue.intValue() + 1));
		} else {
			map.put(strKey, new Integer(1));
		}
	}

	/**
	 * Creates a key from a timestamp.
	 * 
	 * @param lTimestampInMillis
	 *            Milliseconds the key is based on.
	 * @return Key that represents the given milliseconds.
	 */

	private String createKeyBySeconds(long lTimestampInMillis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(lTimestampInMillis);

		String strKey = S_SDF_KEY_SECOND.format(calendar.getTime());
		return strKey;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		StringBuilder sbOut = new StringBuilder();

		sbOut.append("SessionStateCountMin: " + this.m_nMinEventCountInSession
				+ "\n");
		sbOut.append("SessionStateCountMax: " + this.m_nMaxEventCountInSession
				+ "\n");
		sbOut.append("SessionCount: " + this.m_nSessionCount + "\n");
		sbOut.append("AllStateCount: " + this.m_nEventCountOverAllSessions
				+ "\n");
		sbOut.append("FirstSessionStartTime: "
				+ createKeyBySeconds(this.m_lObservationStartTime) + "\n");
		sbOut.append("LastSessionStopTime: "
				+ createKeyBySeconds(this.m_lObservationStopTime) + "\n");
		sbOut.append("FilterStartTime: "
				+ createKeyBySeconds(this.m_lFilterStartTime) + "\n");
		sbOut.append("FilterStopTime: "
				+ createKeyBySeconds(this.m_lFilterStopTime) + "\n");
		sbOut.append("Average Session Length: "
				+ getAverageSessionLengthByEventCount() + "\n");

		// -- Active session count
		sbOut.append("\n\nActive Session Count:\n");
		debugOut(sbOut, this.m_mapActiveSessionCount);

		// -- Arrival rate
		sbOut.append("\n\nArrival Rate:\n");
		debugOut(sbOut, this.m_mapArrivalRate);

		return sbOut.toString();
	}

	/**
	 * Debug only.
	 */

	private void debugOut(StringBuilder sbOut, Map<String, Integer> map) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(this.m_lObservationStartTime);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);

		String strCurrentKey = createKeyBySeconds(calendar.getTimeInMillis());

		while (calendar.getTimeInMillis() < this.m_lObservationStopTime) {
			sbOut.append(strCurrentKey + " = ");

			Integer iValue = map.get(strCurrentKey);
			sbOut.append(iValue != null ? iValue.toString() : "---");
			sbOut.append("\n");

			calendar.add(Calendar.MINUTE, 1);
			strCurrentKey = createKeyBySeconds(calendar.getTimeInMillis());
		}
	}
}
