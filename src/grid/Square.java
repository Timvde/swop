package grid;

import item.IItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import notnullcheckweaver.NotNull;
import player.IPlayer;

<<<<<<< HEAD
public class Square extends ASquare {

	private List<IItem> itemList = new ArrayList<IItem>();
	private IPlayer player;
	private boolean hasLightTrail;

=======
public class Square extends ASquare implements Observer {
	
	private List<IItem>	itemList	= new ArrayList<IItem>();
	private IPlayer		player;
	private int			lightTrail;
	
>>>>>>> refs/heads/Tests
	public Square() {

	}

	public void addItem(IItem item) {
		itemList.add(item);
	}

	@Override
	public List<IItem> getCarryableItems() {
		List<IItem> result = new ArrayList<IItem>();
		for (IItem item : itemList) {
			if (item.isCarriable())
				result.add(item);
		}
		return result;
	}
<<<<<<< HEAD

	/**
	 * Returns whether or not a light trail is currently active on this square.
	 * 
	 * @return True when there is a light trail active on this square, otherwise
	 *         false.
	 */
=======
	
	@Override
>>>>>>> refs/heads/Tests
	public boolean hasLightTrail() {
<<<<<<< HEAD
		return this.hasLightTrail;
	}

	/**
	 * This method sets the haslightTrail-bit for this square.
	 * 
	 * @param b
	 *            the new value for <code>this.hasLightTrail</code>
	 * 
	 * @note <b>Do NOT use this method.</b> The lighttrail-bit is automatically
	 *       updated as the player (and its lighttrail) move around the grid.
	 */
	public void setHasLightTrail(boolean b) {
		this.hasLightTrail = b;
=======
		return lightTrail > 0;
>>>>>>> refs/heads/Tests
	}
	
	public void placeLightTrail() {
		lightTrail = 3;
	}
	
	@Override
	public IItem pickupItem(int ID) {
		for (IItem itemOnSquare : this.itemList)
			if (ID == itemOnSquare.getId())
				return itemOnSquare;
		// if not yet returned --> not on square
		throw new IllegalArgumentException("The square doesn't hold the requested item");
	}
	
	@Override
	public boolean hasItemWithID(int ID) {
		for (IItem itemOnSquare : this.itemList)
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
	 * Set an IPlayer on this square.
	 */
	public void setPlayer(@NotNull IPlayer player) {
		this.player = player;
	}

	/**
	 * This method removes a player from a square. This method is enforced:
	 * setPlayer(null) will throw. This is for both readability's sake and to
	 * prevent errors.
	 */
	public void removePlayer() {
		this.player = null;
	}
<<<<<<< HEAD

=======
	
	@Override
	public void update(Observable o, Object arg) {
		if (lightTrail > 0)
			lightTrail--;
	}
	
>>>>>>> refs/heads/Tests
}
