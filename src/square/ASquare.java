package square;

import item.IItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import player.IPlayer;

/**
 * This class defines objects that represent a location on a Grid.
 */
public abstract class ASquare implements ISquare {
	
	/** a map of the squares adjacent to this square */
	private Map<Direction, ASquare>	neighbours;
	
	/**
	 * Create a new abstract square with specified neighbours, after this method
	 * the squares specified in the map will be set as the neighbours for this
	 * square. Also this square will be set as the neighbour for all the squares
	 * in the map. More formally, after the creation of this object the
	 * following will be true for each neighbour in a corresponding direction:
	 * 
	 * <pre>
	 * <code> 
	 *  this == getNeighbourInDirection(direction)
	 *  			.getNeighbourInDirection(direction.getOppositeDirection)
	 *  </code>
	 * </pre>
	 * 
	 * @param neighbours
	 *        the neighbours of this square
	 */
	public ASquare(Map<Direction, ASquare> neighbours) {
		
		// check if the parameter is valid
		if (!canHaveAsNeighbours(neighbours))
			throw new IllegalArgumentException(
					"the specified neighbours could not be set as the neighbours for this square!");
		
		// set the neighbours as the neighbours for this square
		this.neighbours = new HashMap<Direction, ASquare>(neighbours);
		
		// for each neighbour of this square, set this square as the neighbour
		// in the opposite direction
		for (Direction direction : neighbours.keySet())
			neighbours.get(direction).neighbours.put(direction.getOppositeDirection(), this);
	}
	
	/**
	 * Returns whether the specified map can be set as the map of neighbours for
	 * this square. More formally this method returns false if and only if
	 * <code>neighbours == null</code>
	 * 
	 * @param neighbours
	 *        the neighbours to be tested
	 * @return true if the neighours can be set for this square, else false
	 */
	private boolean canHaveAsNeighbours(Map<Direction, ASquare> neighbours) {
		if (neighbours == null)
			return false;
		return true;
	}
	
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
	 * Add a specified player to this square. This method will automatically
	 * adds the effects of the items on this square to the player.
	 * 
	 * @param p
	 *        the player who wants to be placed on this square
	 * 
	 * @throws IllegalArgumentException
	 *         It must be possible to add the player to this square. More
	 *         formally <code>{@link #canBeAdded(IPlayer)}</code> .
	 */
	public abstract void addPlayer(IPlayer p) throws IllegalArgumentException;
	
	/**
	 * This method removes a player from a square. This method is enforced:
	 * setPlayer(null) will throw. This is for both readability's sake and to
	 * prevent errors.
	 * 
	 * @deprecated The method {@link #remove(Object)} can be used if a player is
	 *             to be removed from this square. This method needs a reference
	 *             to the player, but if you do not have one it is probably not
	 *             up to you to remove a player from this square ...
	 */
	@Deprecated
	public abstract void removePlayer();
	
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
	 * Returns the neighbour of this square in the specified direction or null
	 * if there is no neighbour in the specified direction
	 * 
	 * @param direction
	 *        the direction of the neighbour
	 * @return the neighbour in the specified direction
	 */
	public ASquare getNeighbour(Direction direction) {
		return neighbours.get(direction);
	}
	
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
}
