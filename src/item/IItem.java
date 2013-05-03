package item;

import ObjectronExceptions.CannotPlaceLightGrenadeException;
import player.Player;
import square.AbstractSquare;
import square.NormalSquare;
import square.TronObject;

/**
 * An Item is an object that can be placed on a {@link NormalSquare}. Each item has a
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
	 * Use the this item on a specified square. The implementation of
	 * use(Square) will be different for each subclass of item.
	 * 
	 * @param square
	 *        the square on which the item was used.
	 * @throws CannotPlaceLightGrenadeException
	 */
	public void use(AbstractSquare square) throws CannotPlaceLightGrenadeException;
	
	/**
	 * Return whether or not this item can be picked up by a {@link Player}.
	 * 
	 * @return whether or not this item can be picked up by a {@link Player}.
	 */
	public boolean isCarriable();
	
	
	/**
	 * Returns this object as a char. 
	 * 
	 * @return A character representation of this object.
	 */
	public char toChar();
}
