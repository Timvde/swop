package grid;

import grid.Wall.WallPart;
import item.IItem;
import java.util.List;
import java.util.Map;
import java.util.Set;
import player.IPlayer;
import player.Player;

/**
 * A grid that consists of abstract {@link ASquare squares}.
 * 
 * @author Bavo Mees
 */
public class Grid implements IGrid {
	
	private Map<Coordinate, ASquare>	grid;
	private List<Wall>					walls;
	private int							width;
	private int							height;
	private static final int			MINIMUM_WALL_SIZE	= 2;
	
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
		return grid;
	}
	
	@Override
	public List<IItem> getItemList(Coordinate coordinate) {
		return grid.get(coordinate).getCarryableItems();
	}
	
	@Override
	public ASquare getSquareAt(Coordinate coordinate) {
		return grid.get(coordinate);
	}
	
	@Override
	public String toString() {
		String str = "";
		for (int j = 0; j < 10; j++) {
			for (int i = 0; i < 10; i++) {
				if (grid.get(new Coordinate(i, j)) == null)
					str += "  ";
				else if (grid.get(new Coordinate(i, j)).getClass() == WallPart.class)
					str += "w ";
				else if (grid.get(new Coordinate(i, j)).getClass() == Square.class) {
					if (grid.get(new Coordinate(i, j)).getCarryableItems().size() != 0)
						str += "l ";
					else
						str += "s ";
				}
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
		return this.grid.keySet();
	}
	
	@Override
	public boolean canMoveFromCoordInDirection(Coordinate fromCoord, Direction direction) {
		// direction and fromCoord must exist
		if (direction == null || fromCoord == null)
			return false;
		Coordinate toCoord = fromCoord.getCoordinateInDirection(direction);
		// next coordinate must be on the grid
		if (!grid.containsKey(toCoord))
			return false;
		// players cannot move through walls
		if (grid.get(toCoord) instanceof WallPart)
			return false;
		// players cannot occupy the same position
		Square toSquare = (Square) grid.get(toCoord);
		if (toSquare.hasPlayer())
			return false;
		// players cannot move through light trails
		if (this.crossesLightTrail(fromCoord, direction)) {
			return false;
		}
		return true;
	}
	
	/**
	 * This method returns whether or not one crosses or ends in a lightTrail if
	 * he moves one square from a specified coordinate in a specified direction.
	 * 
	 * @param fromCoord
	 *        The coordinate one wants to leave.
	 * 
	 * @param direction
	 *        he direction one wants to move in.
	 * @return whether one crosses or ends in a lightTrail if he moves one
	 *         square from a specified coordinate in a specified direction.
	 */
	private boolean crossesLightTrail(Coordinate fromCoord, Direction direction) {
		// direction must exist and coordinates must exist on grid
		if (fromCoord == null || direction == null
				|| !grid.containsKey(fromCoord.getCoordinateInDirection(direction)))
			return false;
		
		Coordinate toCoord = fromCoord.getCoordinateInDirection(direction);
		// the diagonal squares must exist both on the grid (the grid is always
		// a square)
		if (!grid.containsKey(direction.getCrossingCoordinateOnYAxis(toCoord))
				|| !grid.containsKey(direction.getCrossingCoordinateOnYAxis(toCoord))) {
			return false;
		}
		
		// players cannot move on light trails
		if (grid.get(toCoord).hasLightTrail()) {
			return true;
		}
		
		ASquare diagSquare1 = grid.get(direction.getCrossingCoordinateOnXAxis(toCoord));
		ASquare diagSquare2 = grid.get(direction.getCrossingCoordinateOnYAxis(toCoord));
		
		if (diagSquare1.equals(diagSquare2)) {
			return false;
		}
		
		// players cannot cross light trails diagonally
		if ((diagSquare1.hasLightTrail() || diagSquare1.hasPlayer())
				&& (diagSquare2.hasLightTrail() || diagSquare2.hasPlayer())) {
			return true;
		}
		return false;
	}
}
