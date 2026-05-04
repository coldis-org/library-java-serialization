package org.coldis.library.serialization;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.fory.BaseFory;
import org.apache.fory.Fory;
import org.apache.fory.config.CompatibleMode;
import org.apache.fory.config.ForyBuilder;
import org.apache.fory.config.Language;
import org.coldis.library.model.Typable;
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
	public static final BaseFory createSerializer(
			final Boolean threadSafe,
			final Integer minPoolSize,
			final Integer maxPoolSize,
			final Language language,
			final String... packagesNames) {
		final ForyBuilder foryBuilder = Fory.builder().registerGuavaTypes(false).withLanguage(language).withCompatibleMode(CompatibleMode.COMPATIBLE);
		foryBuilder.requireClassRegistration(ArrayUtils.isNotEmpty(packagesNames));
		final BaseFory fory = (threadSafe ? ((maxPoolSize != null) ? foryBuilder.buildThreadSafeForyPool(maxPoolSize)
				: foryBuilder.buildThreadSafeFory()) : foryBuilder.build());
		if (ArrayUtils.isNotEmpty(packagesNames)) {
			final Map<Class<?>, String> modelClasses = new HashMap<>();
			modelClasses
					.putAll(ObjectMapperHelper.getModelClasses(
							new OptimizedSerializationHelper.NoAnnotationTypeFilter(
									Set.of(Component.class, Controller.class, Service.class, Repository.class, Configuration.class), true, true),
							packagesNames));
			final Map<String, Class<?>> canonicalByTypeName = new HashMap<>();
			modelClasses.keySet().forEach(clazz -> {
				final String typeName = OptimizedSerializationHelper.resolveTypeName(clazz);
				if (typeName != null) {
					final Class<?> current = canonicalByTypeName.get(typeName);
					if ((current == null) || (OptimizedSerializationHelper.isDtoClass(current) && !OptimizedSerializationHelper.isDtoClass(clazz))) {
						canonicalByTypeName.put(typeName, clazz);
					}
				}
			});
			final Set<Class<?>> canonicalClasses = new HashSet<>(canonicalByTypeName.values());
			OptimizedSerializationHelper.LOGGER.info("Registering {} classes under shared type names and {} other classes in optimized serialization.",
					canonicalClasses.size(), modelClasses.size() - canonicalClasses.size());
			OptimizedSerializationHelper.LOGGER.debug("Canonical type names: {}", canonicalByTypeName);
			modelClasses.keySet().forEach(clazz -> {
				if (canonicalClasses.contains(clazz)) {
					fory.register(clazz, "", OptimizedSerializationHelper.resolveTypeName(clazz));
				}
				else {
					fory.register(clazz, "", clazz.getName());
				}
			});
		}
		return fory;
	}

	/**
	 * Resolves the shared logical type name for a class via Typable.getTypeName(),
	 * by instantiating a default instance through its no-arg constructor. Returns
	 * null when the class is not Typable or has no usable no-arg constructor.
	 */
	private static String resolveTypeName(
			final Class<?> clazz) {
		String typeName = null;
		if (Typable.class.isAssignableFrom(clazz)) {
			try {
				final Constructor<?> constructor = clazz.getDeclaredConstructor();
				constructor.setAccessible(true);
				typeName = ((Typable) constructor.newInstance()).getTypeName();
			}
			catch (final Throwable error) {
				// Falls back to no shared name; class is registered with default identifier.
			}
		}
		return typeName;
	}

	/**
	 * Detects DTO classes by the presence of org.coldis.library.dto.DtoOrigin
	 * without taking a compile-time dependency on the dto module.
	 */
	private static boolean isDtoClass(
			final Class<?> clazz) {
		return Arrays.stream(clazz.getAnnotations())
				.anyMatch(annotation -> "org.coldis.library.dto.DtoOrigin".equals(annotation.annotationType().getName()));
	}

}
