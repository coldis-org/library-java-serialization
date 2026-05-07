package org.coldis.library.test.serialization;

import java.util.List;

import org.apache.fory.BaseFory;
import org.apache.fory.Fory;
import org.apache.fory.config.CompatibleMode;
import org.apache.fory.config.Language;
import org.coldis.library.serialization.ObjectMapperHelper;
import org.coldis.library.serialization.OptimizedSerializationHelper;
import org.coldis.library.test.serialization.conflict.SharedNameAlias;
import org.coldis.library.test.serialization.conflict.SharedNameOwner;
import org.coldis.library.test.serialization.crossproc.dto.CrossModelDto;
import org.coldis.library.test.serialization.crossproc.model.CrossModel;
import org.coldis.library.test.serialization.dto.DtoTestObject2Dto;
import org.coldis.library.test.serialization.dto.DtoTestObjectDto;
import org.coldis.library.test.serialization.excluded.ExcludedService;
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
		// serializer1 round-trips both Models and DTOs in the same instance — uses ALL scope so
		// every scanned class is registered (under FQN, not shared typeName).
		OptimizedSerializationHelperTest.serializer1 = (Fory) OptimizedSerializationHelper.createAllSerializer(false, null, null, Language.JAVA,
				"org.coldis.library.test.serialization");
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
	 * Single-instance limit: cross-class translation works for direct nested
	 * fields (because Fory uses the target class's field declaration) but not
	 * for elements of a generic collection, where the type parameter is
	 * erased at runtime and Fory falls back to the source class embedded in
	 * the bytes. The List ends up holding source-class elements, so accessing
	 * them through the DTO's typed getter throws ClassCastException.
	 */
	@Test
	public void test04SingleInstanceCollectionLimit() throws Exception {
		final Fory fory = Fory.builder().registerGuavaTypes(false).withLanguage(Language.JAVA).withCompatibleMode(CompatibleMode.COMPATIBLE)
				.requireClassRegistration(false).build();

		final DtoTestObject2 a = new DtoTestObject2();
		a.setId(1L);
		a.setTest("a");
		final DtoTestObject2 b = new DtoTestObject2();
		b.setId(2L);
		b.setTest("b");
		final DtoTestObject model = new DtoTestObject();
		model.setId(99L);
		model.setTest2(List.of(a, b));

		final DtoTestObjectDto dto = fory.deserialize(fory.serialize(model), DtoTestObjectDto.class);
		Assertions.assertEquals(99L, dto.getId());
		Assertions.assertNotNull(dto.getTest2());
		Assertions.assertEquals(2, dto.getTest2().size());
		Assertions.assertThrows(ClassCastException.class, () -> dto.getTest2().get(0).getId());
	}

	/**
	 * Two-instance producer/consumer pattern with name-based registration.
	 * Mirrors a cross-process JMS scenario where the consumer does not have
	 * the producer-side classes on the classpath. Types map across the wire
	 * by shared (namespace, typeName), and elements of generic collections
	 * resolve to the consumer-side classes — the case the single-instance
	 * shortcut cannot handle (see {@link #test04SingleInstanceCollectionLimit()}).
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
	 * Documents two Fory registration constraints that shape the helper's
	 * single-name-per-class strategy: a class can only be registered under
	 * one name (Fory auto-reserves the FQN slot), and two classes cannot
	 * share the same name.
	 */
	@Test
	public void testRegistrationConstraints() throws Exception {
		final Fory dualName = Fory.builder().registerGuavaTypes(false).withLanguage(Language.JAVA).withCompatibleMode(CompatibleMode.COMPATIBLE)
				.requireClassRegistration(true).build();
		dualName.register(DtoTestObject.class, "shared", "TestObj");
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> dualName.register(DtoTestObject.class, "", DtoTestObject.class.getName()));

		final Fory sharedName = Fory.builder().registerGuavaTypes(false).withLanguage(Language.JAVA).withCompatibleMode(CompatibleMode.COMPATIBLE)
				.requireClassRegistration(true).build();
		sharedName.register(DtoTestObject.class, "", "shared.Conflict");
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> sharedName.register(DtoTestObjectDto.class, "", "shared.Conflict"));
	}

	/**
	 * Helper-built Fory round-trips both Model and DTO on the same instance
	 * — uses ALL scope so every scanned class is registered under FQN.
	 */
	@Test
	public void test06HelperCanonicalRoundTrips() throws Exception {
		final BaseFory fory = OptimizedSerializationHelper.createAllSerializer(false, null, null, Language.JAVA, "org.coldis.library.test.serialization");

		final DtoTestObject model = new DtoTestObject();
		model.setId(42L);
		model.setTest11("model");
		final DtoTestObject roundTripModel = (DtoTestObject) fory.deserialize(fory.serialize(model));
		Assertions.assertEquals(42L, roundTripModel.getId());
		Assertions.assertEquals("model", roundTripModel.getTest11());

		final DtoTestObjectDto dto = new DtoTestObjectDto().withId(7L).withTest11("dto");
		final DtoTestObjectDto roundTripDto = (DtoTestObjectDto) fory.deserialize(fory.serialize(dto));
		Assertions.assertEquals(7L, roundTripDto.getId());
		Assertions.assertEquals("dto", roundTripDto.getTest11());
	}

	/**
	 * Verifies the package-scan filter keeps Spring-stereotype-annotated
	 * classes out of the discovered set, so they never reach Fory
	 * registration. Tested via ObjectMapperHelper.getModelClasses directly
	 * because Fory's serialize permits unregistered classes (only
	 * deserialization enforces requireClassRegistration).
	 */
	@Test
	public void test07HelperExcludesSpringComponents() throws Exception {
		final java.util.Map<Class<?>, String> scanned = ObjectMapperHelper.getModelClasses(
				new OptimizedSerializationHelper.NoAnnotationTypeFilter(
						java.util.Set.of(org.springframework.stereotype.Component.class, org.springframework.stereotype.Controller.class,
								org.springframework.stereotype.Service.class, org.springframework.stereotype.Repository.class,
								org.springframework.context.annotation.Configuration.class),
						true, false),
				"org.coldis.library.test.serialization.excluded");
		Assertions.assertFalse(scanned.containsKey(ExcludedService.class));
	}

	/**
	 * Cross-process simulation through the helper. Producer scans only the
	 * model subpackage; consumer scans only the DTO subpackage. The shared
	 * Typable.getTypeName() makes the DTO canonical on the consumer side
	 * because the model is absent there. Producer-side Model bytes resolve
	 * directly into a DTO instance on the consumer side.
	 */
	@Test
	public void test08CrossProcessThroughHelper() throws Exception {
		final BaseFory producer = OptimizedSerializationHelper.createSerializer(false, null, null, Language.JAVA,
				"org.coldis.library.test.serialization.crossproc.model");
		final BaseFory consumer = OptimizedSerializationHelper.createSerializer(false, null, null, Language.JAVA,
				"org.coldis.library.test.serialization.crossproc.dto");

		final CrossModel model = new CrossModel();
		model.setId(99L);
		model.setLabel("hello");

		final CrossModelDto dto = (CrossModelDto) consumer.deserialize(producer.serialize(model));
		Assertions.assertEquals(99L, dto.getId());
		Assertions.assertEquals("hello", dto.getLabel());
	}

	/**
	 * Two unrelated classes reporting the same Typable.getTypeName() must
	 * not crash startup. The first wins the typeName slot; the second
	 * falls back to its FQN. Both stay round-trippable on the resulting
	 * Fory.
	 */
	@Test
	public void test09HelperHandlesTypeNameCollision() throws Exception {
		final BaseFory fory = OptimizedSerializationHelper.createSerializer(false, null, null, Language.JAVA, "org.coldis.library.test.serialization.conflict");

		final SharedNameOwner owner = new SharedNameOwner();
		owner.setValue(123L);
		final SharedNameOwner ownerRoundTrip = (SharedNameOwner) fory.deserialize(fory.serialize(owner));
		Assertions.assertEquals(123L, ownerRoundTrip.getValue());

		final SharedNameAlias alias = new SharedNameAlias();
		alias.setDescription("alias");
		final SharedNameAlias aliasRoundTrip = (SharedNameAlias) fory.deserialize(fory.serialize(alias));
		Assertions.assertEquals("alias", aliasRoundTrip.getDescription());
	}

	/**
	 * {@link OptimizedSerializationHelper.RegistrationScope#ALL} registers both
	 * the Model and the paired DTO; both should round-trip through the same
	 * serializer.
	 */
	@Test
	public void test10RegistrationScopeAllRegistersBoth() throws Exception {
		final BaseFory fory = OptimizedSerializationHelper.createAllSerializer(false, null, null, Language.JAVA, "org.coldis.library.test.serialization");

		final DtoTestObject model = new DtoTestObject();
		model.setId(101L);
		final DtoTestObject modelRoundTrip = (DtoTestObject) fory.deserialize(fory.serialize(model));
		Assertions.assertEquals(101L, modelRoundTrip.getId());

		final DtoTestObjectDto dto = new DtoTestObjectDto();
		dto.setId(202L);
		final DtoTestObjectDto dtoRoundTrip = (DtoTestObjectDto) fory.deserialize(fory.serialize(dto));
		Assertions.assertEquals(202L, dtoRoundTrip.getId());
	}

	/**
	 * {@link OptimizedSerializationHelper.RegistrationScope#MODELS} drops paired
	 * DTOs from the registry: Models still round-trip, DTOs throw at serialize
	 * time because {@code requireClassRegistration} blocks unregistered types.
	 */
	@Test
	public void test11RegistrationScopeModelsExcludesDtos() throws Exception {
		final BaseFory fory = OptimizedSerializationHelper.createModelSerializer(false, null, null, Language.JAVA, "org.coldis.library.test.serialization");

		final DtoTestObject model = new DtoTestObject();
		model.setId(11L);
		final DtoTestObject modelRoundTrip = (DtoTestObject) fory.deserialize(fory.serialize(model));
		Assertions.assertEquals(11L, modelRoundTrip.getId());

		final DtoTestObjectDto dto = new DtoTestObjectDto();
		dto.setId(22L);
		Assertions.assertThrows(Exception.class, () -> fory.serialize(dto),
				"paired DTO should not be registered under MODELS scope");
	}

	/**
	 * {@link OptimizedSerializationHelper.RegistrationScope#DTOS} drops paired
	 * Models from the registry: DTOs round-trip, Models throw at serialize
	 * time.
	 */
	@Test
	public void test12RegistrationScopeDtosExcludesModels() throws Exception {
		final BaseFory fory = OptimizedSerializationHelper.createDtoSerializer(false, null, null, Language.JAVA, "org.coldis.library.test.serialization");

		final DtoTestObjectDto dto = new DtoTestObjectDto();
		dto.setId(33L);
		final DtoTestObjectDto dtoRoundTrip = (DtoTestObjectDto) fory.deserialize(fory.serialize(dto));
		Assertions.assertEquals(33L, dtoRoundTrip.getId());

		final DtoTestObject model = new DtoTestObject();
		model.setId(44L);
		Assertions.assertThrows(Exception.class, () -> fory.serialize(model),
				"paired Model should not be registered under DTOS scope");
	}

	/**
	 * Two-instance pattern with a List of nested objects.
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
