package org.coldis.library.serialization;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.fury.Fury;
import org.apache.fury.config.CompatibleMode;
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
			final String... packagesNames) {
		final FuryBuilder furyBuilder = Fury.builder().withLanguage(language).withCompatibleMode(CompatibleMode.COMPATIBLE);
		furyBuilder.requireClassRegistration(ArrayUtils.isNotEmpty(packagesNames));
		final Fury fury = furyBuilder.build();
		if (ArrayUtils.isNotEmpty(packagesNames)) {
			ObjectMapperHelper.getModelClasses(packagesNames).forEach(fury::register);
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
