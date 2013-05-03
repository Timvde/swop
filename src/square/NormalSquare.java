package square;

import item.Effect;
import item.IItem;
import item.Item;
import java.util.ArrayList;
import java.util.List;
import player.IPlayer;
import player.Player;
import powerfailure.PowerFailure;

/**
 * A Square represents a place on a grid, which a player can stand on, as long
 * as it is not prevented by the square's internal state. Moving to another
 * Square can have side effects.
 */
public class NormalSquare extends AbstractSquare {
	
	/** the list of items in this square */
	private List<IItem>		itemList;
	/** the player on this square */
	private IPlayer			player;
	/** a boolean representing whether there is a light trail on this square */
	private boolean			lightTrail;
	// Having one PowerFailure object at this moment is enough. Since they all
	// have the same time to live, the last one added will be the one which will
	// live the longest. If this changes, we'd want to change this into a List.
	private PowerFailure	powerFailure;
	
	/**
	 * Default constructor.
	 * 
	 */
	public NormalSquare() {
		itemList = new ArrayList<IItem>();
		lightTrail = false;
	}
	
	@Override
	public void addItem(IItem item) {
		if (!canBeAdded(item))
			throw new IllegalArgumentException("The item could not be placed on this square!");
		
		itemList.add(item);
		
		// execute an effect on the item
		this.executeEffect(item);
	}
	
	@Override
	public void remove(Object object) {
		// test if the player needs to be removed
		if (player == object)
			player = null;
		// else remove player from the item list
		else
			itemList.remove(object);
	}
	
	@Override
	public boolean hasPowerFailure() {
		return powerFailure != null;
	}
	
	/**
	 * returns all the items on this square
	 * 
	 * @return the items on this square
	 */
	public List<IItem> getAllItems() {
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
	
	@Override
	public void placeLightTrail() {
		lightTrail = true;
	}
	
	@Override
	public void removeLightTrail() {
		lightTrail = false;
	}
	
	@Override
	public IItem pickupItem(int ID) {
		// try and retrieve the item
		// only items that are carryable can be picked up!
		for (IItem itemOnSquare : this.getCarryableItems())
			if (ID == itemOnSquare.getId()) {
				this.remove(itemOnSquare);
				return itemOnSquare;
			}
		// if not yet returned --> not on square
		throw new IllegalArgumentException("The square doesn't hold the requested item");
	}
	
	@Override
	public boolean contains(Object object) {
		if (object == null)
			return false;
		else if (object.equals(player))
			return true;
		else
			return itemList.contains(object);
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
	
	@Override
	public void addPlayer(IPlayer player) throws IllegalArgumentException {
		if (!canAddPlayer())
			throw new IllegalArgumentException("The player cannot be added to this square!");
		this.player = player;
		this.executeEffect(player);
	}
	
	/**
	 * returns the power failure of this object
	 * 
	 * @return power failure
	 */
	public PowerFailure getPowerFailure() {
		return this.powerFailure;
	}
	
	/**
	 * Test whether a {@link IPlayer player} can be added to this square. A
	 * player can be added, if there is no other player placed on this square.
	 * More formally this method will return <code>true</code> if and only if<br>
	 * <code>this.hasPlayer() == false && this.hasLightTrail() == false</code>.
	 * 
	 * @return True if a player can be added, otherwise false
	 */
	@Override
	public boolean canAddPlayer() {
		if (this.hasLightTrail())
			return false;
		else if (this.hasPlayer())
			return false;
		else
			return true;
	}
	
	@Override
	public boolean canBeAdded(IItem item) {
		if (item == null)
			return false;
		// TODO are there any other preconditions?
		return true;
	}
	
	/**
	 * Executes a new effect to a {@link TronObject}.
	 * 
	 * <p>
	 * The effects returned by the {@link #effectHook()} method will be added to
	 * the effects of the player. For more information check the documentation
	 * of the method.
	 * </p>
	 * 
	 * @param object
	 *        the object to be placed on this square
	 */
	private void executeEffect(TronObject object) {
		Effect effect = effectHook();
		
		if (powerFailure != null)
			effect.addEffect(powerFailure.getEffect());
		
		for (IItem item : itemList)
			effect.addEffect(((Item) item).getEffect());
		
		effect.execute(object);
	}
	
	@Override
	public String toString() {
		String out = "";
		for (IItem item : getAllItems()) {
			out += item.toChar();
			out += ' ';
		}
		if (out.equals(""))
			out = (hasPowerFailure() ? "p " : "s ");
		
		return out;
	}
}
