package org.fortiss.pmwt.wex.ui.implementation;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.xml.bind.JAXBException;

import org.fortiss.pmwt.wex.ui.generated.BaseMainFrame;
import org.fortiss.pmwt.wex.ui.io.generic.GenericReaderConfiguration;
import org.fortiss.pmwt.wex.ui.jobs.CreateFinalSessionFileJob;
import org.fortiss.pmwt.wex.ui.jobs.CreateInitialSessionFileJob;
import org.fortiss.pmwt.wex.ui.jobs.IJobNotify;
import org.fortiss.pmwt.wex.ui.jobs.JobManager;
import org.fortiss.pmwt.wex.ui.jobs.RetrieveStatesFromSessionJob;
import org.fortiss.pmwt.wex.ui.jobs.TransformJob;
import org.fortiss.pmwt.wex.ui.persistence.AbstractProperties;
import org.fortiss.pmwt.wex.ui.persistence.Project;
import org.fortiss.pmwt.wex.ui.persistence.TimeRangeFilter;
import org.fortiss.pmwt.wex.ui.persistence.TransformationProperties;
import org.fortiss.pmwt.wex.ui.persistence.WESSBAS2PCMProperties;
import org.fortiss.pmwt.wex.ui.persistence.WorkloadProperties;
import org.fortiss.pmwt.wex.ui.utils.FileUtils;
import org.fortiss.pmwt.wex.ui.utils.SerializationUtils;
import org.fortiss.pmwt.wex.ui.utils.UIFileChooser;
import org.fortiss.pmwt.wex.ui.utils.UIUtils;

/**
 * Main Frame.
 */

public class MainFrame extends BaseMainFrame implements IJobNotify {
	/**
	 * Serialization.
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * Current project.
	 */

	private Project m_project = null;

	/**
	 * Job IDs.
	 */

	private static final int JID_CREATE_INITIAL_SESSION = 0;
	private static final int JID_CREATE_STATES = 1;
	// private static final int JID_CREATE_TIMES = 2;
	private static final int JID_CREATE_FINAL_SESSION = 3;
	private static final int JID_TRANSFORM = 4;

	/**
	 * Constructor.
	 */

	public MainFrame() {
		// -- Fresh UI
		enableAll(false);
		this.mnuFileSave.setEnabled(false);
	}

	/**
	 * Opens a project.
	 * 
	 * @param fProjectFile
	 *            Path to the project file.
	 */

