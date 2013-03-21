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
	 * Returns whether or not this square holds the specified object.
	 * 
	 * @param object
	 *        the object to be tested
	 * @return true if the square holds the object, else false
	 */
	public abstract boolean contains(Object object);
	
	/**
	 * Returns whether or not this ASquare holds currently a {@link Player player}
	 * 
	 * @return Whether this square has a Player.
	 */
	public abstract boolean hasPlayer();
	
	/**
	 * Add a specified player to this square. This method will automatically adds the effects of 
	 * the items on this square to the player. 
	 * 
	 * @param p
	 *        the player who wants to be placed on this square
	 */
	public abstract void addPlayer(IPlayer p);
	
	/**
	 * This method removes a player from a square. This method is enforced:
	 * setPlayer(null) will throw. This is for both readability's sake and to
	 * prevent errors.
	 */
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
