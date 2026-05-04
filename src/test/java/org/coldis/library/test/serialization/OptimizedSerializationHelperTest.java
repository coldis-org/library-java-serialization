package org.coldis.library.test.serialization;

import java.util.List;

import org.apache.fory.Fory;
import org.apache.fory.config.CompatibleMode;
import org.apache.fory.config.Language;
import org.coldis.library.serialization.OptimizedSerializationHelper;
import org.coldis.library.test.serialization.dto.DtoTestObject2Dto;
import org.coldis.library.test.serialization.dto.DtoTestObjectDto;
import org.junit.jupiter.api.Assertions;
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
	private static Fory serializer1;

	/**
	 * Object mapper.
	 */
	private static Fory serializer2;

	static {
		OptimizedSerializationHelperTest.serializer1 = (Fory) OptimizedSerializationHelper.createSerializer(false, null, null, Language.JAVA,
		"org.coldis.library.test.serialization");
//		OptimizedSerializationHelperTest.serializer1 = (Fory) OptimizedSerializationHelper.createSerializer(false, null, null, Language.XLANG,
//		"org.coldis.library.test.serializationX");
//		OptimizedSerializationHelperTest.serializer1.register(DtoTestObjectDto.class, DtoTestObject.class.getName());
//		OptimizedSerializationHelperTest.serializer1.register(DtoTestObject2Dto.class, DtoTestObject2.class.getName());
		OptimizedSerializationHelperTest.serializer2 = (Fory) OptimizedSerializationHelper.createSerializer(false, null, null, Language.XLANG,
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
	// @Disabled
	public void test01Serialization() throws Exception {
		// For each test data.
		for (final DtoTestObjectDto originalDto : OptimizedSerializationHelperTest.TEST_DATA) {
			// Converts the DTO to the original object and back.
			long start = System.currentTimeMillis();
			final byte[] serializedObject1 = OptimizedSerializationHelperTest.serializer1.serialize(originalDto);
			final DtoTestObjectDto originalObject1 = (DtoTestObjectDto) OptimizedSerializationHelperTest.serializer1.deserialize(serializedObject1);
			final byte[] reserializedObject1 = OptimizedSerializationHelperTest.serializer1.serialize(originalObject1);
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

	/**
	 * Serializes a model and deserializes the bytes into the corresponding DTO,
	 * and vice versa, on the same Fory instance configured in COMPATIBLE mode.
	 */
	@Test
	public void test02ModelDtoCrossClass() throws Exception {
		final Fory fory = Fory.builder().registerGuavaTypes(false).withLanguage(Language.JAVA).withCompatibleMode(CompatibleMode.COMPATIBLE)
				.requireClassRegistration(false).build();

		final DtoTestObject2 nested = new DtoTestObject2();
		nested.setId(1L);
		nested.setTest("nested");

		final DtoTestObject model = new DtoTestObject();
		model.setId(42L);
		model.setTest1(nested);
		model.setTest3("three");
		model.setTest7(7);
		model.setTest9(99);
		model.setTest11("eleven");

		final byte[] modelBytes = fory.serialize(model);
		final DtoTestObjectDto dto = fory.deserialize(modelBytes, DtoTestObjectDto.class);
		Assertions.assertEquals(model.getId(), dto.getId());
		Assertions.assertNotNull(dto.getTest1());
		Assertions.assertEquals(model.getTest1().getId(), dto.getTest1().getId());
		Assertions.assertEquals(model.getTest1().getTest(), dto.getTest1().getTest());
		Assertions.assertEquals(model.getTest7(), dto.getTest7());
		Assertions.assertEquals(model.getTest9(), dto.getTest9());
		Assertions.assertEquals(model.getTest11(), dto.getTest11());

		final byte[] dtoBytes = fory.serialize(dto);
		final DtoTestObject roundTrip = fory.deserialize(dtoBytes, DtoTestObject.class);
		Assertions.assertEquals(model.getId(), roundTrip.getId());
		Assertions.assertEquals(model.getTest7(), roundTrip.getTest7());
		Assertions.assertEquals(model.getTest9(), roundTrip.getTest9());
		Assertions.assertEquals(model.getTest11(), roundTrip.getTest11());
	}

	/**
	 * Schema mismatch: test3 exists on the model but is excluded from the DTO via
	 * @DtoAttribute(ignore=true). Both directions must succeed; the unmatched
	 * field is silently dropped.
	 */
	@Test
	public void test03SchemaMismatch() throws Exception {
		final Fory fory = Fory.builder().registerGuavaTypes(false).withLanguage(Language.JAVA).withCompatibleMode(CompatibleMode.COMPATIBLE)
				.requireClassRegistration(false).build();

		final DtoTestObject model = new DtoTestObject();
		model.setId(1L);
		model.setTest3("only-on-model");
		model.setTest7(7);
		model.setTest11("eleven");

		final DtoTestObjectDto dto = fory.deserialize(fory.serialize(model), DtoTestObjectDto.class);
		Assertions.assertEquals(1L, dto.getId());
		Assertions.assertEquals(7, dto.getTest7());
		Assertions.assertEquals("eleven", dto.getTest11());

		final DtoTestObjectDto dtoOnly = new DtoTestObjectDto().withId(2L).withTest11("dto-only");
		final DtoTestObject reverseTrip = fory.deserialize(fory.serialize(dtoOnly), DtoTestObject.class);
		Assertions.assertEquals(2L, reverseTrip.getId());
		Assertions.assertEquals("dto-only", reverseTrip.getTest11());
		Assertions.assertNull(reverseTrip.getTest3());
	}

	/**
	 * Two-instance producer/consumer pattern with name-based registration.
	 * Mirrors a cross-process JMS scenario: producer-side Fory knows only the
	 * model classes, consumer-side Fory knows only the DTO classes. Types map
	 * across the wire by shared (namespace, typeName).
	 *
	 * Required for nested cross-class translation: the single-instance
	 * `deserialize(bytes, Class)` shortcut only retargets the outer type and
	 * leaves nested elements as their source class, which throws
	 * ClassCastException when assigned to a DTO field of a different concrete
	 * element type.
	 */
	private static Fory producerFory() {
		final Fory fory = Fory.builder().registerGuavaTypes(false).withLanguage(Language.JAVA).withCompatibleMode(CompatibleMode.COMPATIBLE)
				.requireClassRegistration(true).build();
		fory.register(DtoTestObject.class, "coldis.test", "TestObj");
		fory.register(DtoTestObject2.class, "coldis.test", "TestObj2");
		fory.register(TestEnum.class, "coldis.test", "TestEnum");
		return fory;
	}

	private static Fory consumerFory() {
		final Fory fory = Fory.builder().registerGuavaTypes(false).withLanguage(Language.JAVA).withCompatibleMode(CompatibleMode.COMPATIBLE)
				.requireClassRegistration(true).build();
		fory.register(DtoTestObjectDto.class, "coldis.test", "TestObj");
		fory.register(DtoTestObject2Dto.class, "coldis.test", "TestObj2");
		fory.register(TestEnum.class, "coldis.test", "TestEnum");
		return fory;
	}

	/**
	 * Producer serializes Model + nested object; consumer deserializes into
	 * matching DTO + nested DTO via shared type names.
	 */
	@Test
	public void test04TwoInstancesScalarsAndNested() throws Exception {
		final Fory producer = OptimizedSerializationHelperTest.producerFory();
		final Fory consumer = OptimizedSerializationHelperTest.consumerFory();

		final DtoTestObject2 nested = new DtoTestObject2();
		nested.setId(1L);
		nested.setTest("hello");
		final DtoTestObject model = new DtoTestObject();
		model.setId(7L);
		model.setTest1(nested);
		model.setTest7(7);
		model.setTest11("eleven");

		final DtoTestObjectDto dto = consumer.deserialize(producer.serialize(model), DtoTestObjectDto.class);
		Assertions.assertEquals(7L, dto.getId());
		Assertions.assertEquals(7, dto.getTest7());
		Assertions.assertEquals("eleven", dto.getTest11());
		Assertions.assertNotNull(dto.getTest1());
		Assertions.assertEquals(1L, dto.getTest1().getId());
		Assertions.assertEquals("hello", dto.getTest1().getTest());
	}

	/**
	 * Same producer/consumer pattern, exercising a List of nested objects to
	 * confirm cross-class translation works through collections — the case
	 * single-instance `deserialize(bytes, Class)` cannot handle.
	 */
	@Test
	public void test05TwoInstancesCollections() throws Exception {
		final Fory producer = OptimizedSerializationHelperTest.producerFory();
		final Fory consumer = OptimizedSerializationHelperTest.consumerFory();

		final DtoTestObject2 a = new DtoTestObject2();
		a.setId(1L);
		a.setTest("a");
		final DtoTestObject2 b = new DtoTestObject2();
		b.setId(2L);
		b.setTest("b");
		final DtoTestObject model = new DtoTestObject();
		model.setId(99L);
		model.setTest2(List.of(a, b));

		final DtoTestObjectDto dto = consumer.deserialize(producer.serialize(model), DtoTestObjectDto.class);
		Assertions.assertEquals(99L, dto.getId());
		Assertions.assertNotNull(dto.getTest2());
		Assertions.assertEquals(2, dto.getTest2().size());
		Assertions.assertEquals(1L, dto.getTest2().get(0).getId());
		Assertions.assertEquals("a", dto.getTest2().get(0).getTest());
		Assertions.assertEquals(2L, dto.getTest2().get(1).getId());
		Assertions.assertEquals("b", dto.getTest2().get(1).getTest());
	}

}
