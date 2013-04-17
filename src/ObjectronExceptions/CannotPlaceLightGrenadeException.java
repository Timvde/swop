package ObjectronExceptions;

/**
 * This class is an exception to represent the situation that a light grenade cannot be placed
 * on a specific square.
 * 
 * @author tom
 *
 */
public class CannotPlaceLightGrenadeException extends IllegalUseException {
	
	private static final long	serialVersionUID	= -4958248113296080339L;
	
	@SuppressWarnings("javadoc")
	public CannotPlaceLightGrenadeException(String message) {
		super(message);
	}
}
