package org.fortiss.pmwt.wex.ui.jobs;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.fortiss.pmwt.wex.ui.Core;
import org.fortiss.pmwt.wex.ui.persistence.Project;
import org.fortiss.pmwt.wex.ui.persistence.SynopticPropertiesFile;
import org.fortiss.pmwt.wex.ui.persistence.TransformationProperties;
import org.fortiss.pmwt.wex.ui.persistence.options.ETransformation;
import org.fortiss.pmwt.wex.ui.tools.BehaviorMixFileToBehaviorModelsFile;
import org.fortiss.pmwt.wex.ui.utils.FileUtils;

/**
 * Final transformation.
 */

public class TransformJob implements IJob<Project> {
	/*
	 * Input/Output files for easy access.
	 */

	private File m_fInputFinalSessionFile = null;
	private File m_fInputWorkloadPropertiesFile = null;
	private File m_fInputTransformationPropertiesFile = null;
	private File m_fInputSynopticProperties = null;
	private File m_fInputSynopticLogFile = null;

	private File m_fOutputDirectory = null;
	private File m_fOutputBehaviorMixFile = null;
	private File m_fOutputBehaviorModelsFile = null;
	private File m_fOutputWorkloadModelXmiFile = null;
	private File m_fOutputGraphFile = null;
	private File m_fOutputTestplanFile = null;

	/*
	 * Additional members.
	 */

	private TransformationProperties m_transformationProperties = null;

	private boolean m_bGenerateModifiedPCMUsageModel = false;
	private boolean m_bGenerateApacheJMeterTestplan = false;

	/**
	 * Entry point.
	 */

	@Override
	public void run(Project project) throws Exception {
		// -- Set input and output files
		initFiles(project);

		// -- Initialize
		initialize(project); // IOException

		// -- Create synoptic properties file for guards and actions
		createSynopticFile(project); // IOException

		// -- Extract behavior models
		extractBehaviorModels(project);

		// -- Create behavior mix file
		createBehaviorMixFile(project);

		// -- Create WESSBAS artefacts
		generateWESSBASArtefacts(project);

		// -- Transformation
		if (this.m_bGenerateModifiedPCMUsageModel) {
			transformPCM(project);
		}

		if (this.m_bGenerateApacheJMeterTestplan) {
			transformTestplan(project);
		}
	}

	/**
	 * Initalizes input/output files.
	 * 
	 * @param project
	 *            Current project.
	 */

	private void initFiles(Project project) {
		// -- Output files
		this.m_fOutputDirectory = project.getProjectDirectoryPathAsFile();
		this.m_fOutputTestplanFile = project.addFile(Project.TAG_FILE_TESTPLAN,
				Project.FILENAME_TESTPLAN);

		// -- Input files
		this.m_fInputFinalSessionFile = project
				.getFile(Project.TAG_FILE_FINAL_SESSION);
		this.m_fInputWorkloadPropertiesFile = project
				.getFile(Project.TAG_FILE_WORKLOAD_PROPERTIES);
		this.m_fInputTransformationPropertiesFile = project
				.getFile(Project.TAG_FILE_TRANSFORMATION_CONFIGURATION);
		this.m_fInputSynopticLogFile = project
				.getFile(Project.TAG_FILE_SYNOPTIC_LOG_FILE);
	}

	/**
	 * Creates a synoptic file (guards and actions).
	 * 
	 * @param project
	 *            Current project.
	 * @throws IOException
	 *             Occurs, if something unexpected happens.
	 */

	private void createSynopticFile(Project project) throws IOException {
		this.m_fInputSynopticProperties = project
				.addFile(Project.TAG_FILE_SYNOPTIC_PROPERTIES,
						Project.FILENAME_SYNOPTIC);

		// -- Properties file
		SynopticPropertiesFile properties = new SynopticPropertiesFile();
		properties.put(SynopticPropertiesFile.LOGFILE,
				this.m_fInputSynopticLogFile.getPath());

		properties.write(this.m_fInputSynopticProperties);
	}

	/**
	 * Initalizes properties.
	 * 
	 * @param project
	 *            Current project.
	 * @throws IOException
	 *             Occurs, if something unexpected happens.
	 */

	private void initialize(Project project) throws IOException {
		// -- Read configuration
		this.m_transformationProperties = TransformationProperties
				.read(this.m_fInputTransformationPropertiesFile); // IOException

		this.m_bGenerateApacheJMeterTestplan = this.m_transformationProperties
				.getBoolean(
						ETransformation.GENERATE_APACHE_JMETER_TESTPLAN
								.getKey(),
						((Boolean) ETransformation.GENERATE_APACHE_JMETER_TESTPLAN
								.getDefaultValue()).booleanValue());
		this.m_bGenerateModifiedPCMUsageModel = this.m_transformationProperties
				.getBoolean(
						ETransformation.GENERATE_MODIFIED_PCM_USAGE_MODEL
								.getKey(),
						((Boolean) ETransformation.GENERATE_MODIFIED_PCM_USAGE_MODEL
								.getDefaultValue()).booleanValue());
	}

	/**
	 * Method to call the behavior model extractor.
	 * 
	 * @param project
	 *            Current project.
	 */

