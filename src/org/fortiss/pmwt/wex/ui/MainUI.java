package org.fortiss.pmwt.wex.ui;

import java.awt.EventQueue;
import java.io.File;

import org.fortiss.pmwt.wex.ui.implementation.MainFrame;
import org.fortiss.pmwt.wex.ui.utils.PCMUtils;

/**
 * Main class for WESSBAS UI.
 */

public class MainUI implements Runnable
{
	/**
	 * Path to project file.
	 */

	private File	m_fProjectFile	= null;

	/**
	 * Constructor.
	 * 
	 * @param fProjectFile
	 *            Path to project file.
	 */

	public MainUI( File fProjectFile )
	{
		this.m_fProjectFile = fProjectFile;
	}

	/**
	 * Main entry point for WESSBAS UI.
	 * 
	 * @param args
	 *            Command line arguments. None yet.
	 */

	public static void main( String args[] )
	{
		// -- Register factory for ECore
		PCMUtils.registerFactoryForECore();

		// --
		File fProjectFile = null;

		// Simple parameter handling -> <project file>
		if( args != null && args.length >= 1 )
		{
			String strValue = args[ 0 ];

			File file = new File( strValue );
			if( file.exists() && file.isFile() )
			{
				fProjectFile = file;
			}
		}

		// -- Launch UI
		MainUI main = new MainUI( fProjectFile );
		EventQueue.invokeLater( main );
	}

	/**
	 * UI thread for the WESSBAS UI.
	 */

	@Override
	public void run()
	{
		MainFrame mainFrame = new MainFrame();
		if( this.m_fProjectFile != null )
		{
			mainFrame.openProject( this.m_fProjectFile );
		}

		mainFrame.setVisible( true );
	}
}