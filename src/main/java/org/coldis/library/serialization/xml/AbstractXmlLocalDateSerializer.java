package org.coldis.library.serialization.xml;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * XML local date serializer.
 */
public abstract class AbstractXmlLocalDateSerializer extends XmlAdapter<String, LocalDate> {

	/**
	 * Gets the date/time format to be used by the serializer.
	 *
	 * @return The date/time format to be used by the serializer.
	 */
	protected abstract DateTimeFormatter getDateFormatter();

	/**
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(final LocalDate date) throws Exception {
		return getDateFormatter().format(date);
	}

	/**
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public LocalDate unmarshal(final String date) throws Exception {
		return getDateFormatter().parse(date, LocalDate::from);
	}

}
