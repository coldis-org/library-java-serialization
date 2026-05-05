package org.coldis.library.serialization;

/**
 * Strategy for deep-copying an object. Implementations may delegate to a
 * binary serializer (e.g. Fory), a JSON round-trip (e.g. Jackson), or any
 * other mechanism capable of producing an independent copy of the input.
 */
@FunctionalInterface
public interface ObjectCloner {

	/**
	 * Returns an independent deep copy of the given object.
	 *
	 * @param  <ObjectType> Object type.
	 * @param  object       Object to clone (may be {@code null}).
	 * @return              An independent copy of the input.
	 * @throws Exception    If the object cannot be cloned by this strategy.
	 */
	<ObjectType> ObjectType clone(
			ObjectType object) throws Exception;

}
