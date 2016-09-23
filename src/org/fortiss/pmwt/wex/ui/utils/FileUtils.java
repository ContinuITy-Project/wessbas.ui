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

package org.fortiss.pmwt.wex.ui.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Utility functions related to files.
 */

public class FileUtils {
	/**
	 * Checks if a file (not directory) exists.
	 * 
	 * @param file
	 *            File to be checked.
	 * @return true if file exists, false otherwise.
	 */
	public static boolean fileExists(File file) {
		return file != null && file.exists() && file.isFile();
	}

	/**
	 * Concatenate multiple files to a single one.
	 * 
	 * @param fOutputFile
	 *            Output file to which the provided files will be appended to.
	 * @param arrInputFileArray
	 *            Array of input files to be concatenated.
	 * @throws IOException
	 *             Thrown, if there are problems with
	 *             accessing/reading/writing/... files.
	 */

	public static void concat(File fOutputFile, File... arrInputFileArray)
			throws IOException {
		fOutputFile.createNewFile();

		FileWriter fileWriter = new FileWriter(fOutputFile);

		for (File fInputFile : arrInputFileArray) {
			FileReader fileReader = new FileReader(fInputFile);

			char[] cBuffer = new char[2048];
			int nRead = 0;

			while ((nRead = fileReader.read(cBuffer, 0, cBuffer.length)) > 0) {
				fileWriter.write(cBuffer, 0, nRead);
			}

			fileReader.close();
		}

		fileWriter.close();
	}

	/**
	 * Concatenates multiple files to a single one.
	 * 
	 * @param fOutputFile
	 *            Output file for the concatenation.
	 * @param arrInputFileArray
	 *            Input files to concat.
	 * @throws IOException
	 *             Occurs if something unexpected happens.
	 */

	public static void concatEx(File fOutputFile, File... arrInputFileArray)
			throws IOException {
		fOutputFile.createNewFile();

		FileWriter fwOut = new FileWriter(fOutputFile);

		for (File fInputFile : arrInputFileArray) {
			FileReader frIn = new FileReader(fInputFile);
			BufferedReader brIn = new BufferedReader(frIn);

			String strLine = null;
			while ((strLine = brIn.readLine()) != null) {
				if (strLine.length() == 0) {
					continue;
				}

				fwOut.write(strLine + "\n");
			}

			brIn.close();
			frIn.close();
		}

		fwOut.close();
	}

	/**
	 * Retrieve all files in a directory by file extension.
	 * 
	 * @param strDirectoryPath
	 *            Directory to look for files.
	 * @param strFileExtension
	 *            Extension to look for in a file.
	 * @return Array of all files that end with the desired file extension.
	 * @throws IOException
	 *             Thrown, if there are problems with
	 *             accessing/reading/writing/... files.
	 */

