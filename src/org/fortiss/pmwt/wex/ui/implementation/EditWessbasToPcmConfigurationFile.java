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

package org.fortiss.pmwt.wex.ui.implementation;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTextField;

import org.fortiss.pmwt.wex.ui.generated.BaseWessbasToPcmConfigurationFileDialog;
import org.fortiss.pmwt.wex.ui.persistence.WESSBAS2PCMProperties;
import org.fortiss.pmwt.wex.ui.utils.FileUtils;
import org.fortiss.pmwt.wex.ui.utils.PCMUtils;
import org.fortiss.pmwt.wex.ui.utils.StringUtils;
import org.fortiss.pmwt.wex.ui.utils.UIFileChooser;

import de.uka.ipd.sdq.pcm.system.System;

/**
 * WESSBAS2PCM Extended configuration dialog.
 */

public class EditWessbasToPcmConfigurationFile extends
		BaseWessbasToPcmConfigurationFileDialog {
	/**
	 * Serialization.
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * Properties instance.
	 */

	private WESSBAS2PCMProperties m_properties = null;

	/**
	 * Property file path.
	 */

	private File m_fPropertiesFile = null;

	/**
	 * PCM model file extensions.
	 */

	private static final String EXTENSION_ALLOCATION = ".allocation";
	private static final String EXTENSION_REPOSITORY = ".repository";
	private static final String EXTENSION_RESENV = ".resourceenvironment";
	private static final String EXTENSION_SYSTEM = ".system";

	/**
	 * Sets a w2pe property file.
	 * 
	 * @param strWessbasToPcmPropertiesFilePath
	 *            Path to the property file.
	 */

	public void setWessbasToPcmPropertiesFile(
			String strWessbasToPcmPropertiesFilePath) {
		// -- Edit
		File file = new File(strWessbasToPcmPropertiesFilePath);
		if (FileUtils.fileExists(strWessbasToPcmPropertiesFilePath)) {
			try {
				this.m_fPropertiesFile = file;
				this.m_properties = WESSBAS2PCMProperties
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
	 * @param properties
	 *            Current properties.
	 */

	private void setPropertiesUI(WESSBAS2PCMProperties properties) {
		this.chkGenerateGuardsAndActions.setSelected(properties.getBoolean(
				WESSBAS2PCMProperties.GUARDS_AND_ACTIONS, false));
		this.chkUseConsoleForOutput.setSelected(properties.getBoolean(
				WESSBAS2PCMProperties.USE_CONSOLE_OUT, false));
		this.txtAllocationModelFile.setText(properties.getString(
				WESSBAS2PCMProperties.MODEL_ALLOCATION, ""));
		this.txtDirectorySource.setText(properties.getString(
				WESSBAS2PCMProperties.MODEL_DIRECTORY_SOURCE, ""));
		this.txtDirectoryTarget.setText(properties.getString(
				WESSBAS2PCMProperties.MODEL_DIRECTORY_TARGET, ""));
		this.txtRepositoryModelFile.setText(properties.getString(
				WESSBAS2PCMProperties.MODEL_REPOSITORY, ""));
		this.txtResourceModelFile.setText(properties.getString(
				WESSBAS2PCMProperties.MODEL_RESOURCE_ENVIRONMENT, ""));
		this.txtSystemModelFile.setText(properties.getString(
				WESSBAS2PCMProperties.MODEL_SYSTEM, ""));
		this.txtUsageModelFile.setText(properties.getString(
				WESSBAS2PCMProperties.MODEL_USAGE, ""));
		this.txtProbabilityFilterAmountOfDigits
				.setText(properties
						.getString(
								WESSBAS2PCMProperties.PROBABILITY_FILTER_AMOUNT_OF_DIGITS,
								"3"));
		this.chkUseProbabilityFilter.setSelected(properties.getBoolean(
				WESSBAS2PCMProperties.PROBABILITY_FILTER_USE, false));

		// -- Load provided roles
		btnRefreshSystemProvidedRoleActionPerformed(null);

		String strProvidedRole = properties.getString(
				WESSBAS2PCMProperties.MODEL_SYSTEM_PROVIDED_ROLE, "");
		cboSystemProvidedRole.setSelectedItem(strProvidedRole);
	}

	/**
	 * Saves the properties.
	 */

	private void saveProperties() {
		this.m_properties.putBoolean(WESSBAS2PCMProperties.GUARDS_AND_ACTIONS,
				this.chkGenerateGuardsAndActions.isSelected());
		this.m_properties.putBoolean(WESSBAS2PCMProperties.USE_CONSOLE_OUT,
				this.chkUseConsoleForOutput.isSelected());
		this.m_properties.putString(WESSBAS2PCMProperties.MODEL_ALLOCATION,
				this.txtAllocationModelFile.getText());
		this.m_properties.putString(
				WESSBAS2PCMProperties.MODEL_DIRECTORY_SOURCE,
				this.txtDirectorySource.getText());
		this.m_properties.putString(
				WESSBAS2PCMProperties.MODEL_DIRECTORY_TARGET,
				this.txtDirectoryTarget.getText());
		this.m_properties.putString(WESSBAS2PCMProperties.MODEL_REPOSITORY,
				this.txtRepositoryModelFile.getText());
		this.m_properties.putString(
				WESSBAS2PCMProperties.MODEL_RESOURCE_ENVIRONMENT,
				this.txtResourceModelFile.getText());
		this.m_properties.putString(WESSBAS2PCMProperties.MODEL_SYSTEM,
				this.txtSystemModelFile.getText());
		this.m_properties.putString(WESSBAS2PCMProperties.MODEL_USAGE,
				this.txtUsageModelFile.getText());
		this.m_properties.putString(
				WESSBAS2PCMProperties.MODEL_SYSTEM_PROVIDED_ROLE,
				(String) this.cboSystemProvidedRole.getSelectedItem());
		this.m_properties.putString(
				WESSBAS2PCMProperties.PROBABILITY_FILTER_AMOUNT_OF_DIGITS,
				this.txtProbabilityFilterAmountOfDigits.getText());
		this.m_properties.putBoolean(
				WESSBAS2PCMProperties.PROBABILITY_FILTER_USE,
				this.chkUseProbabilityFilter.isSelected());

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

	public EditWessbasToPcmConfigurationFile(JFrame frameOwner) {
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

	@Override
	protected void btnSelectDirectorySourceActionPerformed(
			ActionEvent actionEvent) {
		selectDirectoryHelper(WESSBAS2PCMProperties.MODEL_DIRECTORY_SOURCE,
				this.txtDirectorySource);

		// -- Usability tweak
		String[] strExtensionArray = new String[] { EXTENSION_ALLOCATION,
				EXTENSION_REPOSITORY, EXTENSION_RESENV, EXTENSION_SYSTEM };
		JTextField[] txtTargetTextFieldArray = new JTextField[] {
				this.txtAllocationModelFile, this.txtRepositoryModelFile,
				this.txtResourceModelFile, this.txtSystemModelFile };

		setModels(this.txtDirectorySource, strExtensionArray,
				txtTargetTextFieldArray);

		// -- Refresh provided roles
		btnRefreshSystemProvidedRoleActionPerformed(null);
	}

	@Override
	protected void btnSelectResourceModelFileActionPerformed(
			ActionEvent actionEvent) {
		selectFileNameHelper(WESSBAS2PCMProperties.MODEL_RESOURCE_ENVIRONMENT,
				this.txtResourceModelFile);
	}

	@Override
	protected void btnSelectRepositoryModelFileActionPerformed(
			ActionEvent actionEvent) {
		selectFileNameHelper(WESSBAS2PCMProperties.MODEL_REPOSITORY,
				this.txtRepositoryModelFile);
	}

	@Override
	protected void btnSelectSystemModelFileActionPerformed(
			ActionEvent actionEvent) {
		selectFileNameHelper(WESSBAS2PCMProperties.MODEL_SYSTEM,
				this.txtSystemModelFile);

		// -- Refresh roles
		btnRefreshSystemProvidedRoleActionPerformed(null);
	}

	@Override
	protected void btnSelectAllocationModelFileActionPerformed(
			ActionEvent actionEvent) {
		selectFileNameHelper(WESSBAS2PCMProperties.MODEL_ALLOCATION,
				this.txtAllocationModelFile);
	}

	@Override
	protected void btnSelectUsageModelFileActionPerformed(
			ActionEvent actionEvent) {
		selectFileNameHelper(WESSBAS2PCMProperties.MODEL_USAGE,
				this.txtUsageModelFile);
	}

	@Override
	protected void btnSelectDirectoryTargetActionPerformed(
			ActionEvent actionEvent) {
		selectDirectoryHelper(WESSBAS2PCMProperties.MODEL_DIRECTORY_TARGET,
				this.txtDirectoryTarget);
	}

	@Override
	protected void btnRefreshSystemProvidedRoleActionPerformed(
			ActionEvent actionEvent) {
		String strPath0 = StringUtils.toNull(this.txtDirectorySource.getText());
		String strPath1 = StringUtils.toNull(this.txtSystemModelFile.getText());

		if (strPath0 == null || strPath1 == null) {
			return;
		}

		String[] strProvidedRoleArray = null;

		File fPath = new File(strPath0, strPath1);
		if (fPath != null && fPath.exists()) {
			try {
				System system = PCMUtils.getSystem(fPath);
				strProvidedRoleArray = PCMUtils.getProvidedRoleArray(system);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (strProvidedRoleArray != null) {
			this.cboSystemProvidedRole.removeAll();

			for (String strProvidedRole : strProvidedRoleArray) {
				this.cboSystemProvidedRole.addItem(strProvidedRole);
			}

			if (this.cboSystemProvidedRole.getItemCount() > 0) {
				this.cboSystemProvidedRole.setSelectedIndex(0);
			}
		}
	}

	/**
	 * Selects a directory and sets the corresponding value in the properties.
	 * 
	 * @param strKey
	 *            Key of the value to be set.
	 * @param textField
	 *            Textfield the path is set to.
	 */

	private void selectDirectoryHelper(String strKey, JTextField textField) {
		File fDirectory = UIFileChooser.selectDirectoryPathHelper(textField,
				this);
		if (fDirectory != null) {
			this.m_properties.put(strKey, textField.getText());
		}
	}

	/**
	 * Selects a file and sets the corresponding value in the properties.
	 * 
	 * @param strKey
	 *            Key of the value to be set.
	 * @param textField
	 *            Textfield the path is set to.
	 */

	private void selectFileNameHelper(String strKey, JTextField textField) {
		File file = UIFileChooser.selectFileNameHelper(textField, this);
		if (file != null) {
			this.m_properties.put(strKey, textField.getText());
		}
	}

	/**
	 * Automatically sets the PCM models.
	 * 
	 * @param txtSourceDirectory
	 *            Source directory of the models.
	 * @param strExtensionArray
	 *            Extensions of the PCM models.
	 * @param txtTargetTextFieldArray
	 *            Target textfields the PCM models are set to.
	 */

	private void setModels(JTextField txtSourceDirectory,
			String[] strExtensionArray, JTextField[] txtTargetTextFieldArray) {
		String strDirectoryPath = StringUtils.toNull(txtSourceDirectory
				.getText());
		if (strDirectoryPath == null) {
			return;
		}

		File fDirectory = new File(strDirectoryPath);
		if (!fDirectory.exists() || fDirectory.isFile()) {
			return;
		}

		int n = 0;

		File[] fFileArray = fDirectory.listFiles();
		for (File file : fFileArray) {
			String strFileName = file.getName().toLowerCase();

			for (int i = 0; i < strExtensionArray.length; i++) {
				String strExtension = strExtensionArray[i];
				if (strFileName.endsWith(strExtension)) {
					JTextField textField = txtTargetTextFieldArray[i];
					if (StringUtils.toNull(textField.getText()) != null) {
						break;
					} else {
						textField.setText(FileUtils.extractFileName(file
								.getPath()));

						if (++n == txtTargetTextFieldArray.length) {
							return;
						}
					}
				}
			}
		}
	}
}