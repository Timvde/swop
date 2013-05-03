package grid;

import grid.builder.GridBuilder;
import grid.builder.GridBuilderDirector;
import item.IItem;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import powerfailure.PrimaryPowerFailure;
import square.AbstractSquare;
import square.PlayerStartingPosition;
import square.Square;
import square.SquareContainer;

/**
 * A grid that consists of abstract {@link AbstractSquare squares}.
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
			throw new IllegalArgumentException("Grid could not be created!");
		this.grid = grid;
		System.out.println(grid.toString());
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
	 * Return the grid.
	 * 
	 * @return returns the grid
	 */
	public Map<Coordinate, AbstractSquare> getGrid() {
		return new HashMap<Coordinate, AbstractSquare>(grid);
	}
	
	@Override
	public List<IItem> getItemList(Coordinate coordinate) {
		return grid.get(coordinate).getAllItems();
	}
	
	@Override
	public AbstractSquare getSquareAt(Coordinate coordinate) {
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
	 * Get all the starting positions.
	 * 
	 * @return a set of all the startingpositions on the grid.
	 */
	public Set<SquareContainer> getAllStartingPositions() {
		Set<SquareContainer> result = new HashSet<SquareContainer>();
		for (SquareContainer square : grid.values()) {
			if (square.isStartingPosition())
				result.add(square);
		}
		return result;
	}
	
	/**
	 * Add a primary powerfailure at the given coordinate. This possibly results
	 * in the creation of a secondary and tertiary powerfailure.
	 * 
	 * @param coordinate
	 *        The coordinate on which to add a powerfailure.
	 */
	public void addPowerFailureAtCoordinate(Coordinate coordinate) {
		if (grid.containsKey(coordinate))
			
			new PrimaryPowerFailure(grid.get(coordinate));
	}
}
