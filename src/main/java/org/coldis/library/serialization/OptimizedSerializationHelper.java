package org.coldis.library.serialization;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.fury.BaseFury;
import org.apache.fury.Fury;
import org.apache.fury.config.CompatibleMode;
import org.apache.fury.config.FuryBuilder;
import org.apache.fury.config.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

/**
 * Optimized serialization helper.
 */
public class OptimizedSerializationHelper {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OptimizedSerializationHelper.class);

	/**
	 * No annotation type filter.
	 */
	static class NoAnnotationTypeFilter extends AbstractTypeHierarchyTraversingFilter {

		/** Without annotations. */
		private final Set<Class<? extends Annotation>> withoutAnnotations;

		/** Consider meta-annotations. */
		private final boolean considerMetaAnnotations;

		/**
		 * Constructor.
		 *
		 * @param annotations             Annotations.
		 * @param considerMetaAnnotations Consider meta-annotations.
		 * @param considerInterfaces      Consider interfaces.
		 */
		public NoAnnotationTypeFilter(
				final Set<Class<? extends Annotation>> withoutAnnotations,
				final boolean considerMetaAnnotations,
				final boolean considerInterfaces) {
			super(withoutAnnotations.stream().anyMatch(annotationType -> annotationType.isAnnotationPresent(Inherited.class)), considerInterfaces);
			this.withoutAnnotations = withoutAnnotations;
			this.considerMetaAnnotations = considerMetaAnnotations;
		}

		/**
		 * Checks if the class has any annotation.
		 *
		 * @param  typeName Type name.
		 * @return          If the class has any annotation.
		 */
		@Nullable
		protected Boolean hasAnyAnnotation(
				final String typeName) {
			Boolean hasAnnotations = false;
			try {
				final Class<?> clazz = ClassUtils.forName(typeName, this.getClass().getClassLoader());
				hasAnnotations = this.withoutAnnotations.stream()
						.anyMatch(annotationType -> ((this.considerMetaAnnotations ? AnnotationUtils.getAnnotation(clazz, annotationType)
								: clazz.getAnnotation(annotationType)) != null));
			}
			catch (final Throwable throwable) { // Ignores.
			}
			return hasAnnotations;
		}

		/**
		 * @see org.springframework.core.type.filter.AnnotationTypeFilter#matchSelf(org.springframework.core.type.classreading.MetadataReader)
		 */
		@Override
		protected boolean matchSelf(
				final MetadataReader metadataReader) {
			final AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
			return this.withoutAnnotations.stream().noneMatch(annotationType -> metadata.hasAnnotation(annotationType.getName())
					|| (this.considerMetaAnnotations && metadata.hasMetaAnnotation(annotationType.getName())));
		}

		/**
		 * @see org.springframework.core.type.filter.AnnotationTypeFilter#matchSuperClass(java.lang.String)
		 */
		@Override
		protected Boolean matchSuperClass(
				final String superClassName) {
			return !this.hasAnyAnnotation(superClassName);
		}

		/**
		 * @see org.springframework.core.type.filter.AnnotationTypeFilter#matchInterface(java.lang.String)
		 */
		@Override
		protected Boolean matchInterface(
				final String interfaceName) {
			return !this.hasAnyAnnotation(interfaceName);
		}

	}

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
		final FuryBuilder furyBuilder = Fury.builder().registerGuavaTypes(false).withLanguage(language).withCompatibleMode(CompatibleMode.COMPATIBLE);
		furyBuilder.requireClassRegistration(ArrayUtils.isNotEmpty(packagesNames));
		final BaseFury fury = (threadSafe ? (((minPoolSize != null) && (maxPoolSize != null)) ? furyBuilder.buildThreadSafeFuryPool(minPoolSize, maxPoolSize)
				: furyBuilder.buildThreadSafeFury()) : furyBuilder.build());
		if (ArrayUtils.isNotEmpty(packagesNames)) {
			final Map<Class<?>, String> modelClasses = new HashMap<>();
			modelClasses
					.putAll(ObjectMapperHelper.getModelClasses(
							new OptimizedSerializationHelper.NoAnnotationTypeFilter(
									Set.of(Component.class, Controller.class, Service.class, Repository.class, Configuration.class), true, true),
							packagesNames));
			OptimizedSerializationHelper.LOGGER.info("Registering {} classes in optimized serialization.", modelClasses.size());
			OptimizedSerializationHelper.LOGGER.debug("Classes reistered: {}", modelClasses.keySet().stream().map(Class::getName).toArray());
			modelClasses.forEach((
					clazz,
					className) -> {
				fury.register(clazz);
			});
		}
		return fury;
	}

}
