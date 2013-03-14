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
	private Map<IPlayer, Coordinate>	players;
	
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
	Grid(Map<Coordinate, ASquare> grid, Map<IPlayer, Coordinate> players) {
		if (grid == null || players == null)
			throw new IllegalArgumentException("Grid could not be created!");
		
		this.grid = grid;
		this.players = players;
	}
	
	/**
	 * TODO
	 * 
	 * @param p
	 * @param d
	 */
	private void updatePlayerLocation(IPlayer p, Direction d) {
		Coordinate currCoord = this.players.get(p);
		int currCoordX = currCoord.getX();
		int currCoordY = currCoord.getY();
		
		if (d == Direction.NORTH) {
			Coordinate newCoord = new Coordinate(currCoordX, currCoordY - 1);
			this.players.put(p, newCoord);
		}
		else if (d == Direction.NORTHEAST) {
			Coordinate newCoord = new Coordinate(currCoordX + 1, currCoordY - 1);
			this.players.put(p, newCoord);
		}
		else if (d == Direction.EAST) {
			Coordinate newCoord = new Coordinate(currCoordX + 1, currCoordY);
			this.players.put(p, newCoord);
		}
		else if (d == Direction.SOUTHEAST) {
			Coordinate newCoord = new Coordinate(currCoordX + 1, currCoordY + 1);
			this.players.put(p, newCoord);
		}
		else if (d == Direction.SOUTH) {
			Coordinate newCoord = new Coordinate(currCoordX, currCoordY + 1);
			this.players.put(p, newCoord);
		}
		else if (d == Direction.SOUTHWEST) {
			Coordinate newCoord = new Coordinate(currCoordX - 1, currCoordY + 1);
			this.players.put(p, newCoord);
		}
		else if (d == Direction.WEST) {
			Coordinate newCoord = new Coordinate(currCoordX - 1, currCoordY);
			this.players.put(p, newCoord);
		}
		else if (d == Direction.NORTHWEST) {
			Coordinate newCoord = new Coordinate(currCoordX - 1, currCoordY - 1);
			this.players.put(p, newCoord);
		}
		else {
			throw new IllegalArgumentException("Not a valid direction!");
		}
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
	public boolean canMovePlayer(IPlayer player, Direction direction) {
		// player and direction must exist
		if (!players.containsKey(player) || direction == null)
			return false;
		// next coordinate must be on the grid
		else if (!grid.containsKey(players.get(player).getCoordinateInDirection(direction)))
			return false;
		// players cannot move through walls
		else if (grid.get(players.get(player).getCoordinateInDirection(direction)).getClass() == WallPart.class)
			return false;
		// players cannot occupy the same position
		else if (players.containsValue(players.get(player).getCoordinateInDirection(direction)))
			return false;
		// players cannot move through light trails
		else if (grid.get(players.get(player).getCoordinateInDirection(direction)).hasLightTrail())
			return false;
		// else if (direction.getPrimeryDirections().size() == 2
		// && grid.get(
		// players.get(playerID).getCoordinateInDirection(
		// direction.getPrimeryDirections().get(0))).hasLightTrail()
		// && grid.get(
		// players.get(playerID).getCoordinateInDirection(
		// direction.getPrimeryDirections().get(1))).hasLightTrail())
		// return false;
		return true;
	}
	
	@Override
	public void movePlayer(IPlayer player, Direction direction) {
		if (!canMovePlayer(player, direction))
			throw new IllegalArgumentException("Player can not be moved in that direction!");
		Coordinate newCoord = players.get(player).getCoordinateInDirection(direction);
		Coordinate oldCoord = players.get(player);
		// update the squares
		((Square) grid.get(newCoord)).setPlayer(grid.get(oldCoord).getPlayer());
		((Square) grid.get(oldCoord)).setPlayer(null);
		// set a new position in the list of players
		players.put(player, newCoord);
		// set the ligh trail on a previous square
		((Square) grid.get(oldCoord)).placeLightTrail();
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
	public Coordinate getPlayerCoordinate(IPlayer player) {
		return this.players.get(player.getID());
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
	
	/**
	 * TODO
	 * 
	 * @return
	 */
	@Override
	public Coordinate movePlayerInDirection(IPlayer p, Direction d) {
		if (!canMovePlayer(p, d))
			throw new IllegalStateException("Cannot move the player in this direction!");
		
		ASquare oldSquare = this.grid.get(this.players.get(p));
		updatePlayerLocation(p, d);
		ASquare newSquare = this.grid.get(this.players.get(p));
		
		oldSquare.removePlayer();
		newSquare.setPlayer(p);
		
		return this.players.get(p);
	}
	
	/**
	 * TODO
	 */
	@Override
	public ASquare getSquareOfPlayer(IPlayer p) {
		// TODO Auto-generated method stub
		return null;
	}
}
