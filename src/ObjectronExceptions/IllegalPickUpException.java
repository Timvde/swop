package ObjectronExceptions;

import item.Item;

/**
 * Thrown to indicate an {@link Item} could not succesfully be picke up.
 */
public class IllegalPickUpException extends IllegalActionException {
	
	private static final long	serialVersionUID	= -3774112096232334686L;
	
	@SuppressWarnings("javadoc")
	public IllegalPickUpException(String message) {
		super(message);
	}
}
