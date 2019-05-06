package org.coldis.library.serialization;

import org.coldis.library.exception.IntegrationException;
import org.coldis.library.model.SimpleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON helper.
 */
public class ObjectMapperHelper {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectMapperHelper.class);

	/**
	 * Adds the subtypes found from in a package to the object mapper.
	 *
	 * @param  objectMapper  Object mapper.
	 * @param  packagesNames The packages to find the subtypes within.
	 * @return               The configured object mapper.
	 */
	public static ObjectMapper addSubtypesFromPackage(final ObjectMapper objectMapper, final String... packagesNames) {
		// Creates a scanner to find class with type name information.
		final ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
				false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(JsonTypeName.class));
		// For each package and found type.
		if (packagesNames != null) {
			for (final String packageName : packagesNames) {
				for (final BeanDefinition currentCsvType : scanner.findCandidateComponents(packageName)) {
					// Tries to register it as an object mapper subtype.
					try {
						objectMapper.registerSubtypes(Class.forName(currentCsvType.getBeanClassName()));
					}
					// If the class cannot be found.
					catch (final Exception exception) {
						// Logs it.
						ObjectMapperHelper.LOGGER.error("Class '" + currentCsvType.getBeanClassName()
						+ "' could not be registered as a subtype.", exception);
					}
				}
			}
		}
		// Returns the configured object mapper.
		return objectMapper;
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
	public static <TargetType> TargetType deserialize(final ObjectMapper objectMapper, final String object,
			final Class<TargetType> objectType, final Boolean resumeOnErrors) throws IntegrationException {
		// Tries to de-serialize the given object into a target type.
		try {
			return objectMapper.readValue(object, objectType);
		}
		// If there is a problem de-serializing the object.
		catch (final Exception exception) {
			// If errors should be silently ignored.
			if (resumeOnErrors) {
				// Logs and returns null.
				ObjectMapperHelper.LOGGER.debug("Error silently ignored: object '" + object
						+ "' could not be de-serialized into target class '" + objectType + "'.", exception);
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
	public static <TargetType> TargetType deserialize(final ObjectMapper objectMapper, final String object,
			final TypeReference<TargetType> objectType, final Boolean resumeOnErrors) throws IntegrationException {
		// Tries to de-serialize the given object into a target type.
		try {
			return objectMapper.readValue(object, objectType);
		}
		// If there is a problem de-serializing the object.
		catch (final Exception exception) {
			// If errors should be silently ignored.
			if (resumeOnErrors) {
				// Logs and returns null.
				ObjectMapperHelper.LOGGER.debug("Error silently ignored: object '" + object
						+ "' could not be de-serialized into target class '" + objectType + "'.", exception);
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
	public static <OriginalType> String serialize(final ObjectMapper objectMapper, final OriginalType object,
			final Class<?> view, final Boolean resumeOnErrors) throws IntegrationException {
		// Tries to serialize the given object into a target type.
		try {
			return objectMapper.writerWithView(view).writeValueAsString(object);
		}
		// If there is a problem serializing the object.
		catch (final Exception exception) {
			// If errors should be silently ignored.
			if (resumeOnErrors) {
				// Logs and returns null.
				ObjectMapperHelper.LOGGER.debug("Error silently ignored: object '" + object
						+ "' could not be serialized with view '" + view + "'.", exception);
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
	public static <TargetType> TargetType convert(final ObjectMapper objectMapper, final Object object,
			final Class<TargetType> objectType, final Boolean resumeOnErrors) throws IntegrationException {
		// Tries to convert the given object into a target type.
		try {
			return objectMapper.convertValue(object, objectType);
		}
		// If there is a problem converting the object.
		catch (final Exception exception) {
			// If errors should be silently ignored.
			if (resumeOnErrors) {
				// Logs and returns null.
				ObjectMapperHelper.LOGGER.debug("Error silentely ignored: object '" + object
						+ "' could not be converted into target class '" + objectType + "'.", exception);
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
	public static <TargetType> TargetType convert(final ObjectMapper objectMapper, final Object object,
			final TypeReference<TargetType> objectType, final Boolean resumeOnErrors) throws IntegrationException {
		// Tries to convert the given object into a target type.
		try {
			return objectMapper.convertValue(object, objectType);
		}
		// If there is a problem converting the object.
		catch (final Exception exception) {
			// If errors should be silently ignored.
			if (resumeOnErrors) {
				// Logs and returns null.
				ObjectMapperHelper.LOGGER.debug("Error silentely ignored: object '" + object
						+ "' could not be converted into target class '" + objectType + "'.", exception);
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
	 * Deep clones an object by serializing and de-serializing it.
	 *
	 * @param  <TargetType>         The target object type.
	 * @param  objectMapper         The object mapper to use during cloning.
	 * @param  object               The original object to be cloned.
	 * @param  objectType           The target object type.
	 * @param  resumeOnErrors       If errors should be silently ignored.
	 * @return                      The cloned object.
	 * @throws IntegrationException If the object cannot be cloned.
	 */
	public static <TargetType> TargetType deepClone(final ObjectMapper objectMapper, final Object object,
			final TypeReference<TargetType> objectType) throws IntegrationException {
		return ObjectMapperHelper.deserialize(objectMapper,
				ObjectMapperHelper.serialize(objectMapper, object, null, false), objectType, false);
	}

}
