package org.coldis.library.test.serialization.crossproc.hierarchy.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Arrays;
import org.coldis.library.dto.DtoOrigin;

/**
 * HierarchyAbstractLogDto.
 */
@DtoOrigin(originalClassName = "org.coldis.library.test.serialization.crossproc.hierarchy.model.HierarchyAbstractLog")
public class HierarchyAbstractLogDto extends org.coldis.library.model.AbstractTimestampable {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 609346610L;
	
	/**
	 * id.
	 */
	private java.lang.Long id;

	/**
	 * message.
	 */
	private java.lang.String message;

	/**
	 * type.
	 */
	private org.coldis.library.test.serialization.crossproc.hierarchy.model.HierarchyLogType type;


	/**
	 * No arguments constructor.
	 */
	public HierarchyAbstractLogDto() {
		super();
	}

	/**
	 * Gets the id.
	 * @return The id.
	 */
	
	public java.lang.Long getId() {
		return  id ;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id
	 *            The id.
	 */
	public void setId(final java.lang.Long id) {
		this.id = id;
	}
	
	/**
	 * Sets the id and returns the updated object.
	 *
	 * @param id
	 *            The id.
	 * @return The updated object.
	 */
	public HierarchyAbstractLogDto withId(final java.lang.Long id) {
		this.setId(id);
		return this;
	}
	/**
	 * Gets the message.
	 * @return The message.
	 */
	
	public java.lang.String getMessage() {
		return  message ;
	}
	
	/**
	 * Sets the message.
	 *
	 * @param message
	 *            The message.
	 */
	public void setMessage(final java.lang.String message) {
		this.message = message;
	}
	
	/**
	 * Sets the message and returns the updated object.
	 *
	 * @param message
	 *            The message.
	 * @return The updated object.
	 */
	public HierarchyAbstractLogDto withMessage(final java.lang.String message) {
		this.setMessage(message);
		return this;
	}
	/**
	 * Gets the type.
	 * @return The type.
	 */
	
	public org.coldis.library.test.serialization.crossproc.hierarchy.model.HierarchyLogType getType() {
		return  type ;
	}
	
	/**
	 * Sets the type.
	 *
	 * @param type
	 *            The type.
	 */
	public void setType(final org.coldis.library.test.serialization.crossproc.hierarchy.model.HierarchyLogType type) {
		this.type = type;
	}
	
	/**
	 * Sets the type and returns the updated object.
	 *
	 * @param type
	 *            The type.
	 * @return The updated object.
	 */
	public HierarchyAbstractLogDto withType(final org.coldis.library.test.serialization.crossproc.hierarchy.model.HierarchyLogType type) {
		this.setType(type);
		return this;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(
id

,
message

,
type



			);
		return result;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		final HierarchyAbstractLogDto other = (HierarchyAbstractLogDto) obj;
		if (! Objects.equals(id, other.id)) {
			return false;
		}
		if (! Objects.equals(message, other.message)) {
			return false;
		}
		if (! Objects.equals(type, other.type)) {
			return false;
		}
		return true;
	}
	
}