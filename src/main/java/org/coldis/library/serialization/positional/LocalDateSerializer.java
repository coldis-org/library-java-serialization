package org.coldis.library.serialization.positional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.coldis.library.exception.IntegrationException;
import org.coldis.library.model.SimpleMessage;

/**
 * Local date (de)serializer.
 */
public class LocalDateSerializer implements PositionalSerializerInterface<LocalDate>, PostionalDeserializerInterface<LocalDate> {

	/**
	 * Formatter to be used during (de)serialization.
	 */
	private DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

	/**
	 * No arguments constructor.
	 */
	public LocalDateSerializer() {
	}

	/**
	 * Formatter constructor.
	 *
	 * @param dateFormatPattern Formatter to be used during (de)serialization.
	 */
	public LocalDateSerializer(final String dateFormatPattern) {
		formatter = DateTimeFormatter.ofPattern(dateFormatPattern);
	}

	/**
	 * @see org.coldis.library.serialization.positional.PostionalDeserializerInterface#deserialize(java.lang.String)
	 */
	@Override
	public LocalDate deserialize(
			final String serializedObject) {
		// Tries to de-serialize the local date.
		try {
			return formatter.parse(serializedObject, LocalDate::from);
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
			final LocalDate object) {
		return object == null ? "" : object.format(formatter);
	}

}
