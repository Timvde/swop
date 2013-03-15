package ObjectronExceptions;

/**
 * This class is an exception to represent an illegal move in the game.
 * 
 * @author tom
 *
 */
public class IllegalMoveException extends Exception {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4958248113296080339L;
	private String message;
	
	@SuppressWarnings("javadoc")
	public IllegalMoveException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
