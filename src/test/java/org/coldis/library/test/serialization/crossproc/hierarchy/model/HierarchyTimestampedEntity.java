package org.coldis.library.test.serialization.crossproc.hierarchy.model;

import org.coldis.library.dto.DtoType;
import org.coldis.library.model.AbstractTimestampable;

/**
 * Abstract entity intermediate. Mirrors service-log's
 * {@code AbstractTimestampableEntity}: extends the shared library POJO
 * {@link AbstractTimestampable} and declares the same hand-written class as
 * its DTO via {@link DtoType#dtoClass()}, so the generator does not emit a
 * parallel DTO and downstream child DTOs {@code extends AbstractTimestampable}
 * up the chain.
 */
@DtoType(
		namespace = "org.coldis.library.test.serialization.crossproc.hierarchy.dto",
		dtoClass = AbstractTimestampable.class
)
public abstract class HierarchyTimestampedEntity extends AbstractTimestampable {

	private static final long serialVersionUID = 1L;

}
