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
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBException;

import org.fortiss.pmwt.wex.ui.generated.BaseCreateReaderConfigurationFileDialog;
import org.fortiss.pmwt.wex.ui.io.generic.GenericReader;
import org.fortiss.pmwt.wex.ui.io.generic.GenericReaderConfiguration;
import org.fortiss.pmwt.wex.ui.utils.FileUtils;
import org.fortiss.pmwt.wex.ui.utils.SerializationUtils;
import org.fortiss.pmwt.wex.ui.utils.StringUtils;
import org.fortiss.pmwt.wex.ui.utils.UIFileChooser;

/**
 * Reader configuration dialog.
 */

public class CreateReaderConfigurationFileDialog extends
		BaseCreateReaderConfigurationFileDialog {
	/**
	 * Serialization.
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * Generic reader configuration.
	 */

	private GenericReaderConfiguration m_configuration = null;

	/**
	 * Convenience reference to the fields in the generic reader configuration.
	 */

	private GenericReaderConfiguration.CField[] m_arrField = null;

	/**
	 * Last selected index in the "field" combobox.
	 */

	private int m_nLastIndex = -1;

	/**
	 * Line to be used for previewing the current expression.
	 */

	private String m_strPreviewLine = null;

	/**
	 * Lines to be shown in the preview text field.
	 */

	private String[] m_strPreviewValueArray = null;

	/**
	 * Reader configuration file.
	 */

	private File m_fReaderConfigurationFile = null;

	/**
	 * Constructor.
	 */

	public CreateReaderConfigurationFileDialog(JFrame ownerFrame) {
		super(ownerFrame);
	}

	/**
	 * Sets the path to the configuration file.
	 * 
	 * @param strReaderConfigurationFilePath
	 *            Path to the configuration file.
	 */

	public void setReaderConfigurationFilePath(
			String strReaderConfigurationFilePath) {
		// -- New
		File file = new File(strReaderConfigurationFilePath);
		if (!file.exists() || !file.isFile()) {
			GenericReaderConfiguration configuration = GenericReaderConfiguration
					.newDefaultInstance();
			setConfiguration(configuration);
		}
		// -- Edit
		else {
			try {
				// -- Read configuration
				GenericReaderConfiguration configuration = SerializationUtils
						.deserializeFromXML(file,
								GenericReaderConfiguration.class);
				setConfiguration(configuration);

				this.m_fReaderConfigurationFile = file;
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets the path to the input data.
	 * 
	 * @param strCSVFilePath
	 *            Path to the input data.
	 */

	public void setCSVFilePath(String strCSVFilePath) {
		this.txtPathToSourceFile
				.setText(strCSVFilePath != null ? strCSVFilePath : "");

		// -- Automatic preview
		btnPreviewActionPerformed(null);
	}

	/**
	 * @return Reader configuration file.
	 */

	public File getReaderConfigurationFile() {
		return this.m_fReaderConfigurationFile;
	}

	/**
	 * Handles setting the generic reader configuration.
	 * 
	 * @param configuration
	 *            Generic reader configuration to be set.
	 */

	@SuppressWarnings("unchecked")
	private void setConfiguration(GenericReaderConfiguration configuration) {
		// -- Set member variables
		this.m_configuration = configuration;
		this.m_arrField = configuration.getFieldArray();

		// -- Refresh UI
		this.txtScript1.setText("");
		this.txtScriptPreview.setText("");
		this.cboTargetField1.removeAllItems();
		this.m_nLastIndex = 0;

		// -- Set delimiter
		String strDelimiter = configuration.getDelimiter();
		if (strDelimiter != null) {
			this.txtDelimiter.setText(strDelimiter);
		}

		//
		// -- Tab #0
		//

		this.txtScript0.setText(StringUtils.toEmpty(configuration
				.getPreConditionScript()));

		//
		// -- Tab #1
		//

		// -- Append fields to cboTargetFields
		for (GenericReaderConfiguration.CField field : this.m_arrField) {
			String strType = field.getClazzType().toString();
			strType = strType.substring(strType.lastIndexOf(".") + 1);

			String strValue = field.getName()
					+ (field.isMandatory() ? " ***" : "") + " (" + strType
					+ ")";
			this.cboTargetField1.addItem(strValue);
		}

		// -- Select first element
		if (this.cboTargetField1.getItemCount() > 0) {
			this.m_nLastIndex = -1;
			this.cboTargetField1.setSelectedIndex(0);
		}
	}

	/**
	 * Stores all scripts.
	 */

	private void saveScripts() {
		if (this.m_configuration == null) {
			// -- Intercepts state change on startup
			return;
		}

		// -- Tab #0
		this.m_configuration.setPreConditionScript(StringUtils
				.toEmpty(this.txtScript0.getText()));

		// -- Tab #1
		GenericReaderConfiguration.CField lastField = this.m_arrField[this.m_nLastIndex];
		lastField.setScript(StringUtils.toEmpty(this.txtScript1.getText()));
	}

	/**
	 * Convenient method for retrieving the delimiter.
	 * 
	 * @return Delimiter set by user or ";" as default delimiter.
	 */

	private String getDelimiter() {
		String strDelimiter = this.txtDelimiter.getText();
		if (strDelimiter == null || strDelimiter.length() == 0) {
			strDelimiter = ";";
			this.txtDelimiter.setText(strDelimiter);
		}

		return strDelimiter;
	}

	@Override
	protected void tpMainStateChanged(ChangeEvent changeEvent) {
		saveScripts();
	}

	/**
	 * Triggered when the user hits the "Preview file content" button.
	 */

	@Override
	protected void btnPreviewActionPerformed(ActionEvent actionEvent) {
		// -- Retrieve and validate file path
		File fInputFile = new File(this.txtPathToSourceFile.getText());
		if (!fInputFile.exists()) {
			JOptionPane.showMessageDialog(this,
					"File \"" + FileUtils.toPathString(fInputFile)
							+ "\" could not be found.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// -- Read first three lines
		try {
			// -- Read lines
			final int LINES_TO_READ = 3;
			final int LINE_FOR_PREVIEW = 1;
			String[] strLineArray = FileUtils.readLines(fInputFile.getPath(),
					LINES_TO_READ); // IOException

			// -- Split strings and retrieve meta information
			String strDelimiter = getDelimiter();
			String[][] strValueMatrix = new String[LINES_TO_READ][];
			int nMaxColumnCount = 0;

			for (int i = 0; i < LINES_TO_READ; i++) {
				String strLine = strLineArray[i];
				String[] strValueArray = strLine.split(strDelimiter);
				nMaxColumnCount = Math.max(strValueArray.length,
						nMaxColumnCount);

				strValueMatrix[i] = strValueArray;
			}

			// -- Prepare table
			DefaultTableModel model = (DefaultTableModel) this.tabSourceFilePreview
					.getModel();
			model.setColumnCount(nMaxColumnCount);

			// -- Set column titles in jexl format
			String[] strColumnIdentiferArray = new String[nMaxColumnCount];
			for (int i = 0; i < nMaxColumnCount; i++) {
				strColumnIdentiferArray[i] = "$" + i + "$";
			}

			model.setColumnIdentifiers(strColumnIdentiferArray);

			// -- Add rows
			for (int y = 0; y < LINES_TO_READ; y++) {
				String[] strValues = strValueMatrix[y];
				for (int x = 0; x < nMaxColumnCount; x++) {
					if (x < strValues.length) {
						model.setValueAt(strValues[x], y, x);
					} else {
						model.setValueAt("", y, x);
					}
				}
			}

			// -- Add second line as preview line
			this.m_strPreviewLine = strLineArray[LINE_FOR_PREVIEW];
			this.m_strPreviewValueArray = this.m_strPreviewLine
					.split(strDelimiter);
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, "Error while reading file: "
					+ ioe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			ioe.printStackTrace();
		}
	}

	/**
	 * Triggered when the user hits the "next" button.
	 */

	@Override
	protected void btnSaveActionPerformed(ActionEvent evt) {
		saveScripts();

		// -- Retrieve output file path for configuration
		File fOutputFile = this.m_fReaderConfigurationFile;
		if (fOutputFile == null) {
			fOutputFile = UIFileChooser.showSaveDialog(this);
			if (fOutputFile == null) {
				return;
			}

			this.m_fReaderConfigurationFile = fOutputFile;
		}

		// -- Set delimiter
		this.m_configuration.setDelimiter(this.txtDelimiter.getText());

		// -- Write reader configuration
		try {
			SerializationUtils
					.serializeToXML(fOutputFile, this.m_configuration);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void btnSaveAndCloseActionPerformed(ActionEvent evt) {
		btnSaveActionPerformed(null);
		setVisible(false);
	}

	/**
	 * Triggered when the user hits the "Preview script" button.
	 */

	@Override
	protected void btnPreviewScriptActionPerformed(ActionEvent actionEvent) {
		// -- Retrieve parameters
		String[] arrValue = this.m_strPreviewValueArray;
		if (arrValue == null) {
			JOptionPane.showMessageDialog(this,
					"No preview file content for expression preview.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// -- Tab 0/1
		String strResult = null;
		if (this.tpMain.getSelectedIndex() == 0) {
			String strScript = this.txtScript0.getText();
			Object objResult = GenericReader.preview(strScript, arrValue);
			strResult = objResult instanceof Boolean ? objResult.toString()
					: "No boolean return value: " + objResult.toString();
		} else if (this.tpMain.getSelectedIndex() == 1) {
			String strScript = this.txtScript1.getText();
			Object objResult = GenericReader.preview(strScript, arrValue);
			strResult = objResult.toString();
		}

		this.txtScriptPreview.setText(strResult);
	}

	// -----
	// ----- Tab #0: Pre-Condition
	// -----

	// -----
	// ----- Tab #1: Conversion
	// -----

	/**
	 * Triggered when the user changes the selection of the combobox.
	 */

	@Override
	protected void cboTargetField1ActionPerformed(ActionEvent actionEvent) {
		int nIndex = this.cboTargetField1.getSelectedIndex();
		if (nIndex == -1 || nIndex == this.m_nLastIndex) {
			return;
		}

		GenericReaderConfiguration.CField field = this.m_arrField[nIndex];

		// -- Store old expression
		if (this.m_nLastIndex != nIndex
				&& !(this.m_nLastIndex == -1 && nIndex == 0)) {
			saveScripts();
		}

		this.m_nLastIndex = nIndex;

		// -- Handle current expression
		this.txtScript1.setText(field.getScript());
	}
}
