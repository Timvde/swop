package ObjectronExceptions.builderExceptions;

import grid.builder.GridBuilder;

/**
 * Thrown to indicate the {@link GridBuilder builder} cannot place an item at a specified coordinate.
 */
public class CannotPlaceItemException extends GridBuildException{

	private static final long	serialVersionUID	= 3842690116254373490L;

	@SuppressWarnings("javadoc")
	public CannotPlaceItemException(String message) {
		super(message);
	}
	
	@SuppressWarnings("javadoc")
	public CannotPlaceItemException() {
		super(null);
	}
	
}
