package org.coldis.library.test.serialization.csv;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Test object.
 */
@JsonTypeName(value = "org.coldis.library.test.serialization.csv.TestObj")
public class TestObj {

	/**
	 * Test variable.
	 */
	private BigDecimal test1;

	/**
	 * Test variable.
	 */
	private Integer test2;

	/**
	 * Test variable.
	 */
	private BigDecimal test3;

	/**
	 * No arguments constructor.
	 */
	public TestObj() {
	}

	/**
	 * Default constructor.
	 *
	 * @param test1 Test.
	 * @param test2 Test.
	 * @param test3 Test.
	 */
	public TestObj(final BigDecimal test1, final Integer test2, final BigDecimal test3) {
		super();
		this.test1 = test1;
		this.test2 = test2;
		this.test3 = test3;
	}

	/**
	 * Gets the test1.
	 *
	 * @return The test1.
	 */
	public BigDecimal getTest1() {
		return this.test1;
	}

	/**
	 * Sets the test1.
	 *
	 * @param test1 New test1.
	 */
	public void setTest1(final BigDecimal test1) {
		this.test1 = test1;
	}

	/**
	 * Gets the test2.
	 *
	 * @return The test2.
	 */
	public Integer getTest2() {
		return this.test2;
	}

	/**
	 * Sets the test2.
	 *
	 * @param test2 New test2.
	 */
	public void setTest2(final Integer test2) {
		this.test2 = test2;
	}

	/**
	 * Gets the test3.
	 *
	 * @return The test3.
	 */
	public BigDecimal getTest3() {
		return this.test3;
	}

	/**
	 * Sets the test3.
	 *
	 * @param test3 New test3.
	 */
	public void setTest3(final BigDecimal test3) {
		this.test3 = test3;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.test1, this.test2, this.test3);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TestObj)) {
			return false;
		}
		final TestObj other = (TestObj) obj;
		return Objects.equals(this.test1, other.test1) && Objects.equals(this.test2, other.test2)
				&& Objects.equals(this.test3, other.test3);
	}

}
