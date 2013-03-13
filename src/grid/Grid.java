package grid;

import game.Game;
import grid.Wall.WallPart;
import item.IItem;
import item.LightGrenade;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import player.IPlayer;
import player.Player;

public class Grid implements IGrid {
	
	private static final double			NUMBER_OF_ITEMS_ON_BOARD	= 0.05;
	private Map<Coordinate, ASquare>	grid;
	private Map<Integer, Coordinate>	players;
	private List<Wall>					walls;
	private int							width;
	private int							height;
	private static final int			MINIMUM_WALL_SIZE			= 2;
	
	/**
	 * Create a new grid with a specified builder. This will automatically place
	 * random wall on the board
	 * 
	 * 
	 * @param builder
	 */
	private Grid(Builder builder) {
		// set the width en height of the board
		this.width = builder.width;
		this.height = builder.height;
		// build empty board
		walls = new ArrayList<Wall>();
		grid = new HashMap<Coordinate, ASquare>();
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				Square sq = new Square();
				grid.put(new Coordinate(i, j), sq);
			}
		
		// place walls on the grid
		int max = MINIMUM_WALL_SIZE
				+ (int) (builder.maximumNumberOfWalls * grid.size() - MINIMUM_WALL_SIZE - builder.maximalLengthOfWall
						* Math.max(width, height));
		int maximumNumberOfWalls = new Random().nextInt(max);
		while (maximumNumberOfWalls >= getNumberOfWallParts())
			placeWall(builder.maximumNumberOfWalls, builder.maximalLengthOfWall);
		
		// if there are players in the builder, place them on the board
		if (builder.players.size() == 2)
			placePlayersOnBoard(builder.players);
		
