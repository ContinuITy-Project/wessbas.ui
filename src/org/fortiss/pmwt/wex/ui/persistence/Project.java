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

package org.fortiss.pmwt.wex.ui.persistence;

import java.io.File;
import java.util.HashMap;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.fortiss.pmwt.wex.ui.utils.FileUtils;
import org.fortiss.pmwt.wex.ui.utils.SerializationUtils;

/**
 * Represents a project.
 */

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.NONE)
public class Project {
	/**
	 * Parameter keys.
	 */

	public static final String TAG_FILE_GENERIC_READER_CONFIGURATION = "file.configuration.genericReader";
	public static final String TAG_FILE_STATE_FILTER_CONFIGURATION = "file.configuration.stateFilter";
	public static final String TAG_FILE_TIME_RANGE_FILTER_CONFIGURATION = "file.configuration.timeRangeFilter";
	public static final String TAG_FILE_WORKLOAD_PROPERTIES = "file.configuration.workalod";
	public static final String TAG_FILE_WESSBAS2PCM_CONFIGURATION = "file.configuration.wessbas2pcm";
	public static final String TAG_FILE_TRANSFORMATION_CONFIGURATION = "file.configuration.transformation";
	public static final String TAG_FILE_TESTPLAN_PROPERTIES = "file.configuration.testplan";
	public static final String TAG_FILE_SYNOPTIC_PROPERTIES = "file.configuration.synoptic";

	public static final String TAG_FILE_CSV = "file.csv";
	public static final String TAG_FILE_INITIAL_SESSION = "file.session.initial";
	public static final String TAG_FILE_FINAL_SESSION = "file.session.final";

	public static final String TAG_FILE_BEHAVIOR_MIX = "file.behaviorMix";
	public static final String TAG_FILE_USECASES = "file.useCases";
	public static final String TAG_FILE_FLOWS = "file.flows";
	public static final String TAG_FILE_BEHAVIOR_MODELS = "file.behaviorModels";
	public static final String TAG_FILE_WORKLOAD_MODEL_XMI = "file.workloadModelXmi";
	public static final String TAG_FILE_GRAPH = "file.graph";
	public static final String TAG_FILE_TESTPLAN = "file.testplan";
	public static final String TAG_FILE_SYNOPTIC_LOG_FILE = "file.synoptic.log";

	public static final String TAG_VALUE_FIRST_TIMESTAMP = "value.session.timestamp.first";
	public static final String TAG_VALUE_LAST_TIMESTAMP = "value.session.timestamp.last";

	// @formatter:off

	public static final String[] TAG_FILE = { TAG_FILE_CSV,
			TAG_FILE_GENERIC_READER_CONFIGURATION, TAG_FILE_INITIAL_SESSION,
			TAG_FILE_STATE_FILTER_CONFIGURATION,
			TAG_FILE_TIME_RANGE_FILTER_CONFIGURATION,
			TAG_FILE_WORKLOAD_PROPERTIES, TAG_FILE_WESSBAS2PCM_CONFIGURATION,
			TAG_FILE_FINAL_SESSION, TAG_FILE_BEHAVIOR_MIX, TAG_FILE_USECASES,
			TAG_FILE_FLOWS, TAG_FILE_BEHAVIOR_MODELS,
			TAG_FILE_WORKLOAD_MODEL_XMI, TAG_FILE_GRAPH,
			TAG_FILE_TRANSFORMATION_CONFIGURATION,
			TAG_FILE_SYNOPTIC_PROPERTIES, TAG_FILE_SYNOPTIC_LOG_FILE };

	// @formatter:on

	/**
	 * Default parameter values.
	 */

