package org.coldis.library.serialization.json;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Currency serializer.
 */
public class CurrencySerializer extends NumberSerializer {

	/**
	 * Gets the number format for the given locale.
	 *
	 * @param  locale          Locale to be used.
	 * @param  parseBigDecimal If big decimals should also be parsed
	 * @param  groupingUsed    If grouping should be used.
	 * @return                 The number format.
	 */
	public static DecimalFormat getNumberFormat(final Locale locale, final Boolean parseBigDecimal,
			final Boolean groupingUsed) {
		// Creates and returns the number format.
		final DecimalFormat numberFormat = NumberSerializer.getNumberFormat(locale, parseBigDecimal, groupingUsed);
		numberFormat.setMaximumFractionDigits(2);
		return numberFormat;
	}

	/**
	 * @see org.coldis.library.serialization.json.NumberSerializer#getNumberFormat(java.util.Locale)
	 */
	@Override
	protected NumberFormat getNumberFormat(final Locale locale) {
		return CurrencySerializer.getNumberFormat(locale, true, false);
	}

}
