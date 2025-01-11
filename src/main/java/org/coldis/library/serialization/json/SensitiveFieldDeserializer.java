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

	/** Delegate deserializer. */
	JsonDeserializer<?> delegate;

	/** Constructor. */
	public SensitiveFieldDeserializer(final JsonDeserializer<Type> delegate) {
		super();
		this.delegate = (delegate == null ? new StringDeserializer() : delegate);
	}

	/** Constructor. */
	public SensitiveFieldDeserializer() {
		this(null);
	}

	/**
	 * @see com.fasterxml.jackson.databind.deser.ContextualDeserializer#createContextual(com.fasterxml.jackson.databind.DeserializationContext,
	 *      com.fasterxml.jackson.databind.BeanProperty)
	 */
	@Override
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
		JsonDeserializer<?> deserializer = StringDeserializer.instance;

		// If it is a number, uses the number deserializer.
		if (property.getType().isTypeOrSubTypeOf(Number.class)) {
			final JsonDeserializer<?> numberSerializer = NumberDeserializers.find(property.getType().getRawClass(), property.getType().getRawClass().getName());
			if (numberSerializer != null) {
				deserializer = numberSerializer;
			}
		}

		// If it is a contextual deserializer, creates the contextual deserializer.
		if ((!(deserializer instanceof SensitiveFieldDeserializer)) && deserializer instanceof final ContextualDeserializer contextualNumberSerializer) {
			deserializer = contextualNumberSerializer.createContextual(deserializationContext, property);
		}

		// If it is a sensitive field, uses the sensitive field deserializer.
		if (sensitiveField || personalFields) {
			deserializer = new SensitiveFieldDeserializer<>(deserializer);
		}

		// Returns the deserializer.
		return deserializer;

	}

	/**
	 * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser,
	 *      com.fasterxml.jackson.databind.DeserializationContext)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Type deserialize(
			final JsonParser jsonParser,
			final DeserializationContext deserializationContext) throws IOException, JacksonException {
		Type value = null;
		final String textValue = jsonParser.getText();
		final String maskedValue = (textValue == null ? null : textValue.substring(3, SensitiveFieldSerializer.MASK_BASE.length() - 3));
		if ((textValue == null) || (textValue.length() != SensitiveFieldSerializer.MASK_BASE.length())
				|| !maskedValue.equals(SensitiveFieldDeserializer.MASKED_VALUE)) {
			value = (Type) this.delegate.deserialize(jsonParser, deserializationContext);
		}
		return value;
	}

}
