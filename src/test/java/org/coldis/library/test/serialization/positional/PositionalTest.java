package org.coldis.library.test.serialization.positional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.coldis.library.serialization.positional.PositionalSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Custom serializer test.
 */
public class PositionalTest {

	/**
	 * Test data.
	 */
	private static final List<Object> TEST_DATA = List.of(

			new CaminhaoVantagensRequest("1", LocalDate.of(2022, 1, 1), "caminhao", LocalDate.of(2022, 1, 1), LocalDate.of(2023, 1, 1), "123",
					"Rômulo Coutinho", "F", 22222222222L, "Rua bla bla", 123L, "Apt.123", "Copa", "Rio de Janeiro", "RJ", 22040010L, 21L, 995005990L,
					"21645345", "RJ", new BigDecimal("10.00"), new BigDecimal("0.50"), new BigDecimal("10.50"), "C", 12L, new BigDecimal("10.50"), "CAMINHAO",
					new BigDecimal("126.00"), LocalDate.of(2022, 1, 1), LocalDate.of(1985, 1, 23), "S", "M", "romulo@gmail.com", LocalDate.of(2022, 1, 5))

	);

	/**
	 * Tests a custom serialization.
	 *
	 * @throws Exception If the test fails.
	 */
	@Test
	public void testSerialization() throws Exception {
		for (final Object testData : PositionalTest.TEST_DATA) {
			final PositionalSerializer<Object> customComplexSerializer = new PositionalSerializer<>(testData.getClass());
			final String serializedTestData = customComplexSerializer.serialize(testData);
			final Object deserializedTestData = customComplexSerializer.deserialize(serializedTestData);
			System.out.println(serializedTestData);
			Assertions.assertEquals(testData, deserializedTestData);
		}

	}

}
