package org.coldis.library.serialization.positional;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks an attribute that should be (de)serialized.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface PositionalAttribute {

	/**
	 * Null filler holder. This filler will be replaced with the (de)serializer
	 * default filler.
	 */
	public static final char NULL_FILLER_HOLDER = '%';

	/**
	 * Serialized attribute initial string position (inclusive).
	 */
	public int initialPosition();

	/**
	 * Serialized a final string position (exclusive).
	 */
	public int finalPosition();

	/**
	 * PositionalSerializerInterface to be used. Default is
	 * {@link StringSerializer}.
	 */
	@SuppressWarnings("rawtypes")
	public Class<? extends PositionalSerializerInterface> positionalSerializerInterface() default StringSerializer.class;

	/**
	 * PositionalSerializerInterface constructor parameter.
	 */
	public String serializerInitParam() default "";

	/**
	 * De-serializer to be used. Default is {@link StringSerializer}.
	 */
	@SuppressWarnings("rawtypes")
	public Class<? extends PostionalDeserializerInterface> postionalDeserializerInterface() default StringSerializer.class;

	/**
	 * De-serializer constructor parameter.
	 */
	public String deserializerInitParam() default "";

	/**
	 * Filler to be used. By default, filler is the null filler holder.
	 */
	public char filler() default NULL_FILLER_HOLDER;

	/**
	 * If the filler should be used in the left (or right) of the serialized
	 * attribute.
	 */
	public boolean fillLeft() default false;

	/**
	 * If filler should be removed before de-seralization.
	 */
	public boolean unfill() default true;

	/**
	 * The default serialized value (for empty values).
	 */
	public String defaultValue() default "";
}
