package grid;

import item.IItem;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Set;
import player.PlayerDataBase;
import square.ASquare;
import square.ISquare;
import square.PowerFailure;
import square.Square;
import square.WallPart;

/**
 * A grid that consists of abstract {@link ASquare squares}.
 * 
 * @author Bavo Mees
 */
public class Grid implements IGrid, Observer {
	
	private Map<Coordinate, ASquare>	grid;
	private static final float			POWER_FAILURE_CHANCE	= 0.05F;
	private boolean						ENABLE_POWER_FAILURE;
	
	/**
	 * Create a new grid with a specified grid and player map.
	 * 
	 * @param grid
	 *        a map that maps the coordinates of each square to the actual
	 *        square itself
	 * @param players
	 *        a map that maps each player to its coordinate on the grid
	 * 
	 * 
	 */
	Grid(Map<Coordinate, ASquare> grid) {
		if (grid == null)
			throw new IllegalArgumentException("Grid could not be created!");
		this.grid = grid;
		
		ENABLE_POWER_FAILURE = true;
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
		for (int j = 0; j < getWidth(); j++) {
			for (int i = 0; i < getHeight(); i++) {
				if (grid.get(new Coordinate(i, j)) == null)
					str += "  ";
				else
					str += grid.get(new Coordinate(i, j)).toString();
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
		
		int maxRowNum = 0;
		for (Coordinate c : gridCoords) {
			if (c.getY() > maxRowNum)
				maxRowNum = c.getY();
		}
		
		return maxRowNum + 1;
	}
	
	/**
	 * This method will return the number of columns in the grid.
	 * 
	 * @return The number of columns in the grid.
	 */
	public int getWidth() {
		Set<Coordinate> gridCoords = grid.keySet();
		
		int maxColNum = 0;
		for (Coordinate c : gridCoords) {
			if (c.getX() > maxColNum)
				maxColNum = c.getX();
		}
		
		return maxColNum + 1;
	}
	
	@Override
	public Set<Coordinate> getAllGridCoordinates() {
		return new HashSet<Coordinate>(this.grid.keySet());
	}
	
	/**
	 * This method updates all power failure related things.
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
	 * Add a power failure at a given coordinate. This method will also make
	 * sure the surrounding squares are power failured.
	 * 
	 * @param coordinate
	 *        The coordinate to receive the power failure.
	 */
	public void addPowerFailureAtCoordinate(Coordinate coordinate) {
		if (grid.containsKey(coordinate))
			new PowerFailure(grid.get(coordinate));
	}
	
	private Set<PowerFailure> getAllPowerFailures() {
		Set<PowerFailure> failures = new HashSet<PowerFailure>();
		for (ISquare square : getGrid().values()) {
			if (square.hasPowerFailure())
				failures.add(((Square) square).getPowerFailure());
		}
		return failures;
	}
	
	/**
	 * Enable or disable power failures.
	 * 
	 * @param enabled
	 *        True if we want power failures in the game, false otherwise. By
	 *        default, power failures will exist. This has been added for
	 *        debugging purposes.
	 */
	public void enablePowerFailures(boolean enabled) {
		this.ENABLE_POWER_FAILURE = enabled;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof PlayerDataBase) {
			this.updatePowerFailures();
		}
		// else do nothing; return
	}
}
