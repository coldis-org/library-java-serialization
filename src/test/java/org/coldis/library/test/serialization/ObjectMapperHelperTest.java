package org.coldis.library.test.serialization;

import java.util.List;

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
			.withTest7(7).withTest88(new int[] { 2, 3, 4 }).withTest9(9) };

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

}
