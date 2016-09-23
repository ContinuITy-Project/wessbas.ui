package org.fortiss.pmwt.wex.ui.utils;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

/**
 * Collection of methods to help to choose files.
 */

public class UIFileChooser
{
	/**
	 * Last selected path.
	 */

	private static File	s_fLastPath	= null;

	/**
	 * Dialog to open multiple files.
	 * 
	 * @param parentFrame
	 *            Parent frame to be disabled while showing the dialog.
	 * @return Array of selected files.
	 */

	public static File[] showMultipleFileOpenDialog( Component parentFrame )
	{
		JFileChooser fileChooser = new JFileChooser( s_fLastPath );
		fileChooser.setMultiSelectionEnabled( true );

		File[] arrFile = null;
		if( fileChooser.showOpenDialog( parentFrame ) == JFileChooser.APPROVE_OPTION )
		{
			arrFile = fileChooser.getSelectedFiles();
			if( arrFile != null )
			{
				s_fLastPath = arrFile[ 0 ];
			}
		}

		return arrFile;
	}

	/**
	 * Select-file-dialog.
	 * 
	 * @param parentFrame
	 *            Parent frame to be disabled while showing the dialog.
	 * @return The selected file.
	 */

	public static File showFileOpenDialog( Component parentFrame )
	{
		return showOpenDialog( parentFrame, false );
	}

	/**
	 * Select-directory-dialog.
	 * 
	 * @param parentFrame
	 *            Parent frame to be disabled while showing the dialog.
	 * @return The selected directory.
	 */

	public static File showDirectoryOpenDialog( Component parentFrame )
	{
		return showOpenDialog( parentFrame, true );
	}

	/**
	 * Sets the last chosen path.
	 * 
	 * @param fLastPath
	 *            Path to be set.
	 */

	public static void setLastPath( File fLastPath )
	{
		s_fLastPath = fLastPath;
	}

	/**
	 * Dialog for the selection of files or directories.
	 * 
	 * @param parentFrame
	 *            Parent frame to be disabled while showing the dialog.
	 * @param bSelectDirectoryInsteadOfFile
	 *            True indicates the selection of directories, false indicates the selection of files.
	 * @return Selected files or directories.
	 */

	public static File showOpenDialog( Component parentFrame, boolean bSelectDirectoryInsteadOfFile )
	{
		JFileChooser fileChooser = new JFileChooser( s_fLastPath );

		if( bSelectDirectoryInsteadOfFile )
		{
			fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
			fileChooser.setAcceptAllFileFilterUsed( false );
		}

		File file = null;
		if( fileChooser.showOpenDialog( parentFrame ) == JFileChooser.APPROVE_OPTION )
		{
			file = fileChooser.getSelectedFile();
			if( file != null )
			{
				s_fLastPath = file;
			}
		}

		return file;
	}

	/**
	 * Save-to-file-dialog.
	 * 
	 * @param parentFrame
	 *            Parent frame to be disabled while showing the dialog.
	 * @return The chosen target file.
	 */

	public static File showSaveDialog( Component parentFrame )
	{
		JFileChooser fileChooser = new JFileChooser( s_fLastPath );

		File file = null;
		if( fileChooser.showSaveDialog( parentFrame ) == JFileChooser.APPROVE_OPTION )
		{
			file = fileChooser.getSelectedFile();
			if( file != null )
			{
				s_fLastPath = file;
			}
		}
		return file;
	}

	/**
	 * Helper method to choose a file and output the path to a textfield.
	 * 
	 * @param textField
	 *            Textfield the path is set to.
	 * @param parent
	 *            Parent container to be disabled while showing the dialog.
	 * @return The chosen file.
	 */

	public static File selectFilePathHelper( JTextField textField, Component parent )
	{
		File file = showFileOpenDialog( parent );
		if( file != null && file.isFile() )
		{
			String strPath = FileUtils.toSlashPath( file.getPath() );
			textField.setText( strPath );
		}

		return file;
	}

	/**
	 * Helper method to choose a file and output its name to a textfield.
	 * 
	 * @param textField
	 *            Textfield the name is set to.
	 * @param parent
	 *            Parent container to be disabled while showing the dialog.
	 * @return The chosen file name.
	 */

	public static File selectFileNameHelper( JTextField textField, Component parent )
	{
		File file = showFileOpenDialog( parent );
		if( file != null && file.isFile() )
		{
			textField.setText( file.getName() );
		}

		return file;
	}

	/**
	 * Helper method to choose a directory and output its path to textfield.
	 * 
	 * @param textField
	 *            Textfield the path is set to.
	 * @param parent
	 *            Parent container to be disabled while showing the dialog.
	 * @return The chosen directory path.
	 */

	public static File selectDirectoryPathHelper( JTextField textField, Component parent )
	{
		File file = showDirectoryOpenDialog( parent );
		if( file != null && file.isDirectory() )
		{
			String strPath = FileUtils.toSlashPath( file.getPath() );
			textField.setText( strPath );
		}

		return file;
	}
}
