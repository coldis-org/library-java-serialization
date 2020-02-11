package org.coldis.library.test.serialization.csv;

import java.math.BigDecimal;

import org.coldis.library.serialization.csv.CsvHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;

/**
 * CSV helper test.
 */
public class CsvHelperTest {

	/**
	 * Test data.
	 */
	public static final TestObj[] TEST_DATA = { new TestObj(new BigDecimal(7000), 12, new BigDecimal(900)) };

	/**
	 * Tests the CSV helper.
	 *
	 * @throws Exception If the test fails.
	 */
	@Test
	public void test00CsvConversion() throws Exception {
		// For each test data.
		for (final TestObj originalObject : CsvHelperTest.TEST_DATA) {
			// Convert the object to a CSV.
			final String csvObject = CsvHelper.getObjectMapper()
					.writer(new CsvMapper().schemaFor(TestObj.class).withHeader()).writeValueAsString(originalObject);
			// Re-converts the CSV into an object.
			final TestObj reConvertedObject = CsvHelper.getObjectMapper()
					.reader(new CsvMapper().schemaFor(TestObj.class).withHeader()).forType(TestObj.class)
					.readValue(csvObject);
			// Asserts that the re converted object is the same as the original one.
			Assertions.assertEquals(originalObject, reConvertedObject);
		}
	}

}
