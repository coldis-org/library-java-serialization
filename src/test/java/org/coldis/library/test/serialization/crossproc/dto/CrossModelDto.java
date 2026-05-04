package org.coldis.library.test.serialization.crossproc.dto;

import org.coldis.library.dto.DtoOrigin;
import org.coldis.library.model.Typable;

/**
 * Cross-process test fixture: DTO side. Lives in its own subpackage so a
 * helper-built Fory scanned at this package alone has only the DTO on
 * its registry, mimicking a consumer service that does not have the
 * model on its classpath. The shared logical name in getTypeName()
 * matches CrossModel's so the helper makes this DTO canonical for that
 * name.
 */
@DtoOrigin(originalClassName = "org.coldis.library.test.serialization.crossproc.model.CrossModel")
public class CrossModelDto implements Typable {

	private static final long serialVersionUID = 1L;

	public static final String TYPE_NAME = "coldis.test.crossproc.CrossModel";

	private Long id;

	private String label;

	public Long getId() {
		return this.id;
	}

	public void setId(
			final Long id) {
		this.id = id;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(
			final String label) {
		this.label = label;
	}

	@Override
	public String getTypeName() {
		return CrossModelDto.TYPE_NAME;
	}

}
