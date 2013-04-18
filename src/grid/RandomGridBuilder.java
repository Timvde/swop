package grid;

import item.identitydisk.UnchargedIdentityDisk;
import item.lightgrenade.LightGrenade;
import item.teleporter.Teleporter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import player.Player;
import square.ASquare;
import square.ISquare;
import square.Square;
import square.Wall;
import square.WallPart;

/**
 * A grid builder for generating random grids.
 * 
 * @author Tom
 * 
 */
public class RandomGridBuilder extends AGridBuilder {
	
	private static final int	MINIMUM_WALL_SIZE	= 2;
	
	private double				maximalLengthOfWall;
	private double				maximumNumberOfWalls;
	
	/**
	 * 
	 * @param players
	 */
	public RandomGridBuilder(List<Player> players) {
		super(players);
		
		this.maximalLengthOfWall = 0.50;
		this.maximumNumberOfWalls = 0.20;
	}
	
	/**
	 * set the maximal length of a wall as a percentage of the grid's
	 * length/width.
	 * 
	 * @param maximalLength
	 *        the maximal length of a wall
	 * @return this
	 */
	public AGridBuilder setMaximalLengthOfWall(double maximalLength) {
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
	public AGridBuilder setMaximumNumberOfWalls(int maximum) {
		this.maximumNumberOfWalls = maximum;
		return this;
	}
	
	/**
	 * returns the number of walls in the grid
	 * 
	 * @return number of walls
	 */
	private int getNumberOfWallParts() {
		int i = 0;
		for (ISquare square : grid.values())
			if (square.getClass() == WallPart.class)
				i++;
		return i;
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
			grid.put(coord, getWallPart(coord));
		walls.add(wall);
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
		if (!grid.containsKey(start) || !grid.containsKey(end))
			return false;
		// walls cannot be placed on start positions
		if (getSquare(start).hasPlayer() || getSquare(end).hasPlayer())
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
					+ " are not aligned!");
		return positions;
	}
	
	/**
	 * place a new wall on the grid. This method will automatically determine
	 * the maximum length of the wall.
	 * 
	 * @param maxPercentage
	 *        The maximum percentage of walls on the grid
	 */
	private void placeWall(double maxPercentage, double maximalLengthOfWall) {
		// generate random number between a minimum and a maximum
		int max = getMaximumLengthOfWall(maxPercentage, maximalLengthOfWall);
		int wallLength = new Random().nextInt(max - MINIMUM_WALL_SIZE + 1) + MINIMUM_WALL_SIZE;
		
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
	 * Build a new grid object. The grid will be build with the parameters set
	 * in this builder. If these parameters were not set, the default values
	 * will be used.
	 * 
	 * @return a new grid object
	 */
	public Grid build() {
		walls = new ArrayList<Wall>();
		grid = new HashMap<Coordinate, ASquare>();
		teleporterCoords = new HashMap<Teleporter, Coordinate>();
		
		// Populate the grid with squares
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				grid.put(new Coordinate(i, j), getSquare(new Coordinate(i, j)));
			}
		
		// place players on the board and set their starting positions
		List<Coordinate> startingCoordinates = calculateStartingPositionsOfPlayers();
		
		for (int i = 0; i < players.size(); ++i) {
			players.get(i).setStartingPosition(getSquare(startingCoordinates.get(i)));
			getSquare(startingCoordinates.get(i)).addPlayer(players.get(i));
		}
		
		// place walls on the grid
		int max = MINIMUM_WALL_SIZE
				+ (int) (maximumNumberOfWalls * grid.size() - MINIMUM_WALL_SIZE - maximalLengthOfWall
						* Math.max(width, height));
		int maximumNumberOfWalls = new Random().nextInt(max);
		while (maximumNumberOfWalls >= getNumberOfWallParts())
			placeWall(maximumNumberOfWalls, maximalLengthOfWall);
		
		// place the items on the board
		placeItemsOnBoard(startingCoordinates);
		return new Grid(grid);
	}
	
	/* -------------------- GRID FOR TESTING --------------------- */
	
