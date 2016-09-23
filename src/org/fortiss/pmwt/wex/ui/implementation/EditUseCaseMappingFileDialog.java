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
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBException;

import org.fortiss.pmwt.wex.ui.generated.BaseEditUseCaseMappingFileDialog;
import org.fortiss.pmwt.wex.ui.io.session.StateFilter;
import org.fortiss.pmwt.wex.ui.utils.NumberUtils;
import org.fortiss.pmwt.wex.ui.utils.SerializationUtils;
import org.fortiss.pmwt.wex.ui.utils.StringUtils;

/**
 * Configuration dialog for states.
 */

public class EditUseCaseMappingFileDialog extends
		BaseEditUseCaseMappingFileDialog implements Runnable {
	/**
	 * Serialization.
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * State filter to filter sessions by state name(s).
	 */

	private StateFilter m_stateFilter = null;

	/**
	 * Output configuration file.
	 */

	private File m_fStateFilterFile = null;

	/**
	 * Index of last edited table row.
	 */

	private int m_nRowIndex = -1;

	/**
	 * Index of last edited table column.
	 */

	private int m_nColumnIndex = -1;

	/**
	 * Value of the previously edited cell.
	 */

	private Object m_objOldCellValue = null;

	/**
	 * Constructor.
	 * 
	 * @param frameChain
	 *            Reference to the frame chain.
	 * @param project
	 *            Reference to the frame-wide configuration.
	 */

	public EditUseCaseMappingFileDialog(JFrame frameOwner) {
		super(frameOwner);
	}

	/**
	 * Sets the path to the configuration file.
	 * 
	 * @param strStateFilterFilePath
	 *            Path to the configuration file.
	 */

	public void setStateFilterFilePath(String strStateFilterFilePath) {
		File fInputStateFilterFile = new File(strStateFilterFilePath);
		if (fInputStateFilterFile.exists() && fInputStateFilterFile.isFile()) {
			try {
				this.m_stateFilter = SerializationUtils.deserializeFromXML(
						fInputStateFilterFile, StateFilter.class);
				setStateFilter(this.m_stateFilter);

				this.m_fStateFilterFile = fInputStateFilterFile;
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Worker/UI Thread.
	 */

	@Override
	public void run() {
		this.m_nRowIndex = this.tabUseCaseMapping
				.convertRowIndexToModel(this.tabUseCaseMapping.getEditingRow());
		this.m_nColumnIndex = this.tabUseCaseMapping
				.convertColumnIndexToModel(this.tabUseCaseMapping
						.getEditingColumn());
		this.m_objOldCellValue = this.tabUseCaseMapping.getModel().getValueAt(
				this.m_nRowIndex, this.m_nColumnIndex);
	}

	/**
	 * Handles all steps necessary to set the state filter in the frame.
	 * 
	 * @param stateFilter
	 *            State filter to be set.
	 */

	private void setStateFilter(StateFilter stateFilter) {
		DefaultTableModel model = (DefaultTableModel) this.tabUseCaseMapping
				.getModel();

		// -- Clear table
		for (int i = model.getRowCount() - 1; i >= 0; i--) {
			model.removeRow(i);
		}

		// -- Add state filter content to table
		Map<String, StateFilter.CState> mapUseCaseMapping = this.m_stateFilter
				.getStateMap();
		Iterator<Map.Entry<String, StateFilter.CState>> iterator = mapUseCaseMapping
				.entrySet().iterator();
		while (iterator.hasNext()) {
			// -- Add to table
			Map.Entry<String, StateFilter.CState> mapEntry = iterator.next();
			String strOldStateName = mapEntry.getKey();

			StateFilter.CState state = mapEntry.getValue();
			String strNewStateName = state.getNewStateName();
			Boolean bRemove = new Boolean(state.isRemoveState());
			Boolean bMandatory = new Boolean(state.isMandatoryState());

			model.addRow(new Object[] { strOldStateName, strNewStateName,
					bRemove, bMandatory });
		}

		// -- Set text
		this.txtSessionMustStartWithUseCase.setText(stateFilter
				.getSessionMustStartWithUseCase());
		this.txtSessionMustEndWithUseCase.setText(stateFilter
				.getSessionMustEndWithUseCase());
		this.txtTransitionThresholdTime.setText(stateFilter
				.getTransitionThresholdTime() != null ? stateFilter
				.getTransitionThresholdTime().toString() : "");
	}

	/**
	 * Triggered when the user changes the value of a single cell.
	 * 
	 * @param nRowIndex
	 *            Index of row where the change took place.
	 * @param nColumnIndex
	 *            Index of the column where the change took place.
	 */

	private void valueOfCellHasChanged(int nRowIndex, int nColumnIndex) {
		Object objNewValue = this.tabUseCaseMapping.getValueAt(nRowIndex,
				nColumnIndex);
		String strKey = (String) this.tabUseCaseMapping
				.getValueAt(nRowIndex, 0);

		// -- Column for new state name
		if (nColumnIndex == 1) {
			String strNewValue = (String) objNewValue;
			// -- Only set valid values
			if (StateFilter.isValidStateName(strNewValue)) {
				this.m_stateFilter.setNewStateName(strKey, strNewValue);
			} else {
				JOptionPane.showMessageDialog(this, "Value \"" + strNewValue
						+ "\" contains illegal characters.", "Error",
						JOptionPane.ERROR_MESSAGE);

				// -- Reset to old value
				this.tabUseCaseMapping.setValueAt(this.m_objOldCellValue,
						nRowIndex, nColumnIndex);
			}
		}
		// -- Column for remove state
		else if (nColumnIndex == 2) {
			boolean bValue = ((Boolean) objNewValue).booleanValue();
			this.m_stateFilter.markStateForRemoval(strKey, bValue);
		}
		// -- Column for mandatory state
		else if (nColumnIndex == 3) {
			boolean bValue = ((Boolean) objNewValue).booleanValue();
			this.m_stateFilter.markStateForMandatory(strKey, bValue);
		}
	}

	@Override
	protected void btnSaveAndCloseActionPerformed(ActionEvent evt) {
		btnSaveActionPerformed(null);
		setVisible(false);
	}

	@Override
	protected void btnSaveActionPerformed(ActionEvent evt) {
		try {
			// -- Handle values
			String strSessionMustStartWithUseCase = StringUtils
					.toEmpty(this.txtSessionMustStartWithUseCase.getText());
			this.m_stateFilter
					.setSessionMustStartWithUseCase(strSessionMustStartWithUseCase);

			String strSessionMustEndWithUseCase = StringUtils
					.toEmpty(this.txtSessionMustEndWithUseCase.getText());
			this.m_stateFilter
					.setSessionMustEndWithUseCase(strSessionMustEndWithUseCase);

			String strTransitionThresholdTime = StringUtils
					.toEmpty(this.txtTransitionThresholdTime.getText());
			Long lTransitionThresholdTime = NumberUtils.toLong(
					strTransitionThresholdTime, null);
			this.m_stateFilter
					.setTransitionThresholdTime(lTransitionThresholdTime);

			// -- Serialize
			SerializationUtils.serializeToXML(this.m_fStateFilterFile,
					this.m_stateFilter);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Triggered when the user changes a cell value. See
	 * "http://www.camick.com/java/source/TableCellListener.java" for more
	 * information.
	 */

	protected void tabUseCaseMappingPropertyChange(PropertyChangeEvent evt) {
		if ("tableCellEditor".equals(evt.getPropertyName())) {
			if (this.tabUseCaseMapping.isEditing()) {
				SwingUtilities.invokeLater(this);
			} else {
				valueOfCellHasChanged(this.m_nRowIndex, this.m_nColumnIndex);
			}
		}
	}
}
