package ObjectronExceptions;

import item.Item;


/**
 * Thrown to indicate something wend wrong when someone tried to pickup an {@link Item}.
 */
public class IllegalPickUpException extends Exception {

	private static final long	serialVersionUID	= -3774112096232334686L;
	
	@SuppressWarnings("javadoc")
	public IllegalPickUpException(String message) {
		super(message);
	}
}
