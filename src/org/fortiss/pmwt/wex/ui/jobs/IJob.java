package org.fortiss.pmwt.wex.ui.jobs;

/**
 * Defines the default signature for jobs.
 * 
 * @param <T>
 *            Type of the job arguments.
 */

public interface IJob< T >
{
	public void run( T t ) throws Exception;
}