	public void openProject(File fProjectFile) {
		if (fProjectFile.exists() && fProjectFile.isFile()) {
			try {
				Project project = Project.restore(fProjectFile);

				// -- Project has been moved
				File fParentDirectory = fProjectFile.getParentFile();
				if (!fParentDirectory.equals(project
						.getProjectDirectoryPathAsFile())) {
					System.out
							.println("Project file has been moved. Changing path entries.");

					project.afterMoveRestore(fParentDirectory);
					project.store();
				}

				setProject(this.m_project = project);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets the project.
	 * 
	 * @param project
	 *            Project to set.
	 */

	private void setProject(Project project) {
		// -- Convenient access
		UIFileChooser.setLastPath(project.getProjectDirectoryPathAsFile());

		updateUI(project);

		enableAll(true);
		this.mnuFileSave.setEnabled(true);
	}

	/**
	 * Updates the UI with the current project.
	 */

	private void updateUI() {
		updateUI(this.m_project);
	}

	/**
	 * Enables/Disables all elements in the UI.
	 * 
	 * @param bEnable
	 *            Enable/Disable switch.
	 */

	@SuppressWarnings("unchecked")
	private void enableAll(boolean bEnable) {
		for (Component component : UIUtils.getComponentsRecursive(this,
				JMenuBar.class, JMenu.class, JMenuItem.class)) {
			component.setEnabled(bEnable);
		}
	}

	/**
	 * Saves the project.
	 */

	public void saveProject() {
		try {
			this.m_project.store();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new project.
	 * 
	 * @return Empty project instance.
	 */

	public boolean newProject() {
		boolean bRetVal = false;

		File fProjectDirectory = UIFileChooser.showDirectoryOpenDialog(this);
		if (fProjectDirectory != null) {
			this.m_project = new Project(fProjectDirectory);

			// -- UI
			enableAll(true);
			this.mnuFileSave.setEnabled(true);
			updateUI();

			bRetVal = true;
		}

		return bRetVal;
	}

	/**
	 * Step 1: UI/CMD: Select Input CSV File
	 */

	@Override
	protected void btnSelectInputCSVFileActionPerformed(ActionEvent actionEvent) {
		try {
			File fTargetFile = null;

			File[] arrFile = UIFileChooser.showMultipleFileOpenDialog(this);
			if (arrFile != null) {
				// -- Single file
				if (arrFile.length == 1) {
					fTargetFile = arrFile[0];
					this.m_project.addFile(Project.TAG_FILE_CSV, fTargetFile);
				}
				// -- Multiple files
				else {
					fTargetFile = this.m_project.addFile(Project.TAG_FILE_CSV,
							Project.FILENAME_CSV);
					FileUtils.concatEx(fTargetFile, arrFile); // IOException
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// -- UI
		updateUI();
	}

	/**
	 * Step 1: UI/CMD: Select Reader Configuration File
	 */

	@Override
	protected void btnSelectReaderConfigurationFileActionPerformed(
			ActionEvent actionEvent) {
		File file = UIFileChooser.selectFilePathHelper(
				this.txtReaderConfigurationFile, this);
		if (file != null) {
			this.m_project.addFile(
					Project.TAG_FILE_GENERIC_READER_CONFIGURATION, file);
		}

		// -- UI
		updateUI();
	}

	/**
	 * Step 1: UI/CMD: Create Reader Configuration File
	 */

	@Override
	protected void btnCreateReaderConfigurationFileActionPerformed(
			ActionEvent actionEvent) {
		// -- Create default generic reader configuration
		createDefaultGenericReaderConfiguration();
		saveProject();

		// -- UI
		updateUI();

		// -- Edit
		btnEditReaderConfigurationFileActionPerformed(null);
	}

	/**
	 * Step 1: UI/CMD: Edit Reader Configuration File
	 */

	@Override
	protected void btnEditReaderConfigurationFileActionPerformed(
			ActionEvent actionEvent) {
		// -- Validate necessary inputs
		String strInputCSVFilePath = getMandatoryPathByTagName(
				Project.TAG_FILE_CSV, "Cannot access input data file.");
		String strReaderConfigurationFilePath = getMandatoryPathByTagName(
				Project.TAG_FILE_GENERIC_READER_CONFIGURATION,
				"Cannot access reader configuration file.");

		if (strInputCSVFilePath == null
				|| strReaderConfigurationFilePath == null) {
			return;
		}

		// -- Dialog
		CreateReaderConfigurationFileDialog dialog = new CreateReaderConfigurationFileDialog(
				this);
		dialog.setCSVFilePath(strInputCSVFilePath);
		dialog.setReaderConfigurationFilePath(strReaderConfigurationFilePath);
		dialog.setVisible(true);

		// -- Set
		File file = dialog.getReaderConfigurationFile();
		if (file != null) {
			// -- Add to project
			this.m_project.addFile(
					Project.TAG_FILE_GENERIC_READER_CONFIGURATION, file);
		}

		// -- UI
		updateUI();
	}

	/**
	 * Step 1: UI/CMD: Create Session File
	 */

	@Override
	protected void btnCreateSessionFileActionPerformed(ActionEvent actionEvent) {
		// -- Validate necessary inputs
		String strInputCSVFilePath = getMandatoryPathByTagName(
				Project.TAG_FILE_CSV, "Cannot access input data file.");
		String strReaderConfigurationFilePath = getMandatoryPathByTagName(
				Project.TAG_FILE_GENERIC_READER_CONFIGURATION,
				"Cannot access reader configuration file.");

		if (strInputCSVFilePath == null
				|| strReaderConfigurationFilePath == null) {
			return;
		}

		// -- Disable UI
		enableComponents(false);

		// -- Create initial session file
		JobManager.schedule(new CreateInitialSessionFileJob(), this.m_project,
				JID_CREATE_INITIAL_SESSION, this);
	}

	/**
	 * Step 2: UI/CMD: Select State Filter File
	 */

	@Override
	protected void btnSelectStateFilterConfigurationFileActionPerformed(
			ActionEvent actionEvent) {
		File file = UIFileChooser.selectFilePathHelper(
				this.txtStateFilterConfigurationFile, this);
		if (file != null) {
			this.m_project.addFile(Project.TAG_FILE_STATE_FILTER_CONFIGURATION,
					file);
		}

		// -- UI
		updateUI();
	}

	/**
	 * Step 2: UI/CMD: Edit State Filter File
	 */

	@Override
	protected void btnEditStateFilterConfigurationFileActionPerformed(
			ActionEvent actionEvent) {
		// -- Validate necessary inputs
		String strInputStateFilterFilePath = getMandatoryPathByTagName(
				Project.TAG_FILE_STATE_FILTER_CONFIGURATION,
				"Cannot access state filter configuration file.");
		if (strInputStateFilterFilePath == null) {
			return;
		}

		// -- Dialog
		EditUseCaseMappingFileDialog dialog = new EditUseCaseMappingFileDialog(
				this);
		dialog.setStateFilterFilePath(strInputStateFilterFilePath);
		dialog.setVisible(true);
	}

	/**
	 * Step 2: UI/CMD: Create State Filter File
	 */

	@Override
	protected void btnCreateStateFilterConfigurationFileActionPerformed(
			ActionEvent actionEvent) {
		// -- Validate necessary inputs
		String strInputInitialSessionFilePath = getMandatoryPathByTagName(
				Project.TAG_FILE_INITIAL_SESSION,
				"Cannot access initial session file.");
		if (strInputInitialSessionFilePath == null) {
			return;
		}

		// -- Disable UI
		enableComponents(false);

		// -- Create new state filter configuration
		JobManager.schedule(new RetrieveStatesFromSessionJob(), this.m_project,
				JID_CREATE_STATES, this);
	}

	/**
	 * Step 2: UI/CMD: Select Time Range Filter File
	 */

	@Override
	protected void btnSelectTimeRangeConfigurationFileActionPerformed(
			ActionEvent actionEvent) {
		File file = UIFileChooser.selectFilePathHelper(
				this.txtTimeRangeFilterConfigurationFile, this);
		if (file != null) {
			this.m_project.addFile(
					Project.TAG_FILE_TIME_RANGE_FILTER_CONFIGURATION, file);
		}

		// -- UI
		updateUI();
	}

	/**
	 * Step 2: UI/CMD: Create Time Range Filter File
	 */

	@Override
	protected void btnCreateTimeRangeConfigurationFileActionPerformed(
			ActionEvent actionEvent) {
		// -- Validate necessary inputs
		String strInputInitialSessionFilePath = getMandatoryPathByTagName(
				Project.TAG_FILE_INITIAL_SESSION,
				"Cannot access initial session file.");
		if (strInputInitialSessionFilePath == null) {
			return;
		}

		// -- Create new time range filter configuration
		createTimeRangeFilterConfigurationFile();

		// -- UI
		updateUI();
	}

	/**
	 * Step 2: UI/CMD: Edit Time Range Filter File
	 */

	@Override
	protected void btnEditTimeRangeConfigurationFileActionPerformed(
			ActionEvent actionEvent) {
		// -- Validate necessary inputs
		String strInputTimeRangeFilterFilePath = getMandatoryPathByTagName(
				Project.TAG_FILE_TIME_RANGE_FILTER_CONFIGURATION,
				"Cannot access timerange filter configuration file.");

		if (strInputTimeRangeFilterFilePath == null) {
			return;
		}

		// -- Dialog
		EditTimeRangeFileDialog dialog = new EditTimeRangeFileDialog(this,
				this.m_project);
		dialog.setVisible(true);
	}

	/**
	 * Step 2: UI/CMD: Create Final Session File
	 */

	@Override
	protected void btnCreateFinalSessionFileActionPerformed(
			ActionEvent actionEvent) {
		// -- Validate necessary inputs
		String strValue0 = getMandatoryPathByTagName(
				Project.TAG_FILE_INITIAL_SESSION,
				"Cannot access initial session file.");
		String strValue1 = getMandatoryPathByTagName(
				Project.TAG_FILE_STATE_FILTER_CONFIGURATION,
				"Cannot access state filter file.");
		String strValue2 = getMandatoryPathByTagName(
				Project.TAG_FILE_TIME_RANGE_FILTER_CONFIGURATION,
				"Cannot access time range filter file.");
		if (strValue0 == null || strValue1 == null || strValue2 == null) {
			return;
		}

		// -- Disable UI
		enableComponents(false);

		// -- Create new state filter configuration
		JobManager.schedule(new CreateFinalSessionFileJob(), this.m_project,
				JID_CREATE_FINAL_SESSION, this);
	}

	/**
	 * Step 3: UI/CMD: Select Workload Properties File
	 */

	@Override
	protected void btnSelectWorkloadPropertiesFileActionPerformed(
			ActionEvent actionEvent) {
		File file = UIFileChooser.selectFilePathHelper(
				this.txtWorkloadPropertiesFile, this);
		if (file != null) {
			this.m_project.addFile(Project.TAG_FILE_WORKLOAD_PROPERTIES, file);
		}

		// -- UI
		updateUI();
	}

	/**
	 * Step 3: UI/CMD: Create Workload Properties File
	 */

	@Override
	protected void btnCreateWorkloadPropertiesFileActionPerformed(
			ActionEvent actionEvent) {
		// -- Create WorkloadIntensity.properties with defautlt values
		createWorkloadPropertiesFile();
		saveProject();

		// -- UI
		updateUI();

		// -- Edit
		btnEditWorkloadPropertiesFileActionPerformed(null);
	}

	/**
	 * Step 3: UI/CMD: Edit Workload Properties File
	 */

	@Override
	protected void btnEditWorkloadPropertiesFileActionPerformed(
			ActionEvent actionEvent) {
		// -- Validate necessary inputs
		String strPath = getMandatoryPathByTagName(
				Project.TAG_FILE_WORKLOAD_PROPERTIES,
				"Cannot access workload properties file.");
		if (strPath == null) {
			return;
		}

		CreateWorkloadPropertiesFileDialog dialog = new CreateWorkloadPropertiesFileDialog(
				this);
		dialog.setProject(this.m_project);
		dialog.setWorkloadPropertiesFilePath(strPath);
		dialog.setVisible(true);
	}

	/**
	 * Step 3: UI/CMD: Select w2p Properties File
	 */

	@Override
	protected void btnSelectWessbasToPcmPropertiesFileActionPerformed(
			ActionEvent actionEvent) {
		File file = UIFileChooser.selectFilePathHelper(
				this.txtWessbasToPcmPropertiesFile, this);
		if (file != null) {
			this.m_project.addFile(Project.TAG_FILE_WESSBAS2PCM_CONFIGURATION,
					file);
		}

		// -- UI
		updateUI();
	}

	/**
	 * Step 3: UI/CMD: Create w2p Properties File
	 */

	@Override
	protected void btnCreateWessbasToPcmPropertiesFileActionPerformed(
			ActionEvent actionEvent) {
		// -- Create default WESSBAS2PCM configuration file
		createWESSBAS2PCMPropertiesFile();
		saveProject();

		// -- UI
		updateUI();

		// -- Edit
		btnEditWessbasToPcmPropertiesFileActionPerformed(null);
	}

	/**
	 * Step 3: UI/CMD: Edit w2p Properties File
	 */

	@Override
	protected void btnEditWessbasToPcmPropertiesFileActionPerformed(
			ActionEvent actionEvent) {
		// -- Validate necessary inputs
		String strPath = getMandatoryPathByTagName(
				Project.TAG_FILE_WESSBAS2PCM_CONFIGURATION,
				"Cannot access wessbas2pcm configuration file.");
		if (strPath == null) {
			return;
		}

		// -- Dialog
		EditWessbasToPcmConfigurationFile dialog = new EditWessbasToPcmConfigurationFile(
				this);
		dialog.setWessbasToPcmPropertiesFile(strPath);
		dialog.setVisible(true);
	}

	/**
	 * Step 3: UI/CMD: Transform
	 */

	@Override
	protected void btnTransformActionPerformed(ActionEvent actionEvent) {
		// -- Validate necessary inputs
		String strValue0 = getMandatoryPathByTagName(
				Project.TAG_FILE_FINAL_SESSION,
				"Cannot access final session file.");
		String strValue1 = getMandatoryPathByTagName(
				Project.TAG_FILE_WESSBAS2PCM_CONFIGURATION,
				"Cannot access wessbas2pcm configuration file.");
		if (strValue0 == null || strValue1 == null) {
			return;
		}

		// -- Disable UI
		enableComponents(false);

		// -- Create new state filter configuration
		JobManager.schedule(new TransformJob(), this.m_project, JID_TRANSFORM,
				this);
	}

	/**
	 * Step 3: UI/CMD: Select Transformation Properties File
	 */

	@Override
	protected void btnSelectTransformationPropertiesFileActionPerformed(
			ActionEvent actionEvent) {
		File file = UIFileChooser.selectFilePathHelper(
				this.txtTransformationPropertiesFile, this);
		if (file != null) {
			this.m_project.addFile(
					Project.TAG_FILE_TRANSFORMATION_CONFIGURATION, file);
		}

		// -- UI
		updateUI();
	}

	/**
	 * Step 3: UI/CMD: Create Transformation Properties File
	 */

	@Override
	protected void btnCreateTransformationPropertiesFileActionPerformed(
			ActionEvent actionEvent) {
		// -- Create default Transformation configuration file
		createTransformationPropertiesFile();
		saveProject();

		// -- UI
		updateUI();

		// -- Edit
		btnEditTransformationPropertiesFileActionPerformed(null);
	}

	/**
	 * Step 3: UI/CMD: Edit Transformation Properties File
	 */

	@Override
	protected void btnEditTransformationPropertiesFileActionPerformed(
			ActionEvent actionEvent) {
		// -- Validate necessary inputs
		String strPath = getMandatoryPathByTagName(
				Project.TAG_FILE_TRANSFORMATION_CONFIGURATION,
				"Cannot access transformation configuration file.");
		if (strPath == null) {
			return;
		}

		// -- Dialog
		EditTransformationConfigurationFileDialog dialog = new EditTransformationConfigurationFileDialog(
				this);
		dialog.setTransformationPropertiesFile(strPath);
		dialog.setVisible(true);
	}

	/**
	 * Step 3: UI/CMD: Edit Testplan Properties File
	 */

	@Override
	protected void btnEditTestplanPropertiesFileActionPerformed(
			ActionEvent actionEvent) {
		File file = this.m_project
				.getFile(Project.TAG_FILE_TESTPLAN_PROPERTIES);
		if (FileUtils.fileExists(file)) {
			try {
				Runtime.getRuntime().exec("notepad.exe " + file.getPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Cannot open file.");
		}
	}

	/**
	 * Menu
	 */

	/**
	 * File -> New
	 */

	@Override
	protected void mnuFileNewActionPerformed(ActionEvent actionEvent) {
		if (newProject()) {
			clearUI();
			updateUI();
		}
	}

	/**
	 * File -> Open
	 */
	@Override
	protected void mnuFileOpenActionPerformed(ActionEvent actionEvent) {
		File file = UIFileChooser.showFileOpenDialog(this);
		if (file != null) {
			openProject(file);
		}
	}

	/**
	 * File -> Save
	 */

	@Override
	protected void mnuFileSaveActionPerformed(ActionEvent actionEvent) {
		saveProject();
	}

	/**
	 * File -> Exit
	 */

	@Override
	protected void mnuFileExitActionPerformed(ActionEvent actionEvent) {
		System.exit(0);
	}

	/**
	 * Creates a default generic reader configuration file.
	 * 
	 * @return Path to the configuration file.
	 */

	private File createDefaultGenericReaderConfiguration() {
		// -- Create default file
		File fOutputFile = this.m_project.addFile(
				Project.TAG_FILE_GENERIC_READER_CONFIGURATION,
				"cfg_generic_reader.xml");
		GenericReaderConfiguration genericReaderConfiguration = GenericReaderConfiguration
				.newDefaultInstance();

		try {
			SerializationUtils.serializeToXML(fOutputFile,
					genericReaderConfiguration); // JAXBException
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return fOutputFile;
	}

	/**
	 * Triggered, if a job succeeded/failed.
	 */

	@Override
	public void jobNotify(int nID, int nReturnType, Object objValue) {
		if (nReturnType == JobManager.OK) {
			saveProject();

			// -- Create default configurations if not yet existent
			if (nID == JID_CREATE_INITIAL_SESSION) {
				// -- Create default time range filter if not existent
				if (!this.m_project
						.fileExists(Project.TAG_FILE_TIME_RANGE_FILTER_CONFIGURATION)) {
					btnCreateTimeRangeConfigurationFileActionPerformed(null);
				}

				// -- Create default state filter if not existent
				if (!this.m_project
						.fileExists(Project.TAG_FILE_STATE_FILTER_CONFIGURATION)) {
					btnCreateStateFilterConfigurationFileActionPerformed(null);
				}
			}
		} else if (nReturnType == JobManager.ERROR) {
			Exception e = (Exception) objValue;
			e.printStackTrace();
		}

		// -- UI
		enableComponents(true);
		updateUI();
	}

	/**
	 * Enables/Disables all panels.
	 * 
	 * @param bEnable
	 *            Enable/Disable switch.
	 */

	private void enableComponents(boolean bEnable) {
		UIUtils.enablePanel(this.pnlStep1, bEnable);
		UIUtils.enablePanel(this.pnlStep2, bEnable);
		UIUtils.enablePanel(this.pnlStep3, bEnable);

		this.btnTransform.setEnabled(bEnable);
	}

	/**
	 * Error helper if path does not exist.
	 * 
	 * @param strTagName
	 *            Tag name that identifies the file in the project.
	 * @param strErrorMessage
	 *            Error message to show.
	 * @return Path that belongs to the file.
	 */

	private String getMandatoryPathByTagName(String strTagName,
			String strErrorMessage) {
		String strPath = this.m_project.getFilePathAsString(strTagName);
		if (!FileUtils.fileExists(strPath)) {
			JOptionPane.showMessageDialog(this, strErrorMessage, "Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return strPath;
	}

	/**
	 * Helper method for setting the path of a textfield.
	 * 
	 * @param textField
	 *            Textfield to set the path.
	 * @param strTagName
	 *            Tag name of the file in the project.
	 */

	private void setPathHelper(JTextField textField, String strTagName) {
		String strPath = this.m_project.getFilePathAsString(strTagName);
		if (FileUtils.fileExists(strPath)) {
			textField.setText(strPath);
		}
	}

	/**
	 * Creates a default workload properties file.
	 */

	private void createWorkloadPropertiesFile() {
		File fOutputFile = this.m_project.addFile(
				Project.TAG_FILE_WORKLOAD_PROPERTIES,
				Project.FILENAME_WOKRLOAD_PROPERTIES);

		try {
			AbstractProperties configuration = new WorkloadProperties();
			configuration.write(fOutputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a default time range filter configuration file.
	 */

	private void createTimeRangeFilterConfigurationFile() {
		File fOutputFile = this.m_project.addFile(
				Project.TAG_FILE_TIME_RANGE_FILTER_CONFIGURATION,
				Project.FILENAME_TIME_RANGE_FILTER);

		try {
			TimeRangeFilter configuration = new TimeRangeFilter();
			configuration.write(fOutputFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a default w2pe properties file.
	 */

	private void createWESSBAS2PCMPropertiesFile() {
		File fOutputFile = this.m_project.addFile(
				Project.TAG_FILE_WESSBAS2PCM_CONFIGURATION,
				Project.FILENAME_WESSBAS2PCM);

		try {
			AbstractProperties configuration = new WESSBAS2PCMProperties();
			configuration.write(fOutputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a default transformation properties file.
	 */

	private void createTransformationPropertiesFile() {
		File fOutputFile = this.m_project.addFile(
				Project.TAG_FILE_TRANSFORMATION_CONFIGURATION,
				Project.FILENAME_TRANSFORMATION);

		try {
			AbstractProperties configuration = new TransformationProperties();
			configuration.write(fOutputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the values of the UI.
	 * 
	 * @param project
	 *            Current project.
	 */

	private void updateUI(Project project) {
		// Step #1
		setPathHelper(this.txtInputCSVFile, Project.TAG_FILE_CSV);
		setPathHelper(this.txtReaderConfigurationFile,
				Project.TAG_FILE_GENERIC_READER_CONFIGURATION);
		setPathHelper(this.txtSessionFile, Project.TAG_FILE_INITIAL_SESSION);

		// Step #2
		setPathHelper(this.txtStateFilterConfigurationFile,
				Project.TAG_FILE_STATE_FILTER_CONFIGURATION);
		setPathHelper(this.txtTimeRangeFilterConfigurationFile,
				Project.TAG_FILE_TIME_RANGE_FILTER_CONFIGURATION);
		setPathHelper(this.txtFinalSessionFile, Project.TAG_FILE_FINAL_SESSION);

		// Step #3
		setPathHelper(this.txtWorkloadPropertiesFile,
				Project.TAG_FILE_WORKLOAD_PROPERTIES);
		setPathHelper(this.txtWessbasToPcmPropertiesFile,
				Project.TAG_FILE_WESSBAS2PCM_CONFIGURATION);
		setPathHelper(this.txtTransformationPropertiesFile,
				Project.TAG_FILE_TRANSFORMATION_CONFIGURATION);
	}

	/**
	 * Clears the UI.
	 */

	private void clearUI() {
		// Step #1
		this.txtInputCSVFile.setText("");
		this.txtReaderConfigurationFile.setText("");
		this.txtSessionFile.setText("");

		// Step #2
		this.txtStateFilterConfigurationFile.setText("");
		this.txtTimeRangeFilterConfigurationFile.setText("");
		this.txtFinalSessionFile.setText("");

		// Step #3
		this.txtWorkloadPropertiesFile.setText("");
		this.txtWessbasToPcmPropertiesFile.setText("");
		this.txtTransformationPropertiesFile.setText("");

	}
}