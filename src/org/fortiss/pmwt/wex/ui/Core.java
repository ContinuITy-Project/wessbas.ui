package org.fortiss.pmwt.wex.ui;

import java.io.File;

import org.fortiss.pmwt.wex.ui.utils.FileUtils;

import wessbas.commons.util.ReflectionUtils;

/**
 * Core functionality for creating the output artefacts of the WESSBAS workflow.
 */

public class Core {
	/**
	 * Main class for behavior model extractor.
	 */
	private static final String MAIN_CLASS_BEHAVIOR_MODEL_EXTRACTOR = "net.sf.markov4jmeter.behaviormodelextractor.BehaviorModelExtractor";

	/**
	 * Main class for m4jdsl model generator.
	 */
	private static final String MAIN_CLASS_M4JDSL_MODEL_GENERATOR = "net.sf.markov4jmeter.m4jdslmodelgenerator.M4jdslModelGenerator";

	/**
	 * Main class for wessbas2pcm.
	 */
	private static final String MAIN_CLASS_WESSBAS2PCM = "org.fortiss.pmwt.wex.wessbas2pcm.extended.Wessbas2PCM";

	/**
	 * Main class for testplan generation.
	 */
	private static final String MAIN_CLASS_TESTPLAN = "net.sf.markov4jmeter.testplangenerator.TestPlanGenerator";

	/**
	 * Wrapper for executing main class of the behavior model extractor.
	 * 
	 * @param fInputSessionFile
	 *            Session file to be parsed.
	 * @param fOutputArtefactDirectory
	 *            Target directory where the output artefacts of the behavior
	 *            model extractor will be created in.
	 */

	public static void execBehaviorModelExtractor(File fInputSessionFile,
			File fOutputArtefactDirectory, int nXMeanMin, int nXMeanMax,
			Boolean useXmeans, int nSeed) {
		// nSeed currently unused!

		String clusteringMethod = "";
		if (useXmeans) {
			clusteringMethod = "xmeans";
		} else {
			clusteringMethod = "kmeans";
		}

		String strCommandLine = "-i " + fInputSessionFile.getPath() + " -min "
				+ nXMeanMin + " -max " + nXMeanMax + " -c " + clusteringMethod
				+ " -o " + fOutputArtefactDirectory.getPath();

		ReflectionUtils.callMain(MAIN_CLASS_BEHAVIOR_MODEL_EXTRACTOR,
				strCommandLine.split(" "));
	}

	/**
	 * Wrapper for executing the main class of the model generator.
	 * 
	 * @param fInputDirectoryWithFlowFiles
	 *            Directory that contains flow files.
	 * @param fInputSessionFile
	 *            Session file to be parsed.
	 * @param fInputWorkloadIntensityPropertiesFile
	 *            Property file with workload intensity information.
	 * @param fOutputWorkloadModelXmi
	 *            Output file that contains an instance of the WESSBAS-DSL.
	 * @param fInputBehaviorModelsPropertiesFile
	 *            Property file with "where-to-find-behavior-models"
	 *            information.
	 * @param fOutputGraphFile
	 *            Output file that contains a graph representation of the parsed
	 *            content.
	 */

	public static void execModelGenerator(File fInputSessionFile,
			File fInputWorkloadIntensityPropertiesFile,
			File fOutputWorkloadModelXmi,
			File fInputBehaviorModelsPropertiesFile, File fOutputGraphFile,
			File fInputSynopticProperties) {
		StringBuilder sbCommandLine = new StringBuilder();
		sbCommandLine.append("-w "
				+ fInputWorkloadIntensityPropertiesFile.getPath() + " "); // must
																			// be
																			// provided
																			// by
																			// user
																			// //
																			// IN
		sbCommandLine.append("-o " + fOutputWorkloadModelXmi.toURI() + " "); // OUT
		sbCommandLine.append("-b "
				+ fInputBehaviorModelsPropertiesFile.getPath() + " "); // IN
		sbCommandLine.append("-g " + fOutputGraphFile.getPath() + " "); // OUT
		sbCommandLine.append("-t " + fInputSynopticProperties.getPath() + " "); // IN
		sbCommandLine.append("-s " + fInputSessionFile.getPath() + " "); // session
																			// logs
																			// (mod)
																			// //
																			// IN
		String strCommandLine = sbCommandLine.toString();

		ReflectionUtils.callMain(MAIN_CLASS_M4JDSL_MODEL_GENERATOR,
				strCommandLine.split(" "));
	}

	/**
	 * Wrapper for executing the wessbas2pcm.
	 * 
	 * @param fW2PPropertiesFile
	 *            Property file that drives the creation of the PCM usage model.
	 * @throws Exception
	 *             Thrown if something goes wrong.
	 */

	public static void execWESSBAS2PCM(File fW2PPropertiesFile,
			File fWorkloadIntensityPropertiesFile) throws Exception {
		StringBuilder sbCommandLine = new StringBuilder();
		sbCommandLine.append(fW2PPropertiesFile.getPath() + " ");
		sbCommandLine.append(fWorkloadIntensityPropertiesFile.getPath());
		String strCommandLine = sbCommandLine.toString();

		ReflectionUtils.callMain(MAIN_CLASS_WESSBAS2PCM,
				strCommandLine.split(" "));
	}

	/**
	 * Wrapper for Apache JMeter Testplan Generator.
	 * 
	 * @param fInputWorkloadXmiFile
	 *            Input file that contains an instance of the WESSBAS-DSL.
	 * @param fInputTestplanProperties
	 *            Input file that contains the configuration for the testplan
	 *            generator.
	 * @param fOutputTestplan
	 *            Output path for the generated testplan.
	 * @throws Exception
	 *             Occurs when something unexpected happens.
	 */

	public static void execTestplanGenerator(File fInputWorkloadXmiFile,
			File fOutputTestplan) throws Exception {
		// -i -- path to the input M4J-DSL model.
		// -o -- path to the output JMeter Test Plan.
		// -t -- path to the properties file with Test Plan element default
		// settings.

		File fInputTestplanProperties = new File(
				"./configuration/testplan.default.properties");

		String strInputWorkloadXmiFile = "file://"
				+ FileUtils.toSlashPath(fInputWorkloadXmiFile.getPath());
		StringBuilder sbCommandLine = new StringBuilder();
		sbCommandLine.append("-i " + strInputWorkloadXmiFile + " ");
		sbCommandLine.append("-o " + fOutputTestplan.getPath() + " ");
		sbCommandLine.append("-t " + fInputTestplanProperties.getPath());
		String strCommandLine = sbCommandLine.toString();

		ReflectionUtils
				.callMain(MAIN_CLASS_TESTPLAN, strCommandLine.split(" "));
	}
}
