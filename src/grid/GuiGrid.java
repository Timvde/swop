package grid;

import java.util.Set;

/**
 * This is a grid object used by the GUI, with limited methods for encapsulation.
 *
 */
public interface GuiGrid {
	
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
	GuiSquare getSquareAt(Coordinate coordinate) throws IllegalArgumentException;
	
	/**
	 * Return all the possible coordinates in the grid.
	 * 
	 * @return all coordinates in the grid
	 * @throws IllegalArgumentException
	 *         The specified coordinate cannot be <code>null</code>.
	 */
	Set<Coordinate> getAllGridCoordinates() throws IllegalArgumentException;
	
	/**
	 * returns the width of the grid
	 * 
	 * @return width of the grid
	 */
	int getWidth();
	
	/**
	 * returns the height of the grid
	 * 
	 * @return height of the grid
	 */
	int getHeight();
}
