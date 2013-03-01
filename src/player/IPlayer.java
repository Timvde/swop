package player;

import java.util.List;
import grid.Coordinate;
import item.IItem;
import item.Item;

/**
 * A Player is the main actor of the game. It can move round, pick up objects
 * and tries to win the game
 */
public interface IPlayer {
	
	/**
	 * Returns the unique ID-number associated with this player.
	 * 
	 * @return the unique ID-number associated with this player.
	 */
	public int getID();
	
	/**
	 * Returns the coordinate this player has to reach to win the game.
	 * 
	 * @return the coordinate this player has to reach to win the game.
	 */
	public Coordinate getTargetPosition();
	
	/**
	 * Returns the Inventory-content associated with this player.
	 * 
	 * @return the Inventory-content associated with this player.
	 */
	public List<IItem> getInventory();
	
}
