package grid;

import item.IItem;
import java.util.List;
import java.util.Set;
import square.ASquare;
import square.Direction;
import square.ISquare;
import notnullcheckweaver.NotNull;

/**
 * A grid that consists of abstract {@link ASquare squares}.
 * 
 */
public interface IGrid {
	
	/**
	 * Returns whether or not one is allowed to move one Square from a specified
	 * {@link Coordinate} in a specified {@link Direction}. One can only move to
	 * coordinates that exists on the grid (i.e.
	 * <code>fromCoordinate.getCoordinateInDirection(direction)</code> must
	 * exist on the grid). One cannot move through walls or light trails. No two
	 * players can be on the same Square.
	 * 
	 * @param fromCoordinate
	 *        The coordinate one wants to leave.
	 * @param direction
	 *        The direction one wants to move in.
	 * @return whether one is allowed to move one Square from a specified
	 *         {@link Coordinate} in a specified {@link Direction}.
	 */
	public abstract boolean canMoveFromCoordInDirection(Coordinate fromCoordinate,
			Direction direction);
	
	/**
	 * returns the list of items on a square, when there are no items on the
	 * specified coordinate an empty list will be returned.
	 * 
	 * @param coordinate
	 *        the coordinate of the square where the items are located
	 * @return a list of items on a square
	 */
	public List<IItem> getItemList(@NotNull Coordinate coordinate);
	
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
	public ISquare getSquareAt(@NotNull Coordinate coordinate);
	
	// FIXME check null
	
	/**
	 * Return all the possible coordinates in the grid.
	 * 
	 * @return all coordinates in the grid 
	 */
	public Set<Coordinate> getAllGridCoordinates();
}
