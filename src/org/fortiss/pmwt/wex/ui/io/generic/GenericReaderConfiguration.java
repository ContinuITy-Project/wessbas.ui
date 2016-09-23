package org.fortiss.pmwt.wex.ui.io.generic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Used to configure an instance of
 * {@link org.fortiss.pmwt.wex.ui.io.generic.GenericReader}.
 */

@XmlRootElement(name = "genericReaderConfiguration")
@XmlAccessorType(XmlAccessType.NONE)
public class GenericReaderConfiguration implements Serializable {
	/**
	 * Serialization.
	 */

	@XmlTransient
	private static final long serialVersionUID = 1L;

	/**
	 * Map of format: <name of field,<field instance>>.
	 */

	@XmlElement(name = "fieldMap")
	private LinkedHashMap<String, CField> fieldMap = null;

	/**
	 * Delimiter for CSV data.
	 */

	@XmlAttribute(name = "delimiter")
	private String delimiter = ";";

	/**
	 * Script to pre-filter single sessions.
	 */

	@XmlAttribute(name = "preConditionScript")
	private String preConditionScript = null;

	/**
	 * Constructor.
	 */

	private GenericReaderConfiguration() {
	}

	/**
	 * @return A new configuration instance with default field values.
	 */

	public static GenericReaderConfiguration newDefaultInstance() {
		GenericReaderConfiguration configuration = new GenericReaderConfiguration();
		configuration.createDefaultFieldMap();

		return configuration;
	}

	/**
	 * Returns a new configuration instance with custom field values.
	 * 
	 * @param arrField
	 *            Array of field values.
	 * @return New configuration instance with custom field values.
	 */

	public static GenericReaderConfiguration newInstance(
			GenericReaderConfiguration.CField[] arrField) {
		GenericReaderConfiguration configuration = new GenericReaderConfiguration();
		configuration.createCustomFieldMap(arrField);

		return configuration;
	}

	/**
	 * Returns a field value by its name.
	 * 
	 * @param strName
	 *            Name of the field value.
	 * @return Instance of the field value or null if the name does not exist.
	 */

	public CField getByName(String strName) {
		return this.fieldMap.get(strName);
	}

	/**
	 * @return All field values as an array.
	 */

	public CField[] getFieldArray() {
		List<CField> lstField = new ArrayList<CField>();
		Iterator<CField> iterator = this.fieldMap.values().iterator();
		while (iterator.hasNext()) {
			lstField.add(iterator.next());
		}

		return lstField.toArray(new CField[0]);
	}

	/**
	 * @return Delimiter for CSV data.
	 */

	public String getDelimiter() {
		return this.delimiter;
	}

	/**
	 * Sets the delimiter for the CSV data.
	 * 
	 * @param strDelimiter
	 *            Delimiter for the CSV data.
	 */

	public void setDelimiter(String strDelimiter) {
		this.delimiter = strDelimiter;
	}

	/**
	 * Sets the pre-condition script.
	 * 
	 * @param strPreConditionScript
	 *            Pre-condition script.
	 */

	public void setPreConditionScript(String strPreConditionScript) {
		this.preConditionScript = strPreConditionScript;
	}

	/**
	 * @return Pre-condition script.
	 */

	public String getPreConditionScript() {
		return this.preConditionScript;
	}

	/**
	 * Debug only.
	 */

	@Override
	public String toString() {
		StringBuilder sbOut = new StringBuilder();

		Iterator<Map.Entry<String, CField>> iterator = this.fieldMap.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, CField> mapEntry = iterator.next();
			sbOut.append(mapEntry.getKey() + "="
					+ mapEntry.getValue().toString() + "\n");
		}

		sbOut.append("PreConditionScript: " + this.preConditionScript + "\n");

