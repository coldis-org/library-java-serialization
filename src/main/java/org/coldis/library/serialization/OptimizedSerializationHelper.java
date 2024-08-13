package org.coldis.library.serialization;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.fury.BaseFury;
import org.apache.fury.Fury;
import org.apache.fury.config.CompatibleMode;
import org.apache.fury.config.FuryBuilder;
import org.apache.fury.config.Language;
import org.coldis.library.dto.DtoOrigin;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import com.fasterxml.jackson.annotation.JsonTypeName;

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
		final BaseFury fury = (threadSafe ? (((minPoolSize != null) && (maxPoolSize != null)) ? furyBuilder.buildThreadSafeFuryPool(minPoolSize, maxPoolSize)
				: furyBuilder.buildThreadSafeFury()) : furyBuilder.build());
		if (ArrayUtils.isNotEmpty(packagesNames)) {
			final Map<Class<?>, String> modelClasses = new HashMap<>();
			modelClasses.putAll(ObjectMapperHelper.getModelClasses(new TypeFilter() {
				
				@Override
				public boolean match(
						MetadataReader metadataReader,
						MetadataReaderFactory metadataReaderFactory) throws IOException {
					return true;
				}
			}, packagesNames));
			modelClasses.forEach((
					clazz,
					className) -> {
				fury.register(clazz);
			});
		}
		return fury;
	}

}
