package org.coldis.library.test.serialization.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.coldis.library.helper.DateTimeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * XML serialization test.
 */
public class XmlSerializationTest {

	/**
	 * Test data.
	 */
	private static final TestXmlObject[] TEST_DATA = {
			new TestXmlObject(DateTimeHelper.getCurrentLocalDate(), DateTimeHelper.getCurrentLocalDate(),
					DateTimeHelper.getCurrentLocalDateTime(), DateTimeHelper.getCurrentLocalDateTime()) };

	/**
	 * Tests custom XML serializers.
	 * 
	 * @throws Exception If the test fails.
	 */
	@Test
	public void test00CustomSerializers() throws Exception {
		// Gets the XML marshaller/unmarshaller.
		JAXBContext jaxbContext = JAXBContext.newInstance(TestXmlObject.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		// For each test data.
		if (TEST_DATA != null) {
			for (TestXmlObject testData : TEST_DATA) {
				// XML content.
				String testXmlContent = null;
				// Serializes the object into XML.
				StringWriter writer = new StringWriter();
				jaxbMarshaller.marshal(testData, writer);
				writer.write(testXmlContent);
				writer.close();
				// De-serializes the XML content.
				StringReader reader = new StringReader(testXmlContent);
				TestXmlObject deserializedTestData = (TestXmlObject) jaxbUnmarshaller.unmarshal(reader);
				reader.close();
				// Asserts that the test data has not changed after serialization.
				Assertions.assertEquals(testData, deserializedTestData);
			}
		}
	}

}
