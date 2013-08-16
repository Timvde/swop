package player.actions;

import gui.ArgumentsHandler;
import item.IItem;
import item.UseArguments;
import player.TronPlayer;
import square.SquareContainer;
import ObjectronExceptions.IllegalActionException;
import ObjectronExceptions.IllegalUseException;

/**
 * UseAction let's the player use an item
 */
public class UseAction implements Action {
	
	private IItem				item;
	private ArgumentsHandler	argumentshandler;
	
	/**
	 * Create a new UseAction to use a specified item
	 * 
	 * @param item
	 *        The item to use
	 * @param handler
	 *        The handler used to get extra user information
	 */
	public UseAction(IItem item, ArgumentsHandler handler) {
		if (item == null)
			throw new IllegalArgumentException("The specified item cannot be null.");
		this.item = item;
		this.argumentshandler = handler;
	}
	
	/**
	 * {@link IItem#use(SquareContainer) use} the given item. The item must be
	 * in the inventory of the player.
	 * 
	 * @throws IllegalActionException
	 *         This player must be allowed to perform an action, i.e.
	 *         <code>{@link player#canPerformAction()}</code>.
	 * @throws IllegalArgumentException
	 *         The specified item cannot be <code>null</code>.
	 * @throws IllegalUseException
	 *         The player must be allowed to use the item.
	 */
	@SuppressWarnings("javadoc")
	public void execute(TronPlayer player) {
		SquareContainer square = (SquareContainer) player.getCurrentPosition();
		if (!player.canPerformAction())
			throw new IllegalActionException("The player must be allowed to perform an action.");
		if (!player.getInventoryContent().contains(item))
			throw new IllegalUseException("The item is not in the inventory");
		
		// remove the item from the inventory
		player.getInventory().removeItem(item);
		
		// try and use the item
		try {
			UseArguments<?> arguments = item.getUseArguments();
			if (arguments != null)
				argumentshandler.handleArguments(arguments);
			item.use(square, arguments);
		}
		catch (IllegalUseException e) {
			// re-add the item to the inventory and re-throw the exception
			player.getInventory().addItem(item);
			throw e;
		}
	}
	
}
