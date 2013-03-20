package grid;

import item.Effect;
import item.IItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import player.IPlayer;
import player.Player;

/**
 * A Square represents a place on a grid, which a player can stand on, as long
 * as it is not prevented by the square's internal state. Moving to another
 * Square can have side effects.
 */
public class Square extends ASquare {
	
	/** a map of the squares adjacent to this square */
	private Map<Direction, Square>	neighbours;
	/** the list of items in this square */
	private List<IItem>				itemList;
	/** the player on this square */
	private IPlayer					player;
	/** a boolean representing whether there is a light trail on this square */
	private boolean					lightTrail;
	// Having one PowerFailure object at this moment is enough. Since they all
	// have the same time to live, the last one added will be the one which will
	// live the longest. If this changes, we'd want to change this into a List.
	private PowerFailure			powerFailure;
	
	/**
	 * Default constructor.
	 * 
	 * @param neighbours
	 *        the squares adjacent to this square
	 */
	public Square(Map<Direction, Square> neighbours) {
		itemList = new ArrayList<IItem>();
		lightTrail = false;
		this.neighbours = new HashMap<Direction, Square>(neighbours);
	}
	
	/**
	 * add an item to the square
	 * 
	 * @param item
	 *        the item to add
	 * @deprecated This method will be replaced with the
	 *             {@link #placeOn(TronObject)} method. This provides more
	 *             consistency and automatically adds an {@link Effect effect}
	 *             if this is required.
	 */
	@Deprecated
	public void addItem(IItem item) {
		itemList.add(item);
	}
	
	/**
	 * Remove an item from the square
	 * 
	 * @param item
	 *        The item to remove
	 * @deprecated This method is replaced by the more general
	 *             {@link #remove(TronObject)} method.
	 */
	@Deprecated
	public void removeItem(IItem item) {
		itemList.remove(item);
	}
	
	/**
	 * Removes an object from this square.
	 * 
	 * @param object
	 *        the object to be removed
	 */
	public void remove(TronObject object) {
		if (player == object)
			player = null;
		else
			itemList.remove(object);
	}
	
	/**
	 * This method will check if a Square has a power failure. This depends on
	 * both the state of the Square itself, as on its neighbours.
	 * 
	 * @return whether or not it has a power failure.
	 */
	public boolean hasPowerFailure() {
		return powerFailure != null;
	}
	
	/**
	 * returns all the items on this square
	 * 
	 * @return the items on this square
	 */
	public List<IItem> getAllItems() {
		// Encapsulation isn't required, as this is a private method.
		// NOT ANYMORE IT'S NOT
		return new ArrayList<IItem>(itemList);
	}
	
	@Override
	public List<IItem> getCarryableItems() {
		List<IItem> result = new ArrayList<IItem>();
		for (IItem item : getAllItems())
			if (item.isCarriable())
				result.add(item);
		return result;
	}
	
	@Override
	public boolean hasLightTrail() {
		return lightTrail;
	}
	
	/**
	 * This method sets the haslightTrail for this square.
	 * 
	 * 
	 * 
	 * @note <b>Do NOT use this method.</b> The light trail is automatically
	 *       updated by the light trail as the player moves around the grid.
	 */
	public void placeLightTrail() {
		lightTrail = true;
	}
	
	/**
	 * Remove the light trail from this square.
	 * 
	 * @note <b>Do NOT use this method.</b> The light trail is automatically
	 *       updated by the light trail as the player moves around the grid.
	 */
	public void removeLightTrail() {
		lightTrail = false;
	}
	
	@Override
	public IItem pickupItem(int ID) {
		for (IItem itemOnSquare : this.getAllItems())
			if (ID == itemOnSquare.getId()) {
				this.remove(itemOnSquare);
				return itemOnSquare;
			}
		// if not yet returned --> not on square
		throw new IllegalArgumentException("The square doesn't hold the requested item");
	}
	
	@Override
	public boolean contains(IItem item) {
		for (IItem itemOnSquare : this.getAllItems())
			if (item.equals(itemOnSquare))
				return true;
		return false;
	}
	
	/**
	 * Returns the IPlayer on this square
	 * 
	 * @return An IPlayer, if there is one, otherwise null.
	 */
	public IPlayer getPlayer() {
		return player;
	}
	
