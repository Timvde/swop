package item;

import player.Player;
import square.NormalSquare;
import square.SquareContainer;
import square.TronObject;
import ObjectronExceptions.CannotPlaceLightGrenadeException;

/**
 * An Item is an object that can be placed on a {@link NormalSquare}. Each item
 * has a unique ID. Some Items can be picked up by a {@link Player}.
 */
public interface IItem extends TronObject {
	
	/**
	 * Returns the unique ID of this item
	 * 
	 * @return The unique ID of this item
	 */
	public int getId();
	
	/**
	 * Use the this item on a specified square with addition specified
	 * arguments. The additional arguments can be retrieved by the
	 * {@link #getUseArguments()} method. But these arguments need input from
	 * the user.
	 * 
	 * @param square
	 *        the square on which the item was used.
	 * @param arguments
	 *        The additional parameters an item needs
	 * @throws CannotPlaceLightGrenadeException
	 *         Common sense says that this exception does not belong here, even
	 *         the prof will see that ... (bavo)
	 */
	public void use(SquareContainer square, UseArguments<?> arguments)
			throws CannotPlaceLightGrenadeException;
	
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
	
	/**
	 * Returns a {@link UseArguments} object for the user to add additional
	 * parameters requested by the item when executing the use method. If this
	 * item does not require additional parameters, null will be returned.
	 * 
	 * @return a new useArguments that can be passed to the
	 *         {@link #use(SquareContainer, UseArguments)} method
	 */
	public UseArguments<?> getUseArguments();
}
