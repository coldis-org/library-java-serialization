package org.coldis.library.test.serialization.positional;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Civil status.
 */
public enum CivilStatus {

	SINGLE("S"),

	MARRIED("C"),

	SEPARATED("E"),

	DIVORCED("D"),

	CIVIL_UNION("U"),

	WIDOWED("V"),

	OTHER("O"),

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
	CivilStatus(final String code) {
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
