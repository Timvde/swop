package ObjectronExceptions;

import player.IPlayer;

/**
 * Thrown to indicate a {@link IPlayer player} could not finish his move.
 */
public class IllegalMoveException extends IllegalActionException {

	private static final long	serialVersionUID	= -4958248113296080339L;
	
	@SuppressWarnings("javadoc")
	public IllegalMoveException(String message) {
		super(message);
	}
}