		// place the items on the board
		placeItemsOnBoard();
	}
	
	/**
	 * TODO
	 * 
	 * @param p
	 * @param d
	 */
	private void updatePlayerLocation(Player p, Direction d) {
		
	}
	
	private void placePlayersOnBoard(List<IPlayer> players) {
		this.players = new HashMap<Integer, Coordinate>();
		((Square) grid.get(new Coordinate(width - 1, 0))).setPlayer(players.get(0));
		this.players.put(players.get(0).getID(), new Coordinate(width - 1, 0));
		((Square) grid.get(new Coordinate(0, height - 1))).setPlayer(players.get(1));
		this.players.put(players.get(1).getID(), new Coordinate(0, height - 1));
	}
	
	private void placeItemsOnBoard() {
		int numberOfLightGrenades = 0;
		while (((double) numberOfLightGrenades) / grid.size() < NUMBER_OF_ITEMS_ON_BOARD) {
			Coordinate position = Coordinate.random(width, height);
			if (canPlaceItem(position)) {
				((Square) grid.get(position)).addItem(new LightGrenade());
				numberOfLightGrenades++;
			}
		}
	}
	
	private boolean canPlaceItem(Coordinate coordinate) {
		if (coordinate.equals(new Coordinate(width - 1, 0))
				|| coordinate.equals(new Coordinate(0, height - 1)))
			return false;
		return grid.get(coordinate).getClass() != WallPart.class;
	}
	
	/**
	 * place a new wall on the grid. This method will automatically determine
	 * the maximum length of the wall.
	 * 
	 * @param maxPercentage
	 *        The maximum percentage of walls on the grid
	 */
	private void placeWall(double maxPercentage, double maximalLengthOfWall) {
		int wallLength = MINIMUM_WALL_SIZE
				+ new Random().nextInt(getMaximumLengthOfWall(maxPercentage, maximalLengthOfWall)
						- MINIMUM_WALL_SIZE);
		
		Coordinate start, end;
		
		do {
			start = Coordinate.random(width, height);
			end = start.getRandomCoordinateWithDistance(wallLength);
			// Logically, we should do -1 here, but wallLength is a random
			// excluding the wallLength itself, so it implicitly already
			// happened.
		} while (!canPlaceWall(start, end));
		// place the wall on the grid
		placeWallOnGrid(start, end);
	}
	
	/**
	 * This method will place a wall on the grid with a specified start and end
	 * position. If the wall cannot be placed on the board this method will
	 * throw an {@link IllegalArgumentException}
	 * 
	 * @param start
	 *        the start position of the grid
	 * @param end
	 *        the end position of the grid
	 * @throws IllegalArgumentException
	 *         if the wall cannot be placed on the board
	 */
	private void placeWallOnGrid(Coordinate start, Coordinate end) throws IllegalArgumentException {
		if (!canPlaceWall(start, end))
			throw new IllegalArgumentException("the wall cannot be placed on the board");
		Wall wall = new Wall(start, end);
		for (Coordinate coord : getWallPositions(start, end))
			grid.put(coord, wall.getWallPart());
		walls.add(wall);
	}
	
	/**
	 * Returns the maximum length for new wall that is to be placed on the
	 * board. this method takes into account the maximum percentage of walls on
	 * the board. This number will be rounded up.
	 * 
	 * @param maxPercentage
	 *        the maximum percentage of walls on the board
	 * @return the maximum length of a new wall
	 */
	private int getMaximumLengthOfWall(double maxPercentage, double maximalLengthOfWall) {
		int maxLength = 0;
		int walls = getNumberOfWallParts();
		// increase maxLength until a maximum value is reached
		while (maxPercentage >= (walls + maxLength++) / ((double) size()));
		
		int maxLength2 = (int) (maximalLengthOfWall * Math.max(height, width));
		
		return Math.min(maxLength, maxLength2);
	}
	
	/**
	 * returns whether a wall, specified by its start and end position, can be
	 * placed on the board.
	 * 
	 * @param start
	 *        the start position of the wall
	 * @param end
	 *        the end position of the wall
	 * @return true if a wall can be placed, else false
	 * 
	 */
	private boolean canPlaceWall(Coordinate start, Coordinate end) {
		// walls must be placed on the board
		if (start.getX() >= width || start.getX() < 0 || start.getY() >= height || start.getY() < 0)
			return false;
		if (end.getX() >= width || end.getX() < 0 || end.getY() >= height || end.getY() < 0)
			return false;
		// walls cannot be placed on start positions
		if (start.equals(new Coordinate(0, height - 1))
				|| start.equals(new Coordinate(width - 1, 0)))
			return false;
		if (end.equals(new Coordinate(0, height - 1)) || end.equals(new Coordinate(width - 1, 0)))
			return false;
		// walls cannot touch other walls on the board
		for (Wall w : walls)
			if (w.touchesWall(new Wall(start, end)))
				return false;
		return true;
	}
	
	/**
	 * Return all the coordinates of a wall that starts and ends at two certain
	 * points.
	 * 
	 * @param start
	 *        The start position of the wall.
	 * @param end
	 *        The end position of the wall.
	 * @return A collection of coordinates of this wall.
	 * @throws IllegalArgumentException
	 *         If the given positions are not aligned.
	 */
	private Collection<Coordinate> getWallPositions(Coordinate start, Coordinate end) {
		Collection<Coordinate> positions = new ArrayList<Coordinate>();
		
		// start adding the coordinates
		if (start.getX() == end.getX() && start.getY() < end.getY())
			for (int i = start.getY(); i <= end.getY(); i++)
				positions.add(new Coordinate(start.getX(), i));
		else if (start.getX() == end.getX() && start.getY() > end.getY())
			for (int i = start.getY(); i >= end.getY(); i--)
				positions.add(new Coordinate(start.getX(), i));
		else if (start.getY() == end.getY() && start.getX() < end.getX())
			for (int i = start.getX(); i <= end.getX(); i++)
				positions.add(new Coordinate(i, start.getY()));
		else if (start.getY() == end.getY() && start.getX() > end.getX())
			for (int i = start.getX(); i >= end.getX(); i--)
				positions.add(new Coordinate(i, start.getY()));
		else
			// the positions are not aligned...
			throw new IllegalArgumentException("The given positions " + start + ", " + end
					+ " are not alligned!");
		return positions;
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
	
	private int getNumberOfWallParts() {
		int i = 0;
		for (ASquare square : grid.values())
			if (square.getClass() == WallPart.class)
				i++;
		return i;
	}
	
	/**
	 * Return the grid.
	 */
	public Map<Coordinate, ASquare> getGrid() {
		return grid;
	}
	
	@Override
	public boolean canMovePlayer(int playerID, Direction direction) {
		// player and direction must exist
		if (!players.containsKey(playerID) || direction == null)
			return false;
		// next coordinate must be on the grid
		else if (!grid.containsKey(players.get(playerID).getCoordinateInDirection(direction)))
			return false;
		// players cannot move through walls
		else if (grid.get(players.get(playerID).getCoordinateInDirection(direction)).getClass() == WallPart.class)
			return false;
		// players cannot occupy the same position
		else if (players.containsValue(players.get(playerID).getCoordinateInDirection(direction)))
			return false;
		// players cannot move through light trails
		else if (grid.get(players.get(playerID).getCoordinateInDirection(direction))
				.hasLightTrail())
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
	public void movePlayer(int playerID, Direction direction) {
		if (!canMovePlayer(playerID, direction))
			throw new IllegalArgumentException("Player can not be moved in that direction!");
		Coordinate newCoord = players.get(playerID).getCoordinateInDirection(direction);
		Coordinate oldCoord = players.get(playerID);
		// update the squares
		((Square) grid.get(newCoord)).setPlayer(grid.get(oldCoord).getPlayer());
		((Square) grid.get(oldCoord)).setPlayer(null);
		// set a new position in the list of players
		players.put(playerID, newCoord);
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
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (grid.get(new Coordinate(i, j)) == null)
					str += "  ";
				else if (grid.get(new Coordinate(i, j)).getClass() == WallPart.class)
					str += "w ";
				else if (grid.get(new Coordinate(i, j)).getClass() == Square.class)
					str += "s ";
			}
			str += "\n";
		}
		return str;
	}
	
	/**
	 * A builder class for building a new grid.
	 * 
	 * @author tom
	 * 
	 */
	public static class Builder {
		
		private List<IPlayer>	players;
		private int				minimalLengthOfWall;
		private double			maximalLengthOfWall;
		private double			maximumNumberOfWalls;
		private int				width;
		private int				height;
		private Game			game;
		
		public Builder(Game game, List<IPlayer> players) {
			this.minimalLengthOfWall = 2;
			this.maximalLengthOfWall = 0.50;
			this.maximumNumberOfWalls = 0.20;
			this.width = 10;
			this.height = 10;
			this.game = game;
			this.players = players;
		}
		
		/**
		 * Set the minimum length of a wall in the grid. The specified length
		 * should be greater then or equal to 2.
		 * 
		 * @param minimalLength
		 *        the minimum length of a wall
		 * @return this
		 */
		// @Requires("minimalLengthOfWall >= 2")
		public Builder setMinimalLengthOfWall(int minimalLength) {
			this.minimalLengthOfWall = minimalLength;
			return this;
		}
		
		/**
		 * set the maximal length of a wall as a percentage of the grid's
		 * length/width.
		 * 
		 * @param maximalLength
		 *        the maximal length of a wall
		 * @return this
		 */
		public Builder setMaximalLengthOfWall(double maximalLength) {
			this.maximalLengthOfWall = maximalLength;
			return this;
		}
		
		/**
		 * set the maximum number of walls in the grid
		 * 
		 * @param maximum
		 *        the maximum number of walls
		 * @return this
		 */
		public Builder setMaximumNumberOfWalls(int maximum) {
			this.maximumNumberOfWalls = maximum;
			return this;
		}
		
		/**
		 * set the width of the grid. This must be a strictly positive integer
		 * 
		 * @param width
		 *        the width of the grid
		 * @return this
		 */
		// @Requires("width > 0")
		public Builder setGridWidth(int width) {
			this.width = width;
			return this;
		}
		
		/**
		 * set the height of the grid. This must be a strictly positive integer
		 * 
		 * @param height
		 *        the height of the grid
		 * @return this
		 */
		// @Requires("height > 0")
		public Builder setGridHeigth(int height) {
			this.height = height;
			return this;
		}
		
		public Grid build() {
			return new Grid(this);
		}
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
	 * @return 
	 */
	@Override
	public Coordinate movePlayerInDirection(IPlayer p, Direction d) {
		return null;
		// TODO Auto-generated method stub
		
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
