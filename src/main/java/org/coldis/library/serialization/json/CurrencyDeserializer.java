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
		// Gets the default number format.
		final NumberFormat numberFormat = super.getNumberFormat(locale);
		// Sets the fraction digits.
		numberFormat.setMaximumFractionDigits(2);
		// Returns the number format.
		return numberFormat;
	}

}
