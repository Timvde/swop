package player.actions;

import item.IItem;
import player.TronPlayer;
import square.SquareContainer;
import ObjectronExceptions.IllegalActionException;
import ObjectronExceptions.IllegalPickUpException;
import ObjectronExceptions.InventoryFullException;

/**
 * Pick up item action let's the player pick up an item from a square.
 */
public class PickupItemAction implements Action {
	
	private IItem	item;
	
	/**
	 * Create a new PickupItemAction to pick up a specified item.
	 * 
	 * @param item
	 *        the item to pick up
	 */
	public PickupItemAction(IItem item) {
		if (item == null)
			throw new IllegalArgumentException("The item cannot be null");
		this.item = item;
		
	}
	
	/**
	 * Pick up the given item. The item must be on the square the player is
	 * currently on.
	 *
	 * @throws IllegalActionException
	 *         This player must be allowed to perform an action, i.e.
	 *         <code>{@link player#canPerformAction()}</code>.
	 * @throws IllegalArgumentException
	 *         The item cannot be <code>null</code>.
	 * @throws IllegalPickUpException
	 *         The player must be able to pick up the specified item from the
	 *         current square.
	 */
	public void execute(TronPlayer player) {
		SquareContainer square = (SquareContainer) player.getCurrentPosition();
		if (!player.canPerformAction())
			throw new IllegalActionException("The player must be allowed to perform an action.");
		if (!square.contains(item))
			throw new IllegalPickUpException("The specified item is not on the square.");
		
		// remove the item from the square
		square.remove(item);
		
		try {
			// add the item to the inventory
			player.getInventory().addItem(item);
		}
		catch (IllegalArgumentException e) {
			// the inventory is full, rollback
			square.addItem(item);
			throw new InventoryFullException("The item cannot be added to the inventory.");
		}
	}
	
}
