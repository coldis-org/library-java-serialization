package org.coldis.library.serialization.positional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.coldis.library.exception.IntegrationException;
import org.coldis.library.model.SimpleMessage;

/**
 * Local date/time (de)serializer.
 */
public class LocalDateTimeSerializer implements PositionalSerializerInterface<LocalDateTime>, PostionalDeserializerInterface<LocalDateTime> {

	/**
	 * Formatter to be used during (de)serialization.
	 */
	private DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

	/**
	 * No arguments constructor.
	 */
	public LocalDateTimeSerializer() {
	}

	/**
	 * Formatter constructor.
	 *
	 * @param dateFormatPattern Formatter to be used during (de)serialization.
	 */
	public LocalDateTimeSerializer(final String dateFormatPattern) {
		formatter = DateTimeFormatter.ofPattern(dateFormatPattern);
	}

	/**
	 * @see org.coldis.library.serialization.positional.PostionalDeserializerInterface#deserialize(java.lang.String)
	 */
	@Override
	public LocalDateTime deserialize(
			final String serializedObject) {
		// Tries to de-serialize the local date/time.
		try {
			return formatter.parse(serializedObject, LocalDateTime::from);
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
			final LocalDateTime object) {
		return object == null ? "" : object.format(formatter);
	}

}
