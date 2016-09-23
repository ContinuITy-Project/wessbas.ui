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
import java.io.IOException;
import java.util.Properties;

import org.fortiss.pmwt.wex.ui.persistence.options.ETransformation;
import org.fortiss.pmwt.wex.ui.utils.FileUtils;

/**
 * Property file for transformation.
 */

public class TransformationProperties extends AbstractProperties {
	/**
	 * Serialization.
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */

	public TransformationProperties() {
		for (ETransformation etp : ETransformation.values()) {
			put(etp.getKey(), etp.getDefaultValue().toString());
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param properties
	 *            Untyped properties to read from.
	 */

	private TransformationProperties(Properties properties) {
		copy(properties, this);
	}

	/**
	 * Read property file of certain type.
	 * 
	 * @param fInputFile
	 *            Property file to be read.
	 * @return A transformation property file instance.
	 * @throws IOException
	 *             Occurs, when something unexpected happens.
	 */

	public static TransformationProperties read(File fInputFile)
			throws IOException {
		return new TransformationProperties(
				FileUtils.restoreProperties(fInputFile));
	}
}
