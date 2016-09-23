package org.fortiss.pmwt.wex.ui.implementation;

import java.awt.event.ActionEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.xml.bind.JAXBException;

import org.fortiss.pmwt.wex.ui.generated.BaseEditTimeRangeFileDialog;
import org.fortiss.pmwt.wex.ui.jobs.IJobNotify;
import org.fortiss.pmwt.wex.ui.jobs.JobManager;
import org.fortiss.pmwt.wex.ui.jobs.RetrieveTimesFromSessionJob;
import org.fortiss.pmwt.wex.ui.persistence.Project;
import org.fortiss.pmwt.wex.ui.persistence.TimeRangeFilter;
import org.fortiss.pmwt.wex.ui.utils.NumberUtils;
import org.fortiss.pmwt.wex.ui.utils.UIUtils;

/**
 * Time range filter configuration dialog.
 */

public class EditTimeRangeFileDialog extends BaseEditTimeRangeFileDialog implements IJobNotify
{
	/**
	 * Serialization.
	 */

	private static final long				serialVersionUID	= 1L;

	/**
	 * Format for all dates presented in the UI.
	 */

	private static final SimpleDateFormat	DATE_FORMAT			= new SimpleDateFormat( "yyyy-MM-dd    HH:mm:ss" );

	/**
	 * Left slider value.
	 */

	private int								m_nLeftSliderValue	= 0;

	/**
	 * Right slider value.
	 */

	private int								m_nRightSliderValue	= 0;

	/**
	 * Max value for both, left and right slider.
	 */

	private int								m_nMaxSliderValue	= 0;

	/**
	 * First time stamp extracted from the session file.
	 */

	private Project							m_project			= null;

	/**
	 * Initialized flag.
	 */

	private boolean							m_bInitialized		= false;

	/**
	 * Time range filter instance.
	 */

	private TimeRangeFilter					m_timeRangeFilter	= null;

	/**
	 * Constructor.
	 * 
	 * @param frameChain
	 *            Reference to the frame chain.
	 * @param project
	 *            Reference to the frame-wide configuration.
	 */

	public EditTimeRangeFileDialog( JFrame frameOwner, Project project )
	{
		super( frameOwner );

		// --
		this.m_project = project;

		init();
	}

	/**
	 * Initialize the dialog.
	 */

	private void init()
	{
		// -- Disable UI
		UIUtils.enablePanel( this.pnlCrop, false );

		// -- Load time range filter
		TimeRangeFilter timeRangeFilter = null;
		try
		{
			File fInputTimeRangeFilterFile = this.m_project.getFile( Project.TAG_FILE_TIME_RANGE_FILTER_CONFIGURATION );
			timeRangeFilter = this.m_timeRangeFilter = TimeRangeFilter.read( fInputTimeRangeFilterFile );
		}
		catch( JAXBException e )
		{
			e.printStackTrace();
		}

		// -- Set UI values
		if( timeRangeFilter != null )
		{
			this.txtCropMinutesFromTheLeft.setText( this.m_timeRangeFilter.getCropMinutesFromTheLeft() + "" );
			this.txtCropMinutesFromTheRight.setText( this.m_timeRangeFilter.getCropMinutesFromTheRight() + "" );
		}
	}

	/**
	 * Helper method for formatting a date.
	 * 
	 * @param lValue
	 *            Long value that represents the date in ms.
	 * @return String that represents the date.
	 */

	private String toDateString( Long lValue )
	{
		return lValue != null ? DATE_FORMAT.format( new Date( lValue.longValue() ) ) : "-";
	}

	/**
	 * Handles everything that is related to a single slider.
	 * 
	 * @param changeEvent
	 *            See {@link javax.swing.event.ChangeEvent}
	 * @param textField
	 *            Text field to display slider information.
	 * @return Value of the slider or 0 if the slider is still adjusting.
	 */

	private int handleSlider( ChangeEvent changeEvent, JTextField textField )
	{
		JSlider slider = (JSlider)changeEvent.getSource();
		if( !slider.getValueIsAdjusting() )
		{
			int nValue = slider.getValue();
			textField.setText( NumberUtils.toPercent( nValue, this.m_nMaxSliderValue ) + " " + nValue + " min" );

			return nValue;
		}

		return 0;
	}

	/**
	 * Handles everything that is related to both sliders.
	 */

	private void handleSliderValues()
	{
		if( this.m_bInitialized )
		{
			this.txtCropMinutesFromTheLeft.setText( this.sliRemoveFromTheLeft.getValue() + "" );
			this.txtCropMinutesFromTheRight.setText( this.sliRemoveFromTheRight.getValue() + "" );
		}
	}

