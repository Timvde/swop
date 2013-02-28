package grid;

import item.Item;

import java.util.List;

import player.IPlayer;
import player.Player;

/**
 * 
 *
 */
public abstract class ASquare {

	/**
	 * returns the items on this square that can be picked up by a
	 * {@link Player}
	 */
	public abstract List<Item> getCarryableItems();

	/**
	 * Get the player that is located on this square.
	 * 
	 * @return The player that is on this square. Returns null if there is no
	 *         player.
	 */
	public abstract IPlayer getPlayer();

	/**
	 * Returns whether or not a light trail is currently active on this square.
	 * 
	 * @return whether or not a light trail is currently active on this square.
	 */
	public abstract boolean hasLightTrail();

	/**
	 * This method can be used to pick up an item with a given IDfrom a square.
	 * 
	 * Precondition: The square must hold the item with the given ID.
	 * 
	 * PostCondition: The returned item is removed from this square.
	 * 
	 * @param ID
	 *            The ID of the requested item
	 * @return The picked up item
	 * @throws IllegalArgumentException
	 *             The square must hold the requested item
	 *             <code>this.hasItemWithID(ID)<\code>
	 */
	public abstract Item pickupItem(int ID) throws IllegalArgumentException;

	/**
	 * Returns whether or not this square holds the item with a given ID.
	 * 
	 * @param ID
	 *            The ID of the querried Item
	 * @return whether or not this square holds the item with a given ID.
	 */
	public abstract boolean hasItemWithID(int ID);
}
