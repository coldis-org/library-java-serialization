package org.coldis.library.serialization.positional;

/**
 * PositionalSerializerInterface.
 * 
 * @param <Type> Type being serialized.
 */
public interface PositionalSerializerInterface<Type> {

	/**
	 * Serializes an object into a string.
	 *
	 * @param object Object to be serialized.
	 * @return The serialized object.
	 */
	String serialize(Type object);

}
