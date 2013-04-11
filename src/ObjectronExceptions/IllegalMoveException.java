package ObjectronExceptions;

import player.IPlayer;

/**
 * Thrown to indicate somethong went wrong with moving an {@link IPlayer}.
 */
public class IllegalMoveException extends IllegalActionException {

	private static final long	serialVersionUID	= -4958248113296080339L;
	
	@SuppressWarnings("javadoc")
	public IllegalMoveException(String message) {
		super(message);
	}
}
