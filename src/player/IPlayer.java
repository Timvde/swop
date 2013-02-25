package player;

import java.util.List;
import grid.Coordinate;
import item.Item;

/**
 * A Player is the main actor of the game. It can move round, pick up objects
 * and tries to win the game
 */
public interface IPlayer {
	
	public int getID();
	
	public Coordinate getTargetPosition();
	
	public List<Item> getInventory();
	
}
