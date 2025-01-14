package org.coldis.library.test.serialization;

import java.util.List;
import java.util.Map;

import org.coldis.library.model.view.ModelView;
import org.coldis.library.serialization.ObjectMapperHelper;
import org.coldis.library.test.serialization.dto.DtoTestObject2Dto;
import org.coldis.library.test.serialization.dto.DtoTestObjectDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Object mapper helper test.
 */
public class ObjectMapperHelperTest {

	/**
	 * Test data.
	 */
	private static final DtoTestObjectDto[] TEST_DATA = { new DtoTestObjectDto().withId(10L).withTest1(new DtoTestObject2Dto().withId(1L).withTest("test1"))
			.withTest2(List.of(new DtoTestObject2Dto().withId(2L).withTest("test2"), new DtoTestObject2Dto().withId(3L).withTest("test3")))
			.withTest4(new DtoTestObject2Dto().withId(4L).withTest("test4"))
			.withTest6(new DtoTestObject2Dto[] { new DtoTestObject2Dto().withId(5L).withTest("test5"), new DtoTestObject2Dto().withId(6L).withTest("test6") })
			.withTest7(7).withTest88(new int[] { 2, 3, 4 }).withTest9(9).withTest10(1000000000000L).withTest11("Romulo Valente Coutinho")
			.withTest12(TestEnum.ABC).withTest13(Map.of("test1", 1, "test2", "2")) };

	/**
	 * Object mapper.
	 */
	private final ObjectMapper objectMapper = ObjectMapperHelper.createMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	/**
	 * Tests object conversion.
	 *
	 * @throws Exception If the test does not succeed.
	 */
	@Test
	public void test00Conversion() throws Exception {
		// For each test data.
		for (final DtoTestObjectDto originalDto : ObjectMapperHelperTest.TEST_DATA) {
			// Converts the DTO to the original object and back.
			final DtoTestObject originalObject1 = ObjectMapperHelper.convert(this.objectMapper, originalDto, DtoTestObject.class, false);
			final DtoTestObjectDto reconvertedDto1 = ObjectMapperHelper.convert(this.objectMapper, originalObject1, DtoTestObjectDto.class, false);

			final DtoTestObject originalObject2 = ObjectMapperHelper.convert(this.objectMapper, originalDto, new TypeReference<DtoTestObject>() {}, false);
			final DtoTestObjectDto reconvertedDto2 = ObjectMapperHelper.convert(this.objectMapper, originalObject2, new TypeReference<DtoTestObjectDto>() {},
					false);

			final DtoTestObject originalObject3 = ObjectMapperHelper.convert(this.objectMapper, originalDto, DtoTestObject.class, true);
			final DtoTestObjectDto reconvertedDto3 = ObjectMapperHelper.convert(this.objectMapper, originalObject3, DtoTestObjectDto.class, true);

			final DtoTestObject originalObject4 = ObjectMapperHelper.convert(this.objectMapper, originalDto, new TypeReference<DtoTestObject>() {}, true);
			final DtoTestObjectDto reconvertedDto4 = ObjectMapperHelper.convert(this.objectMapper, originalObject4, new TypeReference<DtoTestObjectDto>() {},
					true);

			// The DTO should remain the same.
			Assertions.assertEquals(originalDto, reconvertedDto1);
			Assertions.assertEquals(originalDto, reconvertedDto2);
			Assertions.assertEquals(originalDto, reconvertedDto3);
			Assertions.assertEquals(originalDto, reconvertedDto4);

			// Asserts that errors are ignored if resume on errors is set.
			Assertions.assertNull(ObjectMapperHelper.convert(null, originalDto, DtoTestObject.class, true));
			Assertions.assertNull(ObjectMapperHelper.convert(null, originalDto, new TypeReference<DtoTestObject>() {}, true));
		}
	}

