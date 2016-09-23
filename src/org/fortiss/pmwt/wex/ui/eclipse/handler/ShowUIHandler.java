package org.fortiss.pmwt.wex.ui.eclipse.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.fortiss.pmwt.wex.ui.MainUI;

/**
 * Handler for button in eclipse toolbar.
 */

public class ShowUIHandler extends AbstractHandler
{
	/**
	 * @see AbstractHandler#execute(ExecutionEvent)
	 */

	@Override
	public Object execute( ExecutionEvent event ) throws ExecutionException
	{
		MainUI.main( null );
		return null;
	}
}