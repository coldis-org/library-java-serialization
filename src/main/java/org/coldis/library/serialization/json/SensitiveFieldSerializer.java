package org.coldis.library.serialization.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.coldis.library.model.view.ModelView;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;

/** Sensitivity field serializer. */
public class SensitiveFieldSerializer<Type> extends JsonSerializer<Type> implements ContextualSerializer {

	/** Mask base. */
	public static final String MASK_BASE = "-+-+-+-+-+-+-";

	private static final Map<String, JsonSerializer<?>> NUMBER_SERIALIZERS;

	/** Original class. */
	private Class<?> originalClass;

	/** Sensitive. */
	private boolean sensitive;

	/** Minimum mask absolute size. */
	private final Integer minMaskAbsoluteSize;

	/** Minimum mask relative size. */
	private final BigDecimal minMaskRelativeSize;

	/** Delegate */
	private JsonSerializer<Type> delegate;

	static {
		NUMBER_SERIALIZERS = new HashMap<>();
		NumberSerializers.addAll(SensitiveFieldSerializer.NUMBER_SERIALIZERS);
		SensitiveFieldSerializer.NUMBER_SERIALIZERS.put(BigInteger.class.getName(), new NumberSerializer(BigInteger.class));
		SensitiveFieldSerializer.NUMBER_SERIALIZERS.put(BigDecimal.class.getName(), new NumberSerializer(BigDecimal.class));
	}

	/** Constructor. */
	public SensitiveFieldSerializer(final Class<?> originalClass, final boolean sensitive, final JsonSerializer<Type> delegate) {
		super();
		this.originalClass = originalClass;
		this.sensitive = sensitive;
		if (sensitive) {
			this.minMaskAbsoluteSize = 13;
			this.minMaskRelativeSize = BigDecimal.ONE;
		}
		else {
			this.minMaskAbsoluteSize = 7;
			this.minMaskRelativeSize = BigDecimal.valueOf(0.5);
		}
		this.delegate = delegate;
	}

	/** No arguments constructor. */
	public SensitiveFieldSerializer(final Class<?> originalClass) {
		this(originalClass, true, null);
	}
	
	/**
	 * Checks if the class is a number type.
	 */
	public static boolean isNumberType(final Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz) || clazz ==int.class || clazz == long.class || clazz == double.class || clazz == float.class || clazz == short.class || clazz == byte.class;
    }

	/**
	 * @see com.fasterxml.jackson.databind.ser.ContextualSerializer#createContextual(com.fasterxml.jackson.databind.SerializerProvider,
	 *      com.fasterxml.jackson.databind.BeanProperty)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public JsonSerializer<Type> createContextual(
			final SerializerProvider serializerProvider,
			final BeanProperty property) throws JsonMappingException {

		// Gets the view for the field.
		final JsonView propertyJsonView = (property == null ? null : property.getAnnotation(JsonView.class));
		final boolean sensitiveField = (propertyJsonView != null)
				&& Arrays.stream(propertyJsonView.value()).anyMatch(view -> ModelView.Sensitive.class.isAssignableFrom(view));
		final boolean personalFields = (propertyJsonView != null)
				&& Arrays.stream(propertyJsonView.value()).anyMatch(view -> ModelView.Personal.class.isAssignableFrom(view));

		// Default serializer is the String serializer.
		this.delegate = (JsonSerializer<Type>) new StringSerializer();
		// If it is a number, uses the number serializer.
		final Class<?> actualSerializedClass = ((property != null) ? property.getType().getRawClass() : this.originalClass);
		if (isNumberType(actualSerializedClass)) {
			final JsonSerializer<?> numberSerializer = SensitiveFieldSerializer.NUMBER_SERIALIZERS.get(actualSerializedClass.getName());
			if (numberSerializer != null) {
				this.delegate = (JsonSerializer<Type>) numberSerializer;
			}
		}
		// If it is a contextual serializer, creates the contextual serializer.
		if (this.delegate instanceof final ContextualSerializer contextualSerializer) {
			this.delegate = (JsonSerializer<Type>) contextualSerializer.createContextual(serializerProvider, property);
		}

		JsonSerializer<Type> serializer;
		// If it is not a sensitive view and it is a sensitive field, uses the sensitive
		// field serializer.
		if (sensitiveField) {
			serializer = new SensitiveFieldSerializer<>(this.originalClass, true, this.delegate);
		}
		// If it is not a personal view and it is a personal field, uses the sensitive
		// field serializer.
		else if (personalFields) {
			serializer = new SensitiveFieldSerializer<>(this.originalClass, false, this.delegate);
		}
		// If not a sensitive field, uses the delegate.
		else {
			serializer = this.delegate;
		}

		// Returns the serializer.
		return serializer;

	}

	/**
	 * @see com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
	 *      com.fasterxml.jackson.core.JsonGenerator,
	 *      com.fasterxml.jackson.databind.SerializerProvider)
	 */
	@Override
	public void serialize(
			final Type value,
			final JsonGenerator jsonGenerator,
			final SerializerProvider serializerProvider) throws IOException {
		// Gets the active view.
		final Class<?> activeView = serializerProvider.getActiveView();
		final boolean sensitiveView = (activeView != null) && ModelView.Sensitive.class.isAssignableFrom(activeView);
		final boolean personalView = (activeView != null) && ModelView.Personal.class.isAssignableFrom(activeView);

		// If it is not a personal/sensitive view.
		if ((this.sensitive && !sensitiveView) || !personalView) {
			// Writes the value as null if it is null.
			if (value == null) {
				jsonGenerator.writeNull();
			}
			// Masks the value.
			else {
				final String stringValue = Objects.toString(value);
				final Integer stringValueSize = stringValue.length();
				final int printSize = (Math.min(this.minMaskRelativeSize.multiply(new BigDecimal(stringValueSize)).intValue(),
						SensitiveFieldSerializer.MASK_BASE.length() - this.minMaskAbsoluteSize) / 2) * 2;
				final int actualMaskSize = (SensitiveFieldSerializer.MASK_BASE.length() - printSize);
				final String printValue = stringValue.substring(0, printSize / 2)
						+ SensitiveFieldSerializer.MASK_BASE.substring(printSize / 2, (actualMaskSize + (printSize / 2)))
						+ stringValue.substring(stringValueSize - (printSize / 2));
				jsonGenerator.writeString(printValue);
			}
		}
		// Otherwise, uses the delegate.
		else {
			this.delegate.serialize(value, jsonGenerator, serializerProvider);
		}
	}

}
