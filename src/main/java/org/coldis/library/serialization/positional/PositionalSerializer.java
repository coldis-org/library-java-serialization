package org.coldis.library.serialization.positional;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.coldis.library.exception.IntegrationException;
import org.coldis.library.helper.StringHelper;
import org.coldis.library.model.SimpleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple positional (de)serializer.
 *
 * @param <Type> Type being serialized.
 */
public class PositionalSerializer<Type> implements PositionalSerializerInterface<Type>, PostionalDeserializerInterface<Type> {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PositionalSerializer.class);

	/**
	 * Default filler.
	 */
	public static final char DEFAULT_FILLER = ' ';

	/**
	 * Class of the object to be serialized.
	 */
	private final Class<? extends Type> objectClass;

	/**
	 * Default filler to be used between and inside fields.
	 */
	private char filler = PositionalSerializer.DEFAULT_FILLER;

	/**
	 * If serialization/de-serialization should continue on field errors.
	 */
	private Boolean resumeOnFieldErrors = true;

	/**
	 * Simple constructor. With {@link #DEFAULT_FILLER} and resume on field error.
	 *
	 * @param objectClass Class of the object to be serialized.
	 */
	public PositionalSerializer(final Class<? extends Type> objectClass) {
		// If the object class is not given.
		if (objectClass == null) {
			// Throws a new integration exception.
			throw new IntegrationException(new SimpleMessage("object.class.required"));
		}
		// Sets the object class.
		this.objectClass = objectClass;
	}

	/**
	 * Default constructor.
	 *
	 * @param objectClass         Class of the object to be serialized.
	 * @param filler              Default filler to be used between and inside
	 *                                fields.
	 * @param resumeOnFieldErrors If serialization/de-serialization should continue
	 *                                on field errors.
	 */
	public PositionalSerializer(final Class<? extends Type> objectClass, final char filler, final Boolean resumeOnFieldErrors) {
		this.objectClass = objectClass;
		this.filler = filler;
		this.resumeOnFieldErrors = resumeOnFieldErrors;
	}

	/**
	 * Simple constructor. With {@link PositionalAttribute#EMPTY_FILLER} and resume
	 * on field error.
	 *
	 * @param objectClassFillerAndResumeOnErrors Parameters string (object class
	 *                                               name, filler, and resume on
	 *                                               field errors).
	 */
	@SuppressWarnings("unchecked")
	public PositionalSerializer(final String objectClassFillerAndResumeOnErrors) {
		// Tries to set the object type for the given class name.
		try {
			// Splits the arguments.
			final String[] params = objectClassFillerAndResumeOnErrors.split("/");
			// Sets the parameters values.
			this.objectClass = (Class<? extends Type>) Class.forName(params[0]);
			// If there is a second argument.
			if ((params.length > 1) && (params[1] != null) && !params[1].isEmpty()) {
				// Sets the filler.
				this.filler = params[1].charAt(0);
			}
			// If there is a third argument.
			if ((params.length > 2) && (params[2] != null) && !params[2].isEmpty()) {
				// Sets the resume on error property.
				this.resumeOnFieldErrors = Boolean.parseBoolean(params[2]);
			}
		}
		// If no type can be resolved for the name.
		catch (final Exception exception) {
			// Throws an exception.
			throw new IntegrationException(new SimpleMessage("serialization.type.invalid"), exception);
		}
	}

	/**
	 * Positional getter comparator.
	 */
	private class PositionalGetterComparator implements Comparator<Method> {

		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(
				final Method getter1,
				final Method getter2) {
			// Gets the positional annotation from the methods.
			final PositionalAttribute getter1PosInfo = getter1.getAnnotation(PositionalAttribute.class);
			final PositionalAttribute getter2PosInfo = getter2.getAnnotation(PositionalAttribute.class);
			// If the positional content is not present in either methods.
			if ((getter1PosInfo == null) || (getter2PosInfo == null)) {
				// Throws an exception.
				throw new IntegrationException(new SimpleMessage("method.positional.info.required"));
			}
			// Returns the index of the first field minus the index of the second field.
			return getter1PosInfo.initialPosition() - getter2PosInfo.initialPosition();
		}

	}

	/**
	 * Gets the ordered getters list for the object class.
	 *
	 * @param  objectClass      Object class.
	 * @param  methodComparator Method comparator.
	 * @param  annotationType   Annotation type.
	 * @param  <AnnotationT>    Annotation type.
	 * @return                  The ordered getters list for the object class.
	 */
	public static <AnnotationT extends Annotation> List<Method> getOrderedGetters(
			final Class<?> objectClass,
			final Comparator<Method> methodComparator,
			final Class<AnnotationT> annotationType) {
		// Positional (fields) getters list.
		final List<Method> positionalGettersList = new ArrayList<>();
		// For each public method.
		for (final Method method : objectClass.getMethods()) {
			// If the method is a getter or setter.
			if (method.getName().startsWith("get") || method.getName().startsWith("set")) {
				// If the method has positional content.
				final AnnotationT fieldContentInfo = method.getAnnotation(annotationType);
				if (fieldContentInfo != null) {
					// Gets the field name from the method.
					final String fieldName = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
					// If the field has not been added yet.
					if (positionalGettersList.stream().noneMatch(
							currentMethod -> fieldName.equals(currentMethod.getName().substring(3, 4).toLowerCase() + currentMethod.getName().substring(4)))) {
						// Adds the getter to the list.
						positionalGettersList.add(method);
					}
				}
			}
		}
		// Sorts the positional fields (getters).
		Collections.sort(positionalGettersList, methodComparator);
		// Returns the getters.
		return positionalGettersList;
	}

	/**
	 * @see org.coldis.library.serialization.positional.PostionalDeserializerInterface#deserialize(java.lang.String)
	 */
	@Override
	public Type deserialize(
			final String serializedObject) {
		// Tries to de-serialize the object.
		try {
			// Tries to create a simple instance of the object.
			final Type deserializedObject = this.objectClass.getConstructor().newInstance();
			// Gets the ordered getters list.
			final List<Method> positionalGettersList = PositionalSerializer.getOrderedGetters(this.objectClass, new PositionalGetterComparator(),
					PositionalAttribute.class);
			// For each positional (field) getter.
			for (final Method getter : positionalGettersList) {
				// If the method has positional content.
				final PositionalAttribute fieldContentInfo = getter.getAnnotation(PositionalAttribute.class);
				// Gets the field and setter name from the method.
				final String fieldName = getter.getName().substring(3, 4).toLowerCase() + getter.getName().substring(4);
				final String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				// Tries to de-serializes the field.
				try {
					// Creates a new instance for the field de-serializer.
					final PostionalDeserializerInterface<?> deserializer = StringUtils.isEmpty(fieldContentInfo.deserializerInitParam())
							? fieldContentInfo.postionalDeserializerInterface().getConstructor().newInstance()
							: fieldContentInfo.postionalDeserializerInterface().getConstructor(String.class)
									.newInstance(fieldContentInfo.deserializerInitParam());
					// Gets the field setter.
					final Method setter = MethodUtils.getMatchingMethod(this.objectClass, setterName, getter.getReturnType());
					// Gets the serialized field value.
					String serializedField = serializedObject.substring(fieldContentInfo.initialPosition(), fieldContentInfo.finalPosition());
					// Gets the field filler.
					final char fieldFiller = PositionalAttribute.NULL_FILLER_HOLDER == fieldContentInfo.filler() ? this.filler : fieldContentInfo.filler();
					// If fillers should be removed on de-serialization.
					if (fieldContentInfo.unfill()) {
						// If the field is filled to the left.
						if (fieldContentInfo.fillLeft()) {
							// Removes the fillers from the left of the serialized field.
							serializedField = serializedField.replaceAll("\\A" + fieldFiller + "*", "");
						}
						// If the field is filled to the right.
						else {
							// Removes the fillers from the right of the serialized field.
							serializedField = serializedField.replaceAll(fieldFiller + "*\\z", "");
						}
					}
					// Sets the default value if the field serialized value is empty.
					serializedField = StringUtils.isEmpty(serializedField) ? fieldContentInfo.defaultValue() : serializedField;
					// De-serializes the field content.
					final Object deserializedField = deserializer.deserialize(serializedField);
					// Sets the de-serialized field value.
					setter.invoke(deserializedObject, deserializedField);
				}
				// If the field cannot be de-serialized.
				catch (final Exception exception) {
					// If the de-serialization must continue on field errors.
					if (this.resumeOnFieldErrors) {
						// Logs it.
						PositionalSerializer.LOGGER.debug("Suppressed: field '" + fieldName + "' could not be deserialized.", exception);
					}
					// If the de-serialization must not continue on field errors.
					else {
						// Throws an integration exception.
						throw new IntegrationException(new SimpleMessage("object.field.deserialization.error"), exception);
					}
				}
			}
			// Returns the de-serialized object.
			return deserializedObject;
		}
		// If the object cannot be de-serialized.
		catch (final Exception exception) {
			// Throws an integration exception.
			throw new IntegrationException(new SimpleMessage("object.deserialization.error"), exception);
		}
	}

	/**
	 * @see org.coldis.library.serialization.positional.PositionalSerializerInterface#serialize(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String serialize(
			final Type object) {
		// Serialized object.
		final StringBuffer serializedObject = new StringBuffer();
		// Gets the ordered getters list.
		final List<Method> positionalGettersList = PositionalSerializer.getOrderedGetters(this.objectClass, new PositionalGetterComparator(),
				PositionalAttribute.class);
		// For each positional (field) getter.
		for (final Method method : positionalGettersList) {
			// Gets the field and getter name from the method.
			final String fieldName = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
			final String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			// If the method has positional content.
			final PositionalAttribute fieldContentInfo = method.getAnnotation(PositionalAttribute.class);
			if (fieldContentInfo != null) {
				// While the current field initial position is greater that the current
				// serialized object.
				while (serializedObject.length() < fieldContentInfo.initialPosition()) {
					// Appends the filler to the serialized object.
					serializedObject.append(this.filler);
				}
				// Serialized field.
				StringBuffer serializedFieldValue = new StringBuffer();
				// Tries to serializes the field.
				try {
					// Creates a new instance for the field serializer.
					final PositionalSerializerInterface<Object> serializer = StringUtils.isEmpty(fieldContentInfo.serializerInitParam())
							? fieldContentInfo.positionalSerializerInterface().getConstructor().newInstance()
							: fieldContentInfo.positionalSerializerInterface().getConstructor(String.class).newInstance(fieldContentInfo.serializerInitParam());
					// Gets the field getter.
					final Method getter = MethodUtils.getMatchingMethod(this.objectClass, getterName);
					// Serializes the field content.
					final Object fieldValue = getter.invoke(object);
					serializedFieldValue = new StringBuffer(serializer.serialize(fieldValue));
					// Truncate the field for the correct maximum size.
					serializedFieldValue = new StringBuffer(
							StringHelper.truncate(serializedFieldValue, fieldContentInfo.finalPosition() - fieldContentInfo.initialPosition(), ""));
				}
				// If the field cannot be serialized.
				catch (final Exception exception) {
					// If the serialization must continue on field errors.
					if (this.resumeOnFieldErrors) {
						// Logs it.
						PositionalSerializer.LOGGER.debug("Suppressed: field '" + fieldName + "' could not be serialized.", exception);
					}
					// If the serialization must not continue on field errors.
					else {
						// Throws an integration exception.
						throw new IntegrationException(new SimpleMessage("object.field.serialization.error"), exception);
					}
				}
				// Gets the field filler.
				final char fieldFiller = PositionalAttribute.NULL_FILLER_HOLDER == fieldContentInfo.filler() ? this.filler : fieldContentInfo.filler();
				// While the current field length is smaller than defined.
				while (serializedFieldValue.length() < (fieldContentInfo.finalPosition() - fieldContentInfo.initialPosition())) {
					// If the field should be filled to the right.
					if (!fieldContentInfo.fillLeft()) {
						// Appends the filler to the serialized field.
						serializedFieldValue.append(fieldFiller);
					}
					// If the field should be filled to the else.
					else {
						// Appends the filler to the serialized field.
						serializedFieldValue = new StringBuffer().append(fieldFiller).append(serializedFieldValue);
					}
				}
				// Appends the serialized field to the serialized object.
				serializedObject.append(serializedFieldValue);
			}
		}
		// Returns the serialized object.
		return serializedObject.toString();
	}

}
