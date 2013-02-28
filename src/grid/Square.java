package grid;

import item.IItem;
import java.util.ArrayList;
import java.util.List;
import notnullcheckweaver.NotNull;
import player.IPlayer;

public class Square extends ASquare {
	
	private List<IItem>	itemList	= new ArrayList<IItem>();
	private IPlayer		player;
	private boolean		hasLightTrail;
	
	public Square() {
		
	}
	
	public void addItem(IItem item) {
		itemList.add(item);
	}
	
	@Override
	public List<IItem> getItemList() {
		return new ArrayList<IItem>(this.itemList);
	}
	
	/**
	 * Returns whether or not a light trail is currently active on this square.
	 * 
	 * @return True when there is a light trail active on this square, otherwise
	 *         false.
	 */
	public boolean hasLightTrail() {
		return this.hasLightTrail;
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
	
}
