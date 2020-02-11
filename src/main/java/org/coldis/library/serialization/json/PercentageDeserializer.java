package org.coldis.library.serialization.json;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Percentage deserializer.
 */
public class PercentageDeserializer extends NumberDeserializer {

	/**
	 * @see org.coldis.library.serialization.json.NumberDeserializer#getNumberFormat(java.util.Locale)
	 */
	@Override
	protected NumberFormat getNumberFormat(final Locale locale) {
		return PercentageSerializer.getNumberFormat(locale, true, false);
	}

}
