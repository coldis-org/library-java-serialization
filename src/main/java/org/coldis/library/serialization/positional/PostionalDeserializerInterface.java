package org.coldis.library.serialization.positional;

/**
 * De-serializer.
 * 
 * @param <Type> Type being de-serialized.
 */
public interface PostionalDeserializerInterface<Type> {

	/**
	 * De-serializes a string into an object.
	 *
	 * @param serializedObject Serialized object.
	 * @return The de-serialized object.
	 */
	Type deserialize(String serializedObject);

}
