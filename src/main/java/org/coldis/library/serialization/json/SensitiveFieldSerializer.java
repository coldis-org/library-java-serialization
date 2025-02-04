package org.coldis.library.serialization.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
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
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/** Sensitivity field serializer. */
public class SensitiveFieldSerializer<Type> extends JsonSerializer<Type> implements ContextualSerializer {

	/** Mask base. */
	public static final String MASK_BASE = "-+-+-+-+-+-+-";

	/**
	 * Default to be masked regex.
	 */
	public static final String DEFAULT_TO_BE_MASKED_REGEX = ".*";

	private static final Map<String, JsonSerializer<?>> NUMBER_SERIALIZERS;

	/** Original class. */
	private Class<?> originalClass;

	/** Sensitive. */
	private boolean sensitive;

	/** To be masked regex. */
	private final String toBeMaskedRegex;

	/** Minimum mask absolute size. */
	private final Integer minMaskAbsoluteSize;

	/** Minimum mask relative size. */
	private final BigDecimal minMaskRelativeSize;

	/** Delegate */
	private JsonSerializer<Type> delegate;

	/** Initializes the number serializers. */
	static {
		NUMBER_SERIALIZERS = new HashMap<>();
		NumberSerializers.addAll(SensitiveFieldSerializer.NUMBER_SERIALIZERS);
		SensitiveFieldSerializer.NUMBER_SERIALIZERS.put(BigInteger.class.getName(), new NumberSerializer(BigInteger.class));
		SensitiveFieldSerializer.NUMBER_SERIALIZERS.put(BigDecimal.class.getName(), new NumberSerializer(BigDecimal.class));
	}

	/** Constructor. */
	public SensitiveFieldSerializer(final Class<?> originalClass, final boolean sensitive, final String toBeMaskedRegex, final JsonSerializer<Type> delegate) {
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
		this.toBeMaskedRegex = (StringUtils.isBlank(toBeMaskedRegex) ? SensitiveFieldSerializer.DEFAULT_TO_BE_MASKED_REGEX : toBeMaskedRegex);
		this.delegate = delegate;
	}

	/** No arguments constructor. */
	public SensitiveFieldSerializer(final Class<?> originalClass) {
		this(originalClass, true, null, null);
	}

	/**
	 * Gets the actual class for the property.
	 *
	 * @param  property      The property.
	 * @param  originalClass The original class.
	 * @return               The actual class for the property.
	 */
	public static Class<?> getActualClass(
			final BeanProperty property,
			final Class<?> originalClass) {
		return ((property != null) ? property.getType().getRawClass() : originalClass);
	}

	/**
	 * Checks if the class is a number type.
	 */
	private static boolean isNumberType(
			final Class<?> clazz) {
		return (clazz != null) && (Number.class.isAssignableFrom(clazz) || SensitiveFieldSerializer.NUMBER_SERIALIZERS.containsKey(clazz.getName()));
	}