	private void extractBehaviorModels(Project project) {
		int nXMeansMin = this.m_transformationProperties.getInt(
				ETransformation.XMEANS_MIN.getKey(), -1);
		int nXMeansMax = this.m_transformationProperties.getInt(
				ETransformation.XMEANS_MAX.getKey(), -1);
		int nXMeansSeed = this.m_transformationProperties.getInt(
				ETransformation.XMEANS_SEED.getKey(), -1);
		Boolean usexmeans = this.m_transformationProperties.getBoolean(
				ETransformation.USEXMEANS.getKey(), true);

		// -- Extract behavior models
		Core.execBehaviorModelExtractor(this.m_fInputFinalSessionFile,
				this.m_fOutputDirectory, nXMeansMin, nXMeansMax, usexmeans,
				nXMeansSeed);

		// -- Add behaviormix.txt to the project
		this.m_fOutputBehaviorMixFile = project.addFile(
				Project.TAG_FILE_BEHAVIOR_MIX, Project.FILENAME_BEHAVIOR_MIX);

		// -- Add usecases.txt to the project
		project.addFile(Project.TAG_FILE_USECASES, Project.FILENAME_USECASES);
	}

	/**
	 * Method to create the behavior mix file.
	 * 
	 * @param project
	 *            Current project.
	 * @throws IOException
	 *             Occurs, if something unexpected happens.
	 */

	private void createBehaviorMixFile(Project project) throws IOException {
		// -- Add to-be-created behavior models properties file to the project
		this.m_fOutputBehaviorModelsFile = project.addFile(
				Project.TAG_FILE_BEHAVIOR_MODELS,
				Project.FILENAME_BEHAVIOR_MODELS);

		// -- Create behavior models properties file
		File fInputBehaviorMixFile = this.m_fOutputBehaviorMixFile;
		BehaviorMixFileToBehaviorModelsFile.create(fInputBehaviorMixFile,
				this.m_fOutputBehaviorModelsFile, this.m_fOutputDirectory); // IOException

		// -- Add to-be-created workloadmodel.xmi
		this.m_fOutputWorkloadModelXmiFile = project.addFile(
				Project.TAG_FILE_WORKLOAD_MODEL_XMI,
				Project.FILENAME_WORKLOAD_XMI);

		// -- Add to-be created graph.dot
		this.m_fOutputGraphFile = project.addFile(Project.TAG_FILE_GRAPH,
				Project.FILENAME_GRAPH);
	}

	/**
	 * Generates all WESSBAS-related artefacts.
	 * 
	 * @param project
	 *            Current project.
	 * @throws Exception
	 *             Occurs, if something unexpected happens.
	 */

	private void generateWESSBASArtefacts(Project project) throws Exception {
		File fInputBehaviorModelsPropertiesFile = this.m_fOutputBehaviorMixFile;
		File fInputWorkloadModelXmiFile = this.m_fOutputWorkloadModelXmiFile;

		// -- Extract behavior models
		Core.execModelGenerator(this.m_fInputFinalSessionFile,
				this.m_fInputWorkloadPropertiesFile,
				fInputWorkloadModelXmiFile, fInputBehaviorModelsPropertiesFile,
				this.m_fOutputGraphFile, this.m_fInputSynopticProperties);
	}

	/**
	 * Method to transform a WESSBAS-DSL instance to a modified PCM Usage Model.
	 * 
	 * @param project
	 *            Current project.
	 * @throws Exception
	 *             Occurs, if something unexpected happens.
	 */

	private void transformPCM(Project project) throws Exception {
		// -- WESSBAS2PCM
		File fInputW2PProperties = project
				.getFile(Project.TAG_FILE_WESSBAS2PCM_CONFIGURATION);

		// -- Prepare wessbas2pcm properties
		Properties properties = FileUtils
				.restoreProperties(fInputW2PProperties);
		properties.put("graph.store", "false"); // delete soon
		properties.put("graph.store.outputFile", ""); // delete soon
		properties
				.put("xmiInputFile",
						"file://"
								+ FileUtils.toSlashPath(project
										.getFilePathAsString(Project.TAG_FILE_WORKLOAD_MODEL_XMI)));
		// properties.put( "model.directory.target", FileUtils.toSlashPath(
		// project.createDirectory( "model_out" ).getPath() ) ); // delete soon

		FileUtils.storeProperties(fInputW2PProperties, properties);

		// --
		File fInputWorkloadIntensityPropertiesFile = project
				.getFile(Project.TAG_FILE_WORKLOAD_PROPERTIES);

		// -- Transform
		Core.execWESSBAS2PCM(fInputW2PProperties,
				fInputWorkloadIntensityPropertiesFile); // Exception
	}

	/**
	 * Method to transform a WESSBAS-DSL instance to an Apache JMeter Testplan.
	 * 
	 * @param project
	 *            Current project.
	 * @throws Exception
	 *             Occurs, if something unexpected happens.
	 */

	private void transformTestplan(Project project) throws Exception {
		File fInputWorkloadXmiFile = this.m_fOutputWorkloadModelXmiFile;
		File fOutputTestplanFile = this.m_fOutputTestplanFile;

		Core.execTestplanGenerator(fInputWorkloadXmiFile, fOutputTestplanFile);
	}
}