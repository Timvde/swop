package ObjectronExceptions.builderExceptions;

import grid.Grid;

/**
 * Thrown to indicate a problem occured when constructing the {@link Grid}.
 */
public class GridBuildException extends RuntimeException {
	
	private static final long	serialVersionUID	= -4478429767740354335L;

	@SuppressWarnings("javadoc")
	public GridBuildException(String message) {
		super(message);
	}
	
}
