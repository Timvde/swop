package grid;

import item.Item;
import java.util.List;
import player.Player;

/**
 * TODO
 * 
 * @author Bavo Mees
 */
public interface IGrid {
	
	/**
	 * returns whether a specified player can move in a specific direction
	 * 
	 * @param player
	 *        the player who wants to move
	 * @param direction
	 *        the direction the player wants to move in
	 * @return returns whether a specified player can move in a specific
	 *         direction.
	 */
	public boolean canMovePlayer(Coordinate coordinate, Direction direction);
	
	/**
	 * Move a specified player to a new coordinate in a given direction
	 * 
	 * @param player
	 *        the player who wants to move
	 * @param direction
	 *        the direction the player wants to move in.
	 */
	public void movePlayer(Coordinate coordinate, Direction direction);
	
	/**
	 * returns the list of items on a square, when there are no items on the
	 * specified coordinate an empty list will be returned.
	 * 
	 * @param coordinate
	 *        the coordinate of the square where the items are located
	 * @return a list of items on a square
	 */
	public List<Item> getItemList(Coordinate coordinate);
}
