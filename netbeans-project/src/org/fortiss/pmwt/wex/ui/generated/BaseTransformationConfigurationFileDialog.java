package org.fortiss.pmwt.wex.ui.generated;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

public class BaseTransformationConfigurationFileDialog extends
		javax.swing.JDialog implements ActionListener, ComponentListener {

	public BaseTransformationConfigurationFileDialog(JFrame frameOwner) {
		super(frameOwner);
		initComponents();
	}

	/**
	 * Creates new form BasePrepareInputDataPage1
	 */
	public BaseTransformationConfigurationFileDialog() {
		super();
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		pnlBehaviorModelExtractor = new JPanel();
		lblXMeansMin = new JLabel();
		txtXMeansMin = new JTextField();
		lblXMeansMax = new JLabel();
		txtXMeansMax = new JTextField();
		lblXMeansSeed = new JLabel();
		txtXMeansSeed = new JTextField();
		pnlGeneral = new JPanel();
		chkGeneratePCMUsageModel = new JCheckBox();
		chkGenerateApacheJMeterTestplan = new JCheckBox();
		chkUseXMeans = new JCheckBox();
		pnlBottom = new JPanel();
		btnSave = new JButton();
		btnSaveAndClose = new JButton();

		setTitle("Transformation properties");
		setBounds(new Rectangle(0, 0, 700, 680));
		setMinimumSize(new Dimension(700, 550));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setName("wndEditTransformationConfiguration"); // NOI18N
		setResizable(false);
		setType(Type.UTILITY);
		addComponentListener(this);

		pnlBehaviorModelExtractor.setBorder(BorderFactory
				.createTitledBorder("Behavior Model Extractor"));

		lblXMeansMin.setText("Min Clusters:");
		lblXMeansMin.setMaximumSize(new Dimension(200, 20));
		lblXMeansMin.setMinimumSize(new Dimension(200, 20));
		lblXMeansMin.setPreferredSize(new Dimension(200, 20));

		txtXMeansMin.setMaximumSize(new Dimension(200, 30));
		txtXMeansMin.setMinimumSize(new Dimension(200, 30));
		txtXMeansMin.setPreferredSize(new Dimension(200, 30));

		lblXMeansMax.setText("Max Clusters:");
		lblXMeansMax.setMaximumSize(new Dimension(200, 20));
		lblXMeansMax.setMinimumSize(new Dimension(200, 20));
		lblXMeansMax.setPreferredSize(new Dimension(200, 20));

		txtXMeansMax.setMaximumSize(new Dimension(200, 30));
		txtXMeansMax.setMinimumSize(new Dimension(200, 30));
		txtXMeansMax.setPreferredSize(new Dimension(200, 30));

		lblXMeansSeed.setText("Seed Value");
		lblXMeansSeed.setMaximumSize(new Dimension(200, 20));
		lblXMeansSeed.setMinimumSize(new Dimension(200, 20));
		lblXMeansSeed.setPreferredSize(new Dimension(200, 20));

		txtXMeansSeed.setMaximumSize(new Dimension(200, 30));
		txtXMeansSeed.setMinimumSize(new Dimension(200, 30));
		txtXMeansSeed.setPreferredSize(new Dimension(200, 30));

		GroupLayout pnlBehaviorModelExtractorLayout = new GroupLayout(
				pnlBehaviorModelExtractor);
		pnlBehaviorModelExtractor.setLayout(pnlBehaviorModelExtractorLayout);
		pnlBehaviorModelExtractorLayout
				.setHorizontalGroup(pnlBehaviorModelExtractorLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								pnlBehaviorModelExtractorLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												pnlBehaviorModelExtractorLayout
														.createParallelGroup(
																GroupLayout.Alignment.LEADING,
																false)
														.addComponent(
																lblXMeansMin,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																txtXMeansMin,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												pnlBehaviorModelExtractorLayout
														.createParallelGroup(
																GroupLayout.Alignment.LEADING,
																false)
														.addComponent(
																lblXMeansMax,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																txtXMeansMax,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												pnlBehaviorModelExtractorLayout
														.createParallelGroup(
																GroupLayout.Alignment.LEADING)
														.addGroup(
																pnlBehaviorModelExtractorLayout
																		.createSequentialGroup()
																		.addComponent(
																				lblXMeansSeed,
																				GroupLayout.DEFAULT_SIZE,
																				232,
																				Short.MAX_VALUE)
																		.addGap(46,
																				46,
																				46))
														.addGroup(
																pnlBehaviorModelExtractorLayout
																		.createSequentialGroup()
																		.addComponent(
																				txtXMeansSeed,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addContainerGap(
																				GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)))));
		pnlBehaviorModelExtractorLayout
				.setVerticalGroup(pnlBehaviorModelExtractorLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								pnlBehaviorModelExtractorLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												pnlBehaviorModelExtractorLayout
														.createParallelGroup(
																GroupLayout.Alignment.TRAILING)
														.addGroup(
																pnlBehaviorModelExtractorLayout
																		.createSequentialGroup()
																		.addGroup(
																				pnlBehaviorModelExtractorLayout
																						.createParallelGroup(
																								GroupLayout.Alignment.BASELINE)
																						.addComponent(
																								lblXMeansMax,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								lblXMeansSeed,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE))
																		.addPreferredGap(
																				LayoutStyle.ComponentPlacement.RELATED)
																		.addGroup(
																				pnlBehaviorModelExtractorLayout
																						.createParallelGroup(
																								GroupLayout.Alignment.BASELINE)
																						.addComponent(
																								txtXMeansMax,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								txtXMeansSeed,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)))
														.addGroup(
																pnlBehaviorModelExtractorLayout
																		.createSequentialGroup()
																		.addComponent(
																				lblXMeansMin,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				txtXMeansMin,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)))
										.addContainerGap(
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		pnlGeneral.setBorder(BorderFactory.createTitledBorder("General"));

		chkGeneratePCMUsageModel.setText("Generate modified PCM Usage Model");
		chkGeneratePCMUsageModel.setMaximumSize(new Dimension(250, 30));
		chkGeneratePCMUsageModel.setMinimumSize(new Dimension(250, 30));
		chkGeneratePCMUsageModel.setPreferredSize(new Dimension(250, 30));
		chkGeneratePCMUsageModel.addActionListener(this);

		chkGenerateApacheJMeterTestplan
				.setText("Generate Apache JMeter Testplan");
		chkGenerateApacheJMeterTestplan.setMaximumSize(new Dimension(250, 30));
		chkGenerateApacheJMeterTestplan.setMinimumSize(new Dimension(250, 30));
		chkGenerateApacheJMeterTestplan
				.setPreferredSize(new Dimension(250, 30));
		chkGenerateApacheJMeterTestplan.addActionListener(this);

		chkUseXMeans
				.setText("Use X-means clustering. If not, K-means clustering is used");
		chkUseXMeans.setMaximumSize(new Dimension(500, 30));
		chkUseXMeans.setMinimumSize(new Dimension(500, 30));
		chkUseXMeans.setPreferredSize(new Dimension(500, 30));
		chkUseXMeans.addActionListener(this);

		GroupLayout pnlGeneralLayout = new GroupLayout(pnlGeneral);
		pnlGeneral.setLayout(pnlGeneralLayout);
		pnlGeneralLayout
				.setHorizontalGroup(pnlGeneralLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								pnlGeneralLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												pnlGeneralLayout
														.createParallelGroup(
																GroupLayout.Alignment.LEADING)
														.addComponent(
																chkGeneratePCMUsageModel,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																chkGenerateApacheJMeterTestplan,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																chkUseXMeans,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addContainerGap(
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		pnlGeneralLayout.setVerticalGroup(pnlGeneralLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING)
				.addGroup(
						pnlGeneralLayout
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(chkGeneratePCMUsageModel,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(chkGenerateApacheJMeterTestplan,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(chkUseXMeans,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap(11, Short.MAX_VALUE)));

		pnlBottom.setMaximumSize(new Dimension(680, 32767));
		pnlBottom.setMinimumSize(new Dimension(680, 0));

		btnSave.setText("Save");
		btnSave.setMaximumSize(new Dimension(140, 30));
		btnSave.setMinimumSize(new Dimension(140, 30));
		btnSave.setPreferredSize(new Dimension(140, 30));
		btnSave.addActionListener(this);

		btnSaveAndClose.setText("Save & Close");
		btnSaveAndClose.setMaximumSize(new Dimension(140, 30));
		btnSaveAndClose.setMinimumSize(new Dimension(140, 30));
		btnSaveAndClose.setPreferredSize(new Dimension(140, 30));
		btnSaveAndClose.addActionListener(this);

		GroupLayout pnlBottomLayout = new GroupLayout(pnlBottom);
		pnlBottom.setLayout(pnlBottomLayout);
		pnlBottomLayout.setHorizontalGroup(pnlBottomLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING)
				.addGroup(
						GroupLayout.Alignment.TRAILING,
						pnlBottomLayout
								.createSequentialGroup()
								.addContainerGap(GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(btnSave,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(btnSaveAndClose,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGap(17, 17, 17)));
		pnlBottomLayout.setVerticalGroup(pnlBottomLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				pnlBottomLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								pnlBottomLayout
										.createParallelGroup(
												GroupLayout.Alignment.BASELINE)
										.addComponent(btnSaveAndClose,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(btnSave,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
						.addContainerGap(GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING)
												.addComponent(
														pnlGeneral,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		pnlBehaviorModelExtractor,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE)
																.addGap(0,
																		0,
																		Short.MAX_VALUE))
												.addComponent(
														pnlBottom,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE))
								.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(pnlGeneral,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(pnlBehaviorModelExtractor,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGap(296, 296, 296)
								.addComponent(pnlBottom,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap(GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));

		getAccessibleContext().setAccessibleName("");

		pack();
	}

	// Code for dispatching events from components to event handlers.

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == chkGeneratePCMUsageModel) {
			BaseTransformationConfigurationFileDialog.this
					.chkGeneratePCMUsageModelActionPerformed(evt);
		} else if (evt.getSource() == chkGenerateApacheJMeterTestplan) {
			BaseTransformationConfigurationFileDialog.this
					.chkGenerateApacheJMeterTestplanActionPerformed(evt);
		} else if (evt.getSource() == btnSave) {
			BaseTransformationConfigurationFileDialog.this
					.btnSaveActionPerformed(evt);
		} else if (evt.getSource() == btnSaveAndClose) {
			BaseTransformationConfigurationFileDialog.this
					.btnSaveAndCloseActionPerformed(evt);
		}
	}

	public void componentHidden(ComponentEvent evt) {
	}

	public void componentMoved(ComponentEvent evt) {
	}

	public void componentResized(ComponentEvent evt) {
	}

	public void componentShown(ComponentEvent evt) {
		if (evt.getSource() == BaseTransformationConfigurationFileDialog.this) {
			BaseTransformationConfigurationFileDialog.this
					.formComponentShown(evt);
		}
	}// </editor-fold>//GEN-END:initComponents

	private void chkGeneratePCMUsageModelActionPerformed(ActionEvent evt) {// GEN-FIRST:event_chkGeneratePCMUsageModelActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_chkGeneratePCMUsageModelActionPerformed

	private void chkGenerateApacheJMeterTestplanActionPerformed(ActionEvent evt) {// GEN-FIRST:event_chkGenerateApacheJMeterTestplanActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_chkGenerateApacheJMeterTestplanActionPerformed

	protected void btnSaveAndCloseActionPerformed(ActionEvent evt) {// GEN-FIRST:event_btnSaveAndCloseActionPerformed
																	// TODO add
																	// your
																	// handling
																	// code
																	// here:
	}// GEN-LAST:event_btnSaveAndCloseActionPerformed

	protected void formComponentShown(ComponentEvent evt) {// GEN-FIRST:event_formComponentShown
															// TODO add your
															// handling code
															// here:
	}// GEN-LAST:event_formComponentShown

	protected void btnSaveActionPerformed(ActionEvent evt) {// GEN-FIRST:event_btnSaveActionPerformed
															// TODO add your
															// handling code
															// here:
	}// GEN-LAST:event_btnSaveActionPerformed

	protected void btnSelectDirectorySourceActionPerformed(ActionEvent evt) {// GEN-FIRST:event_btnSelectDirectorySourceActionPerformed
																				// TODO
																				// add
																				// your
																				// handling
																				// code
																				// here:
	}// GEN-LAST:event_btnSelectDirectorySourceActionPerformed

	protected void btnSelectResourceModelFileActionPerformed(ActionEvent evt) {// GEN-FIRST:event_btnSelectResourceModelFileActionPerformed
																				// TODO
																				// add
																				// your
																				// handling
																				// code
																				// here:
	}// GEN-LAST:event_btnSelectResourceModelFileActionPerformed

	protected void btnSelectRepositoryModelFileActionPerformed(ActionEvent evt) {// GEN-FIRST:event_btnSelectRepositoryModelFileActionPerformed
																					// TODO
																					// add
																					// your
																					// handling
																					// code
																					// here:
	}// GEN-LAST:event_btnSelectRepositoryModelFileActionPerformed

	protected void btnSelectSystemModelFileActionPerformed(ActionEvent evt) {// GEN-FIRST:event_btnSelectSystemModelFileActionPerformed
																				// TODO
																				// add
																				// your
																				// handling
																				// code
																				// here:
	}// GEN-LAST:event_btnSelectSystemModelFileActionPerformed

	protected void btnSelectAllocationModelFileActionPerformed(ActionEvent evt) {// GEN-FIRST:event_btnSelectAllocationModelFileActionPerformed
																					// TODO
																					// add
																					// your
																					// handling
																					// code
																					// here:
	}// GEN-LAST:event_btnSelectAllocationModelFileActionPerformed

	protected void btnSelectUsageModelFileActionPerformed(ActionEvent evt) {// GEN-FIRST:event_btnSelectUsageModelFileActionPerformed
																			// TODO
																			// add
																			// your
																			// handling
																			// code
																			// here:
	}// GEN-LAST:event_btnSelectUsageModelFileActionPerformed

	protected void btnSelectDirectoryTargetActionPerformed(ActionEvent evt) {// GEN-FIRST:event_btnSelectDirectoryTargetActionPerformed
																				// TODO
																				// add
																				// your
																				// handling
																				// code
																				// here:
	}// GEN-LAST:event_btnSelectDirectoryTargetActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	protected JButton btnSave;
	protected JButton btnSaveAndClose;
	protected JCheckBox chkGenerateApacheJMeterTestplan;
	protected JCheckBox chkGeneratePCMUsageModel;
	protected JCheckBox chkUseXMeans;
	protected JLabel lblXMeansMax;
	protected JLabel lblXMeansMin;
	protected JLabel lblXMeansSeed;
	protected JPanel pnlBehaviorModelExtractor;
	protected JPanel pnlBottom;
	protected JPanel pnlGeneral;
	protected JTextField txtXMeansMax;
	protected JTextField txtXMeansMin;
	protected JTextField txtXMeansSeed;
	// End of variables declaration//GEN-END:variables
}
