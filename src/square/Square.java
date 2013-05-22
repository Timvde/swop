package square;

import item.IItem;
import item.lightgrenade.LightGrenade;
import java.util.List;
import player.IPlayer;

/**
 * An interface for squares that is safe to be used outside of our own system.
 * It defines getters that can be used by whoever implements a user interface
 * for our game.
 */
public interface Square {
	
	/**
	 * Returns the items on this square that can be picked up by a
	 * {@link IPlayer player}
	 * 
	 * @return a list of all the carriable items on the square
	 */
	public List<IItem> getCarryableItems();
	
	/**
	 * Get the player that is located on this square.
	 * 
	 * @return The player that is on this square. Returns null if there is no
	 *         player.
	 */
	public IPlayer getPlayer();
	
	/**
	 * Returns whether or not this square holds currently a {@link IPlayer
	 * player}
	 * 
	 * @return Whether this square has a player.
	 */
	public boolean hasPlayer();

	/**
	 * Returns whether this square contains the specified object
	 * @param object
	 * 			the object to test
	 * @return true if this square contains the item 
	 */
	public boolean contains(Object object);
	
}
