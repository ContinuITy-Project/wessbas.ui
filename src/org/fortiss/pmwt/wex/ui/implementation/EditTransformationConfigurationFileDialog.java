package org.fortiss.pmwt.wex.ui.implementation;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.fortiss.pmwt.wex.ui.generated.BaseTransformationConfigurationFileDialog;
import org.fortiss.pmwt.wex.ui.persistence.TransformationProperties;
import org.fortiss.pmwt.wex.ui.persistence.options.ETransformation;
import org.fortiss.pmwt.wex.ui.utils.FileUtils;

/**
 * Configuration dialog for the transformation in general.
 */

public class EditTransformationConfigurationFileDialog extends
		BaseTransformationConfigurationFileDialog {
	/**
	 * Serialization.
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * Properties.
	 */

	private TransformationProperties m_properties = null;

	/**
	 * Output properties file.
	 */

	private File m_fPropertiesFile = null;

	/**
	 * Sets the path to the configuration file.
	 * 
	 * @param strTransformationPropertiesFilePath
	 *            Path to the configuration file.
	 */

	public void setTransformationPropertiesFile(
			String strTransformationPropertiesFilePath) {
		// -- Edit
		File file = new File(strTransformationPropertiesFilePath);
		if (FileUtils.fileExists(strTransformationPropertiesFilePath)) {
			try {
				this.m_fPropertiesFile = file;
				this.m_properties = TransformationProperties
						.read(this.m_fPropertiesFile);

				setPropertiesUI(this.m_properties);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Updates the UI.
	 * 
	 * @param p
	 *            Current properties.
	 */

	private void setPropertiesUI(TransformationProperties p) {
		String strXMeansMin = p.getString(ETransformation.XMEANS_MIN.getKey(),
				ETransformation.XMEANS_MIN.getDefaultValue().toString());
		String strXMeansMax = p.getString(ETransformation.XMEANS_MAX.getKey(),
				ETransformation.XMEANS_MAX.getDefaultValue().toString());
		String strXMeansSeed = p.getString(
				ETransformation.XMEANS_SEED.getKey(),
				ETransformation.XMEANS_SEED.getDefaultValue().toString());
		Boolean bUseXMeans = p.getBoolean(ETransformation.USEXMEANS.getKey(),
				(Boolean) ETransformation.USEXMEANS.getDefaultValue());
		Boolean bGenerateApacheJMeterTestplan = p.getBoolean(
				ETransformation.GENERATE_APACHE_JMETER_TESTPLAN.getKey(),
				(Boolean) ETransformation.GENERATE_APACHE_JMETER_TESTPLAN
						.getDefaultValue());
		Boolean bGeneratePCMUsageModel = p.getBoolean(
				ETransformation.GENERATE_MODIFIED_PCM_USAGE_MODEL.getKey(),
				(Boolean) ETransformation.GENERATE_MODIFIED_PCM_USAGE_MODEL
						.getDefaultValue());

		this.txtXMeansMin.setText(strXMeansMin);
		this.txtXMeansMax.setText(strXMeansMax);
		this.txtXMeansSeed.setText(strXMeansSeed);
		this.chkUseXMeans.setSelected(bUseXMeans.booleanValue());
		this.chkGenerateApacheJMeterTestplan
				.setSelected(bGenerateApacheJMeterTestplan.booleanValue());
		this.chkGeneratePCMUsageModel.setSelected(bGeneratePCMUsageModel
				.booleanValue());
	}

	/**
	 * Saves the properties.
	 */

	private void saveProperties() {
		this.m_properties.putString(ETransformation.XMEANS_MIN.getKey(),
				this.txtXMeansMin.getText());
		this.m_properties.putString(ETransformation.XMEANS_MAX.getKey(),
				this.txtXMeansMax.getText());
		this.m_properties.putString(ETransformation.XMEANS_SEED.getKey(),
				this.txtXMeansSeed.getText());
		this.m_properties.putBoolean(ETransformation.USEXMEANS.getKey(),
				new Boolean(this.chkUseXMeans.isSelected()));
		this.m_properties.putBoolean(
				ETransformation.GENERATE_APACHE_JMETER_TESTPLAN.getKey(),
				new Boolean(this.chkGenerateApacheJMeterTestplan.isSelected()));
		this.m_properties.putBoolean(
				ETransformation.GENERATE_MODIFIED_PCM_USAGE_MODEL.getKey(),
				new Boolean(this.chkGeneratePCMUsageModel.isSelected()));

		try {
			this.m_properties.write(this.m_fPropertiesFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param frameOwner
	 *            Parent frame to block.
	 */

	public EditTransformationConfigurationFileDialog(JFrame frameOwner) {
		super(frameOwner);
	}

	@Override
	protected void btnSaveAndCloseActionPerformed(ActionEvent actionEvent) {
		btnSaveActionPerformed(null);

		setVisible(false);
	}

	@Override
	protected void btnSaveActionPerformed(ActionEvent actionEvent) {
		saveProperties();
	}
}