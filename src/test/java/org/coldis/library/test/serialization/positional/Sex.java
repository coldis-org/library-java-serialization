package org.coldis.library.test.serialization.positional;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Sex.
 */
public enum Sex {

	MALE("M"),

	FEMALE("F"),

	;

	/**
	 * Code.
	 */
	private String code;

	/**
	 * Default constructor.
	 *
	 * @param code Code.
	 */
	Sex(final String code) {
		this.code = code;
	}

	/**
	 * Gets the code.
	 *
	 * @return The code.
	 */
	@JsonValue
	public String getCode() {
		return this.code;
	}

}
