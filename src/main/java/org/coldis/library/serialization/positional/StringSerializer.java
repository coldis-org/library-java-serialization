package org.coldis.library.serialization.positional;

import java.lang.reflect.Method;

import org.coldis.library.exception.IntegrationException;
import org.coldis.library.model.SimpleMessage;

/**
 * Single string factory method/constructor (de)serializer.
 *
 * @param <Type> Type to be (de)serialized.
 */
public class StringSerializer<Type> implements PositionalSerializerInterface<Type>, PostionalDeserializerInterface<Type> {

	/**
	 * Type being (de)serialized.
	 */
	private Class<? extends Type> type;

	/**
	 * Factory method to be used. If no method is defined, constructor is used.
	 */
	private Method factoryMethod;

	/**
	 * Simple constructor (only valid for string objects).
	 */
	@SuppressWarnings("unchecked")
	public StringSerializer() {
		this.type = ((Class<? extends Type>) String.class);
	}

	/**
	 * Factory method constructor.
	 *
	 * @param type          Object type.
	 * @param factoryMethod Factory method to be used. If no method is defined,
	 *                      constructor is used.
	 */
	public StringSerializer(final Class<? extends Type> type, final Method factoryMethod) {
		this.type = type;
		this.factoryMethod = factoryMethod;
	}

	/**
	 * Factory method constructor (from single string parameter).
	 *
	 * @param typeAndMethod Object type name and factory method split by ",".
	 */
	@SuppressWarnings("unchecked")
	public StringSerializer(final String typeAndMethod) {
		// Tries to set the object type and factory method for the given parameters.
		try {
			// Splits the type and method names.
			final String[] typeAndMethodNames = typeAndMethod.split(",");
			// Finds the type by the given name.
			this.type = (Class<? extends Type>) Class.forName(typeAndMethodNames[0]);
			// Tries to get the method for the name (if a name is given).
			this.factoryMethod = typeAndMethodNames.length > 1
					? this.type.getMethod(typeAndMethodNames[1], String.class)
					: null;
		}
		// If the parameters are not valid.
		catch (final Exception exception) {
			// Throws an exception.
			throw new IntegrationException(new SimpleMessage("serialization.config.nvalid"), exception);
		}
	}

	/**
	 * @see org.coldis.library.serialization.positional.PostionalDeserializerInterface#deserialize(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Type deserialize(final String serializedObject) {
		// Tries to de-serialize the object.
		try {
			// De-serialized object (initially null).
			Type deserializedObject = null;
			// If no factory method is defined.
			if (factoryMethod == null) {
				// Uses a single string constructor do de-serialize the object.
				deserializedObject = type.getConstructor(String.class).newInstance(serializedObject);
			}
			// If a factory method is defined.
			else {
				// Uses the factory method to de-serialize the object.
				deserializedObject = ((Type) factoryMethod.invoke(null, serializedObject));
			}
			// Returns the de-serialized object.
			return deserializedObject;
		}
		// If the object cannot be de-serialized.
		catch (final Exception exception) {
			// Throws an exception.
			throw new IntegrationException(new SimpleMessage("deserialization.error"), exception);
		}
	}

	/**
	 * @see org.coldis.library.serialization.positional.PositionalSerializerInterface#serialize(java.lang.Object)
	 */
	@Override
	public String serialize(final Type object) {
		return object == null ? "" : object.toString();
	}

}
