package org.coldis.library.serialization.xml;

import java.text.NumberFormat;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * XML number serializer.
 */
public abstract class AbstractXmlNumberSerializer extends XmlAdapter<String, Number> {

	/**
	 * Gets the number format to be used by the serializer.
	 *
	 * @return The number format to be used by the serializer.
	 */
	protected abstract NumberFormat getNumberFormat();

	/**
	 * @see jakarta.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(final Number number) throws Exception {
		return getNumberFormat().format(number);
	}

	/**
	 * @see jakarta.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public Number unmarshal(final String number) throws Exception {
		return getNumberFormat().parse(number);
	}

}