	/**
	 * This function should ONLY be used by getPredefinedGrid(), to get a
	 * deterministic grid we can use to test. This should not be used in
	 * gameplay.
	 */
	private Grid buildTestGrid(int width, int height, List<Wall> walls, boolean usePowerfailure) {
		this.walls = new ArrayList<Wall>();
		grid = new HashMap<Coordinate, ASquare>();
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				grid.put(new Coordinate(i, j), getSquare(new Coordinate(i, j)));
			}
		
		// place players on the board
		List<Coordinate> startingCoordinates = calculateStartingPositionsOfPlayers();
		
		for (int i = 0; i < players.size(); ++i) {
			players.get(i).setStartingPosition(getSquare(startingCoordinates.get(i)));
			getSquare(startingCoordinates.get(i)).addPlayer(players.get(i));
		}
		
		placeWallOnGrid(walls.get(0).getStart(), walls.get(0).getEnd());
		
		((Square) getSquare(new Coordinate(2, 7))).addItem(new LightGrenade());
		((Square) getSquare(new Coordinate(5, 8))).addItem(new LightGrenade());
		((Square) getSquare(new Coordinate(6, 8))).addItem(new LightGrenade());
		((Square) getSquare(new Coordinate(7, 8))).addItem(new LightGrenade());
		((Square) getSquare(new Coordinate(7, 6))).addItem(new LightGrenade());
		((Square) getSquare(new Coordinate(8, 8))).addItem(new LightGrenade());
		((Square) getSquare(new Coordinate(8, 7))).addItem(new LightGrenade());
		((Square) getSquare(new Coordinate(7, 2))).addItem(new LightGrenade());
		
		((Square) getSquare(new Coordinate(7, 0))).addItem(new UnchargedIdentityDisk());
		((Square) getSquare(new Coordinate(2, 9))).addItem(new UnchargedIdentityDisk());
		
		Grid final_grid = new Grid(grid);
		
		if (usePowerfailure)
			final_grid.addPowerFailureAtCoordinate(new Coordinate(4, 1));
		
		final_grid.enablePowerFailures(false);
		
		return final_grid;
	}
	
	/*----------- Predefined testGrid methods -----------*/
	
	/**
	 * This function returns a predefined grid which we can use to test. This
	 * should not be used in game play. The returned grid is shown below. The
	 * legend for this grid is a followed:
	 * <ul>
	 * <li>numbers: starting position of the players</li>
	 * <li>x: walls</li>
	 * <li>o: light grenades</li>
	 * <li>t: teleporters (these teleport to the square right above)</li>
	 * <li>d: destination of the teleporters</li>
	 * <li>F: Power failure</li>
	 * <li>i: Identity disc</li>
	 * </ul>
	 * 
	 * <pre>
	 *   _____________________________
	 *  |  |  |  | F| F| F|  | i|  | 2|
	 *  |  |  |  | F| F| F|  |  |  |  |
	 *  |  |  |  | F| F| F|  | o|  |t1|
	 *  |  |  |  |  |  |  |  |  |  |d2|
	 *  |  |  |  |  |  |  |  |  |  |  |
	 *  |  |  |  |  | x| x| x| x| x|  |
	 *  |  |  |  |  |  |  |  | o|  |  |
	 *  |t2|  | o|  |  |  |  |  | o|  |
	 *  |d1|  |  |  |  | o| o| o| o|  |
	 *  | 1|  | i|  |  |  |  |  |  |  |
	 *  -------------------------------
	 * </pre>
	 * 
	 * @param usePowerfailure
	 *        Boolean to express if there must be a power failure in the test
	 *        grid. Shown by F in the map above.
	 * 
	 * @return the new predefined grid
	 */
	public Grid getPredefinedTestGrid(boolean usePowerfailure) {
		List<Wall> walls = new ArrayList<Wall>();
		walls.add(new Wall(new Coordinate(4, 5), new Coordinate(8, 5)));
		
		return buildTestGrid(10, 10, walls, usePowerfailure);
	}
	
	/**
	 * The size of the grid returned by
	 * {@link RandomGridBuilder#getPredefinedTestGrid(boolean)} Used for testing
	 * purposes, should not be used in game play.
	 */
	public static final int	PREDIFINED_GRID_SIZE	= 10;
	
}