	/**
	 * Tests object serialization.
	 *
	 * @throws Exception If the test does not succeed.
	 */
	@Test
	public void test01Serialization() throws Exception {
		// For each test data.
		for (final DtoTestObjectDto originalDto : ObjectMapperHelperTest.TEST_DATA) {
			// Converts the DTO to the original object and back.
			long start = System.currentTimeMillis();
			final String serializedObject1 = ObjectMapperHelper.serialize(this.objectMapper, originalDto, null, false);
			final DtoTestObject originalObject1 = ObjectMapperHelper.deserialize(this.objectMapper, serializedObject1, DtoTestObject.class, false);
			final String reserializedObject1 = ObjectMapperHelper.serialize(this.objectMapper, originalObject1, null, false);
			final DtoTestObjectDto reconvertedDto1 = ObjectMapperHelper.deserialize(this.objectMapper, reserializedObject1, DtoTestObjectDto.class, false);
			System.out.println("Serialization and deserialization took " + (System.currentTimeMillis() - start) + "ms.");

			start = System.currentTimeMillis();
			final String serializedObject2 = ObjectMapperHelper.serialize(this.objectMapper, originalDto, null, false);
			final DtoTestObject originalObject2 = ObjectMapperHelper.deserialize(this.objectMapper, serializedObject2, new TypeReference<DtoTestObject>() {},
					false);
			final String reserializedObject2 = ObjectMapperHelper.serialize(this.objectMapper, originalObject2, null, false);
			final DtoTestObjectDto reconvertedDto2 = ObjectMapperHelper.deserialize(this.objectMapper, reserializedObject2,
					new TypeReference<DtoTestObjectDto>() {}, false);
			System.out.println("Serialization and deserialization took " + (System.currentTimeMillis() - start) + "ms.");

			start = System.currentTimeMillis();
			final String serializedObject3 = ObjectMapperHelper.serialize(this.objectMapper, originalDto, null, false);
			final DtoTestObject originalObject3 = ObjectMapperHelper.deserialize(this.objectMapper, serializedObject3, DtoTestObject.class, true);
			final String reserializedObject3 = ObjectMapperHelper.serialize(this.objectMapper, originalObject3, null, false);
			final DtoTestObjectDto reconvertedDto3 = ObjectMapperHelper.deserialize(this.objectMapper, reserializedObject3, DtoTestObjectDto.class, true);
			System.out.println("Serialization and deserialization took " + (System.currentTimeMillis() - start) + "ms.");

			start = System.currentTimeMillis();
			final String serializedObject4 = ObjectMapperHelper.serialize(this.objectMapper, originalDto, null, false);
			final DtoTestObject originalObject4 = ObjectMapperHelper.deserialize(this.objectMapper, serializedObject4, new TypeReference<DtoTestObject>() {},
					true);
			final String reserializedObject4 = ObjectMapperHelper.serialize(this.objectMapper, originalObject4, null, false);
			final DtoTestObjectDto reconvertedDto4 = ObjectMapperHelper.deserialize(this.objectMapper, reserializedObject4,
					new TypeReference<DtoTestObjectDto>() {}, false);
			System.out.println("Serialization and deserialization took " + (System.currentTimeMillis() - start) + "ms.");

			// The DTO should remain the same.
			Assertions.assertEquals(originalDto, reconvertedDto1);
			Assertions.assertEquals(originalDto, reconvertedDto2);
			Assertions.assertEquals(originalDto, reconvertedDto3);
			Assertions.assertEquals(originalDto, reconvertedDto4);

			// Asserts that errors are ignored if resume on errors is set.
			Assertions.assertNull(ObjectMapperHelper.serialize(null, originalDto, null, true));
			Assertions.assertNull(ObjectMapperHelper.deserialize(null, serializedObject1, DtoTestObject.class, true));
			Assertions.assertNull(ObjectMapperHelper.deserialize(null, serializedObject1, new TypeReference<DtoTestObject>() {}, true));

		}
	}

