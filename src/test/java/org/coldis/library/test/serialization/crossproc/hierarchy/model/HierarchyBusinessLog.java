package org.coldis.library.test.serialization.crossproc.hierarchy.model;

import java.util.Objects;

import org.coldis.library.dto.DtoAttribute;
import org.coldis.library.dto.DtoType;
import org.coldis.library.model.Typable;

/**
 * Concrete Model. Mirrors service-log's {@code BusinessLog}: extends the
 * abstract Model intermediate and is the source for a generated concrete
 * DTO. Reports a logical type name via {@link Typable} so the helper
 * canonicalises both Model and generated DTO under the same registered
 * name on either peer.
 */
@DtoType(
		targetPath = "src/test/java",
		namespace = "org.coldis.library.test.serialization.crossproc.hierarchy.dto"
)
public class HierarchyBusinessLog extends HierarchyAbstractLog implements Typable {

	private static final long serialVersionUID = 1L;

	public static final String TYPE_NAME = "coldis.test.hierarchy.BusinessLog";

	private String businessKey;

	public String getBusinessKey() {
		return this.businessKey;
	}

	public void setBusinessKey(
			final String businessKey) {
		this.businessKey = businessKey;
	}

	@Override
	@DtoAttribute(
			modifiers = { "final" },
			readOnly = true,
			defaultValue = HierarchyBusinessLog.TYPE_NAME
	)
	public String getTypeName() {
		return HierarchyBusinessLog.TYPE_NAME;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.businessKey);
	}

	@Override
	public boolean equals(
			final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof HierarchyBusinessLog) || !super.equals(obj)) {
			return false;
		}
		final HierarchyBusinessLog other = (HierarchyBusinessLog) obj;
		return Objects.equals(this.businessKey, other.businessKey);
	}

}
