package org.coldis.library.test.serialization.conflict;

import org.coldis.library.model.Typable;

/**
 * Pairs with SharedNameAlias to reproduce the typeName-collision case
 * (two unrelated classes reporting the same Typable.getTypeName()) so
 * the helper can be exercised with a fixture that shouldn't exist in
 * well-formed application code, but does in practice.
 */
public class SharedNameOwner implements Typable {

	private static final long serialVersionUID = 1L;

	public static final String SHARED_TYPE_NAME = "coldis.test.conflict.SharedName";

	private Long value;

	public Long getValue() {
		return this.value;
	}

	public void setValue(
			final Long value) {
		this.value = value;
	}

	@Override
	public String getTypeName() {
		return SharedNameOwner.SHARED_TYPE_NAME;
	}

}
