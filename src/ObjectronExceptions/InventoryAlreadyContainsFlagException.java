package ObjectronExceptions;

/**
 * Exception that is thrown when a player tries to pick up a flag while his or her
 * inventory already contains a flag.
 *
 */
public class InventoryAlreadyContainsFlagException extends IllegalActionException {
	
private static final long	serialVersionUID	= -3774112096236660086L;
	
	@SuppressWarnings("javadoc")
	public InventoryAlreadyContainsFlagException(String message) {
		super(message);
	}
	
}
