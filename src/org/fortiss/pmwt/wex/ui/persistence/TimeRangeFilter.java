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

package org.fortiss.pmwt.wex.ui.persistence;

import java.io.File;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.fortiss.pmwt.wex.ui.utils.SerializationUtils;

/**
 * Used to filter instances of
 * {@link org.fortiss.pmwt.wex.ui.io.session.model.Session} by timestamps.
 */

@XmlRootElement(name = "timeRangeFilter")
@XmlAccessorType(XmlAccessType.NONE)
public class TimeRangeFilter {
	/**
	 * Remove minutes from the left.
	 */

	@XmlAttribute(name = "cropMinutesFromTheLeft")
	private int m_nCropMinutesFromTheLeft = 0;

	/**
	 * Remove minutes from the right.
	 */

	@XmlAttribute(name = "cropMinutesFromTheRight")
	private int m_nCropMinutesFromTheRight = 0;

	/**
	 * Constructor.
	 */

	public TimeRangeFilter() {
	}

	/**
	 * Constructor.
	 * 
	 * @param nCropMinutesFromTheLeft
	 *            Remove minutes from the left.
	 * @param nCropMinutesFromTheRight
	 *            Remove minutes from the right.
	 */

	public TimeRangeFilter(int nCropMinutesFromTheLeft,
			int nCropMinutesFromTheRight) {
		this.m_nCropMinutesFromTheLeft = nCropMinutesFromTheLeft;
		this.m_nCropMinutesFromTheRight = nCropMinutesFromTheRight;
	}

	/**
	 * Sets the minutes to be removed from the left side.
	 */

	public void setCropMinutesFromTheLeft(int nCropMinutesFromTheLeft) {
		this.m_nCropMinutesFromTheLeft = nCropMinutesFromTheLeft;
	}

	/**
	 * @return Minutes to be removed from the left side.
	 */

	public int getCropMinutesFromTheLeft() {
		return this.m_nCropMinutesFromTheLeft;
	}

	/**
	 * Sets the minutes to be removed from the right side.
	 */

	public void setCropMinutesFromTheRight(int nCropMinutesFromTheRight) {
		this.m_nCropMinutesFromTheRight = nCropMinutesFromTheRight;
	}

	/**
	 * @return Minutes to be removed from the right side.
	 */

	public int getCropMinutesFromTheRight() {
		return this.m_nCropMinutesFromTheRight;
	}

	/**
	 * Serializes the time range filter to a file.
	 * 
	 * @param fOutputFile
	 *            Output file to be written.
	 * @throws JAXBException
	 *             Occurs, if something unexpected happens.
	 */

	public void write(File fOutputFile) throws JAXBException {
		SerializationUtils.serializeToXML(fOutputFile, this);
	}

	/**
	 * Deserializes a time range filter object from a file.
	 * 
	 * @param fInputFile
	 *            Input file to be read.
	 * @return Instance of a time range filter.
	 * @throws JAXBException
	 *             Occurs, if something unexptected happens.
	 */

	public static TimeRangeFilter read(File fInputFile) throws JAXBException {
		return SerializationUtils.deserializeFromXML(fInputFile,
				TimeRangeFilter.class);
	}
}