package square;

import item.Effect;
import item.EmptyEffect;
import item.IItem;
import item.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import player.Player;
import player.TronPlayer;

/**
 * A Square represents a place on a grid, which a player can stand on, as long
 * as it is not prevented by the square's internal state. Moving to another
 * Square can have side effects.
 */
public class NormalSquare extends AbstractSquare {
	
	/** the list of items in this square */
	private List<IItem>		itemList;
	/** the player on this square */
	private Player			player; 
	/** a boolean representing whether there is a light trail on this square */
	private boolean			lightTrail;
	
	/**
	 * Create a new normal square
	 * 
	 */
	public NormalSquare() {
		itemList = new ArrayList<IItem>();
		lightTrail = false;
	}
	
	@Override
	public void addItem(IItem item) {
		addItem(item, new EmptyEffect());
	}
	
	protected void addItem(IItem item, Effect effect) {
		if (!canBeAdded(item))
			throw new IllegalArgumentException("The item could not be placed on this square!");
		
		itemList.add(item);
		
		// execute an effect on the item
		this.executeEffect(item, effect);
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
		// only items that are carriable can be picked up!
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
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Returns whether or not this Square has currently a {@link TronPlayer}, i.e.
	 * <code>{@link #getPlayer()}!= null</code>.
	 * 
	 * @return Whether this square has a Player.
	 */
	@Override
	public boolean hasPlayer() {
		return player != null;
	}
	
	@Override
	public void addPlayer(Player player) {
		addPlayer(player, new EmptyEffect());
	}
	
	/**
	 * Add a player to the square and execute a specified effect on it.
	 * 
	 * @param player
	 *        the player to add
	 * @param effect
	 *        the effect to execute on the player after the player has been
	 *        added
	 */
	protected void addPlayer(Player player, Effect effect) {
		if (!canAddPlayer())
			throw new IllegalArgumentException("The player cannot be added to this square: " + this);
		this.player = player;
		this.executeEffect(player, effect);
	}
	
	/**
	 * Test whether a {@link Player player} can be added to this square. A
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
	 * Executes a new effect to a {@link TronObject}. Additional effects can be
	 * added to this method. If there are no additional effects, an
	 * {@link EmptyEffect} should
	 * 
	 * @param object
	 *        the object to be placed on this square
	 */
	private void executeEffect(TronObject object, Effect effect) {
		
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
	
	@Override
	public void update(Observable o, Object arg) {
		// nothing to do 
	}
}
