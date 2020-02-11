package org.coldis.library.serialization.json;

import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;

/**
 * Generic date/time de-serializer. Basically exposes the
 * {@link InstantDeserializer} constructor.
 *
 * @param <TemporalType> Temporal type.
 */
public class GenericDateTimeDeserializer<TemporalType extends Temporal> extends InstantDeserializer<TemporalType> {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 7881894686456309754L;

	/**
	 * Base de-serializer and formatter constructor.
	 *
	 * @param baseDeserializer Base de-serializer to be used during
	 *                             de-serialization.
	 * @param formatter        Formatter to be used during de-serialization.
	 */
	public GenericDateTimeDeserializer(final InstantDeserializer<TemporalType> baseDeserializer,
			final DateTimeFormatter formatter) {
		super(baseDeserializer, formatter);
	}

}