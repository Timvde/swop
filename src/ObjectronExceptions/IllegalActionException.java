package ObjectronExceptions;

import player.Player;

/**
 * Thrown to indicate that an {@link Player} has invoked an action-method when
 * he's not allowed to do so.
 */
public class IllegalActionException extends RuntimeException {
	
	private static final long	serialVersionUID	= 7293729431505836882L;
	
	
	@SuppressWarnings("javadoc")
	public IllegalActionException(String message) {
		super(message);
	}
	
}
