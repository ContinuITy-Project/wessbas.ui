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
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

import org.fortiss.pmwt.wex.ui.generated.BaseCreateWorkloadPropertiesFileDialog;
import org.fortiss.pmwt.wex.ui.jobs.IJobNotify;
import org.fortiss.pmwt.wex.ui.jobs.JobManager;
import org.fortiss.pmwt.wex.ui.jobs.RetrieveSimpleMetricsJob;
import org.fortiss.pmwt.wex.ui.metrics.SimpleMetrics;
import org.fortiss.pmwt.wex.ui.persistence.Project;
import org.fortiss.pmwt.wex.ui.persistence.WorkloadProperties;
import org.fortiss.pmwt.wex.ui.utils.StringUtils;
import org.fortiss.pmwt.wex.ui.utils.UIUtils;

/**
 * Workload properties configuration dialog.
 */

public class CreateWorkloadPropertiesFileDialog extends
		BaseCreateWorkloadPropertiesFileDialog implements IJobNotify {
	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Path to workload properties file.
	 */

	private File m_fWorkloadPropertiesFile = null;

	/**
	 * Workload properties.
	 */

	private WorkloadProperties m_workloadPropertiesFile = null;

	/**
	 * Current project.
	 */

	private Project m_project = null;

	/**
	 * Helper for keeping track of selected populations.
	 */

	private String m_strLastSelectedPopulation = null;

	/**
	 * Helper for keeping track of selected arrival rates.
	 */

	private String m_strLastSelectedArrivalRate = null;

	/**
	 * Constructor.
	 */

	public CreateWorkloadPropertiesFileDialog(JFrame frameOwner) {
		super(frameOwner);
	}

	/**
	 * Sets the path to the configuration file.
	 * 
	 * @param strWorkloadIntensityFilePath
	 *            Path to the configuration file.
	 */

	public void setWorkloadPropertiesFilePath(
			String strWorkloadIntensityFilePath) {
		// -- Edit
		File file = new File(strWorkloadIntensityFilePath);
		if (file.exists() || file.isFile()) {
			try {
				WorkloadProperties workloadPropertiesFile = WorkloadProperties
						.read(file);
				setWorkloadPropertiesFile(workloadPropertiesFile);

				this.m_fWorkloadPropertiesFile = file;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets the current project.
	 * 
	 * @param project
	 *            Current project.
	 */

	public void setProject(Project project) {
		this.m_project = project;
	}

	@Override
	public void jobNotify(int nID, int nReturnType, Object objValue) {
		// -- Enable UI
		this.enableAll(true);

		// -- Refresh UI values
		RetrieveSimpleMetricsJob job = (RetrieveSimpleMetricsJob) objValue;
		SimpleMetrics simpleMetrics = (SimpleMetrics) job.getSimpleMetrics();

		this.txtAverageSessionCount.setText(simpleMetrics
				.getAverageActiveSessionCount() + "");
		this.txtAverageArrivalRate.setText(simpleMetrics
				.getAverageArrivalRate() + "");
		this.txtAverageSessionLength.setText(simpleMetrics
				.getAverageSessionLengthByEventCount() + "");
		this.txtEventCount.setText(simpleMetrics.getEventCountOverAllSessions()
				+ "");
		this.txtMaxEventCountInSession.setText(simpleMetrics
				.getMaxEventCountInSession() + "");
		this.txtMinEventCountInSession.setText(simpleMetrics
				.getMinEventCountInSession() + "");
		this.txtSessionCount.setText(simpleMetrics.getSessionCount() + "");

		// -- Table
		UIUtils.clearTable(this.tblSessionCountPerTimeRange);

		DefaultTableModel model = (DefaultTableModel) this.tblSessionCountPerTimeRange
				.getModel();

		String[] strSegmentArray = simpleMetrics.createSegmentArray();
		Map<String, Integer> mapActiveSession = simpleMetrics
				.getActiveSessionMap();
		Map<String, Integer> mapArrivalRate = simpleMetrics.getArrivalRateMap();
		if (strSegmentArray != null && strSegmentArray.length > 0) {
			for (String strSegment : strSegmentArray) {
				Integer iActiveSessionCount = mapActiveSession.get(strSegment);
				Integer iArrivalRate = mapArrivalRate.get(strSegment);

				model.addRow(new Object[] { strSegment,
						StringUtils.toString(iActiveSessionCount, "0"),
						StringUtils.toString(iArrivalRate, "0") });
			}
		}

		// -- Select first row in table
		if (this.tblSessionCountPerTimeRange.getRowCount() > 0) {
			this.tblSessionCountPerTimeRange.setRowSelectionInterval(0, 0);
			tblSessionCountPerTimeRangeMouseClicked(null);
		}
	}

	@Override
	protected void btnAnalyzeActionPerformed(ActionEvent evt) {
		// -- Disable UI
		enableAll(false);

		// -- Init simple metrics
		JobManager.schedule(new RetrieveSimpleMetricsJob(), this.m_project, 0,
				this);
	}

	@Override
	protected void btnSaveAndCloseActionPerformed(ActionEvent evt) {
		btnSaveActionPerformed(null);
		setVisible(false);
	}

	@Override
	protected void btnSaveActionPerformed(ActionEvent evt) {
		this.m_workloadPropertiesFile.put(WorkloadProperties.PROPERTY_FORMULA,
				this.txtFormula.getText());
		this.m_workloadPropertiesFile.put(WorkloadProperties.PROPERTY_TYPE,
				this.txtType.getText());
		this.m_workloadPropertiesFile.put(WorkloadProperties.PROPERTY_WL_TYPE,
				getWorkloadType());
		this.m_workloadPropertiesFile.put(
				WorkloadProperties.PROPERTY_WL_TYPE_VALUE,
				this.txtWorkloadTypeValue.getText());

		try {
			this.m_workloadPropertiesFile.write(this.m_fWorkloadPropertiesFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void rbClosedWorkloadActionPerformed(ActionEvent actionEvent) {
		if (this.m_strLastSelectedPopulation != null) {
			this.txtWorkloadTypeValue.setText(this.m_strLastSelectedPopulation);
		}
	}

	@Override
	protected void rbOpenWorkloadActionPerformed(ActionEvent actionEvent) {
		if (this.m_strLastSelectedArrivalRate != null) {
			this.txtWorkloadTypeValue
					.setText(this.m_strLastSelectedArrivalRate);
		}
	}

	@Override
	protected void tblSessionCountPerTimeRangeMouseClicked(MouseEvent mouseEvent) {
		int nRowIndex = this.tblSessionCountPerTimeRange.getSelectedRow();
		if (nRowIndex < 0) {
			return;
		}

		DefaultTableModel model = (DefaultTableModel) this.tblSessionCountPerTimeRange
				.getModel();
		String strPopulation = this.m_strLastSelectedPopulation = (String) model
				.getValueAt(nRowIndex, 1);
		String strArrivalRate = this.m_strLastSelectedArrivalRate = (String) model
				.getValueAt(nRowIndex, 2);

		if (this.rbClosedWorkload.isSelected()) {
			this.txtWorkloadTypeValue.setText(strPopulation);
		} else if (this.rbOpenWorkload.isSelected()) {
			this.txtWorkloadTypeValue.setText(strArrivalRate);
		}
	}

	/**
	 * Enable/Disable the UI.
	 * 
	 * @param bEnable
	 *            Enable/Disable switch.
	 */

	private void enableAll(boolean bEnable) {
		UIUtils.enableContainer(this, bEnable);
	}

	/**
	 * Sets the workload properties.
	 * 
	 * @param workloadPropertiesFile
	 *            Workload properties instance.
	 */

	private void setWorkloadPropertiesFile(
			WorkloadProperties workloadPropertiesFile) {
		this.txtFormula.setText((String) workloadPropertiesFile
				.get(WorkloadProperties.PROPERTY_FORMULA));
		this.txtType.setText((String) workloadPropertiesFile
				.get(WorkloadProperties.PROPERTY_TYPE));
		this.txtWorkloadTypeValue.setText((String) workloadPropertiesFile
				.get(WorkloadProperties.PROPERTY_WL_TYPE_VALUE));

		String strValue = (String) workloadPropertiesFile
				.get(WorkloadProperties.PROPERTY_WL_TYPE);
		if (WorkloadProperties.VALUE_OPEN.equals(strValue)) {
			this.rbOpenWorkload.setSelected(true);
		} else {
			this.rbClosedWorkload.setSelected(true);
		}

		this.m_workloadPropertiesFile = workloadPropertiesFile;
	}

	/**
	 * @return The currently selected workload type.
	 */

	private String getWorkloadType() {
		if (this.rbOpenWorkload.isSelected()) {
			return WorkloadProperties.VALUE_OPEN;
		} else if (this.rbClosedWorkload.isSelected()) {
			return WorkloadProperties.VALUE_CLOSED;
		}

		return null;
	}
}