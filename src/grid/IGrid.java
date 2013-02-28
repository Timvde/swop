package grid;

import item.IItem;
import item.Item;

import java.util.List;
import java.util.Set;

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
	public boolean canMovePlayer(Player player, Direction direction);
	
	/**
	 * Move a specified player to a new coordinate in a given direction
	 * 
	 * @param player
	 *        the player who wants to move
	 * @param direction
	 *        the direction the player wants to move in.
	 */
	public void movePlayer(Player player, Direction direction);
	
	/**
	 * returns the list of items on a square, when there are no items on the
	 * specified coordinate an empty list will be returned.
	 * 
	 * @param coordinate
	 *        the coordinate of the square where the items are located
	 * @return a list of items on a square
	 */
	public List<IItem> getItemList(Coordinate coordinate);
	
	/**
	 * Return the square of the grid that has a specific coordinate.
	 * 
	 * @param coordinate
	 * 			The coordinate of the square.
	 * @return
	 * 			The square at that coordinate.
	 */
	public ASquare getSquareAt(Coordinate coordinate);
	
	/**
	 * Get a set of all the used coordinates in our grid.
	 * 
	 * @return A set of the used grid coordinates.
	 */
	public Set<Coordinate> getAllGridCoordinates();
}
