package grid;

import item.IItem;
import java.util.List;
import java.util.Set;
import square.ASquare;

/**
 * A grid that consists of abstract {@link ASquare squares}.
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
	 */
	public List<IItem> getItemList(Coordinate coordinate);
	
	// FIXME check null
	
	/**
	 * Return the square of the grid that has a specific coordinate. This method
	 * returns null if the location doesn't exists on the grid.
	 * 
	 * @param coordinate
	 *        The coordinate of the square.
	 * @return The square at that coordinate or null if the location doesn't
	 *         exists.
	 */
	public ASquare getSquareAt(Coordinate coordinate);
	
	// FIXME check null
	
	/**
	 * Return all the possible coordinates in the grid.
	 * 
	 * @return all coordinates in the grid 
	 */
	public Set<Coordinate> getAllGridCoordinates();
}
