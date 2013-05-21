package grid;

import grid.builder.GridBuilder;
import grid.builder.GridBuilderDirector;
import item.IItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import square.AbstractSquare;
import square.SquareContainer;

/**
 * A grid that consists of {@link SquareContainer squares}.
 * 
 */
public class Grid implements IGrid {
	
	private Map<Coordinate, SquareContainer>	grid;
	
	/**
	 * Create a new grid with a specified grid and player map.
	 * 
	 * <br>
	 * <b>One should not use this constructor. Use a {@link GridBuilder builder}
	 * and a {@link GridBuilderDirector director} instead.<b>
	 * 
	 * @param grid
	 *        a map that maps the coordinates of each square to the actual
	 *        square itself
	 */
	public Grid(Map<Coordinate, SquareContainer> grid) {
		if (grid == null)
			throw new IllegalArgumentException("Null grid could not be created!");
		this.grid = grid;
	}
	
	/**
	 * returns the number of squares in this grid. If the grid contains more
	 * than Integer.MAX_VALUE elements, returns Integer.MAX_VALUE.
	 * 
	 * @return the number of squares
	 */
	public int size() {
		return grid.size();
	}
	
	/**
	 * Return the grid. (for testing purposes)
	 * 
	 * @return returns the grid
	 */
	Map<Coordinate, SquareContainer> getGrid() {
		return new HashMap<Coordinate, SquareContainer>(grid);
	}
	
	/**
	 * Returns an iterator over all the {@link SquareContainer squares} in the
	 * grid. There are no guarantees concerning the order in which they are
	 * returned.
	 * 
	 * @return an iterator over the squares of this grid.
	 */
	public Iterator<SquareContainer> getGridIterator() {
		return new GridIterator(this.grid.values().iterator());
	}
	
	@Override
	public List<IItem> getItemList(Coordinate coordinate) {
		if (coordinate == null)
			throw new IllegalArgumentException("the specified coordinate cannot be null");
		return grid.get(coordinate).getAllItems();
	}
	
	@Override
	public AbstractSquare getSquareAt(Coordinate coordinate) {
		if (coordinate == null)
			throw new IllegalArgumentException("the specified coordinate cannot be null");
		return grid.get(coordinate);
	}
	
	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				if (grid.get(new Coordinate(j, i)) == null)
					str += "  ";
				else
					str += grid.get(new Coordinate(j, i)).toString();
			}
			str += "\n";
		}
		return str;
	}
	
	/**
	 * This method will return the number of rows in the grid.
	 * 
	 * @return The number of rows in the grid.
	 */
	public int getHeight() {
		Set<Coordinate> gridCoords = grid.keySet();
		
		int maxYCoord = 0;
		for (Coordinate c : gridCoords) {
			if (c.getY() > maxYCoord)
				maxYCoord = c.getY();
		}
		
		return maxYCoord + 1;
	}
	
	/**
	 * This method will return the number of columns in the grid.
	 * 
	 * @return The number of columns in the grid.
	 */
	public int getWidth() {
		Set<Coordinate> gridCoords = grid.keySet();
		
		int maxXCoord = 0;
		for (Coordinate c : gridCoords) {
			if (c.getX() > maxXCoord)
				maxXCoord = c.getX();
		}
		
		return maxXCoord + 1;
	}
	
	@Override
	public Set<Coordinate> getAllGridCoordinates() {
		return new HashSet<Coordinate>(this.grid.keySet());
	}
	
	/**
	 * Get all the starting positions. The list will be sorted by their
	 * startingposition number (from smaller to larger).
	 * 
	 * @return a sorted list of all the startingpositions on the grid.
	 */
	public List<SquareContainer> getAllStartingPositions() {
		List<SquareContainer> result = new ArrayList<SquareContainer>();
		for (SquareContainer square : grid.values()) {
			if (square.getStartingPosition() > 0)
				result.add(square);
		}
		Collections.sort(result, new StartingPositionComparator());
		return result;
	}
	
	private class StartingPositionComparator implements Comparator<SquareContainer> {
		
		@Override
		public int compare(SquareContainer o1, SquareContainer o2) {
			if (o1.getStartingPosition() < o2.getStartingPosition())
				return -1;
			if (o1.getStartingPosition() > o2.getStartingPosition()) {
				return 1;
			}
			return 0;
			
		}
	}
}
