package org.coldis.library.test.serialization.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.temporal.ChronoUnit;

import org.coldis.library.helper.DateTimeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

/**
 * XML serialization test.
 */
public class XmlSerializationTest {

	/**
	 * Test data.
	 */
	private static final TestXmlObject[] TEST_DATA = {
					new TestXmlObject(DateTimeHelper.getCurrentLocalDate(), DateTimeHelper.getCurrentLocalDate(),
							DateTimeHelper.getCurrentLocalDateTime().truncatedTo(ChronoUnit.SECONDS),
							DateTimeHelper.getCurrentLocalDateTime().truncatedTo(ChronoUnit.SECONDS)) };

	/**
	 * Tests org.coldis.library.serialization.positional XML serializers.
	 *
	 * @throws Exception If the test fails.
	 */
	@Test
	public void test00CustomSerializers() throws Exception {
		// Gets the XML marshaller/unmarshaller.
		final JAXBContext jaxbContext = JAXBContext.newInstance(TestXmlObject.class);
		final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		// For each test data.
		if (XmlSerializationTest.TEST_DATA != null) {
			for (final TestXmlObject testData : XmlSerializationTest.TEST_DATA) {
				// Serializes the object into XML.
				final StringWriter writer = new StringWriter();
				jaxbMarshaller.marshal(testData, writer);
				// De-serializes the XML content.
				final StringReader reader = new StringReader(writer.toString());
				writer.close();
				final TestXmlObject deserializedTestData = (TestXmlObject) jaxbUnmarshaller.unmarshal(reader);
				reader.close();
				// Asserts that the test data has not changed after serialization.
				Assertions.assertEquals(testData, deserializedTestData);
			}
		}
	}

}