	public static File[] retrieveFileArrayByExtension(String strDirectoryPath,
			final String strFileExtension) throws IOException {
		return (new File(strDirectoryPath)).listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isFile()
						&& file.getName().endsWith(strFileExtension);
			}
		});
	}

	/**
	 * Converts the path of a file to its string representation.
	 * 
	 * @param file
	 *            Path to be represented as string.
	 * @return Path of the file.
	 */

	public static String toPathString(File file) {
		return file != null ? file.getPath() : "";
	}

	/**
	 * Copies a file to a target directory.
	 * 
	 * @param fSourceFile
	 *            File to be copied.
	 * @param fTargetDirectory
	 *            Target directory.
	 * @throws IOException
	 *             Thrown, if there are problems with
	 *             accessing/reading/writing/... files.
	 */

	public static void copyFileToDirectory(File fSourceFile,
			File fTargetDirectory) throws IOException {
		if (!fTargetDirectory.exists()) {
			fTargetDirectory.mkdirs();
		}

		Files.copy(fSourceFile.toPath(), fTargetDirectory.toPath(),
				StandardCopyOption.REPLACE_EXISTING);
	}

	/**
	 * Adds a file separator to the end of a path.
	 * 
	 * @param file
	 *            File that represents a path.
	 * @return String representation of the provided path that ends with a file
	 *         separator.
	 */

	public static String toPath(File file) {
		boolean bValue = file.getPath().endsWith("/")
				|| file.getPath().endsWith("\\");
		return !bValue ? file.getPath() + File.separator : file.getPath();
	}

	/**
	 * Converts a path with backslashes to a path with slashes.
	 * 
	 * @param strPath
	 *            Path to be converted.
	 * @return Path with slashes as file separator.
	 */

	public static String toSlashPath(String strPath) {
		return strPath.replaceAll("\\\\", "/");
	}

	/**
	 * Extracts the file name of a full path.
	 * 
	 * @param strPath
	 *            Path to a file.
	 * @return Name of the file that is contained in the provided path.
	 */

	public static String extractFileName(String strPath) {
		return strPath.substring(Math.max(strPath.lastIndexOf("\\"),
				strPath.lastIndexOf("/")) + 1);
	}

	/**
	 * Reads the first x lines of a file.
	 * 
	 * @param strPath
	 *            Path to the file to be read.
	 * @param nLineCount
	 *            Number of lines to be read.
	 * @return String array of the lines read.
	 * @throws IOException
	 *             Thrown, if there are problems with
	 *             accessing/reading/writing/... files.
	 */

	public static String[] readLines(String strPath, int nLineCount)
			throws IOException {
		return readLines(strPath, nLineCount, false, false);
	}

	/**
	 * Reads the first x lines of a file.
	 * 
	 * @param strPath
	 *            Path to the file to be read.
	 * @param nLineCount
	 *            Number of lines to be read.
	 * @param bTrim
	 *            Enable trimming of read lines.
	 * @param bRemoveEmpty
	 *            Enable removing empty lines.
	 * @return String array of the lines read.
	 * @throws IOException
	 *             Thrown, if there are problems with
	 *             accessing/reading/writing/... files.
	 */

	public static String[] readLines(String strPath, int nLineCount,
			boolean bTrim, boolean bRemoveEmpty) throws IOException {
		List<String> lstLine = new ArrayList<String>();
		File file = new File(strPath);
		try (FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader)) {
			String strLine = null;
			int nCounter = 0;
			while ((strLine = bufferedReader.readLine()) != null
					&& nCounter <= nLineCount) {
				if (bTrim) {
					strLine = strLine.trim();
				}

				if (bRemoveEmpty && strLine.length() == 0) {
					continue;
				}

				lstLine.add(strLine);
			}
		}

		return lstLine.toArray(new String[0]);
	}

	/**
	 * Reads all lines of a file. Only useful for small files !!!
	 * 
	 * @param strPath
	 *            Path to the file to be read.
	 * @return String array of the lines read.
	 * @throws IOException
	 *             Thrown, if there are problems with
	 *             accessing/reading/writing/... files.
	 */

	public static String[] readLines(String strPath) throws IOException {
		return readLines(strPath, Integer.MAX_VALUE);
	}

	/**
	 * Convenient function to write properties to a file.
	 * 
	 * @param fOutputFile
	 *            Output file the properties file will be written to.
	 * @param properties
	 *            The properties to be written.
	 * @throws IOException
	 *             Thrown, if there are problems with
	 *             accessing/reading/writing/... files.
	 */

	public static void storeProperties(File fOutputFile, Properties properties)
			throws IOException {
		fOutputFile.createNewFile();

		try (FileWriter fileWriter = new FileWriter(fOutputFile);) {
			properties.store(fileWriter, "");
		}
	}

	/**
	 * Convenient function to read properties from a file.
	 * 
	 * @param fInputFile
	 *            Input file to be read.
	 * @return A property instance.
	 * @throws IOException
	 *             Thrown, if there are problems with
	 *             accessing/reading/writing/... files.
	 */

	public static Properties restoreProperties(File fInputFile)
			throws IOException {
		Properties properties = new Properties();
		try (FileReader fileReader = new FileReader(fInputFile)) {
			properties.load(fileReader);
		}

		return properties;
	}

	/**
	 * Reads the contents of a file.
	 * 
	 * @param fInputFile
	 *            The file to read.
	 * @return A string that represents the file content.
	 * @throws IOException
	 *             Thrown, if there are problems with
	 *             accessing/reading/writing/... the file.
	 */

	public static String readFile(File fInputFile) throws IOException {
		StringBuilder sbContent = new StringBuilder((int) fInputFile.length());

		try (FileReader fileReader = new FileReader(fInputFile)) {
			char[] cBuffer = new char[2048];
			int nRead = 0;

			while ((nRead = fileReader.read(cBuffer, 0, cBuffer.length)) > 0) {
				String strValue = new String(cBuffer, 0, nRead);
				sbContent.append(strValue);
			}
		}

		return sbContent.toString();
	}

	/**
	 * Checks if a path is a file and exists.
	 * 
	 * @param strPath
	 *            Path to be checked.
	 * @return true if file exists and is a file (not directory), false
	 *         otherwise.
	 */

	public static boolean fileExists(String strPath) {
		if (strPath != null) {
			File file = new File(strPath);
			return file.isFile() && file.exists();
		}

		return false;
	}

	/**
	 * Creates file objects from given command line arguments.
	 * 
	 * @param args
	 *            Command line arguments.
	 * @param nIndex
	 *            Index of the potential path.
	 * @return File object representing the path at the command line argument
	 *         index.
	 */

	public static File getFile(String[] args, int nIndex) {
		if (args == null || (nIndex + 1) > args.length) {
			System.out.println("Invalid argument index: " + nIndex);
			System.exit(0);
		}

		File file = new File(args[nIndex]);
		if (!file.exists() || !file.isFile()) {
			System.out.println("Path to file \"" + args[nIndex]
					+ "\" not found.");
			System.exit(0);
		}

		return file;
	}
}
