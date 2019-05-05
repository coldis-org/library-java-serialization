package org.coldis.library.test.serialization.json;

import java.util.List;

import org.coldis.library.serialization.json.JsonHelper;
import org.coldis.library.test.serialization.json.dto.DtoTestObject2Dto;
import org.coldis.library.test.serialization.json.dto.DtoTestObjectDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON helper test.
 */
public class JsonHelperTest {

	/**
	 * Simples JSON object test data.
	 */
	private static final DtoTestObjectDto[] TEST_DATA = { new DtoTestObjectDto(10L, new DtoTestObject2Dto(1L, "test1"),
			List.of(new DtoTestObject2Dto(2L, "test2"), new DtoTestObject2Dto(3L, "test3")),
			new DtoTestObject2Dto(4L, "test4"),
			new DtoTestObject2Dto[] { new DtoTestObject2Dto(5L, "test5"), new DtoTestObject2Dto(6L, "test6") }, 1,
			new int[] { 2, 3, 4 }, 5) };

	/**
	 * Object mapper.
	 */
	private final ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	/**
	 * Tests object conversion.
	 *
	 * @throws Exception If the test does not succeed.
	 */
	@Test
	public void test00Conversion() throws Exception {
		// For each test data.
		for (final DtoTestObjectDto originalDto : JsonHelperTest.TEST_DATA) {
			// Converts the DTO to the original object and back.
			final DtoTestObject originalObject1 = JsonHelper.convert(this.objectMapper, originalDto,
					DtoTestObject.class, false);
			final DtoTestObjectDto reconvertedDto1 = JsonHelper.convert(this.objectMapper, originalObject1,
					DtoTestObjectDto.class, false);

			final DtoTestObject originalObject2 = JsonHelper.convert(this.objectMapper, originalDto,
					new TypeReference<DtoTestObject>() {
			}, false);
			final DtoTestObjectDto reconvertedDto2 = JsonHelper.convert(this.objectMapper, originalObject2,
					new TypeReference<DtoTestObjectDto>() {
			}, false);

			final DtoTestObject originalObject3 = JsonHelper.convert(this.objectMapper, originalDto,
					DtoTestObject.class, true);
			final DtoTestObjectDto reconvertedDto3 = JsonHelper.convert(this.objectMapper, originalObject3,
					DtoTestObjectDto.class, true);

			final DtoTestObject originalObject4 = JsonHelper.convert(this.objectMapper, originalDto,
					new TypeReference<DtoTestObject>() {
			}, true);
			final DtoTestObjectDto reconvertedDto4 = JsonHelper.convert(this.objectMapper, originalObject4,
					new TypeReference<DtoTestObjectDto>() {
			}, true);
			// The DTO should remain the same.
			Assertions.assertEquals(originalDto, reconvertedDto1);
			Assertions.assertEquals(originalDto, reconvertedDto2);
			Assertions.assertEquals(originalDto, reconvertedDto3);
			Assertions.assertEquals(originalDto, reconvertedDto4);

			// Asserts that errors are ignored if resume on errors is set.
			Assertions.assertNull(JsonHelper.convert(null, originalDto, DtoTestObject.class, true));
			Assertions.assertNull(JsonHelper.convert(null, originalDto, new TypeReference<DtoTestObject>() {
			}, true));
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
		for (final DtoTestObjectDto originalDto : JsonHelperTest.TEST_DATA) {
			// Converts the DTO to the original object and back.
			final String serializedObject1 = JsonHelper.serialize(this.objectMapper, originalDto, null, false);
			final DtoTestObject originalObject1 = JsonHelper.deserialize(this.objectMapper, serializedObject1,
					DtoTestObject.class, false);
			final String reserializedObject1 = JsonHelper.serialize(this.objectMapper, originalObject1, null, false);
			final DtoTestObjectDto reconvertedDto1 = JsonHelper.deserialize(this.objectMapper, reserializedObject1,
					DtoTestObjectDto.class, false);

			final String serializedObject2 = JsonHelper.serialize(this.objectMapper, originalDto, null, false);
			final DtoTestObject originalObject2 = JsonHelper.deserialize(this.objectMapper, serializedObject2,
					new TypeReference<DtoTestObject>() {
			}, false);
			final String reserializedObject2 = JsonHelper.serialize(this.objectMapper, originalObject2, null, false);
			final DtoTestObjectDto reconvertedDto2 = JsonHelper.deserialize(this.objectMapper, reserializedObject2,
					new TypeReference<DtoTestObjectDto>() {
			}, false);

			final String serializedObject3 = JsonHelper.serialize(this.objectMapper, originalDto, null, false);
			final DtoTestObject originalObject3 = JsonHelper.deserialize(this.objectMapper, serializedObject3,
					DtoTestObject.class, true);
			final String reserializedObject3 = JsonHelper.serialize(this.objectMapper, originalObject3, null, false);
			final DtoTestObjectDto reconvertedDto3 = JsonHelper.deserialize(this.objectMapper, reserializedObject3,
					DtoTestObjectDto.class, true);

			final String serializedObject4 = JsonHelper.serialize(this.objectMapper, originalDto, null, false);
			final DtoTestObject originalObject4 = JsonHelper.deserialize(this.objectMapper, serializedObject4,
					new TypeReference<DtoTestObject>() {
			}, true);
			final String reserializedObject4 = JsonHelper.serialize(this.objectMapper, originalObject4, null, false);
			final DtoTestObjectDto reconvertedDto4 = JsonHelper.deserialize(this.objectMapper, reserializedObject4,
					new TypeReference<DtoTestObjectDto>() {
			}, false);

			// The DTO should remain the same.
			Assertions.assertEquals(originalDto, reconvertedDto1);
			Assertions.assertEquals(originalDto, reconvertedDto2);
			Assertions.assertEquals(originalDto, reconvertedDto3);
			Assertions.assertEquals(originalDto, reconvertedDto4);

			// Asserts that errors are ignored if resume on errors is set.
			Assertions.assertNull(JsonHelper.serialize(null, originalDto, null, true));
			Assertions.assertNull(JsonHelper.deserialize(null, serializedObject1, DtoTestObject.class, true));
			Assertions.assertNull(JsonHelper.deserialize(null, serializedObject1, new TypeReference<DtoTestObject>() {
			}, true));

		}
	}

	/**
	 * Tests object cloning.
	 *
	 * @throws Exception If the test does not succeed.
	 */
	@Test
	public void test02Cloning() throws Exception {
		// For each test data.
		for (final DtoTestObjectDto originalDto : JsonHelperTest.TEST_DATA) {
			// Clones the test object.
			final DtoTestObjectDto clonedDto = JsonHelper.deepClone(this.objectMapper, originalDto,
					new TypeReference<DtoTestObjectDto>() {
			});
			// The DTO should remain the same.
			Assertions.assertEquals(originalDto, clonedDto);
			// Makes sure the object are not the same (although equal).
			Assertions.assertNotSame(originalDto, clonedDto);
			Assertions.assertNotSame(originalDto.getTest1(), clonedDto.getTest1());
			Assertions.assertNotSame(originalDto.getTest2(), clonedDto.getTest2());
			Assertions.assertNotSame(originalDto.getTest4(), clonedDto.getTest4());
			Assertions.assertNotSame(originalDto.getTest6(), clonedDto.getTest6());
		}
	}

}
