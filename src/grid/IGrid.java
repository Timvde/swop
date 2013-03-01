package grid;

import item.IItem;
import java.util.List;
import java.util.Set;


public interface IGrid {
	
	/**
	 * returns whether a specified player can move in a specific direction
	 * 
	 * @param playerID
	 *        the ID of the player who wants to move
	 * @param direction
	 *        the direction the player wants to move in
	 * @return returns whether a specified player can move in a specific
	 *         direction.
	 */
	public boolean canMovePlayer(int playerID, Direction direction);
	
	/**
	 * Move a specified player to a new coordinate in a given direction
	 * 
	 * @param playerID
	 *        the id of the player who wants to move
	 * @param direction
	 *        the direction the player wants to move in.
	 */
	public void movePlayer(int playerID, Direction direction);
	
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
	 * Return the coordinate of the player with the given id.
	 * @param playerId
	 * 			The id of the player we need the coordinate from.
	 * @return
	 * 			The coordinate as a Coordinate object of the given player.
	 */
	public Coordinate getPlayerCoordinate(int playerId);
	
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
