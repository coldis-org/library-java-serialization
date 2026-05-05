package org.coldis.library.serialization;

import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;

/**
 * {@link ObjectCloner} backed by standard Java serialization
 * ({@link SerializationUtils#clone(Serializable)}). Only handles objects
 * implementing {@link Serializable}; throws otherwise so the surrounding
 * {@link ObjectClonerHelper} can fall through to the next strategy.
 */
public class JavaSerializationCloner implements ObjectCloner {

	/**
	 * @see ObjectCloner#clone(Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <ObjectType> ObjectType clone(
			final ObjectType object) throws Exception {
		if (object == null) {
			return null;
		}
		if (!(object instanceof Serializable)) {
			throw new IllegalArgumentException("Object of type " + object.getClass().getName() + " is not Serializable.");
		}
		return (ObjectType) SerializationUtils.clone((Serializable) object);
	}

}
