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
