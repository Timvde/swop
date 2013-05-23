package grid;

import item.IItem;
import java.util.List;
import java.util.Set;
import square.Square;

/**
 * A grid containing {@link Square squares}.
 * 
 */
public interface IGrid {
	
	/**
	 * returns the list of items on a square, when there are no items on the
	 * specified coordinate an empty list will be returned.
	 * 
	 * @param coordinate
	 *        the coordinate of the square where the items are located
	 * @return a list of items on a square
	 * @throws IllegalArgumentException
	 *         The specified coordinate cannot be <code>null</code>.
	 */
	public List<IItem> getItemList(Coordinate coordinate) throws IllegalArgumentException;
	
	/**
	 * Return the square of the grid that has a specific coordinate. This method
	 * returns null if the location doesn't exists on the grid.
	 * 
	 * @param coordinate
	 *        The coordinate of the square.
	 * @return The square at that coordinate or null if the location doesn't
	 *         exists.
	 * @throws IllegalArgumentException
	 *         The specified coordinate cannot be <code>null</code>.
	 */
	public Square getSquareAt(Coordinate coordinate) throws IllegalArgumentException;
	
	/**
	 * Return all the possible coordinates in the grid.
	 * 
	 * @return all coordinates in the grid
	 * @throws IllegalArgumentException
	 *         The specified coordinate cannot be <code>null</code>.
	 */
	public Set<Coordinate> getAllGridCoordinates() throws IllegalArgumentException;
}
