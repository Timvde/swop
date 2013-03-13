package grid;

import game.Game;
import grid.Wall.WallPart;
import item.LightGrenade;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import player.IPlayer;

/**
 * A builder used for building the grid. This builder contains all the
 * functionality for configuring and building the grid.
 * 
 * @author Bavo Mees
 */
public class GridBuilder {
	
	private static final double	NUMBER_OF_ITEMS_ON_BOARD	= 0.05;
	private static final int	MINIMUM_WALL_SIZE			= 2;
	
	private List<IPlayer>		players;
	private double				maximalLengthOfWall;
	private double				maximumNumberOfWalls;
	private int					width;
	private int					height;
	
	/**
	 * Create a new builder for the grid
	 * 
	 * @param game
	 *        the game object in which the grid is situated
	 * @param players
	 *        the players who need to be placed on the board
	 */
	public GridBuilder(Game game, List<IPlayer> players) {
		this.maximalLengthOfWall = 0.50;
		this.maximumNumberOfWalls = 0.20;
		this.width = 10;
		this.height = 10;
		this.players = players;
	}
	
	/**
	 * set the maximal length of a wall as a percentage of the grid's
	 * length/width.
	 * 
	 * @param maximalLength
	 *        the maximal length of a wall
	 * @return this
	 */
	public GridBuilder setMaximalLengthOfWall(double maximalLength) {
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
	public GridBuilder setMaximumNumberOfWalls(int maximum) {
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
	public GridBuilder setGridWidth(int width) {
		if (width >= 3 && height >= 3)
			throw new IllegalArgumentException("height and width cannot be less than three!");
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
	public GridBuilder setGridHeigth(int height) {
		if (width >= 3 && height >= 3)
			throw new IllegalArgumentException("height and width cannot be less than three!");
		this.height = height;
		return this;
	}
	
	private HashMap<Coordinate, ASquare>	grid;
	private ArrayList<Wall>					walls;
	private HashMap<IPlayer, Coordinate>	playerMap;
	
	/**
	 * Build a new grid object. The grid will be build with the parameters set
	 * in this builder. If these parameters were not set, the default values
	 * will be used.
	 * 
	 * @return a new grid object
	 */
	public Grid build() {
		walls = new ArrayList<Wall>();
		grid = new HashMap<Coordinate, ASquare>();
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				Square sq = new Square();
				grid.put(new Coordinate(i, j), sq);
			}
		
		// place walls on the grid
		int max = MINIMUM_WALL_SIZE
				+ (int) (maximumNumberOfWalls * grid.size() - MINIMUM_WALL_SIZE - maximalLengthOfWall
						* Math.max(width, height));
		int maximumNumberOfWalls = new Random().nextInt(max);
		while (maximumNumberOfWalls >= getNumberOfWallParts())
			placeWall(maximumNumberOfWalls, maximalLengthOfWall);
		
		// if there are players in the builder, place them on the board
		if (players.size() == 2)
			placePlayersOnBoard(players);
		
		// place the items on the board
		placeItemsOnBoard();
		return new Grid(grid, playerMap);
	}
	
	/* -------------------- grid building methods ------------------------ */
	
	/**
	 * returns the number of walls in the grid
	 * 
	 * @return number of walls
	 */
	private int getNumberOfWallParts() {
		int i = 0;
		for (ASquare square : grid.values())
			if (square.getClass() == WallPart.class)
				i++;
		return i;
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
		while (maxPercentage >= (walls + maxLength++) / ((double) grid.size()));
		
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
	 * put the specified players on the board
	 * 
	 * @param players
	 *        players to be put on the board
	 */
	private void placePlayersOnBoard(List<IPlayer> players) {
		this.playerMap = new HashMap<IPlayer, Coordinate>();
		((Square) grid.get(new Coordinate(width - 1, 0))).setPlayer(players.get(0));
		this.playerMap.put(players.get(0), new Coordinate(width - 1, 0));
		((Square) grid.get(new Coordinate(0, height - 1))).setPlayer(players.get(1));
		this.playerMap.put(players.get(1), new Coordinate(0, height - 1));
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
	 * Place a random number of items on the board. The number of items will be
	 * a rounded percentage ({@value #NUMBER_OF_ITEMS_ON_BOARD}) of the total
	 * size of the board. The items will be placed on the board with the
	 * following {@link #canPlaceItem(Coordinate) constraints}.
	 */
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
	
	/**
	 * returns whether an item can be placed on the board. An item cannot be
	 * placed on the board if the specified coordinate is a start position of
	 * one of the players, if the item will be placed on a wall, if the square
	 * already contains an item or if the coordinate is not on the grid.
	 * 
	 * @param coordinate
	 *        the coordinate on which the item will be placed
	 * @return true if the coordinate can be placed on the board
	 */
	private boolean canPlaceItem(Coordinate coordinate) {
		// an item cannot be place on the starting positions
		if (coordinate.equals(new Coordinate(width - 1, 0))
				|| coordinate.equals(new Coordinate(0, height - 1)))
			return false;
		// an item must be placed on the board
		else if (grid.get(coordinate) == null)
			return false;
		// an item cannot be place on a wall
		else if (grid.get(coordinate).getClass() == WallPart.class)
			return false;
		// an item cannot be placed on another item
		else if (!grid.get(coordinate).getCarryableItems().isEmpty())
			return false;
		else
			return true;
	}
}