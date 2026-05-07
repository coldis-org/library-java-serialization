package org.coldis.library.test.serialization.crossproc.hierarchy.model;

import java.util.Objects;

import org.coldis.library.dto.DtoType;

/**
 * Abstract Model intermediate. Mirrors service-log's {@code AbstractLog}:
 * extends an entity that declares {@link org.coldis.library.model.AbstractTimestampable}
 * as its DTO, and is itself the source for a generated abstract DTO.
 */
@DtoType(
		targetPath = "src/test/java",
		namespace = "org.coldis.library.test.serialization.crossproc.hierarchy.dto"
)
public abstract class HierarchyAbstractLog extends HierarchyTimestampedEntity {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String message;

	private HierarchyLogType type;

	public Long getId() {
		return this.id;
	}

	public void setId(
			final Long id) {
		this.id = id;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(
			final String message) {
		this.message = message;
	}

	public HierarchyLogType getType() {
		return this.type;
	}

	public void setType(
			final HierarchyLogType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.id, this.message, this.type);
	}

	@Override
	public boolean equals(
			final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof HierarchyAbstractLog) || !super.equals(obj)) {
			return false;
		}
		final HierarchyAbstractLog other = (HierarchyAbstractLog) obj;
		return Objects.equals(this.id, other.id) && Objects.equals(this.message, other.message) && (this.type == other.type);
	}

}
