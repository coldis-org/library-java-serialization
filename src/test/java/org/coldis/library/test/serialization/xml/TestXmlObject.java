package org.coldis.library.test.serialization.xml;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Test XML object.
 */
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
	 * TODO Javadoc
	 * @param date1
	 * @param date2
	 * @param dateTime1
	 * @param dateTime2 Javadoc
	 */
	public TestXmlObject(LocalDate date1, LocalDate date2, LocalDateTime dateTime1, LocalDateTime dateTime2) {
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
		return date1;
	}

	/**
	 * Sets the date1.
	 * 
	 * @param date1 New date1.
	 */
	public void setDate1(LocalDate date1) {
		this.date1 = date1;
	}

	/**
	 * Gets the date2.
	 * 
	 * @return The date2.
	 */
	@XmlJavaTypeAdapter(value = LocalDateSerializer2.class)
	public LocalDate getDate2() {
		return date2;
	}

	/**
	 * Sets the date2.
	 * 
	 * @param date2 New date2.
	 */
	public void setDate2(LocalDate date2) {
		this.date2 = date2;
	}

	/**
	 * Gets the dateTime1.
	 * 
	 * @return The dateTime1.
	 */
	@XmlJavaTypeAdapter(value = LocalDateTimeSerializer1.class)
	public LocalDateTime getDateTime1() {
		return dateTime1;
	}

	/**
	 * Sets the dateTime1.
	 * 
	 * @param dateTime1 New dateTime1.
	 */
	public void setDateTime1(LocalDateTime dateTime1) {
		this.dateTime1 = dateTime1;
	}

	/**
	 * Gets the dateTime2.
	 * 
	 * @return The dateTime2.
	 */
	@XmlJavaTypeAdapter(value = LocalDateTimeSerializer2.class)
	public LocalDateTime getDateTime2() {
		return dateTime2;
	}

	/**
	 * Sets the dateTime2.
	 * 
	 * @param dateTime2 New dateTime2.
	 */
	public void setDateTime2(LocalDateTime dateTime2) {
		this.dateTime2 = dateTime2;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(date1, date2, dateTime1, dateTime2);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TestXmlObject)) {
			return false;
		}
		TestXmlObject other = (TestXmlObject) obj;
		return Objects.equals(date1, other.date1) && Objects.equals(date2, other.date2)
				&& Objects.equals(dateTime1, other.dateTime1) && Objects.equals(dateTime2, other.dateTime2);
	}

}
