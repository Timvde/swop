package grid;

import java.util.List;
import player.IPlayer;
import item.IItem;


public abstract class ASquare {
	
	/**
	 * returns the items on this square
	 */
	public abstract List<IItem> getItemList();
	
	/**
	 * Get the player that is located on this square.
	 * 
	 * @return	The player that is on this square. 
	 * 			Returns null if there is no player.
	 */
	public abstract IPlayer getPlayer();
	
	/**
	 * Returns whether or not a light trail is currently active on this square.
	 * 
	 * @return whether or not a light trail is currently active on this square.
	 */
	public abstract boolean hasLightTrail();
}
