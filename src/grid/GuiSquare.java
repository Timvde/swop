package grid;

import item.IItem;
import java.util.List;
import player.IPlayer;

/**
 * 
 */
public interface GuiSquare {
	
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
	 * Checks whether or not a light trail is currently active on this square.
	 * 
	 * @return whether or not a light trail is currently active on this square.
	 */
	public boolean hasLightTrail();
	
	/**
	 * Returns whether or not this square holds currently a {@link IPlayer
	 * player}
	 * 
	 * @return Whether this square has a player.
	 */
	public boolean hasPlayer();
	
	/**
	 * @return Whether or not this square has a power failure.
	 */
	public boolean hasPowerFailure();
	
	/**
	 * @return Whether or not this square has a force field.
	 */
	public boolean hasForceField();
	
	/**
	 * Returns whether this square represents a wall
	 * 
	 * @return true if this square represents a wall
	 */
	public boolean isWall();
	
	/**
	 * Returns whether this square is a starting position
	 * 
	 * @return true if this square is a starting position
	 */
	public boolean isStartingPosition();
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	public boolean contains(Object object);
	
	/**
	 * returns a list of items on this square
	 * 
	 * @return items on this square
	 */
	public List<IItem> getItems();
}
