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

package org.fortiss.pmwt.wex.ui.io.generic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.jexl2.Script;
import org.fortiss.pmwt.wex.ui.io.generic.exception.GenericReaderException;
import org.fortiss.pmwt.wex.ui.io.generic.exception.MissingFieldException;
import org.fortiss.pmwt.wex.ui.io.generic.exception.PreConditionFailedException;
import org.fortiss.pmwt.wex.ui.io.generic.model.CustomLogEntry;
import org.fortiss.pmwt.wex.ui.utils.NumberUtils;
import org.fortiss.pmwt.wex.ui.utils.StringUtils;

/**
 * Reader for custom CSV content.
 */

public class GenericReader implements AutoCloseable {
	/**
	 * FileReader instance that refers to the input file.
	 */

	private FileReader m_frInput = null;

	/**
	 * BufferedReader instance that refers to the input data.
	 */

	private BufferedReader m_brInput = null;

	/**
	 * CSV separator.
	 */

	private String m_strSeparator = null;

	/**
	 * Convenient access to the fields stored in the generic reader
	 * configuration.
	 */

	private GenericReaderConfiguration.CField[] m_arrField = null;

	/**
	 * Pre-condition script for filtering.
	 */

	private String m_strPreConditionScript = null;

	/**
	 * Flag to indicate if the pre-condition script will be used.
	 */

	private boolean m_bUsePreConditionScript = false;

	/**
	 * JEXL initialization.
	 */

	private static final JexlEngine JEXL_ENGINE = new JexlEngine();

	static {
		JEXL_ENGINE.setCache(512);
		JEXL_ENGINE.setLenient(false);
		JEXL_ENGINE.setSilent(false);
	}

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            Generic reader configuration.
	 */

	public GenericReader(GenericReaderConfiguration configuration) {
		this.m_arrField = configuration.getFieldArray();

		this.m_strPreConditionScript = configuration.getPreConditionScript();
		this.m_bUsePreConditionScript = StringUtils
				.isNotEmpty(this.m_strPreConditionScript);
	}

	/**
	 * Opens the input CSV file.
	 * 
	 * @param fInputCSVFile
	 *            CSV file to open.
	 * @param strSeparator
	 *            Separator used in the CSV file.
	 * @throws GenericReaderException
	 *             Container exception to collect several exceptions.
	 */

	public void open(File fInputCSVFile, String strSeparator)
			throws GenericReaderException {
		try {
			this.m_strSeparator = strSeparator;

			this.m_frInput = new FileReader(fInputCSVFile);
			this.m_brInput = new BufferedReader(this.m_frInput);
		} catch (IOException ioe) {
			throw new GenericReaderException(ioe);
		}
	}

	/**
	 * Reads a line in the CSV file.
	 * 
	 * @return Instance of
	 *         {@link org.fortiss.pmwt.wex.ui.io.generic.model.CustomLogEntry}.
	 * @throws GenericReaderException
	 *             Container exception to collect several exceptions.
	 */

	public CustomLogEntry nextEntry() throws GenericReaderException {
		try {
			String strLine = this.m_brInput.readLine();
			if (strLine == null) {
				return null;
			}

			String[] strValueArray = strLine.split(this.m_strSeparator);
			if (strValueArray.length == 0) {
				return null;
			}

			// --
			// -- Step #0: Pre-Condition
			// --

			if (this.m_bUsePreConditionScript) {
				Script script = JEXL_ENGINE
						.createScript(this.m_strPreConditionScript);

				JexlContext context = createSimpleJexlContext(strValueArray);
				Object objResult = script.execute(context);

				if (!(objResult instanceof Boolean)
						|| !((Boolean) objResult).booleanValue()) {
					throw new PreConditionFailedException(strLine);
				}
			}

			// --
			// -- Step #1: Conversion
			// --

			CustomLogEntry model = new CustomLogEntry();

			// -- Iterate through fields
			for (GenericReaderConfiguration.CField field : this.m_arrField) {
				// -- Set values in the script (if any)
				JexlContext jexlContext = new MapContext();
				Integer[] iIndexArray = field.getIndexArray();

				if (iIndexArray != null && iIndexArray.length > 0) {
					for (Integer iIndex : iIndexArray) {
						int n = iIndex.intValue();
						if (n > strValueArray.length) {
							throw new IndexOutOfBoundsException("valueCount="
									+ strValueArray.length
									+ "; requestedIndex=" + n + "; line="
									+ strLine);
						}

						jexlContext.set("$" + n + "$", strValueArray[n]);
					}
				}

				// -- Evaluate script
				Script script = JEXL_ENGINE.createScript(field.getScript());

				Object objResult = script.execute(jexlContext);
				String strRetVal = objResult != null ? objResult.toString()
						: null;

				// -- Convert value
				Object objValue = null;
				if (strRetVal != null) {
					objValue = convert(field, strRetVal);
				}

				// -- Mandatory check
				if (objValue == null && field.isMandatory()) {
					throw new MissingFieldException("Mandatory attribute '"
							+ field.getName() + "' missing in '" + strLine
							+ "'");
				}

				callMethod(model, field, objValue);
			}

			return model;
		} catch (IndexOutOfBoundsException ioobe) {
			throw new GenericReaderException(ioobe);
		} catch (GenericReaderException gre) {
			throw gre;
		} catch (Exception e) {
			throw new GenericReaderException(e);
		}
	}

