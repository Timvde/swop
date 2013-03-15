package ObjectronExceptions;

/**
 * This class is an exception to represent an illegal move in the game.
 * 
 * @author tom
 *
 */
public class IllegalMoveException extends Exception {
	
	private String message;
	
	public IllegalMoveException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
