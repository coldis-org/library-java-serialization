package org.coldis.library.serialization.csv;

import org.coldis.library.serialization.ObjectMapperHelper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;

/**
 * CSV helper methods.
 */
public class CsvHelper {

	/**
	 * Object mapper.
	 */
	private static CsvMapper objectMapper;

	/**
	 * Gets a CSV object mapper.
	 *
	 * @param  packagesNames Packages names.
	 * @return               A CSV object mapper.
	 */
	public static CsvMapper createObjectMapper(
			final String... packagesNames) {
		// Configures a CSV mapper.
		CsvMapper objectMapper = (CsvMapper) new CsvMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).enable(MapperFeature.DEFAULT_VIEW_INCLUSION);
		// Registers the subtypes from the given pagackes.
		objectMapper = (CsvMapper) ObjectMapperHelper.addSubtypesFromPackage(objectMapper, packagesNames);
		// Returns the mapper.
		return objectMapper;
	}

	/**
	 * Gets a CSV object mapper.
	 *
	 * @param  packagesNames Packages names.
	 * @return               A CSV object mapper.
	 */
	public static CsvMapper getObjectMapper(
			final String... packagesNames) {
		// If the object mapper has not been configured yet.
		if (CsvHelper.objectMapper == null) {
			CsvHelper.objectMapper = CsvHelper.createObjectMapper(packagesNames);
		}
		// Returns the configured object mapper.
		return CsvHelper.objectMapper;
	}

}
