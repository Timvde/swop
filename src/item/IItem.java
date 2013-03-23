package item;

import player.Player;
import square.ASquare;
import square.Direction;
import square.Square;
import square.TronObject;

/**
 * An Item is an object that can be placed on a {@link Square}. Each item has a
 * unique ID. Some Items can be picked up by a {@link Player}.
 */
public interface IItem extends TronObject {
	
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
	 * @param direction
	 *        the direction in which the item will be used
	 */
	public void use(ASquare square, Direction direction);
	
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
	
	/**
	 * Executes the functions of the item on a specified object.
	 * 
	 * @param object
	 *        the object that is influenced by this item
	 */
	public void execute(TronObject object);
}