	/**
	 * Checks if the class is a number type.
	 */
	public static boolean isNumberType(
			final BeanProperty property,
			final Class<?> originalClass) {
		final Class<?> propertyClass = (property == null ? null : property.getType().getRawClass());
		return SensitiveFieldSerializer.isNumberType(propertyClass) || SensitiveFieldSerializer.isNumberType(originalClass);
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

		// Gets the sensitive attribute annotation.
		final SensitiveAttribute sensitiveAttribute = (property == null ? null : property.getAnnotation(SensitiveAttribute.class));
		final String sensitiveAttributeRegex = (sensitiveAttribute == null ? SensitiveFieldSerializer.DEFAULT_TO_BE_MASKED_REGEX
				: sensitiveAttribute.toBeMaskedRegex());

		// Default serializer is the String serializer.
		this.delegate = (JsonSerializer<Type>) ToStringSerializer.instance;
		final Class<?> actualSerializedClass = SensitiveFieldSerializer.getActualClass(property, this.originalClass);

		// If it is a string, uses the string serializer.
		if (String.class.isAssignableFrom(actualSerializedClass)) {
			this.delegate = (JsonSerializer<Type>) new StringSerializer();
		}
		// If it is a number, uses the number serializer.
		else if (SensitiveFieldSerializer.isNumberType(property, this.originalClass)) {
			final JsonSerializer<?> numberSerializer = SensitiveFieldSerializer.NUMBER_SERIALIZERS.get(actualSerializedClass.getName());
			if (numberSerializer == null) {
				this.delegate = (JsonSerializer<Type>) SensitiveFieldSerializer.NUMBER_SERIALIZERS.get(this.originalClass.getName());
			}
			if (numberSerializer != null) {
				this.delegate = (JsonSerializer<Type>) numberSerializer;
			}
		}
		// If it is a contextual serializer, creates the contextual serializer.
		if (this.delegate instanceof final ContextualSerializer contextualSerializer) {
			this.delegate = (JsonSerializer<Type>) contextualSerializer.createContextual(serializerProvider, property);
		}

		// If it is not a sensitive view and it is a sensitive field, uses the sensitive
		// field serializer.
		JsonSerializer<Type> serializer;
		if (sensitiveField) {
			serializer = new SensitiveFieldSerializer<>(this.originalClass, true, sensitiveAttributeRegex, this.delegate);
		}
		// If it is not a personal view and it is a personal field, uses the sensitive
		// field serializer.
		else if (personalFields) {
			serializer = new SensitiveFieldSerializer<>(this.originalClass, false, sensitiveAttributeRegex, this.delegate);
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
		final boolean noView = (activeView == null);
		final boolean sensitiveView = (!noView) && ModelView.Sensitive.class.isAssignableFrom(activeView);
		final boolean personalView = (!noView) && ModelView.Personal.class.isAssignableFrom(activeView);

		// If it is not a personal/sensitive view.
		if (!noView && ((this.sensitive && !sensitiveView) || !personalView)) {
			// Writes the value as null if it is null.
			if (value == null) {
				jsonGenerator.writeNull();
			}
			// Masks the value.
			else {
				// Gets the string value print size.
				final String stringValue = Objects.toString(value);

				// Gets the content to be masked.
				// final String regex = "[^@]*";
				final String regex = this.toBeMaskedRegex;
				final Matcher toBeMaskedMatcher = Pattern.compile(regex).matcher(stringValue);
				final boolean matchFound = toBeMaskedMatcher.find();
				if (!matchFound || !toBeMaskedMatcher.toMatchResult().hasMatch()
						|| Objects.equals(toBeMaskedMatcher.toMatchResult().start(), toBeMaskedMatcher.toMatchResult().end())) {
					Pattern.compile(SensitiveFieldSerializer.DEFAULT_TO_BE_MASKED_REGEX).matcher(stringValue);
					toBeMaskedMatcher.find();
				}
				final MatchResult toBeMaskedMatchResult = toBeMaskedMatcher.toMatchResult();
				final String toBeMaskedContent = stringValue.substring(toBeMaskedMatchResult.start(), toBeMaskedMatchResult.end());
				final Integer toBeMaskedContentSize = toBeMaskedContent.length();
				final int toBeMaskedContentPrintSize = (Math.min(this.minMaskRelativeSize.multiply(new BigDecimal(toBeMaskedContentSize)).intValue(),
						SensitiveFieldSerializer.MASK_BASE.length() - this.minMaskAbsoluteSize) / 2) * 2;

				// Replaces the content to be masked.
				final int actualMaskSize = (SensitiveFieldSerializer.MASK_BASE.length() - toBeMaskedContentPrintSize);
				final String maskedContent = stringValue.substring(0, toBeMaskedContentPrintSize / 2)
						+ SensitiveFieldSerializer.MASK_BASE.substring(toBeMaskedContentPrintSize / 2, (actualMaskSize + (toBeMaskedContentPrintSize / 2)))
						+ stringValue.substring(toBeMaskedContentSize - (toBeMaskedContentPrintSize / 2));
				final String printValue = stringValue.replace(toBeMaskedContent, maskedContent);
				jsonGenerator.writeString(printValue);
			}
		}
		// Otherwise, uses the delegate.
		else {
			this.delegate.serialize(value, jsonGenerator, serializerProvider);
		}
	}

}
