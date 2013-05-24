package square;

import item.Effect;
import item.IItem;
import java.util.List;
import java.util.Observer;
import player.Player;

/**
 * This class defines objects that represent a location on a Grid. It also
 * provides a default implementation of the {@link Square} interface.
 */
public abstract class AbstractSquare implements Square, Observer {
	
	/**
	 * This method can be used to pick up an item with a given ID from a square.
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
	 * Add a specified player to this square. This method is also responsible
	 * for executing any additional {@link Effect effects}.
	 * 
	 * @param p
	 *        the player who wants to be placed on this square
	 * 
	 * @throws IllegalArgumentException
	 *         It must be possible to add the player to this square. More
	 *         formally <code>{@link #canAddPlayer()}</code> .
	 */
	public abstract void addPlayer(Player p) throws IllegalArgumentException;
	
	/**
	 * Add a specified item to the square, the item may be affected by other
	 * items on the square.
	 * 
	 * @param item
	 *        the item to add
	 * @throws IllegalArgumentException
	 *         It must be possible to add the item to this square. More formally
	 *         <code>{@link #canBeAdded(IItem)}</code>.
	 */
	public abstract void addItem(IItem item) throws IllegalArgumentException;
	
	/**
	 * Removes an object from this square, if the object is not placed on this
	 * square nothing will happen.
	 * 
	 * @param object
	 *        the object to be removed
	 */
	public abstract void remove(Object object);
	
	/**
	 * returns all the items on this square
	 * 
	 * @return the items on this square
	 */
	public abstract List<IItem> getAllItems();
	
	/**
	 * Test whether an {@link IItem item} can be added to this square
	 * 
	 * @param item
	 *        the item that is to be added
	 * @return true if the item can be added, else false
	 */
	public abstract boolean canBeAdded(IItem item);
	
	/**
	 * Test whether a {@link Player player} can be added to this square.
	 * 
	 * @return true if a player can be added, else false
	 */
	public abstract boolean canAddPlayer();
	
	/**
	 * Add a player to the square and execute a specified effect on it.
	 * 
	 * @param player
	 *        the player to add
	 * @param effect
	 *        the effect to execute on the player after the player has been
	 *        added
	 */
	protected abstract void addPlayer(Player player, Effect effect);
	
	/**
	 * Add a item to the square and execute a specified effect on it.
	 * 
	 * @param item
	 *        the item to add
	 * @param effect
	 *        the effect to execute on the player after the player has been
	 *        added
	 */
	protected abstract void addItem(IItem item, Effect effect);
	
	/**
	 * Default implementation will return <code>false</code>.
	 */
	@Override
	public boolean hasPlayer() {
		return false;
	}
	
}
