package org.coldis.library.test.serialization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.fory.BaseFory;
import org.apache.fory.config.Language;
import org.coldis.library.exception.IntegrationException;
import org.coldis.library.serialization.CompositeObjectCloner;
import org.coldis.library.serialization.ForyCloner;
import org.coldis.library.serialization.JavaSerializationCloner;
import org.coldis.library.serialization.ObjectCloner;
import org.coldis.library.serialization.ObjectMapperCloner;
import org.coldis.library.serialization.ObjectMapperHelper;
import org.coldis.library.serialization.OptimizedSerializationHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {@link ObjectCloner} adapters and {@link CompositeObjectCloner} tests.
 */
public class ObjectClonerTest {

	/**
	 * Fory serializer (no package scan, registration not required).
	 */
	private static final BaseFory FORY = OptimizedSerializationHelper.createSerializer(false, null, null, Language.JAVA);

	/**
	 * Object mapper.
	 */
	private static final ObjectMapper MAPPER = ObjectMapperHelper.createMapper();

	/**
	 * Sample payload.
	 */
	private static Map<String, Object> sampleMap() {
		final Map<String, Object> m = new HashMap<>();
		m.put("a", 1);
		m.put("b", List.of("x", "y", "z"));
		m.put("c", Map.of("nested", true));
		return m;
	}

	/**
	 * Tests Fory-backed cloner.
	 */
	@Test
	public void testForyCloner() throws Exception {
		final ObjectCloner cloner = new ForyCloner(ObjectClonerTest.FORY);
		final Map<String, Object> original = ObjectClonerTest.sampleMap();
		final Map<String, Object> clone = cloner.clone(original);
		Assertions.assertNotSame(original, clone);
		Assertions.assertEquals(original, clone);
	}

	/**
	 * Tests ObjectMapper-backed cloner.
	 */
	@Test
	public void testObjectMapperCloner() throws Exception {
		final ObjectCloner cloner = new ObjectMapperCloner(ObjectClonerTest.MAPPER);
		final Map<String, Object> original = ObjectClonerTest.sampleMap();
		final Map<String, Object> clone = cloner.clone(original);
		Assertions.assertNotSame(original, clone);
		Assertions.assertEquals(original, clone);
	}

	/**
	 * Tests Java-serialization-backed cloner with a Serializable input.
	 */
	@Test
	public void testJavaSerializationCloner() throws Exception {
		final ObjectCloner cloner = new JavaSerializationCloner();
		final HashMap<String, Object> original = new HashMap<>(ObjectClonerTest.sampleMap());
		final Map<String, Object> clone = cloner.clone(original);
		Assertions.assertNotSame(original, clone);
		Assertions.assertEquals(original, clone);
	}

	/**
	 * Tests Java-serialization-backed cloner rejects non-Serializable input.
	 */
	@Test
	public void testJavaSerializationClonerNonSerializable() {
		final ObjectCloner cloner = new JavaSerializationCloner();
		final Object nonSerializable = new Object();
		Assertions.assertThrows(IllegalArgumentException.class, () -> cloner.clone(nonSerializable));
	}

	/**
	 * Tests null is propagated through every cloner without error.
	 */
	@Test
	public void testNullObject() throws Exception {
		Assertions.assertNull(new ForyCloner(ObjectClonerTest.FORY).clone(null));
		Assertions.assertNull(new ObjectMapperCloner(ObjectClonerTest.MAPPER).clone(null));
		Assertions.assertNull(new JavaSerializationCloner().clone(null));
		Assertions.assertNull(new CompositeObjectCloner(List.of()).clone(null));
	}

	/**
	 * Tests the composite returns the result of the first cloner that
	 * succeeds.
	 */
	@Test
	public void testCompositeFirstSucceeds() throws Exception {
		final ObjectCloner failing = new ObjectCloner() {

			@Override
			public <ObjectType> ObjectType clone(
					final ObjectType object) {
				throw new RuntimeException("should not be called");
			}
		};
		final ObjectCloner composite = new CompositeObjectCloner(List.of(new ObjectMapperCloner(ObjectClonerTest.MAPPER), failing));
		final Map<String, Object> original = ObjectClonerTest.sampleMap();
		final Map<String, Object> clone = composite.clone(original);
		Assertions.assertNotSame(original, clone);
		Assertions.assertEquals(original, clone);
	}

	/**
	 * Tests the composite falls through a failing cloner to a successful
	 * one.
	 */
	@Test
	public void testCompositeFallsThrough() throws Exception {
		final ObjectCloner failing = new ObjectCloner() {

			@Override
			public <ObjectType> ObjectType clone(
					final ObjectType object) throws Exception {
				throw new Exception("boom");
			}
		};
		final ObjectCloner composite = new CompositeObjectCloner(List.of(failing, new ObjectMapperCloner(ObjectClonerTest.MAPPER)));
		final Map<String, Object> original = ObjectClonerTest.sampleMap();
		final Map<String, Object> clone = composite.clone(original);
		Assertions.assertEquals(original, clone);
	}

	/**
	 * Tests the composite throws when every cloner fails.
	 */
	@Test
	public void testCompositeAllFail() {
		final ObjectCloner failing = new ObjectCloner() {

			@Override
			public <ObjectType> ObjectType clone(
					final ObjectType object) throws Exception {
				throw new Exception("boom");
			}
		};
		final ObjectCloner composite = new CompositeObjectCloner(List.of(failing, failing));
		Assertions.assertThrows(IntegrationException.class, () -> composite.clone(ObjectClonerTest.sampleMap()));
	}

	/**
	 * Tests the composite throws when configured with no cloners.
	 */
	@Test
	public void testCompositeEmpty() {
		final ObjectCloner composite = new CompositeObjectCloner(List.of());
		Assertions.assertThrows(IntegrationException.class, () -> composite.clone(ObjectClonerTest.sampleMap()));
	}

}
