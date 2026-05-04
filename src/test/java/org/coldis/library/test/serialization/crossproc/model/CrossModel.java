package org.coldis.library.test.serialization.crossproc.model;

import org.coldis.library.model.Typable;

/**
 * Cross-process test fixture: model side. Lives in its own subpackage so
 * the helper's package scan can include it without also picking up the
 * matching DTO.
 */
public class CrossModel implements Typable {

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
		return CrossModel.TYPE_NAME;
	}

}