		return sbOut.toString();
	}

	/**
	 * Creates a default field map.
	 */

	private void createDefaultFieldMap() {
		this.fieldMap = new LinkedHashMap<String, GenericReaderConfiguration.CField>();

		addField(new CField("Session ID", "setSessionID", String.class, true));
		addField(new CField("Service Name", "setStateName", String.class, true));
		addField(new CField("Request Start Time", "setRequestStartTime",
				Long.class, true));
		addField(new CField("Request End Time", "setRequestEndTime",
				Long.class, true));
		addField(new CField("Request URL", "setRequestURL", String.class, false));
		addField(new CField("Host IP", "setHostIP", String.class, false));
		addField(new CField("Local IP-address", "setLocalIPAddress",
				String.class, false));
		addField(new CField("Local Port", "setLocalPort", Integer.class, false));
		addField(new CField("Method", "setMethod", String.class, false));
		addField(new CField("Encoding", "setEncoding", String.class, false));
		addField(new CField("Parameter Names", "setParameterNames",
				String.class, false));
		addField(new CField("Parameter Values", "setParameterValues",
				String.class, false));

	}

	/**
	 * Creates a custom field map based on the provided field array.
	 * 
	 * @param arrField
	 *            Array that holds custom field values.
	 */

	private void createCustomFieldMap(
			GenericReaderConfiguration.CField[] arrField) {
		this.fieldMap = new LinkedHashMap<String, GenericReaderConfiguration.CField>();

		for (GenericReaderConfiguration.CField field : arrField) {
			addField(field);
		}
	}

	/**
	 * Adds a field to the current field map.
	 * 
	 * @param field
	 *            Field value to be added.
	 */

	private void addField(CField field) {
		this.fieldMap.put(field.name, field);
	}

	/**
	 * Represents a single field value.
	 */

	@XmlRootElement(name = "field")
	@XmlAccessorType(XmlAccessType.NONE)
	public static final class CField implements Serializable {
		/**
		 * Serialization.
		 */

		@XmlTransient
		private static final long serialVersionUID = 1L;

		/**
		 * Human readable name of the field.
		 */

		@XmlAttribute
		private String name = null;

		/**
		 * Expression to be evaluated.
		 */

		@XmlAttribute
		private String script = null;

		/**
		 * Name of the setter method of the target model to be called.
		 */

		@XmlAttribute
		private String setterName = null;

		/**
		 * Convenient array that keeps track of all variables of type
		 * "$[index]$".
		 */

		@XmlAttribute
		private Integer[] indexArray = null;

		/**
		 * Data type of the value to be set in the setter method in the target
		 * model.
		 */

		@XmlAttribute
		private Class<?> clazzType = null;

		/**
		 * Determines if the current field expression must evaluate to != null.
		 */

		@XmlAttribute
		private boolean mandatory = false;

		/**
		 * Constructor.
		 * 
		 * @param name
		 *            Name of the field.
		 * @param setterName
		 *            Name of the setter method to be called on the target
		 *            model.
		 * @param clazzType
		 *            Data type of the value to be set in the setter method in
		 *            the target model.
		 * @param bMandatory
		 *            Determines if the current field expression must evaluate
		 *            to != null.
		 */

		public CField(String name, String setterName, Class<?> clazzType,
				boolean bMandatory) {
			this.name = name;
			this.setterName = setterName;
			this.clazzType = clazzType;
			this.mandatory = bMandatory;
		}

		/**
		 * Constructor.
		 */

		@SuppressWarnings("unused")
		private CField() {
			// -- XML Serialization
		}

		/**
		 * @return Name of the setter method in the target model.
		 */

		public String getSetterName() {
			return this.setterName;
		}

		/**
		 * Sets the script to be evaluated.
		 * 
		 * @param strScript
		 *            Script to be evaluated.
		 */

		public void setScript(String strScript) {
			this.script = strScript;

			// -- Index array
			this.indexArray = GenericReader.getIndexArray(strScript);
		}

		/**
		 * @return Expression to be evaluated.
		 */

		public String getScript() {
			return this.script != null ? script : "";
		}

		/**
		 * @return Name of the field.
		 */

		public String getName() {
			return this.name;
		}

		/**
		 * @return Array of all variable indexes found in the expression.
		 */

		public Integer[] getIndexArray() {
			return this.indexArray;
		}

		/**
		 * @return Determines if the expression must evaluate to != null.
		 */

		public boolean isMandatory() {
			return this.mandatory;
		}

		/**
		 * @return Data type of the value to be set in the setter method in the
		 *         target model.
		 */

		public Class<?> getClazzType() {
			return this.clazzType;
		}

		/**
		 * Debug only.
		 */

		@Override
		public String toString() {
			return this.name + " (" + this.script + ")";
		}
	}
}
