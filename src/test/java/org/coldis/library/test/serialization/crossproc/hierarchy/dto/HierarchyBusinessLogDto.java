package org.coldis.library.test.serialization.crossproc.hierarchy.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Arrays;
import org.coldis.library.dto.DtoOrigin;

/**
 * HierarchyBusinessLogDto.
 */
@DtoOrigin(originalClassName = "org.coldis.library.test.serialization.crossproc.hierarchy.model.HierarchyBusinessLog")
public class HierarchyBusinessLogDto extends org.coldis.library.test.serialization.crossproc.hierarchy.dto.HierarchyAbstractLogDto {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 489555280L;
	
	/**
	 * businessKey.
	 */
	private java.lang.String businessKey;

	/**
	 * typeName.
	 */
	private final java.lang.String typeName = "coldis.test.hierarchy.BusinessLog";


	/**
	 * No arguments constructor.
	 */
	public HierarchyBusinessLogDto() {
		super();
	}

	/**
	 * Gets the businessKey.
	 * @return The businessKey.
	 */
	
	public java.lang.String getBusinessKey() {
		return  businessKey ;
	}
	
	/**
	 * Sets the businessKey.
	 *
	 * @param businessKey
	 *            The businessKey.
	 */
	public void setBusinessKey(final java.lang.String businessKey) {
		this.businessKey = businessKey;
	}
	
	/**
	 * Sets the businessKey and returns the updated object.
	 *
	 * @param businessKey
	 *            The businessKey.
	 * @return The updated object.
	 */
	public HierarchyBusinessLogDto withBusinessKey(final java.lang.String businessKey) {
		this.setBusinessKey(businessKey);
		return this;
	}
	/**
	 * Gets the typeName.
	 * @return The typeName.
	 */
	
	public java.lang.String getTypeName() {
		return  typeName ;
	}
	

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(
businessKey

,
typeName



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
		final HierarchyBusinessLogDto other = (HierarchyBusinessLogDto) obj;
		if (! Objects.equals(businessKey, other.businessKey)) {
			return false;
		}
		if (! Objects.equals(typeName, other.typeName)) {
			return false;
		}
		return true;
	}
	
}