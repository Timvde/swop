package ObjectronExceptions.builderExceptions;

/**
 * Thrown to indicate the grid specified in a file doesn't adhere the correct rules.
 */
public class InvalidGridFileException extends GridBuildException{
	
	private static final long	serialVersionUID	= 8090721602642296025L;

	@SuppressWarnings("javadoc")
	public InvalidGridFileException(String message) {
		super(message);
	}
}
