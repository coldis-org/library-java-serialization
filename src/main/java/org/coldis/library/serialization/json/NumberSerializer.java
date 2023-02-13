package org.coldis.library.serialization.json;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

/**
 * Number serializer.
 */
public class NumberSerializer extends JsonSerializer<Number> {

	/**
	 * Gets the number format for the given locale.
	 *
	 * @param  locale          Locale to be used.
	 * @param  parseBigDecimal If big decimals should also be parsed
	 * @param  groupingUsed    If grouping should be used.
	 * @return                 The number format.
	 */
	public static DecimalFormat getNumberFormat(
			final Locale locale,
			final Boolean parseBigDecimal,
			final Boolean groupingUsed) {
		// Creates and returns the number format.
		final DecimalFormat numberFormat = new DecimalFormat();
		numberFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(locale));
		numberFormat.setGroupingUsed(groupingUsed);
		numberFormat.setParseBigDecimal(parseBigDecimal);
		return numberFormat;
	}

	/**
	 * Gets the number format to be used in the serializer.
	 *
	 * @param  locale Locale to be used.
	 * @return        The number format.
	 */
	protected NumberFormat getNumberFormat(
			final Locale locale) {
		return NumberSerializer.getNumberFormat(locale, true, false);
	}

	/**
	 * If the value should be serialized as a number (or a string).
	 *
	 * @return If the value should be serialized as a number (or a string).
	 */
	protected Boolean asNumber() {
		return false;
	}

	/**
	 * @see com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
	 *      com.fasterxml.jackson.core.JsonGenerator,
	 *      com.fasterxml.jackson.databind.SerializerProvider)
	 */
	@Override
	public void serialize(
			final Number value,
			final JsonGenerator jsonGenerator,
			final SerializerProvider serializers) throws IOException, JsonProcessingException {
		// Gets the number format to be used.
		final NumberFormat numberFormat = this.getNumberFormat(serializers.getLocale());
		// If the number should be serialized as a number.
		if (this.asNumber()) {
			// Serializes the number.
			jsonGenerator.writeNumber(numberFormat.format(value));
		}
		// If the number should be serialized as a string.
		else {
			// Serializes the number.
			jsonGenerator.writeString(numberFormat.format(value));
		}
	}

	/**
	 * @see com.fasterxml.jackson.databind.JsonSerializer#serializeWithType(java.lang.Object,
	 *      com.fasterxml.jackson.core.JsonGenerator,
	 *      com.fasterxml.jackson.databind.SerializerProvider,
	 *      com.fasterxml.jackson.databind.jsontype.TypeSerializer)
	 */
	@Override
	public void serializeWithType(
			final Number value,
			final JsonGenerator jsonGenerator,
			final SerializerProvider serializers,
			final TypeSerializer typeSerializer) throws IOException {
		this.serialize(value, jsonGenerator, serializers);
	}

}
