package org.coldis.library.serialization;

import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.fury.Fury;
import org.apache.fury.config.FuryBuilder;
import org.apache.fury.config.Language;

/**
 * Optimized serialization helper.
 */
public class OptimizedSerializationHelper {

	/**
	 * Creates a serializer.
	 *
	 * @param  language Language.
	 * @return          Serializer.
	 */
	public static final Fury createSerializer(
			final Language language,
			final Set<Class<?>> classes) {
		final FuryBuilder furyBuilder = Fury.builder().withLanguage(language);
		furyBuilder.requireClassRegistration(CollectionUtils.isNotEmpty(classes));
		final Fury fury = furyBuilder.build();
		if (CollectionUtils.isNotEmpty(classes)) {
			classes.forEach(fury::register);
		}
		return fury;
	}

	/**
	 * @param  <TargetType> Target type.
	 * @param  fury         Fury.
	 * @param  object       Object.
	 * @return              Deep clone.
	 */
	@SuppressWarnings("unchecked")
	public static <TargetType> TargetType deepClone(
			final Fury fury,
			final TargetType object) {
		return (TargetType) fury.deserialize(fury.serialize(object));
	}

}
