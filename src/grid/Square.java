package grid;

import item.Effect;
import item.IItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import player.IPlayer;
import player.Player;

/**
 * A Square represents a place on a grid, which a player can stand on, as long
 * as it is not prevented by the square's internal state. Moving to another
 * Square can have side effects.
 */
public class Square extends ASquare {
	
	private List<IItem>		itemList	= new ArrayList<IItem>();
	private IPlayer			player;
	private int				lightTrail;
	// Having one PowerFailure object at this moment is enough. Since they all
	// have the same time to live, the last one added will be the one which will
	// live the longest. If this changes, we'd want to change this into a List.
	private PowerFailure	powerFailure;
	
	/**
	 * Default constructor.
	 */
	public Square() {
		
	}
	
	/**
	 * add an item to the square
	 * 
	 * @param item
	 *        the item to add
	 */
	public void addItem(IItem item) {
		itemList.add(item);
	}
	
	/**
	 * Remove an item from the square
	 * 
	 * @param item
	 *        The item to remove
	 */
	public void removeItem(IItem item) {
		itemList.remove(item);
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
	
	private List<IItem> getAllItems() {
		// Encapsulation isn't required, as this is a private method.
		return itemList;
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
		return lightTrail > 0;
	}
	
	/**
	 * This method sets the haslightTrail for this square.
	 * 
	 * @param b
	 *        the new value for <code>this.hasLightTrail</code>
	 * 
	 * @note <b>Do NOT use this method.</b> The light trail is automatically
	 *       updated as the player (and its light trail) moves around the grid.
	 */
	public void placeLightTrail() {
		lightTrail = 4;
	}
	
	@Override
	public IItem pickupItem(int ID) {
		for (IItem itemOnSquare : this.getAllItems())
			if (ID == itemOnSquare.getId())
				return itemOnSquare;
		// if not yet returned --> not on square
		throw new IllegalArgumentException("The square doesn't hold the requested item");
	}
	
	@Override
	public boolean hasItemWithID(int ID) {
		for (IItem itemOnSquare : this.getAllItems())
			if (ID == itemOnSquare.getId())
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
	 * FIXME null
	 */
	public void setPlayer(IPlayer player) {
		if (player == null)
			throw new IllegalArgumentException();
		this.player = player;
		penalty(player);
	}
	
	/**
	 * This method will set up an Effect object with the right parameters and
	 * execute it.
	 * 
	 * @param player
	 *        The Player which will feel the consequences.
	 * 
	 *        TODO null
	 */
	private void penalty(IPlayer player) {
		Effect effect = new Effect(player);
		
		effect.addPowerFailure(powerFailure);
		for (IItem item : getAllItems())
			item.addToEffect(effect);
		
		effect.execute();
	}
	
	/**
	 * This method removes a player from a square. This method is enforced:
	 * setPlayer(null) will throw. This is for both readability's sake and to
	 * prevent errors.
	 */
	public void removePlayer() {
		this.player = null;
	}
	
	/**
	 * This method removes a power failure from a square. It is called from
	 * within the PowerFailure class.
	 * 
	 * @param powerFailure
	 *        The power failure to remove
	 */
	void removePowerFailure(PowerFailure powerFailure) {
		if (this.powerFailure.equals(powerFailure))
			this.powerFailure = null;
	}
	
	PowerFailure getPowerFailure() {
		return this.powerFailure;
	}
	
	/**
	 * Add a power failure to this square.
	 * 
	 * @param powerFailure
	 *        The power failure to add.
	 */
	public void addPowerFailure(PowerFailure powerFailure) {
		// We can just override this. The last power failure added will
		// currently always be the one with the longest time to live.
		this.powerFailure = powerFailure;
	}
	
}
