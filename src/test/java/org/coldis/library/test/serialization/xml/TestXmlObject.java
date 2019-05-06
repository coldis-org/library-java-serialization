package org.coldis.library.test.serialization.xml;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Test XML object.
 */
@XmlRootElement
public class TestXmlObject {

	/**
	 * Test field.
	 */
	private LocalDate date1;

	/**
	 * Test field.
	 */
	private LocalDate date2;

	/**
	 * Test field.
	 */
	private LocalDateTime dateTime1;

	/**
	 * Test field.
	 */
	private LocalDateTime dateTime2;

	/**
	 * No arguments constructor.
	 */
	public TestXmlObject() {
		super();
	}

	/**
	 * Default constructor.
	 *
	 * @param date1     Test attribute.
	 * @param date2     Test attribute.
	 * @param dateTime1 Test attribute.
	 * @param dateTime2 Test attribute.
	 */
	public TestXmlObject(final LocalDate date1, final LocalDate date2, final LocalDateTime dateTime1,
			final LocalDateTime dateTime2) {
		super();
		this.date1 = date1;
		this.date2 = date2;
		this.dateTime1 = dateTime1;
		this.dateTime2 = dateTime2;
	}

	/**
	 * Gets the date1.
	 *
	 * @return The date1.
	 */
	@XmlJavaTypeAdapter(value = LocalDateSerializer1.class)
	public LocalDate getDate1() {
		return this.date1;
	}

	/**
	 * Sets the date1.
	 *
	 * @param date1 New date1.
	 */
	public void setDate1(final LocalDate date1) {
		this.date1 = date1;
	}

	/**
	 * Gets the date2.
	 *
	 * @return The date2.
	 */
	@XmlJavaTypeAdapter(value = LocalDateSerializer2.class)
	public LocalDate getDate2() {
		return this.date2;
	}

	/**
	 * Sets the date2.
	 *
	 * @param date2 New date2.
	 */
	public void setDate2(final LocalDate date2) {
		this.date2 = date2;
	}

	/**
	 * Gets the dateTime1.
	 *
	 * @return The dateTime1.
	 */
	@XmlJavaTypeAdapter(value = LocalDateTimeSerializer1.class)
	public LocalDateTime getDateTime1() {
		return this.dateTime1;
	}

	/**
	 * Sets the dateTime1.
	 *
	 * @param dateTime1 New dateTime1.
	 */
	public void setDateTime1(final LocalDateTime dateTime1) {
		this.dateTime1 = dateTime1;
	}

	/**
	 * Gets the dateTime2.
	 *
	 * @return The dateTime2.
	 */
	@XmlJavaTypeAdapter(value = LocalDateTimeSerializer2.class)
	public LocalDateTime getDateTime2() {
		return this.dateTime2;
	}

	/**
	 * Sets the dateTime2.
	 *
	 * @param dateTime2 New dateTime2.
	 */
	public void setDateTime2(final LocalDateTime dateTime2) {
		this.dateTime2 = dateTime2;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.date1, this.date2, this.dateTime1, this.dateTime2);
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
		if (!(obj instanceof TestXmlObject)) {
			return false;
		}
		final TestXmlObject other = (TestXmlObject) obj;
		return Objects.equals(this.date1, other.date1) && Objects.equals(this.date2, other.date2)
				&& Objects.equals(this.dateTime1, other.dateTime1) && Objects.equals(this.dateTime2, other.dateTime2);
	}

}
