package org.coldis.library.test.serialization;

import java.util.List;

import org.apache.fury.Fury;
import org.apache.fury.config.Language;
import org.apache.fury.resolver.MetaContext;
import org.coldis.library.serialization.OptimizedSerializationHelper;
import org.coldis.library.test.serialization.dto.DtoTestObject2Dto;
import org.coldis.library.test.serialization.dto.DtoTestObjectDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Object mapper helper test.
 */
public class OptimizedSerializationHelperTest {

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
	private static Fury serializer1;

	/**
	 * Object mapper.
	 */
	private static Fury serializer2;

	static {
		OptimizedSerializationHelperTest.serializer1 = (Fury) OptimizedSerializationHelper.createSerializer(false, null, null, Language.XLANG,
				"org.coldis.library.test.serializationX");
		OptimizedSerializationHelperTest.serializer1.register(DtoTestObjectDto.class, DtoTestObject.class.getName());
		OptimizedSerializationHelperTest.serializer1.register(DtoTestObject2Dto.class, DtoTestObject2.class.getName());
		OptimizedSerializationHelperTest.serializer2 = (Fury) OptimizedSerializationHelper.createSerializer(false, null, null, Language.XLANG,
				"org.coldis.library.test.serializationX");
		OptimizedSerializationHelperTest.serializer2.register(DtoTestObject.class, DtoTestObject.class.getName());
		OptimizedSerializationHelperTest.serializer2.register(DtoTestObject2.class, DtoTestObject2.class.getName());
	}

	/**
	 * Tests object serialization.
	 *
	 * @throws Exception If the test does not succeed.
	 */
	@Test
	@Disabled
	public void test01Serialization() throws Exception {
		// For each test data.
		for (final DtoTestObjectDto originalDto : OptimizedSerializationHelperTest.TEST_DATA) {
			// Converts the DTO to the original object and back.
			long start = System.currentTimeMillis();
			final byte[] serializedObject1 = OptimizedSerializationHelperTest.serializer1.serialize(originalDto);
			final DtoTestObject originalObject1 = (DtoTestObject) OptimizedSerializationHelperTest.serializer2.deserialize(serializedObject1);
			final byte[] reserializedObject1 = OptimizedSerializationHelperTest.serializer2.serialize(originalObject1);
			final DtoTestObjectDto reconvertedDto1 = (DtoTestObjectDto) OptimizedSerializationHelperTest.serializer1.deserialize(reserializedObject1);
			System.out.println("Serialization and deserialization took " + (System.currentTimeMillis() - start) + "ms.");

			start = System.currentTimeMillis();
			final byte[] serializedObject2 = OptimizedSerializationHelperTest.serializer1.serialize(originalDto);
			final DtoTestObjectDto originalObject2 = (DtoTestObjectDto) OptimizedSerializationHelperTest.serializer1.deserialize(serializedObject2);
			final byte[] reserializedObject2 = OptimizedSerializationHelperTest.serializer1.serialize(originalObject2);
			final DtoTestObjectDto reconvertedDto2 = (DtoTestObjectDto) OptimizedSerializationHelperTest.serializer1.deserialize(reserializedObject2);
			System.out.println("Serialization and deserialization took " + (System.currentTimeMillis() - start) + "ms.");

			start = System.currentTimeMillis();
			final byte[] serializedObject3 = OptimizedSerializationHelperTest.serializer1.serialize(originalDto);
			final DtoTestObjectDto originalObject3 = (DtoTestObjectDto) OptimizedSerializationHelperTest.serializer1.deserialize(serializedObject3);
			final byte[] reserializedObject3 = OptimizedSerializationHelperTest.serializer1.serialize(originalObject3);
			final DtoTestObjectDto reconvertedDto3 = (DtoTestObjectDto) OptimizedSerializationHelperTest.serializer1.deserialize(reserializedObject3);
			System.out.println("Serialization and deserialization took " + (System.currentTimeMillis() - start) + "ms.");

			start = System.currentTimeMillis();
			final byte[] serializedObject4 = OptimizedSerializationHelperTest.serializer1.serialize(originalDto);
			final DtoTestObjectDto originalObject4 = (DtoTestObjectDto) OptimizedSerializationHelperTest.serializer1.deserialize(serializedObject4);
			final byte[] reserializedObject4 = OptimizedSerializationHelperTest.serializer1.serialize(originalObject4);
			final DtoTestObjectDto reconvertedDto4 = (DtoTestObjectDto) OptimizedSerializationHelperTest.serializer1.deserialize(reserializedObject4);
			System.out.println("Serialization and deserialization took " + (System.currentTimeMillis() - start) + "ms.");

			// The DTO should remain the same.
			Assertions.assertEquals(originalDto, reconvertedDto1);
			Assertions.assertEquals(originalDto, reconvertedDto2);
			Assertions.assertEquals(originalDto, reconvertedDto3);
			Assertions.assertEquals(originalDto, reconvertedDto4);

		}
	}

}
