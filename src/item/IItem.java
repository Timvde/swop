package item;

import grid.square.ASquare;
import grid.square.Square;
import player.Player;

/**
 * An Item is an object that can be placed on a {@link Square}. Each item has a
 * unique ID. Some Items can be picked up by a {@link Player}.
 */
public interface IItem {
	
	/**
	 * Returns the unique ID of this item
	 * 
	 * @return The unique ID of this item
	 */
	public int getId();
	
	/**
	 * use the this item on a specified square. The implementation of
	 * use(Square) will be different for each subclass of item.
	 * 
	 * @param square
	 *        the square on which the item was used.
	 */
	public void use(ASquare square);
	
	/**
	 * Return whether or not this item can be picked up by a {@link Player}.
	 * 
	 * @return whether or not this item can be picked up by a {@link Player}.
	 */
	public boolean isCarriable();
	
	/**
	 * This method instructs an Item to tell an Effect if it results in a
	 * penalty for stepping on it.
	 * 
	 * @param effect
	 *        The Effect which will be used to calculate the penalty
	 */
	public void addToEffect(Effect effect);
}
