package org.coldis.library.serialization;

import java.util.List;

import org.coldis.library.exception.IntegrationException;
import org.coldis.library.model.SimpleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Composite {@link ObjectCloner} that tries an ordered list of underlying
 * cloners and returns the result of the first one that succeeds. If every
 * configured cloner fails, the last error is wrapped in an
 * {@link IntegrationException}.
 */
public class CompositeObjectCloner implements ObjectCloner {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CompositeObjectCloner.class);

	/**
	 * Cloners to try in order.
	 */
	private final List<ObjectCloner> cloners;

	/**
	 * Constructor.
	 *
	 * @param cloners Cloners to try in order.
	 */
	public CompositeObjectCloner(final List<ObjectCloner> cloners) {
		this.cloners = cloners;
	}

	/**
	 * @see ObjectCloner#clone(Object)
	 */
	@Override
	public <ObjectType> ObjectType clone(
			final ObjectType object) throws IntegrationException {
		if (object == null) {
			return null;
		}
		Exception lastException = null;
		for (final ObjectCloner cloner : this.cloners) {
			try {
				return cloner.clone(object);
			}
			catch (final Exception exception) {
				lastException = exception;
				CompositeObjectCloner.LOGGER.debug("Cloner {} failed for object of type {}: {}", cloner.getClass().getName(), object.getClass().getName(),
						exception.getMessage());
			}
		}
		throw new IntegrationException(
				new SimpleMessage("clone.error", "Could not clone object of type " + object.getClass().getName()
						+ (lastException == null ? " (no cloners configured)." : ": " + lastException.getClass() + " - " + lastException.getLocalizedMessage())),
				lastException);
	}

}
