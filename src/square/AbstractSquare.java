package square;

import item.IItem;
import java.util.List;
import java.util.Observer;
import player.Player;
import effects.Effect;
import effects.EmptyEffect;

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
	 * Add a {@link TronObject} to the square, the {@link TronObject} may be
	 * affected by the state of the square. This can be used when the specific
	 * type of {@link TronObject } is not known to the caller.
	 * 
	 * @param object
	 *        The object to be placed on this square
	 * 
	 * @throws IllegalArgumentException
	 *         It must be possible to add the object to this square. More
	 *         formally <code>{@link #canBeAdded(TronObject)}</code> must be true.
	 */
	public void addTronObject(TronObject object) {
		if (object instanceof IItem)
			addItem((IItem) object);
		else
			addPlayer((Player) object);
	}
	
	/**
	 * Add a specified player to this square. The player may be affected by the
	 * state of the square.
	 * 
	 * @param player
	 *        the player who wants to be placed on this square
	 * 
	 * @throws IllegalArgumentException
	 *         It must be possible to add the player to this square. More
	 *         formally <code>{@link #canAddPlayer()}</code> must be true.
	 */
	public void addPlayer(Player player) throws IllegalArgumentException {
		addPlayer(player, new EmptyEffect());
	}
	
	/**
	 * Add a specified item to the square. The item may be affected by the state
	 * of the square.
	 * 
	 * @param item
	 *        the item to add
	 * @throws IllegalArgumentException
	 *         It must be possible to add the item to this square. More formally
	 *         <code>{@link #canAddItem(IItem)}</code> must be true.
	 */
	public void addItem(IItem item) throws IllegalArgumentException {
		addItem(item, new EmptyEffect());
	}
	
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
	 * A generic method to test whether a specified {@link TronObject } can be
	 * added to this square. This can be used when the specific type of
	 * {@link TronObject } is not known to the caller.
	 * 
	 * @param object
	 *        The object that might or might not be added to this square
	 * @return Whether or not the specified object can be added
	 */
	public boolean canBeAdded(TronObject object) {
		if (object instanceof IItem)
			return canAddItem((IItem) object);
		else
			return canAddPlayer();
	}
	
	/**
	 * Test whether an {@link IItem item} can be added to this square
	 * 
	 * @param item
	 *        the item that is to be added
	 * @return Whether or not the specified item can be added
	 */
	public abstract boolean canAddItem(IItem item);
	
	/**
	 * Test whether a {@link Player player} can be added to this square.
	 * 
	 * @return Whether or not a player can be added
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
	
	/**
	 * Returns the effect a player has to undergo at the beginning of its turn.
	 * 
	 * @return The effect to execute on a player
	 */
	public Effect getStartTurnEffect() {
		return getStartTurnEffect(new EmptyEffect());
	}
	
	protected abstract Effect getStartTurnEffect(Effect effect);
}
