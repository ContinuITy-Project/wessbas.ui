package org.fortiss.pmwt.wex.ui.jobs;

/**
 * Defines the signature for the notification of job success/failure.
 */

public interface IJobNotify
{
	public void jobNotify( int nID, int nReturnType, Object objValue );
}
