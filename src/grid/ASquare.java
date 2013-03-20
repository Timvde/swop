package grid;

import item.IItem;
import java.util.List;
import player.IPlayer;
import player.Player;

/**
 * This class defines objects that represent a location on a Grid.
 */
public abstract class ASquare {
	
	/**
	 * Returns the items on this square that can be picked up by a
	 * {@link Player}
	 * 
	 * @return a list of all the carryable items on the square
	 */
	public abstract List<IItem> getCarryableItems();
	
	/**
	 * Get the player that is located on this square.
	 * 
	 * @return The player that is on this square. Returns null if there is no
	 *         player.
	 */
	public abstract IPlayer getPlayer();
	
	/**
	 * Checks whether or not a light trail is currently active on this square.
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
	 *        The ID of the requested item
	 * @return The picked up item
	 * @throws IllegalArgumentException
	 *         The square must hold the requested item
	 *         <code>this.hasItemWithID(ID)<\code>
	 */
	public abstract IItem pickupItem(int ID) throws IllegalArgumentException;
	
	/**
	 * Returns whether or not this square holds the item.
	 * 
	 * @param item
	 *        the queried Item
	 * @return whether or not this square holds the item.
	 */
	public abstract boolean contains(IItem item);
	
	/**
	 * Returns whether or not this ASquare holds currently a {@link Player}
	 * 
	 * @return Whether this square has a Player.
	 */
	public abstract boolean hasPlayer();
	
	/**
	 * set a specified player on this square. This method returns whether the
	 * turn of the player has been ended by an effect of the square or items on
	 * that square
	 * 
	 * @param p
	 *        the new player
	 * @return whether the turn has been ended.
	 * @throws IllegalStateException
	 */
	public abstract boolean setPlayer(IPlayer p);
	
	/**
	 * This method removes a player from a square. This method is enforced:
	 * setPlayer(null) will throw. This is for both readability's sake and to
	 * prevent errors.
	 * 
	 * @deprecated This method is replaced by the more general
	 *             {@link Square#remove(TronObject)} method
	 */
	@Deprecated
	public abstract void removePlayer();
	
	/**
	 * @return Whether or not this ASquare has a power failure.
	 */
	public abstract boolean hasPowerFailure();
	
	/**
	 * Adds a power failure to this ASquare
	 * 
	 * @param powerFailure
	 *        The power failure to add
	 */
	abstract void addPowerFailure(PowerFailure powerFailure);
	
	/**
	 * This method removes a power failure from a square. It is called from
	 * within the PowerFailure class.
	 * 
	 * @param powerFailure
	 *        The power failure to remove
	 */
	abstract void removePowerFailure(PowerFailure powerFailure);
}
