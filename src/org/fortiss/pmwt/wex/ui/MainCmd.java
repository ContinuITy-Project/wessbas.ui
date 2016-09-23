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

package org.fortiss.pmwt.wex.ui;

import java.io.File;

import org.fortiss.pmwt.wex.ui.jobs.CreateFinalSessionFileJob;
import org.fortiss.pmwt.wex.ui.jobs.CreateInitialSessionFileJob;
import org.fortiss.pmwt.wex.ui.jobs.TransformJob;
import org.fortiss.pmwt.wex.ui.persistence.Project;
import org.fortiss.pmwt.wex.ui.utils.FileUtils;

/**
 * Allows the use of the WESSBAS UI functionality via command line.
 */

public class MainCmd {
	/**
	 * Path to project file.
	 */

	private File m_fProjectFile = null;

	/**
	 * Input data (webserver).
	 */

	private File m_fInputFile = null;

	/**
	 * Main entry point for windowsless WESSBAS UI.
	 * 
	 * @param args
	 *            Command line arguments: #0 Project file. #1 Input file
	 *            (webserver).
	 */

	public static void main(String args[]) {
		// -- Simple argument handling: <project file> <input file>
		File fProjectFile = FileUtils.getFile(args, 0);
		File fInputFile = FileUtils.getFile(args, 1);

		System.out.println("Project file: " + fProjectFile.getPath());
		System.out.println("Input file: " + fInputFile.getPath());

		// -- MainCmd
		MainCmd main = new MainCmd(fProjectFile, fInputFile);
		try {
			main.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param fProjectFile
	 *            Path to project file.
	 * @param fInputFile
	 *            Path to input file (webserver).
	 */

	public MainCmd(File fProjectFile, File fInputFile) {
		this.m_fProjectFile = fProjectFile;
		this.m_fInputFile = fInputFile;
	}

	/**
	 * Convenient method for executing the windowless WESSBAS UI from source.
	 * 
	 * @param fProjectFile
	 *            Path to project file.
	 * @param fInputFile
	 *            Path to input file (webserver)
	 */

	public static void execute(File fProjectFile, File fInputFile) {
		main(new String[] { fProjectFile.getPath(), fInputFile.getPath() });
	}

	/**
	 * Worker thread for WESSBAS UI functionality.
	 * 
	 * @throws Exception
	 *             Occurs when something unexpected happens!
	 */

	private void run() throws Exception {
		// -- Load project file
		Project project = Project.restore(this.m_fProjectFile); // JAXBException
		project.addFile(Project.TAG_FILE_CSV, this.m_fInputFile);

		// -- Create initial session file
		System.out.println("-> Create initial session file...");
		CreateInitialSessionFileJob job00 = new CreateInitialSessionFileJob();
		job00.run(project);

		// -- Create final session file
		System.out.println(" -> Create final session file...");
		CreateFinalSessionFileJob job01 = new CreateFinalSessionFileJob();
		job01.run(project);

		// -- Transform
		System.out.println("-> Transformation to usage model");
		TransformJob job02 = new TransformJob();
		job02.run(project);

		System.out.println("-> End");
	}
}