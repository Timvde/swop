package item;

import grid.Square;
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
	 * TODO
	 */
	public void use();

	/**
	 * Return whether or not this item can be picked up by a {@link Player}.
	 * 
	 * @return whether or not this item can be picked up by a {@link Player}.
	 */
	public boolean isCarriable();
}
