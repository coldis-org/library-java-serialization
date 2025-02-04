package org.coldis.library.serialization.json;

import java.io.IOException;
import java.util.Arrays;

import org.coldis.library.model.view.ModelView;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

/** Sensitivity field deserializer. */
public class SensitiveFieldDeserializer<Type> extends JsonDeserializer<Type> implements ContextualDeserializer {

	/** Masked value. */
	private static final String MASKED_VALUE = SensitiveFieldSerializer.MASK_BASE.substring(3, SensitiveFieldSerializer.MASK_BASE.length() - 3);

	/** Original class. */
	private Class<?> originalClass;

	/** Delegate deserializer. */
	JsonDeserializer<Type> delegate;

	/** Constructor. */
	@SuppressWarnings("unchecked")
	public SensitiveFieldDeserializer(final Class<?> originalClass, final JsonDeserializer<Type> delegate) {
		super();
		this.originalClass = originalClass;
		this.delegate = (delegate == null ? (JsonDeserializer<Type>) new StringDeserializer() : delegate);
	}

	/** Constructor. */
	public SensitiveFieldDeserializer(final Class<?> originalClass) {
		this(originalClass, null);
	}

	/**
	 * @see com.fasterxml.jackson.databind.deser.ContextualDeserializer#createContextual(com.fasterxml.jackson.databind.DeserializationContext,
	 *      com.fasterxml.jackson.databind.BeanProperty)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public JsonDeserializer<?> createContextual(
			final DeserializationContext deserializationContext,
			final BeanProperty property) throws JsonMappingException {

		// Gets the view for the field.
		final JsonView propertyJsonView = (property == null ? null : property.getAnnotation(JsonView.class));
		final boolean sensitiveField = (propertyJsonView != null)
				&& Arrays.stream(propertyJsonView.value()).anyMatch(view -> ModelView.Sensitive.class.isAssignableFrom(view));
		final boolean personalFields = (propertyJsonView != null)
				&& Arrays.stream(propertyJsonView.value()).anyMatch(view -> ModelView.Personal.class.isAssignableFrom(view));

		// Default deserializer is the String deserializer.
		this.delegate = (JsonDeserializer<Type>) StringDeserializer.instance;
		final Class<?> actualSerializedClass = SensitiveFieldSerializer.getActualClass(property, this.originalClass);

		// If it is a number, uses the number deserializer.
		if (SensitiveFieldSerializer.isNumberType(property, this.originalClass)) {
			final JsonDeserializer<?> numberDeserializer = NumberDeserializers.find(actualSerializedClass, actualSerializedClass.getName());
			if (numberDeserializer == null) {
				this.delegate = (JsonDeserializer<Type>) NumberDeserializers.find(this.originalClass, this.originalClass.getName());
			}
			if (numberDeserializer != null) {
				this.delegate = (JsonDeserializer<Type>) numberDeserializer;
			}
		}

		// If it is a contextual deserializer, creates the contextual deserializer.
		if (this.delegate instanceof final ContextualDeserializer contextualDeserializer) {
			this.delegate = (JsonDeserializer<Type>) contextualDeserializer.createContextual(deserializationContext, property);
		}

		// If it is a sensitive field, uses the sensitive field deserializer.
		JsonDeserializer<Type> deserializer;
		if (sensitiveField || personalFields) {
			deserializer = new SensitiveFieldDeserializer<>(this.originalClass, this.delegate);
		}
		// If not a sensitive field, uses the delegate.
		else {
			deserializer = this.delegate;
		}

		// Returns the deserializer.
		return deserializer;

	}

	/**
	 * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser,
	 *      com.fasterxml.jackson.databind.DeserializationContext)
	 */
	@Override
	public Type deserialize(
			final JsonParser jsonParser,
			final DeserializationContext deserializationContext) throws IOException, JacksonException {
		Type value = null;
		final String textValue = jsonParser.getText();
		if ((textValue == null) || (!textValue.contains(SensitiveFieldDeserializer.MASKED_VALUE))) {
			value = this.delegate.deserialize(jsonParser, deserializationContext);
		}
		return value;
	}

}
