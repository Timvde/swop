package ObjectronExceptions;

import player.Inventory;
import player.IPlayer;

/**
 * Thrown to indicate the item to be picked up can not be added to the
 * {@link IPlayer}s {@link Inventory} (because it has reached its
 * {@link Inventory#getMaxNumberOfItems() maximum}).
 */
public class InventoryFullException extends IllegalPickUpException {
	
	private static final long	serialVersionUID	= 2710284001574628164L;
	
	@SuppressWarnings("javadoc")
	public InventoryFullException(String message) {
		super(message);
	}
	
}
