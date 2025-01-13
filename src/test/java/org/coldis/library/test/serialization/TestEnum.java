package org.coldis.library.test.serialization;

import org.coldis.library.helper.EnumHelper;
import org.coldis.library.model.Identifiable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Test enum.
 */
public enum TestEnum implements Identifiable {

	/** ABC. */
	ABC(1L),

	/** DEF. */
	DEF(2L);

	/** Identifier. */
	private final Long id;

	/**
	 * Default constructor.
	 *
	 * @param id Device kind identifier.
	 */
	TestEnum(final Long id) {
		this.id = id;
	}

	/**
	 * @see org.coldis.library.model.Identifiable#getId()
	 */
	@Override
	@JsonValue
	public Long getId() {
		return this.id;
	}

	/**
	 * Gets the value of the enum from a given identifier.
	 *
	 * @param  id Id for the enum.
	 * @return    The value of the enum from a given identifier.
	 */
	@JsonCreator
	public static TestEnum fromId(
			final Long id) {
		return EnumHelper.getById(TestEnum.class, id);
	}

}
