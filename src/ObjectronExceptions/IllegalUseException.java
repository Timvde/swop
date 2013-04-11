package ObjectronExceptions;

import item.Item;

/**
 * Thrown to indicate something wend wrong when someone tried to use an {@link Item}.
 */
public class IllegalUseException extends IllegalActionException {

	private static final long	serialVersionUID	= -7565816318204048979L;
	
	@SuppressWarnings("javadoc")
	public IllegalUseException(String message) {
		super(message);
	}
}
