package org.fortiss.pmwt.wex.ui.utils;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Utility functions related to the UI.
 */

public class UIUtils
{
	/**
	 * Returns all componenents of a container.
	 * 
	 * @param container
	 *            Container to start.
	 * @param arrClazzToIgnore
	 *            Types of instances to ignore.
	 * @return An array of all instances of UI elements.
	 */

	@SuppressWarnings( "unchecked" )
	public static List< Component > getComponentsRecursive( Container container, Class< ? extends Component >... arrClazzToIgnore )
	{
		Component[] arrComponent = container.getComponents();
		List< Component > lstComponent = new ArrayList< Component >();
		for( Component component : arrComponent )
		{
			boolean bIgnore = false;
			for( Class< ? extends Component > clz : arrClazzToIgnore )
			{
				if( component.getClass().equals( clz ) )
				{
					bIgnore = true;
					break;
				}
			}

			if( bIgnore )
			{
				continue;
			}

			lstComponent.add( component );

			if( component instanceof Container )
			{
				lstComponent.addAll( getComponentsRecursive( (Container)component, arrClazzToIgnore ) );
			}
		}
		return lstComponent;
	}

	/**
	 * Enables/Disables a panel.
	 * 
	 * @param panel
	 *            Panel to enable/disable.
	 * @param bEnable
	 *            Enable/Disable switch.
	 */

	public static void enablePanel( JPanel panel, boolean bEnable )
	{
		enableContainer( panel, bEnable );
	}

	/**
	 * Enables/Disables a container and all its child elements.
	 * 
	 * @param parent
	 *            Container to enable/disable.
	 * @param bEnable
	 *            Enable/Disable switch.
	 */

	@SuppressWarnings( "unchecked" )
	public static void enableContainer( Container parent, boolean bEnable )
	{
		for( Component component : getComponentsRecursive( parent ) )
		{
			component.setEnabled( bEnable );
		}
	}

	/**
	 * Clears the content of a table.
	 * 
	 * @param table
	 *            Table to be cleared.
	 */

	public static void clearTable( JTable table )
	{
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		for( int i = model.getRowCount() - 1; i >= 0; i-- )
		{
			model.removeRow( i );
		}
	}
}
