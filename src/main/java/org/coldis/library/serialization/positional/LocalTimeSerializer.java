package org.coldis.library.serialization.positional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.coldis.library.exception.IntegrationException;
import org.coldis.library.model.SimpleMessage;

/**
 * Local time (de)serializer.
 */
public class LocalTimeSerializer implements PositionalSerializerInterface<LocalTime>, PostionalDeserializerInterface<LocalTime> {

	/**
	 * Formatter to be used during (de)serialization.
	 */
	private DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

	/**
	 * No arguments constructor.
	 */
	public LocalTimeSerializer() {
	}

	/**
	 * Formatter constructor.
	 *
	 * @param dateFormatPattern Formatter to be used during (de)serialization.
	 */
	public LocalTimeSerializer(final String dateFormatPattern) {
		formatter = DateTimeFormatter.ofPattern(dateFormatPattern);
	}

	/**
	 * @see org.coldis.library.serialization.positional.PostionalDeserializerInterface#deserialize(java.lang.String)
	 */
	@Override
	public LocalTime deserialize(
			final String serializedObject) {
		// Tries to de-serialize the local time.
		try {
			return formatter.parse(serializedObject, LocalTime::from);
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
	public String serialize(
			final LocalTime object) {
		return object == null ? "" : object.format(formatter);
	}

}