	public static final String FILENAME_INITIAL_SESSION = "session_initial.txt";
	public static final String FILENAME_FINAL_SESSION = "session_final.txt";
	public static final String FILENAME_PROJECT = "project.xml";
	public static final String FILENAME_STATE_FILTER = "state_filter.xml";
	public static final String FILENAME_TIME_RANGE_FILTER = "time_range_filter.xml";
	public static final String FILENAME_WOKRLOAD_PROPERTIES = "workloadIntensity.properties";
	public static final String FILENAME_BEHAVIOR_MIX = "behaviormix.txt";
	public static final String FILENAME_USECASES = "usecases.txt";
	public static final String FILENAME_FLOW_FILE = "usecases.flows";
	public static final String FILENAME_BEHAVIOR_MODELS = "behaviorModels.properties";
	public static final String FILENAME_WORKLOAD_XMI = "workloadmodel.xmi";
	public static final String FILENAME_GRAPH = "graph.dot";
	public static final String FILENAME_WESSBAS2PCM = "wessbas2pcm.properties";
	public static final String FILENAME_TRANSFORMATION = "transformation.properties";
	public static final String FILENAME_TESTPLAN = "testplan.jmx";
	public static final String FILENAME_CSV = "input.dat";
	public static final String FILENAME_SYNOPTIC = "synoptic.txt";
	public static final String FILENAME_SYNOPTIC_LOG = "synoptic_log.txt";

	/**
	 * Path to project directory.
	 */

	@XmlAttribute(name = "projectDirectory")
	private File m_fProjectDirectory = null;

	/**
	 * Path to project file.
	 */

	@XmlAttribute(name = "projectFile")
	private File m_fProjectFile = null;

	/**
	 * Parameter/Value storage.
	 */

	@XmlElement(name = "map")
	private HashMap<String, Object> map = null;

	/**
	 * Constructor
	 */

	private Project() {
		this.map = new HashMap<String, Object>();
	}

	/**
	 * Constructor.
	 * 
	 * @param fProjectDirectory
	 *            Project directory.
	 */

	public Project(File fProjectDirectory) {
		this();

		this.m_fProjectDirectory = fProjectDirectory;
		this.m_fProjectFile = new File(fProjectDirectory, FILENAME_PROJECT);
	}

	/**
	 * Adds an object to the project by tag name.
	 * 
	 * @param strTag
	 *            Tag name the object can be identified with.
	 * @param objValue
	 *            Object to be stored in the project.
	 */

	public void addValue(String strTag, Object objValue) {
		map.put(strTag, objValue);
	}

	/**
	 * Returns an object by its tag.
	 * 
	 * @param strTag
	 *            Tag name of the object.
	 * @return The desired object, if the tag name exists, null otherwise.
	 */

	public Object getValue(String strTag) {
		Object objValue = this.map.get(strTag);
		return objValue != null ? objValue : null;
	}

	/**
	 * Adds a single file to the project.
	 * 
	 * @param strTag
	 *            Tag name the file can be identified with.
	 * @param file
	 *            File to be stored in the project.
	 */

	public void addFile(String strTag, File file) {
		this.map.put(strTag, file.getPath());
	}

	/**
	 * Adds a single file to the proejct.
	 * 
	 * @param strTag
	 *            Tag name the file can be identified with.
	 * @param strFileName
	 *            String-representation of a path.
	 * @return File object that represents the string-representation of the
	 *         given path.
	 */

	public File addFile(String strTag, String strFileName) {
		String strPath = createProjectPath(strFileName);
		this.map.put(strTag, strPath);

		return new File(strPath);
	}

	/**
	 * Returns a file object by its tag name.
	 * 
	 * @param strTag
	 *            Tag name of the file.
	 * @return File object if the tag exists, null otherwise.
	 */

	public File getFile(String strTag) {
		Object objValue = getValue(strTag);
		return objValue != null ? new File(objValue.toString()) : null;
	}

	/**
	 * Checks if a file (not directory) exists.
	 * 
	 * @param strTag
	 *            Tag name of the file.
	 * @return true if the file is a file and is existent, false otherwise.
	 */

