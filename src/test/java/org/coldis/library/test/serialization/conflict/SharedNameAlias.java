package org.coldis.library.test.serialization.conflict;

import org.coldis.library.model.Typable;

/**
 * Reports the same getTypeName() as SharedNameOwner without being related
 * to it, simulating the bug class of two unrelated classes claiming the
 * same logical type name.
 */
public class SharedNameAlias implements Typable {

	private static final long serialVersionUID = 1L;

	private String description;

	public String getDescription() {
		return this.description;
	}

	public void setDescription(
			final String description) {
		this.description = description;
	}

	@Override
	public String getTypeName() {
		return SharedNameOwner.SHARED_TYPE_NAME;
	}

}
