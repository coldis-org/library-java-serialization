package org.coldis.library.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {@link ObjectCloner} backed by a Jackson {@link ObjectMapper}, delegating
 * to {@link ObjectMapperHelper#deepClone(ObjectMapper, Object, Class, Class, Boolean)}.
 */
public class ObjectMapperCloner implements ObjectCloner {

	/**
	 * Object mapper.
	 */
	private final ObjectMapper objectMapper;

	/**
	 * Constructor.
	 *
	 * @param objectMapper Object mapper.
	 */
	public ObjectMapperCloner(final ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
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
		return (ObjectType) ObjectMapperHelper.deepClone(this.objectMapper, object, null, object.getClass(), false);
	}

}
