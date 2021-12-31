package org.coldis.library.serialization.positional;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;
import org.coldis.library.exception.IntegrationException;
import org.coldis.library.model.SimpleMessage;

/**
 * Number (de)serializer.
 */
public class NumberSerializer implements PositionalSerializerInterface<Number>, PostionalDeserializerInterface<Number> {

	/**
	 * Format to be used during (de)serialization.
	 */
	private NumberFormat format = NumberFormat.getInstance();

	/**
	 * No arguments constructor.
	 */
	public NumberSerializer() {
	}

	/**
	 * Format constructor.
	 *
	 * @param format       Format to be used during (de)serialization.
	 * @param locale       Locale to be used during (de)serialization.
	 * @param asBigDecimal If the object should be (de)serialized as a big decimal.
	 * @param multiplier   Multiplier (if any) to be used during (de)serialization.
	 */
	public NumberSerializer(final String format, final Locale locale, final Boolean asBigDecimal,
			final Integer multiplier) {
		// Sets the locale, format, multiplier and if the object should be
		// converted as a big decimal.
		this.format = new DecimalFormat(format);
		((DecimalFormat) this.format).setDecimalFormatSymbols(new DecimalFormatSymbols(locale));
		((DecimalFormat) this.format).setParseBigDecimal(asBigDecimal);
		((DecimalFormat) this.format).setMultiplier(multiplier);
	}

	/**
	 * Format constructor (from single string parameter).
	 *
	 * @param formatLocaleAsBigDecimalAndMultiplier Format pattern to be used and if
	 *                                              the number should be converted
	 *                                              as a big decimal (split by '/').
	 */
	public NumberSerializer(final String formatLocaleAsBigDecimalAndMultiplier) {
		// Tries to set the format.
		try {
			// Splits the arguments.
			final String[] params = formatLocaleAsBigDecimalAndMultiplier.split("/");
			// If there is, at least, one argument.
			if ((params.length > 0) && (params[0] != null) && !params[0].isEmpty()) {
				// Sets the format pattern.
				format = new DecimalFormat(params[0]);
			}
			// If there is, at least, two arguments.
			if ((params.length > 1) && (params[1] != null) && !params[1].isEmpty()) {
				// Sets the format locale.
				((DecimalFormat) format)
						.setDecimalFormatSymbols(new DecimalFormatSymbols(LocaleUtils.toLocale(params[1])));
			}
			// If there is, at least, three arguments.
			if ((params.length > 2) && (params[2] != null) && !params[2].isEmpty()) {
				// Sets if the number should be converted as a big decimal.
				((DecimalFormat) format).setParseBigDecimal(Boolean.parseBoolean(params[2]));
			}
			// If there is, at least, four arguments.
			if ((params.length > 3) && (params[3] != null) && !params[3].isEmpty()) {
				// Sets the format multiplier.
				((DecimalFormat) format).setMultiplier(Integer.parseInt(params[3]));
			}
		}
		// If the format parameters are not valid.
		catch (final Exception exception) {
			// Throws an exception.
			throw new IntegrationException(new SimpleMessage("serialization.config.invalid"), exception);
		}

	}

	/**
	 * @see org.coldis.library.serialization.positional.PostionalDeserializerInterface#deserialize(java.lang.String)
	 */
	@Override
	public Number deserialize(final String serializedObject) {
		// Tries to de-serialize the number.
		try {
			return format.parse(serializedObject);
		}
		// If there is a problem during de-serialization.
		catch (final Exception exception) {
			// Throws an exception.
			throw new IntegrationException(new SimpleMessage("deserialization.error"), exception);
		}

	}

	/**
	 * @see org.coldis.library.serialization.positional.PositionalSerializerInterface#serialize(java.lang.Object)
	 */
	@Override
	public String serialize(final Number object) {
		return object == null ? "" : format.format(object);
	}

}
