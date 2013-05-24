package grid;

import java.util.Set;
import square.Square;

/**
 * A grid containing {@link Square squares}.
 * 
 */
public interface IGrid {
	
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
