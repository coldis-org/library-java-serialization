package org.coldis.library.serialization.json;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

/**
 * Number deserializer.
 */
public class NumberDeserializer extends JsonDeserializer<Number> {

	/**
	 * Gets the number format to be used in the deserializer.
	 *
	 * @param  locale Locale to be used.
	 * @return        The number format.
	 */
	protected NumberFormat getNumberFormat(
			final Locale locale) {
		return NumberSerializer.getNumberFormat(locale, true, false);
	}

	/**
	 * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser,
	 *      com.fasterxml.jackson.databind.DeserializationContext)
	 */
	@Override
	public Number deserialize(
			final JsonParser parser,
			final DeserializationContext context) throws IOException, JsonProcessingException {
		// Gets the number format to be used.
		final NumberFormat numberFormat = this.getNumberFormat(context.getLocale());
		// Deserializes the number.
		try {
			return numberFormat.parse(parser.getText());
		}
		// If the number could not be deserialized.
		catch (final ParseException exception) {
			// Throws an IO exception.
			throw new IOException("Could not deserialize object '" + parser.getText() + "'.", exception);
		}
	}

	/**
	 * @see com.fasterxml.jackson.databind.JsonDeserializer#deserializeWithType(com.fasterxml.jackson.core.JsonParser,
	 *      com.fasterxml.jackson.databind.DeserializationContext,
	 *      com.fasterxml.jackson.databind.jsontype.TypeDeserializer)
	 */
	@Override
	public Object deserializeWithType(
			final JsonParser parser,
			final DeserializationContext context,
			final TypeDeserializer typeDeserializer) throws IOException {
		return this.deserialize(parser, context);
	}

}
