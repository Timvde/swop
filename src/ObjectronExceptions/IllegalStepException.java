package ObjectronExceptions;

import player.Player;
import square.Direction;

/**
 * Trhown when an {@link Player} tried to move one step in a specified
 * {@link Direction} when he's not allowed to do so.
 */
public class IllegalStepException extends IllegalMoveException {

	private static final long	serialVersionUID	= -3464766151401785640L;

	@SuppressWarnings("javadoc")
	public IllegalStepException(String message) {
		super(message);
	}
	
}
