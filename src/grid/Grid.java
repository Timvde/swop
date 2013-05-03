package grid;

import grid.builder.GridBuilder;
import grid.builder.GridBuilderDirector;
import item.IItem;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import square.ASquare;
import square.PlayerStartingPosition;
import square.ISquare;
import square.PowerFailure;
import square.PrimaryPowerFailure;
import square.Square;

/**
 * A grid that consists of abstract {@link ASquare squares}.
 */
public class Grid implements IGrid {
	
	private Map<Coordinate, ASquare>	grid;
	private static final float			POWER_FAILURE_CHANCE	= 0.01F;
	private boolean						ENABLE_POWER_FAILURE;
	
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
	public Grid(Map<Coordinate, ASquare> grid) {
		if (grid == null)
			throw new IllegalArgumentException("Grid could not be created!");
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
	 * Return the grid.
	 * 
	 * @return returns the grid
	 */
	public Map<Coordinate, ASquare> getGrid() {
		return new HashMap<Coordinate, ASquare>(grid);
	}
	
	@Override
	public List<IItem> getItemList(Coordinate coordinate) {
		return grid.get(coordinate).getAllItems();
	}
	
	@Override
	public ASquare getSquareAt(Coordinate coordinate) {
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
	 * <<<<<<< HEAD Returns a set of all the {@link PlayerStartingPosition
	 * startingpositions} on the grid. ======= This method updates all power
	 * failure related things.
	 * 
	 * <pre>
	 * - Current power failures will lose a TTL counter and be removed
	 *   if it drops to zero
	 * - Each Square has a 5% chance to become power failured
	 * </pre>
	 */
	public void updatePowerFailures() {
		for (PowerFailure failure : getAllPowerFailures())
			failure.decreaseTimeToLive();
		
		if (ENABLE_POWER_FAILURE) {
			Random rand = new Random();
			for (Coordinate coordinate : getGrid().keySet()) {
				if (rand.nextFloat() < POWER_FAILURE_CHANCE) {
					addPowerFailureAtCoordinate(coordinate);
				}
			}
		}
	}
	
	/**
	 * Get all the starting positions.
	 * 
	 * @return a set of all the startingpositions on the grid.
	 */
	public Set<PlayerStartingPosition> getAllStartingPositions() {
		Set<PlayerStartingPosition> result = new HashSet<PlayerStartingPosition>();
		for (ASquare square : grid.values()) {
			if (square instanceof PlayerStartingPosition)
				result.add((PlayerStartingPosition) square);
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
	
	private Set<PowerFailure> getAllPowerFailures() {
		Set<PowerFailure> failures = new HashSet<PowerFailure>();
		for (ISquare square : getGrid().values()) {
			if (square.hasPowerFailure())
				failures.add(((Square) square).getPowerFailure());
		}
		return failures;
	}
	
}
