package org.fortiss.pmwt.wex.ui.utils;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Utility functions related to (de)serializing instances of classes.
 */

public class SerializationUtils
{
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

	public static void serializeToXML( File file, Object object ) throws JAXBException
	{
		JAXBContext context = JAXBContext.newInstance( object.getClass() );

		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );

		marshaller.marshal( object, file );
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

	@SuppressWarnings( "unchecked" )
	public static < T > T deserializeFromXML( File file, Class< T > clazz ) throws JAXBException
	{
		JAXBContext context = JAXBContext.newInstance( clazz );

		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (T)unmarshaller.unmarshal( file );
	}
}