	/**
	 * Returns whether or not this Square has currently a {@link Player}, i.e.
	 * <code>{@link #getPlayer()}!= null</code>.
	 * 
	 * @return Whether this square has a Player.
	 */
	@Override
	public boolean hasPlayer() {
		return player != null;
	}
	
	/**
	 * Move an IPlayer on this square. This might cause a penalty to the player,
	 * depending on the square's current power state and the items it contains.
	 * 
	 * @throws IllegalStateException
	 * @return True if the move of the player has caused him to end his turn.
	 * @deprecated This method is replaced by the more general and more
	 *             consistent {@link #placeOn(TronObject)} method.
	 */
	@Deprecated
	public boolean setPlayer(IPlayer player) throws IllegalStateException {
		if (player == null)
			throw new IllegalArgumentException();
		this.player = player;
		return penalty(player);
	}
	
	/**
	 * This method will set up an Effect object with the right parameters and
	 * execute it.
	 * 
	 * @param player
	 *        The Player which will feel the consequences.
	 * @return True when the penalty has caused the player to end his turn.
	 * @deprecated The method will be replaced by a system that will
	 *             automatically add a penalty to each object that is placed on
	 *             this square.
	 */
	@Deprecated
	private boolean penalty(IPlayer player) throws IllegalStateException {
		Effect effect = new Effect(player);
		
		effect.addPowerFailure(powerFailure);
		for (IItem item : getAllItems())
			item.addToEffect(effect);
		
		return effect.execute();
	}
	
	@Override
	public void removePlayer() {
		this.player = null;
	}
	
	@Override
	void removePowerFailure(PowerFailure powerFailure) {
		if (this.powerFailure != null && this.powerFailure.equals(powerFailure))
			this.powerFailure = null;
	}
	
	PowerFailure getPowerFailure() {
		return this.powerFailure;
	}
	
	@Override
	public void addPowerFailure(PowerFailure powerFailure) {
		// We can just override this. The last power failure added will
		// currently always be the one with the longest time to live.
		this.powerFailure = powerFailure;
	}
	
	/**
	 * Test whether an {@link TronObject object} can be added to this square.
	 * 
	 * @param object
	 *        the object that is to be added
	 * @return true if the object can be added, else false
	 */
	public boolean canBeAdded(TronObject object) {
		// see issue#38
		return false;
	}
	
	/**
	 * Test whether a player can be added to this square
	 * 
	 * @param player
	 *        the player that is to be added
	 * @return true if the player can be added, else false
	 */
	public boolean canBeAdded(IPlayer player) {
		return this.player == null;
	}
	
	/**
	 * Place an new object on the square. This method will calculate the
	 * {@link Effect} on the specified object and add that effect to the object.
	 * 
	 * @param object
	 *        the object to be placed on this square
	 */
	public void placeOn(TronObject object) {
		// test if the object can be added to the square
		if (!canBeAdded(object))
			throw new IllegalArgumentException("item could not be placed on this square");
		// create a new effect
		Effect effect = new Effect(object);
		// let this square add to the effect
		this.addToEffect(effect);
		// let the items on this square add to the effect
		for (IItem item : itemList) {
			item.addToEffect(effect);
		}
		// execute the effect
		effect.execute();
		
		// if the effect is executed the object can be added to the list
		this.setObject(object);
	}
	
	private void setObject(TronObject object) {
		// TODO
	}
	
	/**
	 * Add a {@link PowerFailure} to the effect if this square has a power
	 * failure and the {@link Effect#getObject() object} can suffer from a power
	 * failure.
	 * 
	 * @param effect
	 *        the effect where the power failure needs to be added
	 */
	public void addToEffect(Effect effect) {
		if (this.hasPowerFailure() && null != effect.getObject().asAffectedByPowerFailure())
			effect.addPowerFailure(getPowerFailure());
	}
	
	/**
	 * Returns the square adjacent to this square in the specified direction or
	 * null if the neighbour does not exist in the specified direction.
	 * 
	 * @param direction
	 *        the direction of the neighbor
	 * @return the square in the direction
	 */
	public Square getNeighbour(Direction direction) {
		return neighbours.get(direction);
	}
}
