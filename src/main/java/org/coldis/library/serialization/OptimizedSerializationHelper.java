package org.coldis.library.serialization;

import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.fury.BaseFury;
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
	public static final BaseFury createSerializer(
			final Boolean threadSafe,
			final Integer minPoolSize,
			final Integer maxPoolSize,
			final Language language,
			final String... packagesNames) {
		final FuryBuilder furyBuilder = Fury.builder().withLanguage(language).withCompatibleMode(CompatibleMode.COMPATIBLE);
		furyBuilder.requireClassRegistration(ArrayUtils.isNotEmpty(packagesNames));
		final BaseFury fury = (threadSafe
				? (((minPoolSize != null) && (maxPoolSize != null)) ? furyBuilder.buildThreadSafeFuryPool(minPoolSize, maxPoolSize) : furyBuilder.buildThreadSafeFury())
				: furyBuilder.build());
		if (ArrayUtils.isNotEmpty(packagesNames)) {
			final Set<Class<?>> modelClasses = ObjectMapperHelper.getModelClasses(packagesNames);
			modelClasses.forEach(fury::register);
		}
		return fury;
	}

}
