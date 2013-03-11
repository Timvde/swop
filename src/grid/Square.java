package grid;

import item.Effect;
import item.IItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import notnullcheckweaver.NotNull;
import player.IPlayer;

public class Square extends ASquare implements Observer {
	
	private List<IItem>	itemList	= new ArrayList<IItem>();
	private IPlayer		player;
	private int			lightTrail;
	
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
	
	public void removeItem(IItem item) {
		itemList.remove(item);
	}
	
	/**
	 * TODO
	 * 
	 * @return
	 */
	public boolean hasPowerFailure() {
		return false;
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
	 * Move an IPlayer on this square. This might cause a penalty to the player,
	 * depending on the square's current power state and the items it contains.
	 */
	public void setPlayer(@NotNull IPlayer player) {
		this.player = player;
		penalty(player);
	}
	
	private void penalty(IPlayer player) {
		Effect effect = new Effect(player);
		
		if (hasPowerFailure())
			effect.addPowerFailure();
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
	
	@Override
	public void update(Observable o, Object arg) {
		if (lightTrail > 0)
			lightTrail--;
	}
	
}