	@Override
	protected void btnAnalyzeActionPerformed( ActionEvent actionEvent )
	{
		// -- Disable UI
		enableComponents( false );

		// -- Create new state filter configuration
		JobManager.schedule( new RetrieveTimesFromSessionJob(), this.m_project, 0, this );
	}

	@Override
	protected void btnSaveActionPerformed( ActionEvent evt )
	{
		// -- Validate
		if( this.m_nLeftSliderValue + this.m_nRightSliderValue > this.m_nMaxSliderValue )
		{
			JOptionPane.showMessageDialog( this, "Illegal range", "Error", JOptionPane.ERROR_MESSAGE );
			return;
		}

		// -- Store time range filter
		int nCropMinutesFromTheLeft = NumberUtils.stringToInt( this.txtCropMinutesFromTheLeft.getText(), 0 );
		int nCropMinutesFromTheRight = NumberUtils.stringToInt( this.txtCropMinutesFromTheRight.getText(), 0 );

		this.m_timeRangeFilter = new TimeRangeFilter( nCropMinutesFromTheLeft, nCropMinutesFromTheRight );

		File fOutputTimeRangeFilterFile = this.m_project.getFile( Project.TAG_FILE_TIME_RANGE_FILTER_CONFIGURATION );

		try
		{
			this.m_timeRangeFilter.write( fOutputTimeRangeFilterFile );
		}
		catch( JAXBException e )
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void btnSaveAndCloseActionPerformed( ActionEvent evt )
	{
		btnSaveActionPerformed( null );
		setVisible( false );
	}

	/**
	 * Triggered when the user uses the right slider.
	 */

	@Override
	protected void sliRemoveFromTheRightStateChanged( ChangeEvent changeEvent )
	{
		this.m_nRightSliderValue = handleSlider( changeEvent, this.txtRemoveFromTheRight );
		handleSliderValues();
	}

	/**
	 * Triggered when the user uses the left slider.
	 */

	@Override
	protected void sliRemoveFromTheLeftStateChanged( ChangeEvent changeEvent )
	{
		this.m_nLeftSliderValue = handleSlider( changeEvent, this.txtRemoveFromTheLeft );
		handleSliderValues();
	}

	/**
	 * Triggered on job completion.
	 */

	@Override
	public void jobNotify( int nID, int nReturnType, Object objValue )
	{
		if( nReturnType == JobManager.OK )
		{
			RetrieveTimesFromSessionJob job = (RetrieveTimesFromSessionJob)objValue;

			// -- Set values
			long lFirstSessionTime = job.getFirstSessionStartTime();
			long lLastSessionTime = job.getLastSessionStopTime();
			int nDataDurationInMinutes = (int)toMinutes( job.getLastSessionStopTime() - job.getFirstSessionStartTime() );

			// -- Sliders
			this.m_nMaxSliderValue = (int)nDataDurationInMinutes;
			initSliders( this.m_nMaxSliderValue );

			// -- Update UI
			this.txtTimestampFrom.setText( toDateString( lFirstSessionTime ) );
			this.txtTimestampTo.setText( toDateString( lLastSessionTime ) );
			this.txtTimestampCount.setText( nDataDurationInMinutes + " min" );

			// -- Set initialized flag
			this.m_bInitialized = true;
		}
		else
		{
			Exception e = (Exception)objValue;
			e.printStackTrace();
		}

		// -- Enable UI
		enableComponents( true );
	}

	/**
	 * Enables/Disables the UI.
	 * 
	 * @param bEnable
	 *            Enable/Disable switch.
	 */

	private void enableComponents( boolean bEnable )
	{
		UIUtils.enableContainer( this, bEnable );
	}

	/**
	 * Initializes the sliders.
	 * 
	 * @param nMaxSliderValue
	 *            Max value for sliders.
	 */

	private void initSliders( int nMaxSliderValue )
	{
		this.sliRemoveFromTheLeft.setMinimum( 0 );
		this.sliRemoveFromTheLeft.setMaximum( nMaxSliderValue );
		this.sliRemoveFromTheLeft.setValue( 0 );

		this.sliRemoveFromTheRight.setMinimum( 0 );
		this.sliRemoveFromTheRight.setMaximum( nMaxSliderValue );
		this.sliRemoveFromTheRight.setValue( 0 );
	}

	/**
	 * Converts and rounds milliseconds to seconds.
	 * 
	 * @param lValue
	 *            Milliseconds to convert and round.
	 * @return Representation of the milliseconds as seconds.
	 */

	private static long toMinutes( long lValue )
	{
		return (int)( ( lValue / ( 1000 * 60 ) ) % 60 );
	}
}