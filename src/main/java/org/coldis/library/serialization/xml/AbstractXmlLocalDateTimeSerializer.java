package org.coldis.library.serialization.xml;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * XML local date/time serializer.
 */
public abstract class AbstractXmlLocalDateTimeSerializer extends XmlAdapter<String, LocalDateTime> {

	/**
	 * Gets the date/time format to be used by the serializer.
	 *
	 * @return The date/time format to be used by the serializer.
	 */
	protected abstract DateTimeFormatter getDateFormatter();

	/**
	 * @see jakarta.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(final LocalDateTime date) throws Exception {
		return getDateFormatter().format(date);
	}

	/**
	 * @see jakarta.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public LocalDateTime unmarshal(final String date) throws Exception {
		return getDateFormatter().parse(date, LocalDateTime::from);
	}

}
