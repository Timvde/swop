package square;

import item.IItem;
import item.lightgrenade.LightGrenade;
import java.util.List;
import player.Player;
import player.TronPlayer;

/**
 * An interface for squares that is safe to be used outside of our own system.
 * It defines getters that can be used by whoever implements a user interface
 * for our game.
 */
public interface Square {
	
	/**
	 * Returns the items on this square that can be picked up by a
	 * {@link TronPlayer}
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
	public Player getPlayer();
	
	/**
	 * Returns whether or not this Square holds currently a {@link TronPlayer
	 * player}
	 * 
	 * @return Whether this square has a Player.
	 */
	public boolean hasPlayer();
	
	/**
	 * Returns whether this square contains the specified object
	 * 
	 * @param object
	 *        the object to test
	 * @return true if this square contains the item
	 */
	public boolean contains(Object object);
	
	/**
	 * Returns whether this square has a specified property
	 * 
	 * @param property
	 *        the property type to test
	 * @return true if the square had the specified property
	 */
	public boolean hasProperty(PropertyType property);
	
}
