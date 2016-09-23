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

package org.fortiss.pmwt.wex.ui.jobs;

/**
 * Schedules jobs.
 * 
 * @param <T>
 *            Type of the job to be scheduled.
 */

public class JobManager<T> {
	/**
	 * Everything worked as expected.
	 */

	public static final int OK = 0;

	/**
	 * Error while running a certain job.
	 */

	public static final int ERROR = 1;

	/**
	 * Schedules a single job.
	 * 
	 * @param job
	 *            Job to be scheduled.
	 * @param tJobArguments
	 *            Arguments passed to the job.
	 * @param nID
	 *            Identifier of the job.
	 * @param notifyObject
	 *            Object to be notified on success/fail.
	 */

	public static <T> void schedule(final IJob<T> job, final T tJobArguments,
			final int nID, final IJobNotify notifyObject) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					job.run(tJobArguments);

					if (notifyObject != null) {
						notifyObject.jobNotify(nID, JobManager.OK, job);
					}
				} catch (Exception e) {
					if (notifyObject != null) {
						notifyObject.jobNotify(nID, JobManager.ERROR, e);
					}
				}
			}
		};

		Thread thread = new Thread(runnable);
		thread.start();
	}
}
