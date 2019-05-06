package org.coldis.library.test.serialization.xml;

import java.time.format.DateTimeFormatter;

import org.coldis.library.serialization.xml.AbstractXmlLocalDateSerializer;

/**
 * Test date serializer.
 */
public class LocalDateSerializer1 extends AbstractXmlLocalDateSerializer {

	/**
	 * Test format.
	 */
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

	/**
	 * @see org.coldis.library.serialization.xml.AbstractXmlLocalDateTimeSerializer#getDateFormatter()
	 */
	@Override
	protected DateTimeFormatter getDateFormatter() {
		return LocalDateSerializer1.DATE_FORMAT;
	}

}
