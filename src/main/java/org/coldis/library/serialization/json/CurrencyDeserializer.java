package org.coldis.library.serialization.json;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Currency deserializer.
 */
public class CurrencyDeserializer extends NumberDeserializer {

	/**
	 * @see org.coldis.library.serialization.json.NumberDeserializer#getNumberFormat(java.util.Locale)
	 */
	@Override
	protected NumberFormat getNumberFormat(final Locale locale) {
		return CurrencySerializer.getNumberFormat(locale, true, false);
	}

}
