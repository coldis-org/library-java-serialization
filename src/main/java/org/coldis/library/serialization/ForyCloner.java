package org.coldis.library.serialization;

import org.apache.fory.BaseFory;

/**
 * {@link ObjectCloner} backed by an Apache Fory instance.
 */
public class ForyCloner implements ObjectCloner {

	/**
	 * Fory serializer.
	 */
	private final BaseFory fory;

	/**
	 * Constructor.
	 *
	 * @param fory Fory serializer.
	 */
	public ForyCloner(final BaseFory fory) {
		this.fory = fory;
	}

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
		return (ObjectType) this.fory.copy(object);
	}

}
