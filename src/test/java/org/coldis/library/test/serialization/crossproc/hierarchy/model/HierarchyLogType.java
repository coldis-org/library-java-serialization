package org.coldis.library.test.serialization.crossproc.hierarchy.model;

/**
 * Enum used by the BusinessLog-mirror cross-class fixture. Both the Model and
 * the DTO sides reference this same enum so the field encoding resolves to the
 * same registered class on either peer.
 */
public enum HierarchyLogType {

	CREATED,

	UPDATED,

	DELETED;

}
