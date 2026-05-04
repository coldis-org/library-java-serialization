package org.coldis.library.test.serialization.excluded;

import org.coldis.library.model.Typable;
import org.springframework.stereotype.Service;

/**
 * Spring-stereotype-annotated class for verifying that the helper's
 * package scan filter keeps it out of Fory registration.
 */
@Service
public class ExcludedService implements Typable {

	private static final long serialVersionUID = 1L;

	@Override
	public String getTypeName() {
		return "coldis.test.excluded.ExcludedService";
	}

}
