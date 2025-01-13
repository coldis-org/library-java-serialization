package org.coldis.library.serialization;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.coldis.library.exception.IntegrationException;
import org.coldis.library.helper.DateTimeHelper;
import org.coldis.library.model.SimpleMessage;
import org.coldis.library.serialization.json.GenericDateTimeDeserializer;
import org.coldis.library.serialization.json.SensitiveFieldDeserializer;
import org.coldis.library.serialization.json.SensitiveFieldSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;

/**
 * JSON helper.
 */
public class ObjectMapperHelper {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectMapperHelper.class);

	/**
	 * Gets the configured date/time module.
	 *
	 * @return The configured date/time module.
	 */
	public static Module getDateTimeModule() {
		// Creates the time module.
		final JavaTimeModule javaTimeModule = new JavaTimeModule();
		// Creates de-serializers for date/time classes.
		final LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeHelper.DATE_TIME_FORMATTER);
		final LocalDateDeserializer localDateDeserializer = new LocalDateDeserializer(DateTimeHelper.DATE_TIME_FORMATTER);
		final LocalTimeDeserializer localTimeDeserializer = new LocalTimeDeserializer(DateTimeHelper.DATE_TIME_FORMATTER);
		final GenericDateTimeDeserializer<OffsetDateTime> offsetDateTimeDeserializer = new GenericDateTimeDeserializer<>(InstantDeserializer.OFFSET_DATE_TIME,
				DateTimeHelper.DATE_TIME_FORMATTER);
		final GenericDateTimeDeserializer<ZonedDateTime> zonedDateTimeDeserializer = new GenericDateTimeDeserializer<>(InstantDeserializer.ZONED_DATE_TIME,
				DateTimeHelper.DATE_TIME_FORMATTER);
		final GenericDateTimeDeserializer<Instant> instantDeserializer = new GenericDateTimeDeserializer<>(InstantDeserializer.INSTANT,
				DateTimeHelper.DATE_TIME_FORMATTER);
		// Adds the de-serializers to the module.
		javaTimeModule.addDeserializer(LocalDateTime.class, localDateTimeDeserializer).addDeserializer(LocalDate.class, localDateDeserializer)
				.addDeserializer(LocalTime.class, localTimeDeserializer).addDeserializer(OffsetDateTime.class, offsetDateTimeDeserializer)
				.addDeserializer(ZonedDateTime.class, zonedDateTimeDeserializer).addDeserializer(Instant.class, instantDeserializer);
		// Returns the module.
		return javaTimeModule;
	}

	/**
	 * Number serialization classes.
	 */
	private static final Set<Class<? extends Number>> NUMBER_CLASSES = Set.of(Number.class, int.class, Integer.class, long.class, Long.class, byte.class,
			Byte.class, short.class, Short.class, double.class, Double.class, float.class, Float.class, BigDecimal.class, BigInteger.class);

	/**
	 * Gets the sensitive fields serialization module.
	 *
	 * @return
	 */
	public static SimpleModule getSensitiveFieldModule() {
		final SimpleModule sensitiveFieldModule = new SimpleModule();
		ObjectMapperHelper.NUMBER_CLASSES.forEach(numberClass -> sensitiveFieldModule.addSerializer(numberClass, new SensitiveFieldSerializer<>(numberClass)));
		sensitiveFieldModule.addSerializer(String.class, new SensitiveFieldSerializer<>(String.class));
		ObjectMapperHelper.NUMBER_CLASSES
				.forEach(numberClass -> sensitiveFieldModule.addDeserializer(numberClass, new SensitiveFieldDeserializer<>(numberClass)));
		sensitiveFieldModule.addDeserializer(String.class, new SensitiveFieldDeserializer<>(String.class));
		return sensitiveFieldModule;
	}

	/**
	 * Gets classes from packages.
	 */
	public static Map<Class<?>, String> getModelClasses(
			final TypeFilter filter,
			final String... packagesNames) {
		final Map<Class<?>, String> classes = new HashMap<>();
		// Adds class for each JSON type.
		final ClassPathScanningCandidateComponentProvider jsonScanner = new ClassPathScanningCandidateComponentProvider(false);
		jsonScanner.addIncludeFilter(filter);
		if (packagesNames != null) {
			for (final String packageName : packagesNames) {
				try {
					final Set<BeanDefinition> types = jsonScanner.findCandidateComponents(packageName);
					for (final BeanDefinition currentType : types) {
						// Tries to register it as an object mapper subtype.
						try {
							final Class<?> clazz = Class.forName(currentType.getBeanClassName());
							classes.put(clazz, clazz.getName());
						}
						// If the class cannot be found.
						catch (final Exception exception) {
							// Logs it.
							ObjectMapperHelper.LOGGER
									.error("Class '" + currentType.getBeanClassName() + "' could not be added: " + exception.getLocalizedMessage());
							ObjectMapperHelper.LOGGER.debug("Class '" + currentType.getBeanClassName() + "' could not be added.", exception);
						}
					}
				}
				catch (final Exception exception) {
					ObjectMapperHelper.LOGGER.error("Error scanning package  '" + packageName + "': " + exception.getLocalizedMessage());
					ObjectMapperHelper.LOGGER.debug("Error scanning package  '" + packageName + "'.", exception);
				}
			}
		}
		// Returns the classes.
		return classes;
	}

	/**
	 * Adds the subtypes found from in a package to the object mapper.
	 *
	 * @param  objectMapper  Object mapper.
	 * @param  packagesNames The packages to find the subtypes within.
	 * @return               The configured object mapper.
	 */
	public static ObjectMapper addSubtypesFromPackage(
			final ObjectMapper objectMapper,
			final String... packagesNames) {
		for (final Class<?> clazz : ObjectMapperHelper.getModelClasses(new AnnotationTypeFilter(JsonTypeName.class), packagesNames).keySet()) {
			// Tries to register it as an object mapper subtype.
			try {
				objectMapper.registerSubtypes(clazz);
			}
			// If the class cannot be found.
			catch (final Exception exception) {
				// Logs it.
				ObjectMapperHelper.LOGGER.debug("Class '" + clazz.getName() + "' could not be registered as a subtype: " + exception.getLocalizedMessage());
				ObjectMapperHelper.LOGGER.debug("Class '" + clazz.getName() + "' could not be registered as a subtype.", exception);
			}
		}
		// Returns the configured object mapper.
		return objectMapper;
	}

	/**
	 * Configures the object mapper.
	 *
	 * @param  objectMapper  Object mapper.
	 * @param  packagesNames Packages names to add subtypes from.
	 * @return               Object mapper.
	 */
	public static ObjectMapper configureMapper(
			final ObjectMapper objectMapper,
			final String... packagesNames) {
		objectMapper.registerModule(ObjectMapperHelper.getDateTimeModule());
		objectMapper.registerModule(ObjectMapperHelper.getSensitiveFieldModule());
		ObjectMapperHelper.addSubtypesFromPackage(objectMapper, packagesNames);
		return objectMapper;
	}

	/**
	 * Creates the object mapper.
	 *
	 * @param  packagesNames Packages names to add subtypes from.
	 * @return               Object mapper.
	 */
	public static ObjectMapper createMapper(
			final String... packagesNames) {
		return ObjectMapperHelper.configureMapper(new ObjectMapper());
	}

	/**
	 * De-serializes a given object into a target type.
	 *
	 * @param  <TargetType>         The target object type.
	 * @param  objectMapper         The object mapper to use during
	 *                                  de-serialization.
	 * @param  object               The original object to be de-serialized.
	 * @param  objectType           The target object type.
	 * @param  resumeOnErrors       If errors should be silently ignored.
	 * @return                      The de-serialized object.
	 * @throws IntegrationException If the object cannot be de-serialized.
	 */
	public static <TargetType> TargetType deserialize(
			final ObjectMapper objectMapper,
			final String object,
			final Class<TargetType> objectType,
			final Boolean resumeOnErrors) throws IntegrationException {
		// Tries to de-serialize the given object into a target type.
		try {
			return objectMapper.readValue(object, objectType);
		}
		// If there is a problem de-serializing the object.
		catch (final Exception exception) {
			// If errors should be silently ignored.
			if (resumeOnErrors) {
				// Logs and returns null.
				ObjectMapperHelper.LOGGER
						.debug("Error silently ignored: object '" + object + "' could not be de-serialized into target class '" + objectType + "'.", exception);
				return null;
			}
			// If errors should not be silently ignored.
			else {
				// Throws a de-serialization exception.
				throw new IntegrationException(new SimpleMessage("deserialization.error"), exception);
			}
		}
	}

	/**
	 * De-serializes a given object into a target type.
	 *
	 * @param  <TargetType>         The target object type.
	 * @param  objectMapper         The object mapper to use during
	 *                                  de-serialization.
	 * @param  object               The original object to be de-serialized.
	 * @param  objectType           The target object type.
	 * @param  resumeOnErrors       If errors should be silently ignored.
	 * @return                      The de-serialized object.
	 * @throws IntegrationException If the object cannot be de-serialized.
	 */
	public static <TargetType> TargetType deserialize(
			final ObjectMapper objectMapper,
			final String object,
			final TypeReference<TargetType> objectType,
			final Boolean resumeOnErrors) throws IntegrationException {
		// Tries to de-serialize the given object into a target type.
		try {
			return objectMapper.readValue(object, objectType);
		}
		// If there is a problem de-serializing the object.
		catch (final Exception exception) {
			// If errors should be silently ignored.
			if (resumeOnErrors) {
				// Logs and returns null.
				ObjectMapperHelper.LOGGER
						.debug("Error silently ignored: object '" + object + "' could not be de-serialized into target class '" + objectType + "'.", exception);
				return null;
			}
			// If errors should not be silently ignored.
			else {
				// Throws a de-serialization exception.
				throw new IntegrationException(new SimpleMessage("deserialization.error"), exception);
			}
		}
	}

	/**
	 * Serializes a given object into string.
	 *
	 * @param  <OriginalType>       The original object type.
	 * @param  objectMapper         The object mapper to use during serialization.
	 * @param  object               The original object to be serialized.
	 * @param  view                 The serialization view to be used during
	 *                                  serialization.
	 * @param  resumeOnErrors       If errors should be silently ignored.
	 * @return                      The serialized object.
	 * @throws IntegrationException If the object cannot be serialized.
	 */
	public static <OriginalType> String serialize(
			final ObjectMapper objectMapper,
			final OriginalType object,
			final Class<?> view,
			final Boolean resumeOnErrors) throws IntegrationException {
		// Tries to serialize the given object into a target type.
		try {
			return objectMapper.writerWithView(view).writeValueAsString(object);
		}
		// If there is a problem serializing the object.
		catch (final Exception exception) {
			// If errors should be silently ignored.
			if (resumeOnErrors) {
				// Logs and returns null.
				ObjectMapperHelper.LOGGER.debug("Error silently ignored: object '" + object + "' could not be serialized with view '" + view + "'.", exception);
				return null;
			}
			// If errors should not be silently ignored.
			else {
				// Throws a serialization exception.
				throw new IntegrationException(new SimpleMessage("serialization.error"), exception);
			}
		}
	}

	/**
	 * Converts a given object into a target type.
	 *
	 * @param  <TargetType>         The target object type.
	 * @param  objectMapper         The object mapper to use during conversion.
	 * @param  object               The original object to be converted.
	 * @param  objectType           The target object type.
	 * @param  resumeOnErrors       If errors should be silently ignored.
	 * @return                      The converted object.
	 * @throws IntegrationException If the object cannot be converted.
	 */
	public static <TargetType> TargetType convert(
			final ObjectMapper objectMapper,
			final Object object,
			final Class<TargetType> objectType,
			final Boolean resumeOnErrors) throws IntegrationException {
		// Tries to convert the given object into a target type.
		try {
			return objectMapper.convertValue(object, objectType);
		}
		// If there is a problem converting the object.
		catch (final Exception exception) {
			// If errors should be silently ignored.
			if (resumeOnErrors) {
				// Logs and returns null.
				ObjectMapperHelper.LOGGER
						.debug("Error silentely ignored: object '" + object + "' could not be converted into target class '" + objectType + "'.", exception);
				return null;
			}
			// If errors should not be silently ignored.
			else {
				// Throws a conversion exception.
				throw new IntegrationException(new SimpleMessage("conversion.error"), exception);
			}
		}
	}

	/**
	 * Converts a given object into a target type.
	 *
	 * @param  <TargetType>         The target object type.
	 * @param  objectMapper         The object mapper to use during conversion.
	 * @param  object               The original object to be converted.
	 * @param  objectType           The target object type.
	 * @param  resumeOnErrors       If errors should be silently ignored.
	 * @return                      The converted object.
	 * @throws IntegrationException If the object cannot be converted.
	 */
	public static <TargetType> TargetType convert(
			final ObjectMapper objectMapper,
			final Object object,
			final TypeReference<TargetType> objectType,
			final Boolean resumeOnErrors) throws IntegrationException {
		// Tries to convert the given object into a target type.
		try {
			return objectMapper.convertValue(object, objectType);
		}
		// If there is a problem converting the object.
		catch (final Exception exception) {
			// If errors should be silently ignored.
			if (resumeOnErrors) {
				// Logs and returns null.
				ObjectMapperHelper.LOGGER
						.debug("Error silentely ignored: object '" + object + "' could not be converted into target class '" + objectType + "'.", exception);
				return null;
			}
			// If errors should not be silently ignored.
			else {
				// Throws a conversion exception.
				throw new IntegrationException(new SimpleMessage("conversion.error"), exception);
			}
		}
	}

	/**
	 *
	 * @param  <TargetType>   Target type.
	 * @param  objectMapper   Object mapper.
	 * @param  object         Object.
	 * @param  view           View.
	 * @param  objectType     Object type.
	 * @param  resumeOnErrors If errors should be ignored.
	 * @return                The cloned object.
	 */
	public static <TargetType> TargetType deepClone(
			final ObjectMapper objectMapper,
			final Object object,
			final Class<?> view,
			final TypeReference<TargetType> objectType,
			final Boolean resumeOnErrors) {
		return ObjectMapperHelper.deserialize(objectMapper, ObjectMapperHelper.serialize(objectMapper, object, view, resumeOnErrors), objectType,
				resumeOnErrors);
	}

}