	/**
	 * Test sensitive field serialization.
	 */
	@Test
	public void testSensitiveFieldSerialization() throws Exception {
		// Tests sensitive field serialization using no JSON view.
		final String serializedObject1 = ObjectMapperHelper.serialize(this.objectMapper, ObjectMapperHelperTest.TEST_DATA[0], null, false);
		final DtoTestObject originalObject1 = ObjectMapperHelper.deserialize(this.objectMapper, serializedObject1, DtoTestObject.class, false);
		Assertions.assertFalse(serializedObject1.contains("\"test10\":\"100+-+-+-+000\""));
		Assertions.assertFalse(serializedObject1.contains("\"test11\":\"-+-+-+-+-+-+-\""));
		Assertions.assertEquals(ObjectMapperHelperTest.TEST_DATA[0].getTest10(), originalObject1.getTest10());
		Assertions.assertEquals(ObjectMapperHelperTest.TEST_DATA[0].getTest11(), originalObject1.getTest11());

		// Tests sensitive field serialization using Public JSON view.
		final String serializedObject2 = ObjectMapperHelper.serialize(this.objectMapper, ObjectMapperHelperTest.TEST_DATA[0], ModelView.Public.class, false);
		final DtoTestObject originalObject2 = ObjectMapperHelper.deserialize(this.objectMapper, serializedObject2, DtoTestObject.class, false);
		Assertions.assertTrue(serializedObject2.contains("\"test10\":\"100+-+-+-+000\""));
		Assertions.assertTrue(serializedObject2.contains("\"test11\":\"-+-+-+-+-+-+-\""));
		Assertions.assertEquals(null, originalObject2.getTest10());
		Assertions.assertEquals(null, originalObject2.getTest11());

		// Tests sensitive field serialization using PublicAndPersonal JSON view.
		final String serializedObject3 = ObjectMapperHelper.serialize(this.objectMapper, ObjectMapperHelperTest.TEST_DATA[0], ModelView.PublicAndPersonal.class,
				false);
		final DtoTestObject originalObject3 = ObjectMapperHelper.deserialize(this.objectMapper, serializedObject3, DtoTestObject.class, false);
		Assertions.assertFalse(serializedObject3.contains("\"test10\":\"100+-+-+-+000\""));
		Assertions.assertTrue(serializedObject3.contains("\"test11\":\"-+-+-+-+-+-+-\""));
		Assertions.assertEquals(ObjectMapperHelperTest.TEST_DATA[0].getTest10(), originalObject3.getTest10());
		Assertions.assertEquals(null, originalObject3.getTest11());

		// Tests sensitive field serialization using PublicAndSensitive JSON view.
		final String serializedObject4 = ObjectMapperHelper.serialize(this.objectMapper, ObjectMapperHelperTest.TEST_DATA[0],
				ModelView.PublicAndSensitive.class, false);
		final DtoTestObject originalObject4 = ObjectMapperHelper.deserialize(this.objectMapper, serializedObject4, DtoTestObject.class, false);
		Assertions.assertFalse(serializedObject4.contains("\"test10\":\"100+-+-+-+000\""));
		Assertions.assertFalse(serializedObject4.contains("\"test11\":\"-+-+-+-+-+-+-\""));
		Assertions.assertEquals(ObjectMapperHelperTest.TEST_DATA[0].getTest10(), originalObject4.getTest10());
		Assertions.assertEquals(ObjectMapperHelperTest.TEST_DATA[0].getTest11(), originalObject4.getTest11());

		// Tests sensitive field serialization using Public JSON view with small field.
		originalObject4.setTest10(1234L);
		final String serializedObject5 = ObjectMapperHelper.serialize(this.objectMapper, originalObject4, ModelView.Public.class, false);
		final DtoTestObject originalObject5 = ObjectMapperHelper.deserialize(this.objectMapper, serializedObject5, DtoTestObject.class, false);
		Assertions.assertTrue(serializedObject5.contains("\"test10\":\"1+-+-+-+-+-+4\""));
		Assertions.assertTrue(serializedObject5.contains("\"test11\":\"-+-+-+-+-+-+-\""));
		Assertions.assertEquals(null, originalObject5.getTest10());

		// Tests sensitive field serialization using PublicAndSensite JSON view with
		// small field.
		originalObject4.setTest10(1234L);
		originalObject4.setTest11("Rom");
		final String serializedObject6 = ObjectMapperHelper.serialize(this.objectMapper, originalObject4, ModelView.PublicAndSensitive.class, false);
		final DtoTestObject originalObject6 = ObjectMapperHelper.deserialize(this.objectMapper, serializedObject6, DtoTestObject.class, false);
		Assertions.assertFalse(serializedObject6.contains("\"test10\":\"1+-+-+-+-+-+4\""));
		Assertions.assertFalse(serializedObject6.contains("\"test11\":\"-+-+-+-+-+-+-\""));
		Assertions.assertEquals(originalObject4.getTest10(), originalObject6.getTest10());
		Assertions.assertEquals(originalObject4.getTest11(), originalObject6.getTest11());

	}

}
