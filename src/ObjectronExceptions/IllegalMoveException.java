package ObjectronExceptions;

/**
 * This class is an exception to represent an illegal move in the game.
 * 
 * @author tom
 *
 */
public class IllegalMoveException extends Exception {
	
	private static final long	serialVersionUID	= 1L;
	private String message;
	
	/**
	 * make a new {@link IllegalMoveException}
	 * @param message
	 */
	public IllegalMoveException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
