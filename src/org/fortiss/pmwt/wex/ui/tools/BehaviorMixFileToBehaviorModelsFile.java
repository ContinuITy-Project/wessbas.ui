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

package org.fortiss.pmwt.wex.ui.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.fortiss.pmwt.wex.ui.utils.FileUtils;

/**
 * Creates a "behaviorModels.properties" file based on the content of a
 * "behaviormix.txt" file.
 */

public class BehaviorMixFileToBehaviorModelsFile {
	/**
	 * Main entry point.
	 * 
	 * @param fInputBehaviorMixFile
	 *            "behaviormix.txt" file to be read.
	 * @param fOutputBehaviorModelsFile
	 *            "behaviorModels.properties" file to be created.
	 * @param fOutputBehaviorModelDirectory
	 *            Directory that contains the behavior model files.
	 * @throws IOException
	 *             Thrown, if there are problems with
	 *             accessing/reading/writing/... files.
	 */

	public static void create(File fInputBehaviorMixFile,
			File fOutputBehaviorModelsFile, File fOutputBehaviorModelDirectory)
			throws IOException {
		fInputBehaviorMixFile.createNewFile();

		List<CBehaviorEntry> lstContent = readBehaviorMixFile(fInputBehaviorMixFile);
		writeBehaviorModelsFile(fOutputBehaviorModelsFile, lstContent,
				fOutputBehaviorModelDirectory);
	}

	/**
	 * Reads a "behaviormix.txt" file and returns an array of model elements
	 * representing each line read.
	 * 
	 * @param fBehaviorMixFile
	 *            "behaviormix.txt" file.
	 * @return Array of
	 *         {@link org.fortiss.pmwt.wex.ui.tools.BehaviorMixFileToBehaviorModelsFile.CBehaviorEntry}
	 *         elements.
	 * @throws IOException
	 *             Thrown, if there are problems with
	 *             accessing/reading/writing/... files.
	 */

	private static List<CBehaviorEntry> readBehaviorMixFile(
			File fBehaviorMixFile) throws IOException {
		List<CBehaviorEntry> lstBehaviorEntry = new ArrayList<CBehaviorEntry>();

		String strContent = FileUtils.readFile(fBehaviorMixFile); // IOException
		strContent = strContent.substring(strContent.indexOf("= ") + 2);
		strContent = strContent.replace('\\', ' ');

		String[] strLineArray = strContent.split(",");
		for (String strLine : strLineArray) {
			String[] strValueArray = strLine.split(";");

			String strValue0 = strValueArray[0].trim();
			String strValue2 = strValueArray[2].trim();
			String strValue3 = strValueArray[3].trim();
			lstBehaviorEntry.add(new CBehaviorEntry(strValue0, Double
					.parseDouble(strValue2), strValue3));
		}

		return lstBehaviorEntry;
	}

	/**
	 * Writes a "behaviorModels.properties" file based on the content of a
	 * "behaviormix.txt" file.
	 * 
	 * @param fOutputBehaviorModelsFile
	 *            "behaviorModels.properties" file to be written.
	 * @param lstContent
	 *            Previously read content of a "behaviormix.txt" file.
	 * @param fOutputBehaviorModelDirectory
	 *            Output directory that is fully ignored (?).
	 * @throws IOException
	 *             Thrown, if there are problems with
	 *             accessing/reading/writing/... files.
	 */

	private static void writeBehaviorModelsFile(File fOutputBehaviorModelsFile,
			List<CBehaviorEntry> lstContent, File fOutputBehaviorModelDirectory)
			throws IOException {
		// Format: <name><[out]outputFilename><frequency><[in]behaviorFile>
		FileWriter fileWriter = new FileWriter(fOutputBehaviorModelsFile);

		fileWriter.write("behaviorModels = \\\n");

		for (int i = 0; i < lstContent.size(); i++) {
			CBehaviorEntry cbe = lstContent.get(i);

			fileWriter.write(cbe.name + i + ";");

			String strValue0 = FileUtils.toSlashPath(FileUtils
					.toPath(fOutputBehaviorModelDirectory))
					+ FileUtils.extractFileName(cbe.behaviorModelPath);
			String strValue1 = cbe.frequency + "";
			String strValue2 = FileUtils.toSlashPath(cbe.behaviorModelPath);

			fileWriter.write(strValue0 + ";");
			fileWriter.write(strValue1 + ";");
			fileWriter.write(strValue2);

			if (i + 1 < lstContent.size()) {
				fileWriter.write(", \\\n");
			}
		}

		fileWriter.close();
	}

	/**
	 * Represents a single line in a "behaviormix.txt" file.
	 */

	private static final class CBehaviorEntry {
		public String name = null;
		public double frequency = 0.0;
		public String behaviorModelPath = null;

		public CBehaviorEntry(String name, double frequency,
				String behaviorModelPath) {
			this.name = name;
			this.frequency = frequency;
			this.behaviorModelPath = behaviorModelPath;
		}
	}
}
