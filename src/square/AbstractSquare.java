package square;

import item.Effect;
import item.EmptyEffect;
import item.IItem;
import java.util.List;
import player.IPlayer;

/**
 * This class defines objects that represent a location on a Grid.
 */
public abstract class AbstractSquare implements Square {
	
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
	 * Add a specified player to this square. This method will automatically add
	 * the {@link Effect effects} of the items on this square to the player.
	 * 
	 * @param p
	 *        the player who wants to be placed on this square
	 * 
	 * @throws IllegalArgumentException
	 *         It must be possible to add the player to this square. More
	 *         formally <code>{@link #canAddPlayer()}</code> .
	 */
	public abstract void addPlayer(IPlayer p) throws IllegalArgumentException;
	
	/**
	 * This method sets the haslightTrail for this square. <br>
	 * <br>
	 * <b>Do NOT use this method.</b> The light trail is automatically updated
	 * by the light trail as the player moves around the grid.
	 */
	public abstract void placeLightTrail();
	
	/**
	 * Remove the light trail from this square. <br>
	 * <br>
	 * <b>Do NOT use this method.</b> The light trail is automatically updated
	 * by the light trail as the player moves around the grid.
	 */
	public abstract void removeLightTrail();
	
	/**
	 * Add a specified item to the square, the item will (if possible) be
	 * affected by other items on the square.
	 * 
	 * @param item
	 *        the item to add
	 * @throws IllegalArgumentException
	 *         It must be possible to add the item to this square. More formally
	 *         <code>{@link #canBeAdded(IItem)}</code>.
	 */
	public abstract void addItem(IItem item) throws IllegalArgumentException;
	
	/**
	 * Removes an object from this square, if the item is not placed on this
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
	 * Test whether a {@link IPlayer player} can be added to this square.
	 * 
	 * @return true if a player can be added, else false
	 */
	public abstract boolean canAddPlayer();
	
	/**
	 * A hook for adding {@link Effect effects} to any object that is placed on
	 * a square. This method will be called before the effects are added. All
	 * the other effects will be added after this effect. This gives great
	 * flexibility for modifying or adding extra effects.
	 * 
	 * @return a new effect that will execute an object that is placed on this
	 *         square
	 */
	protected Effect effectHook() {
		return new EmptyEffect();
	}

	/**
	 * Test whether this square is a wall
	 * 
	 * @return True if this is a wall, false if this is not.
	 */
	public abstract boolean isWall();
}
