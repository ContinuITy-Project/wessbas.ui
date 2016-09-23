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

package org.fortiss.pmwt.wex.ui.utils;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Utility functions related to (de)serializing instances of classes.
 */

public class SerializationUtils {
	/**
	 * Serializes an object to an XML file.
	 * 
	 * @param file
	 *            Target file.
	 * @param object
	 *            Object to be serialized.
	 * @throws JAXBException
	 *             Occurs when something unexpected happens.
	 */

	public static void serializeToXML(File file, Object object)
			throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(object.getClass());

		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		marshaller.marshal(object, file);
	}

	/**
	 * Deserializes an object from an XML file.
	 * 
	 * @param file
	 *            File to be read.
	 * @param clazz
	 *            Target class to be instanciated.
	 * @return Instance of the object contained in the file.
	 * @throws JAXBException
	 *             Occurs when something unexpected happens.
	 */

	@SuppressWarnings("unchecked")
	public static <T> T deserializeFromXML(File file, Class<T> clazz)
			throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clazz);

		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (T) unmarshaller.unmarshal(file);
	}
}
