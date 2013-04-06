package ObjectronExceptions;

/**
 * This class is an exception to represent the situation that a light grenade cannot be placed
 * on a specific square.
 * 
 * @author tom
 *
 */
public class CannotPlaceLightGrenadeException extends Exception {
	
	private static final long	serialVersionUID	= -4958248113296080339L;
	private String message;
	
	@SuppressWarnings("javadoc")
	public CannotPlaceLightGrenadeException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
