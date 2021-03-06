package ObjectronExceptions;

import square.NormalSquare;
import item.Item;

/**
 * Thrown to indicate the specified {@link Item} to pickup on a specified
 * {@link NormalSquare} is not on the square.
 */
public class ItemNotOnSquareException extends IllegalPickUpException {
	
	private static final long	serialVersionUID	= 7533185744788755052L;
	
	@SuppressWarnings("javadoc")
	public ItemNotOnSquareException(String message) {
		super(message);
	}
}
