package org.coldis.library.test.serialization.xml;

import java.time.format.DateTimeFormatter;

import org.coldis.library.serialization.xml.AbstractXmlLocalDateTimeSerializer;

/**
 * Test date/time serializer.
 */
public class LocalDateTimeSerializer2 extends AbstractXmlLocalDateTimeSerializer {

	/**
	 * Test format.
	 */
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("ddMMyyyy-HH:mm:ss");

	/**
	 * @see org.coldis.library.serialization.xml.AbstractXmlLocalDateTimeSerializer#getDateFormatter()
	 */
	@Override
	protected DateTimeFormatter getDateFormatter() {
		return LocalDateTimeSerializer2.DATE_FORMAT;
	}

}
