package square;

import item.IItem;
import item.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import player.Player;
import effects.Effect;
import effects.EmptyEffect;

/**
 * A NormalSquare represents a postion that can hold {@link IItem items} and
 * where a {@link Player player} can stand on, as long as it is not prevented
 * by the square's internal state. Several side effects can occur when moving
 * players and adding items.
 */
public class NormalSquare extends AbstractSquare {
	
	/** the list of items in this square */
	private List<IItem>	itemList;
	/** the player on this square */
	private Player		player;
	
	/**
	 * Default constructor.
	 * 
	 */
	public NormalSquare() {
		itemList = new ArrayList<IItem>();
	}
	
	@Override
	protected void addItem(IItem item, Effect effect) {
		if (!canAddItem(item))
			throw new IllegalArgumentException("The item could not be placed on this square!");
		if (effect == null)
			throw new IllegalArgumentException("null effect");
		
		itemList.add(item);
		
		// execute an effect on the item
		this.executeEffect(item, effect);
	}
	
	@Override
	public void remove(Object object) {
		// test if the player needs to be removed
		if (player!= null && player.equals(object))
			player = null;
		// else remove object from the item list
		else
			itemList.remove(object);
	}
	
	@Override
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
	 * Returns the player on this square
	 * 
	 * @return A player, if there is one, otherwise null.
	 */
	public Player getPlayer() {
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
	protected void addPlayer(Player player, Effect effect) {
		if (!canAddPlayer() || player == null)
			throw new IllegalArgumentException("The player cannot be added to this square: " + this);
		if (effect == null)
			throw new IllegalArgumentException("null effect");
		
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
		if (this.hasPlayer())
			return false;
		else
			return true;
	}
	
	@Override
	public boolean canAddItem(IItem item) {
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
			out = "s ";
		
		return out;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// nothing to do; decorators may overrride this
	}

	@Override
	public boolean hasProperty(PropertyType property) {
		return false;
	}

	@Override
	protected Effect getStartTurnEffect(Effect effect) {
		// Nothing to chain
		return effect;
	}
	
	
}