	public boolean fileExists(String strTag) {
		File file = getFile(strTag);
		if (file != null) {
			return FileUtils.fileExists(file);
		}

		return false;
	}

	/**
	 * Removes a tag and its corresponding object from the project.
	 * 
	 * @param strTag
	 *            Tag name of the object to be removed.
	 */

	public void removeTag(String strTag) {
		this.map.remove(strTag);
	}

	/**
	 * Removes a tag and its corresponding file (physically) from the project.
	 * 
	 * @param strTag
	 *            Tag name of the file to be deleted.
	 */

	public void deleteFile(String strTag) {
		if (strTag != null) {
			File file = getFile(strTag);
			if (file != null) {
				file.delete();
			}
		}

		removeTag(strTag);
	}

	/**
	 * Returns a string-reprsentation of a file object by tag name.
	 * 
	 * @param strTag
	 *            Tag name of the file.
	 * @return String-representation of the file object if the tag exists, null
	 *         otherwise.
	 */

	public String getFilePathAsString(String strTag) {
		File file = getFile(strTag);
		return file != null ? file.getPath() : null;
	}

	/**
	 * @return Project directory as string-value.
	 */

	public String getProjectDirectoryPathAsString() {
		return this.m_fProjectDirectory.getPath();
	}

	/**
	 * @return Project directory as file object.
	 */

	public File getProjectDirectoryPathAsFile() {
		return this.m_fProjectDirectory;
	}

	/**
	 * Creates a directory in the project directory.
	 * 
	 * @param strDirectoryName
	 *            Directory name to be created.
	 * @return File object representing the created directory.
	 */

	public File createDirectory(String strDirectoryName) {
		File file = new File(this.getProjectDirectoryPathAsString(),
				strDirectoryName);
		file.mkdirs();

		return file;
	}

	/**
	 * Creates a project path for a file name.
	 * 
	 * @param strFileName
	 *            File name to be represented as full project path.
	 * @return String value that represents the filename and its full project
	 *         path.
	 */

	private String createProjectPath(String strFileName) {
		File file = new File(getProjectDirectoryPathAsString(), strFileName);
		return FileUtils.toSlashPath(file.getPath());
	}

	/**
	 * This method changes pathes in the project after moving a project from one
	 * directory to another.
	 * 
	 * @param fNewDirectory
	 *            Path to new directory.
	 */

	public void afterMoveRestore(File fNewDirectory) {
		String strOldProjectPath = this.m_fProjectDirectory.getPath();
		String strNewProjectPath = fNewDirectory.getPath();

		// -- Fixed values
		this.m_fProjectDirectory = fNewDirectory;
		this.m_fProjectFile = new File(fNewDirectory, FILENAME_PROJECT);

		// -- Map content
		for (String strTag : TAG_FILE) {
			Object objValue = getFile(strTag);
			if (objValue != null) {
				File file = (File) objValue;
				String strCurrentPath = file.getPath();
				if (strCurrentPath.startsWith(strOldProjectPath)) {
					String str0 = strCurrentPath.substring(strOldProjectPath
							.length());
					file = new File(strNewProjectPath + File.separator + str0);
					addFile(strTag, file);
				}
			}
		}
	}

	/**
	 * Serializes the project to a file.
	 * 
	 * @throws JAXBException
	 *             Occurs, if something unexptected happens.
	 */

	public void store() throws JAXBException {
		SerializationUtils.serializeToXML(this.m_fProjectFile, this); // JAXBException
	}

	/**
	 * Deserializes the project from a file.
	 * 
	 * @param fProjectFile
	 *            Project file to be read.
	 * @return Project instance.
	 * @throws JAXBException
	 *             Occurs, if something unexpected happens.
	 */

	public static Project restore(File fProjectFile) throws JAXBException {
		Project project = SerializationUtils.deserializeFromXML(fProjectFile,
				Project.class); // JAXBException
		return project;
	}
}
