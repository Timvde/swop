package square;

import grid.Grid;
import item.IItem;
import java.util.List;
import player.IPlayer;
import player.Player;

/**
 * A Square is used for playing board games, and are the building blocks of the {@link Grid}. A square
 * can contain {@link IItem items} and a single {@link IPlayer player}
 */
public interface Square {
	
	/**
	 * Returns the items on this square that can be picked up by a
	 * {@link Player}
	 * 
	 * @return a list of all the carryable items on the square
	 */
	public abstract List<IItem> getCarryableItems();
	
	/**
	 * Get the player that is located on this square.
	 * 
	 * @return The player that is on this square. Returns null if there is no
	 *         player.
	 */
	public abstract IPlayer getPlayer();
	
	/**
	 * Checks whether or not a light trail is currently active on this square.
	 * 
	 * @return whether or not a light trail is currently active on this square.
	 */
	public abstract boolean hasLightTrail();
	
	/**
	 * Returns whether or not this AbstractSquare holds currently a {@link Player player}
	 * 
	 * @return Whether this square has a Player.
	 */
	public abstract boolean hasPlayer();
	
	/**
	 * @return Whether or not this AbstractSquare has a power failure.
	 */
	public abstract boolean hasPowerFailure();
	
}
