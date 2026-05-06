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
	 * Annotation classes whose presence excludes a scanned class from
	 * Fory registration. AspectJ is loaded softly so this module does
	 * not need a compile-time dependency on aspectjrt.
	 */
	private static final Set<Class<? extends Annotation>> EXCLUDED_ANNOTATIONS = OptimizedSerializationHelper.buildExcludedAnnotations();

	@SuppressWarnings("unchecked")
	private static Set<Class<? extends Annotation>> buildExcludedAnnotations() {
		final java.util.LinkedHashSet<Class<? extends Annotation>> excluded = new java.util.LinkedHashSet<>();
		excluded.add(Component.class);
		excluded.add(Controller.class);
		excluded.add(Service.class);
		excluded.add(Repository.class);
		excluded.add(Configuration.class);
		try {
			excluded.add((Class<? extends Annotation>) Class.forName("org.aspectj.lang.annotation.Aspect"));
		}
		catch (final ClassNotFoundException notOnClasspath) {
			OptimizedSerializationHelper.LOGGER.debug("AspectJ @Aspect not on classpath; skipping from exclusion set.");
		}
		return Set.copyOf(excluded);
	}

	/**
	 * No annotation type filter.
	 */
	public static class NoAnnotationTypeFilter extends AbstractTypeHierarchyTraversingFilter {

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
		return OptimizedSerializationHelper.createSerializer(threadSafe, minPoolSize, maxPoolSize, language, false, packagesNames);
	}

	/**
	 * Creates a serializer. When two classes share the same logical type name
	 * (typically a Model and its generated DTO), {@code preferDto} controls
	 * which one wins the canonical name slot. Set it to {@code true} to build
	 * a producer-side serializer that writes Dto instances under the shared
	 * name; set it to {@code false} (the default) to build a consumer-side
	 * serializer that reads them back as Models.
	 *
	 * @param  threadSafe    Whether the serializer should be thread-safe.
	 * @param  minPoolSize   Min pool size (thread-safe pool).
	 * @param  maxPoolSize   Max pool size (thread-safe pool).
	 * @param  language      Language.
	 * @param  preferDto     If {@code true}, the Dto wins the canonical type
	 *                           name when both Model and Dto are scanned;
	 *                           otherwise the Model wins.
	 * @param  packagesNames Packages to scan for registration.
	 * @return               Serializer.
	 */
	public static final BaseFory createSerializer(
			final Boolean threadSafe,
			final Integer minPoolSize,
			final Integer maxPoolSize,
			final Language language,
			final boolean preferDto,
			final String... packagesNames) {
		final ForyBuilder foryBuilder = Fory.builder().registerGuavaTypes(false).withLanguage(language).withCompatibleMode(CompatibleMode.COMPATIBLE)
				.withDeserializeUnknownClass(true);
		foryBuilder.requireClassRegistration(ArrayUtils.isNotEmpty(packagesNames));
		final BaseFory fory = (threadSafe ? ((maxPoolSize != null) ? foryBuilder.buildThreadSafeForyPool(maxPoolSize)
				: foryBuilder.buildThreadSafeFory()) : foryBuilder.build());
		// Fory's auto-registration for deserializeUnknownClass=true only covers
		// UnknownStruct/UnknownEmptyStruct. UnknownEnum (and the array variants of all three) are
		// missed, which makes the security gate reject otherwise valid cross-class deserializations
		// when a DTO carries fields the Model doesn't declare. Register them explicitly so the
		// receiver can fall through to a placeholder instead of failing.
		final Class<?>[] unknownPlaceholders = { org.apache.fory.serializer.UnknownClass.UnknownEnum.class,
				org.apache.fory.serializer.UnknownClass.UnknownEnum1DArray, org.apache.fory.serializer.UnknownClass.UnknownEnum2DArray,
				org.apache.fory.serializer.UnknownClass.UnknownEnum3DArray, org.apache.fory.serializer.UnknownClass.UnknownEmptyStruct.class,
				org.apache.fory.serializer.UnknownClass.UnknownEmptyStruct1DArray, org.apache.fory.serializer.UnknownClass.UnknownEmptyStruct2DArray,
				org.apache.fory.serializer.UnknownClass.UnknownEmptyStruct3DArray, org.apache.fory.serializer.UnknownClass.UnknownStruct1DArray,
				org.apache.fory.serializer.UnknownClass.UnknownStruct2DArray, org.apache.fory.serializer.UnknownClass.UnknownStruct3DArray };
		for (final Class<?> placeholder : unknownPlaceholders) {
			try {
				fory.register(placeholder);
			}
			catch (final Throwable ignore) {
				// Already registered (e.g. UnknownStruct on metaShare path) or not exposed by the
				// installed Fory version — both are acceptable.
			}
		}
		if (ArrayUtils.isNotEmpty(packagesNames)) {
			final Map<Class<?>, String> modelClasses = new HashMap<>();
			modelClasses
					.putAll(ObjectMapperHelper.getModelClasses(
							new OptimizedSerializationHelper.NoAnnotationTypeFilter(OptimizedSerializationHelper.EXCLUDED_ANNOTATIONS, true, false),
							packagesNames));
			final Map<String, Class<?>> canonicalByTypeName = new HashMap<>();
			modelClasses.keySet().forEach(clazz -> {
				final String typeName = OptimizedSerializationHelper.resolveTypeName(clazz);
				if (typeName != null) {
					final Class<?> current = canonicalByTypeName.get(typeName);
					final boolean replace;
					if (current == null) {
						replace = true;
					}
					else if (preferDto) {
						replace = !OptimizedSerializationHelper.isDtoClass(current) && OptimizedSerializationHelper.isDtoClass(clazz);
					}
					else {
						replace = OptimizedSerializationHelper.isDtoClass(current) && !OptimizedSerializationHelper.isDtoClass(clazz);
					}
					if (replace) {
						canonicalByTypeName.put(typeName, clazz);
					}
				}
			});
			final Set<Class<?>> canonicalClasses = new HashSet<>(canonicalByTypeName.values());
			OptimizedSerializationHelper.LOGGER.info("Registering {} classes under shared type names and {} other classes in optimized serialization.",
					canonicalClasses.size(), modelClasses.size() - canonicalClasses.size());
			OptimizedSerializationHelper.LOGGER.debug("Canonical type names: {}", canonicalByTypeName);
			final long enumCount = modelClasses.keySet().stream().filter(Class::isEnum).count();
			OptimizedSerializationHelper.LOGGER.info("Optimized serializer scan picked up {} enum classes out of {} total scanned classes.",
					enumCount, modelClasses.size());
			modelClasses.keySet().stream().filter(Class::isEnum)
					.forEach(enumClass -> OptimizedSerializationHelper.LOGGER.debug("Scanned enum: {}", enumClass.getName()));
			modelClasses.keySet().forEach(clazz -> {
				final String preferredName = canonicalClasses.contains(clazz) ? OptimizedSerializationHelper.resolveTypeName(clazz) : clazz.getName();
				final String fallbackName = clazz.getName();
				try {
					fory.register(clazz, "", preferredName);
					OptimizedSerializationHelper.LOGGER.debug("Registered {} under name '{}' (preferDto={}).", clazz.getName(), preferredName, preferDto);
				}
				catch (final IllegalArgumentException conflict) {
					if (!fallbackName.equals(preferredName)) {
						try {
							fory.register(clazz, "", fallbackName);
							OptimizedSerializationHelper.LOGGER.warn("Registered {} under FQN after preferred name '{}' was taken: {}", clazz.getName(),
									preferredName, conflict.getMessage());
						}
						catch (final IllegalArgumentException fallbackConflict) {
							OptimizedSerializationHelper.LOGGER.warn("Skipping optimized serialization registration of {} (preferred='{}', fallback='{}'): {}",
									clazz.getName(), preferredName, fallbackName, fallbackConflict.getMessage());
						}
					}
					else {
						OptimizedSerializationHelper.LOGGER.warn("Skipping optimized serialization registration of {} under name '{}': {}", clazz.getName(),
								preferredName, conflict.getMessage());
					}
				}
			});
		}
		return fory;
	}

	/**
	 * Resolves the shared logical type name for a class. Tries the
	 * {@link Typable} interface first, then falls back to a {@code
	 * getTypeName()} method (so generated DTOs that don't implement Typable
	 * still group with their origin Model under the same name). Both paths
	 * instantiate a default instance via a no-arg constructor and call the
	 * method on it. Returns null when no typeName can be resolved.
	 */
	private static String resolveTypeName(
			final Class<?> clazz) {
		String typeName = null;
		try {
			final Constructor<?> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			final Object instance = constructor.newInstance();
			if (instance instanceof final Typable typable) {
				typeName = typable.getTypeName();
			}
			else {
				final java.lang.reflect.Method getter = clazz.getMethod("getTypeName");
				if ((getter.getReturnType() == String.class) && !java.lang.reflect.Modifier.isStatic(getter.getModifiers())) {
					typeName = (String) getter.invoke(instance);
				}
			}
		}
		catch (final Throwable error) {
			// Falls back to no shared name; class is registered with default identifier.
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
