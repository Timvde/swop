package ObjectronExceptions;

/**
 * Thrown to indicate something went wrong with teleporting.
 */
public class IllegalTeleportException extends IllegalMoveException{

	private static final long	serialVersionUID	= 5579597320204801832L;

	@SuppressWarnings("javadoc")
	public IllegalTeleportException(String message) {
		super(message);
	}
	
}