	/**
	 * Used to preview an script against an input value array.
	 * 
	 * @param strScript
	 *            Script to be evaluated.
	 * @param arrValue
	 *            Input value array that refers to variables in the script
	 *            defined by $[index]$.
	 * @return Result of the evaluated script.
	 */

	public static Object preview(String strScript, String[] arrValue) {
		// -- Create context map
		JexlContext jexlContext = new MapContext();

		Integer[] iIndexArray = getIndexArray(strScript);
		if (iIndexArray != null && iIndexArray.length > 0) {
			for (Integer iIndex : iIndexArray) {
				int n = iIndex.intValue();
				if (n > arrValue.length) {
					return "<error: index out of bounds: " + n + ">";
				}

				jexlContext.set("$" + n + "$", arrValue[n]);
			}
		}

		// -- Evaluate
		try {
			Script script = JEXL_ENGINE.createScript(strScript);
			Object objResult = script.execute(jexlContext);

			return objResult != null ? objResult : "null";
		} catch (Exception e) {
			return "<error: " + e.getMessage() + ">";
		}
	}

	/**
	 * Closes all open handles to the session input file.
	 */

	@Override
	public void close() {
		try {
			this.m_brInput.close();
			this.m_frInput.close();

			this.m_brInput = null;
			this.m_frInput = null;
		} catch (IOException ioe) {
		}
	}

	/**
	 * Parses all variables of form $[index]$ from the given string and return
	 * all found indexes as an integer array.
	 * 
	 * @param strValue
	 *            Value to be parsed for variables of form $[index]$
	 * @return Integer array that contains all found variable indexes.
	 */

	public static Integer[] getIndexArray(String strValue) {
		if (strValue == null) {
			return null;
		}

		List<Integer> lstIndex = new ArrayList<Integer>();

		int nIndex = -1;
		int nLastIndex = -1;
		while ((nIndex = strValue.indexOf('$', nLastIndex)) > -1) {
			nIndex++;

			nLastIndex = strValue.indexOf('$', nIndex);
			Integer iIndex = new Integer(strValue.substring(nIndex, nLastIndex));

			lstIndex.add(iIndex);

			nLastIndex++;
		}

		return lstIndex.toArray(new Integer[0]);
	}

	/**
	 * Calls the desired setter method on the target model.
	 * 
	 * @param object
	 *            Instance of the target model.
	 * @param field
	 *            The field is used to determine the setter method name and the
	 *            data type of the value to set.
	 * @param objValue
	 *            Value to set.
	 */

	private static void callMethod(Object object,
			GenericReaderConfiguration.CField field, Object objValue) {
		try {
			Method method = object.getClass().getMethod(field.getSetterName(),
					field.getClazzType());
			method.invoke(object, objValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Takes a parsed string value and converts this string according to the
	 * given data type in the field instance.
	 * 
	 * @param field
	 *            The field is used to determine the desired data type.
	 * @param strValue
	 *            Value to be converted.
	 * @return Instance of determined data type that represents the content of
	 *         the string value.
	 */

	private static Object convert(GenericReaderConfiguration.CField field,
			String strValue) {
		Object objRetVal = null;

		if (String.class.equals(field.getClazzType())) {
			objRetVal = strValue;
		} else if (Integer.class.equals(field.getClazzType())) {
			objRetVal = NumberUtils.toInteger(strValue, null);
		} else if (Long.class.equals(field.getClazzType())) {
			objRetVal = NumberUtils.toLong(strValue, null);
		} else {
			return null;
		}

		return objRetVal;
	}

	/**
	 * Creates a JEXL context based on the given array.
	 * 
	 * @param strValueArray
	 *            String array.
	 * @return JexlContext instance.
	 */

	private JexlContext createSimpleJexlContext(String[] strValueArray) {
		JexlContext jexlContext = new MapContext();
		for (int i = 0; i < strValueArray.length; i++) {
			String strValue = strValueArray[i];
			jexlContext.set("$" + i + "$", strValue);
		}

		return jexlContext;
	}
}
