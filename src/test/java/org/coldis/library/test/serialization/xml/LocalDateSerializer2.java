package org.coldis.library.test.serialization.xml;

import java.time.format.DateTimeFormatter;

import org.coldis.library.serialization.xml.AbstractXmlLocalDateTimeSerializer;

/**
 * Test date serializer.
 */
public class LocalDateSerializer2 extends AbstractXmlLocalDateTimeSerializer {

	/**
	 * Test format.
	 */
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss");

	/**
	 * @see org.coldis.library.serialization.xml.AbstractXmlLocalDateTimeSerializer#getDateFormatter()
	 */
	@Override
	protected DateTimeFormatter getDateFormatter() {
		return DATE_FORMAT;
	}

}
