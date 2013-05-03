package grid;

import item.IItem;
import item.identitydisk.ChargedIdentityDisk;
import item.identitydisk.UnchargedIdentityDisk;
import item.lightgrenade.LightGrenade;
import item.teleporter.Teleporter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import player.Player;
import square.AbstractSquare;
import square.Direction;
import square.Square;
import square.NormalSquare;
import square.Wall;
import square.WallPart;

/**
 * A builder used for building the grid. This builder contains all the
 * functionality for configuring and building the grid.
 * 
 * @author Bavo Mees
 */
public class GridBuilder {
	
	private static final double	NUMBER_OF_GRENADES			= 0.02;
	private static final double	NUMBER_OF_TELEPORTERS		= 0.03;
	private static final double	NUMBER_OF_IDENTITY_DISKS	= 0.02;
	private static final int	MINIMUM_WALL_SIZE			= 2;
	
	private double				maximalLengthOfWall;
	private double				maximumNumberOfWalls;
	private int					width;
	private int					height;
	private List<Player>		players;
	
	// Constraints
	private int					minimumGridWidth			= 10;
	private int					minimumGridHeight			= 10;
	
	/**
	 * Create a new builder for the grid.
	 * 
	 * @param players
	 *        The players to be placed on this grid.
	 * @throws IllegalArgumentException
	 *         When the number of players is not two.
	 */
	public GridBuilder(List<Player> players) throws IllegalArgumentException {
		if (players.size() != 2)
			throw new IllegalArgumentException(
					"At this moment, only a fixed number of two players is supported.");
		this.players = players;
		this.maximalLengthOfWall = 0.50;
		this.maximumNumberOfWalls = 0.20;
		this.width = 10;
		this.height = 10;
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
	 * set the width of the grid. This must be a strictly positive integer, and
	 * at least 10.
	 * 
	 * @param width
	 *        the width of the grid
	 * @return this
	 */
	// @Requires("width > 0")
	public GridBuilder setGridWidth(int width) {
		if ((width <= 3 && height <= 3) || width < minimumGridWidth)
			throw new IllegalArgumentException("False grid dimensions!");
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
		if ((width <= 3 && height <= 3) || height < minimumGridHeight)
			throw new IllegalArgumentException("False grid dimensions!");
		this.height = height;
		return this;
	}
	
	private HashMap<Coordinate, AbstractSquare>	grid;
	private ArrayList<Wall>					walls;
	private Map<Teleporter, Coordinate>		teleporterCoords;
	
	/**
	 * Build a new grid object. The grid will be build with the parameters set
	 * in this builder. If these parameters were not set, the default values
	 * will be used.
	 * 
	 * @return a new grid object
	 */
	public Grid build() {
		walls = new ArrayList<Wall>();
		grid = new HashMap<Coordinate, AbstractSquare>();
		teleporterCoords = new HashMap<Teleporter, Coordinate>();
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
	
	/**
	 * Will return the ASquare at the specified coordinate if there is any,
	 * otherwise it will create a new Square.
	 */
	private AbstractSquare getSquare(Coordinate coordinate) {
		if (grid.containsKey(coordinate))
			return grid.get(coordinate);
		
		// initialize the neighbours
		Map<Direction, AbstractSquare> neighbours = new HashMap<Direction, AbstractSquare>();
		
		// find the neighbours of the new square
		for (Direction direction : Direction.values())
			if (grid.containsKey(coordinate.getCoordinateInDirection(direction)))
				neighbours.put(direction, grid.get(coordinate.getCoordinateInDirection(direction)));
		
		// return the new square with its neighbours
		return new NormalSquare(neighbours);
	}
	
	private WallPart getWallPart(Coordinate coordinate) {
		// initialize the neighbours
		Map<Direction, AbstractSquare> neighbours = new HashMap<Direction, AbstractSquare>();
		
		// find the neighbours of the new square
		for (Direction direction : Direction.values())
			if (grid.containsKey(coordinate.getCoordinateInDirection(direction)))
				neighbours.put(direction, grid.get(coordinate.getCoordinateInDirection(direction)));
		
		// return the new square with its neighbours
		return new WallPart(neighbours);
	}
	
	/* -------------------- grid building methods ------------------------ */
	
	/**
	 * returns the number of walls in the grid
	 * 
	 * @return number of walls
	 */
	private int getNumberOfWallParts() {
		int i = 0;
		for (Square square : grid.values())
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
	 * Place a random number of items on the board. The number of items will be
	 * a rounded percentage ({@value #NUMBER_OF_GRENADES}) of the total size of
	 * the board. The items will be placed on the board with the following
	 * {@link #canPlaceItem(Coordinate) constraints}.
	 */
	private void placeItemsOnBoard(List<Coordinate> startingCoordinates) {
		placeLightGrenades(startingCoordinates);
		placeTeleporters();
		placeIdentityDisks(startingCoordinates);
	}
	
	private void placeLightGrenades(List<Coordinate> startingCoordinates) {
		// A light grenade should be within a 3x3 square of each starting
		// position. In general, this is a 5x5 square with the starting
		// positions in the middle.
		Random rand = new Random();
		for (Coordinate startCoord : startingCoordinates) {
			Coordinate position = null;
			do {
				int x = startCoord.getX() - 2 + rand.nextInt(5);
				int y = startCoord.getY() - 2 + rand.nextInt(5);
				position = new Coordinate(x, y);
			} while (!canPlaceItem(position));
			getSquare(position).addItem(new LightGrenade());
		}
		
		// place other light grenades
		int numberOfItems = players.size();
		while (((double) numberOfItems) / grid.size() < NUMBER_OF_GRENADES) {
			Coordinate position = Coordinate.random(width, height);
			if (canPlaceItem(position)) {
				getSquare(position).addItem(new LightGrenade());
				numberOfItems++;
			}
		}
	}
	
	/**
	 * Places the teleporters on the grid. This method returns the coordinates,
	 * because they are needed to calculate the shortest path.
	 */
	private void placeTeleporters() {
		int numberOfItems = 0;
		List<Teleporter> teleporters = new ArrayList<Teleporter>();
		while (((double) numberOfItems) / grid.size() < NUMBER_OF_TELEPORTERS) {
			Coordinate position = Coordinate.random(width, height);
			if (canPlaceItem(position)) {
				Teleporter teleporter = new Teleporter(getTeleporterDestination(teleporters),
						getSquare(position));
				teleporterCoords.put(teleporter, position);
				getSquare(position).addItem(teleporter);
				teleporters.add(teleporter);
				numberOfItems++;
			}
		}
		Teleporter teleporter = teleporters.remove(0);
		teleporter.setDestination(getTeleporterDestination(teleporters));
	}
	
	/**
	 * Places both normal identity disks and one charged one on the grid. Each
	 * player will have at least one identity disk near him.
	 * 
	 * @param startingCoordinates
	 *        The starting coordinates of the player
	 */
	private void placeIdentityDisks(List<Coordinate> startingCoordinates) {
		// place normal identity disks
		int numberOfItems = 0;
		while (((double) numberOfItems) / grid.size() < NUMBER_OF_IDENTITY_DISKS) {
			Coordinate position = Coordinate.random(width, height);
			if (canPlaceItem(position)) {
				((NormalSquare) getSquare(position)).addItem(new UnchargedIdentityDisk());
				numberOfItems++;
			}
		}
		
		// place charged identity disk
		Random rand = new Random();
		List<Coordinate> CIDCoords = getPossibleCIDLocations(startingCoordinates);
		Coordinate CIDCoord = null;
		
		while (!CIDCoords.isEmpty()) {
			CIDCoord = CIDCoords.get(rand.nextInt(CIDCoords.size()));
			if (canPlaceItem(CIDCoord))
				break;
			
			// We couldn't place it
			CIDCoords.remove(CIDCoord);
			CIDCoord = null;
		}
		
		if (CIDCoord != null)
			getSquare(CIDCoord).addItem(new ChargedIdentityDisk());
	}
	
	private Teleporter getTeleporterDestination(List<Teleporter> teleporters) {
		if (teleporters.isEmpty())
			return null;
		else
			return teleporters.get(new Random().nextInt(teleporters.size()));
	}
	
	/**
	 * Returns a list of all possible locations for the charged identity disk.
	 * To decide that, it compares the shortest path needed for both players to
	 * reach the squares.
	 * 
	 * @return A list of possible locations for the charged identity disk
	 */
	private List<Coordinate> getPossibleCIDLocations(List<Coordinate> startingCoordinates) {
		List<Map<Coordinate, Integer>> distances = new ArrayList<Map<Coordinate, Integer>>();
		List<Coordinate> CIDLocations = new ArrayList<Coordinate>();
		
		for (Coordinate coord : startingCoordinates)
			distances.add(getDistanceOfSquaresStartingAt(coord));
		
		for (Coordinate coord : distances.get(0).keySet()) {
			if (!getSquare(coord).canBeAdded(new ChargedIdentityDisk()))
				continue;
			
			int min = distances.get(0).get(coord);
			int max = min;
			
			for (int i = 1; i < distances.size(); ++i) {
				if (!distances.get(i).containsKey(coord)) {
					max = Integer.MAX_VALUE;
					break;
				}
				if (distances.get(i).get(coord) < min)
					min = distances.get(i).get(coord);
				if (distances.get(i).get(coord) > max)
					max = distances.get(i).get(coord);
			}
			
			if ((max - min) <= 2)
				CIDLocations.add(coord);
		}
		
		return CIDLocations;
	}
	
	/**
	 * Computes the travel distance to all squares, starting from a specified
	 * coordinate.
	 * 
	 * @param coordinate
	 *        The starting coordinate
	 * @return A map of all coordinates on their travel distances
	 */
	private Map<Coordinate, Integer> getDistanceOfSquaresStartingAt(Coordinate coordinate) {
		Map<Coordinate, Integer> distances = new HashMap<Coordinate, Integer>();
		PriorityQueue<State> pq = new PriorityQueue<State>();
		
		pq.add(new State(coordinate, 0));
		
		while (!pq.isEmpty()) {
			State current = pq.poll();
			List<Coordinate> neighbours;
			Teleporter teleporter = getTeleporterOnLocation(current.getCoordinate());
			
			if (teleporter == null)
				neighbours = getNeighboursOf(current.getCoordinate());
			else {
				Coordinate goal = teleporterCoords.get(teleporter.getDestination());
				distances.put(goal, current.getDistance());
				neighbours = getNeighboursOf(goal);
			}
			
			for (Coordinate neighbour : neighbours) {
				if (!distances.containsKey(neighbour)
						|| current.getDistance() + 1 < distances.get(neighbour)) {
					distances.put(neighbour, current.getDistance() + 1);
					State neighbourState = new State(neighbour, current.getDistance() + 1);
					pq.add(neighbourState);
				}
			}
			
		}
		
		return distances;
	}
	
	/**
	 * Returns all neighbours of a specified coordinate, where a Player can move
	 * to.
	 * 
	 * @param coordinate
	 *        The coordinate to get the neighbours from
	 * @return A list of neighbouring coordinates
	 */
	private List<Coordinate> getNeighboursOf(Coordinate coordinate) {
		List<Coordinate> neighbours = coordinate.getAllNeighbours();
		
		for (int i = neighbours.size() - 1; i >= 0; i--) {
			// Grid doesn't contain this neighbour or a player can't be added to
			// this square
			if (!(grid.containsKey(neighbours.get(i))))
				neighbours.remove(i);
			else if (!grid.get(neighbours.get(i)).canAddPlayer())
				neighbours.remove(i);
		}
		
		return neighbours;
	}
	
	private Teleporter getTeleporterOnLocation(Coordinate coordinate) {
		List<IItem> items = getSquare(coordinate).getAllItems();
		for (IItem item : items) {
			if (item instanceof Teleporter)
				return (Teleporter) item;
		}
		return null;
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
		if (coordinate == null)
			return false;
		// an item must be placed on the board
		else if (!grid.containsKey(coordinate))
			return false;
		// an item cannot be place on the starting positions
		else if (getSquare(coordinate).hasPlayer())
			return false;
		// an item cannot be place on a wall
		else if (getSquare(coordinate).getClass() == WallPart.class)
			return false;
		// an item cannot be placed on another item
		else if (!getSquare(coordinate).getAllItems().isEmpty())
			return false;
		else
			return true;
	}
	
	/**
	 * This method calculates the starting coordinates of the players.
	 * 
	 * @return A list containing the starting coordinates.
	 */
	private List<Coordinate> calculateStartingPositionsOfPlayers() {
		List<Coordinate> startingCoordinates = new ArrayList<Coordinate>();
		
		// The starting positions are hardcoded at this moment, we can change
		// this into an algorithm if at some point multiple players need to be
		// supported.
		
		startingCoordinates.add(new Coordinate(width - 1, 0));
		startingCoordinates.add(new Coordinate(0, height - 1));
		
		return startingCoordinates;
	}
	
	/* -------------------- GRID FOR TESTING --------------------- */
	
	/**
	 * This function should ONLY be used by getPredefinedGrid(), to get a
	 * deterministic grid we can use to test. This should not be used in
	 * gameplay.
	 */
	private Grid build(int width, int height, List<Wall> walls, boolean usePowerfailure) {
		this.walls = new ArrayList<Wall>();
		grid = new HashMap<Coordinate, AbstractSquare>();
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
		
		((NormalSquare) getSquare(new Coordinate(2, 7))).addItem(new LightGrenade());
		((NormalSquare) getSquare(new Coordinate(5, 8))).addItem(new LightGrenade());
		((NormalSquare) getSquare(new Coordinate(6, 8))).addItem(new LightGrenade());
		((NormalSquare) getSquare(new Coordinate(7, 8))).addItem(new LightGrenade());
		((NormalSquare) getSquare(new Coordinate(7, 6))).addItem(new LightGrenade());
		((NormalSquare) getSquare(new Coordinate(8, 8))).addItem(new LightGrenade());
		((NormalSquare) getSquare(new Coordinate(8, 7))).addItem(new LightGrenade());
		((NormalSquare) getSquare(new Coordinate(7, 2))).addItem(new LightGrenade());
		
		((NormalSquare) getSquare(new Coordinate(7, 0))).addItem(new UnchargedIdentityDisk());
		((NormalSquare) getSquare(new Coordinate(2, 9))).addItem(new UnchargedIdentityDisk());
		
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
		
		return build(10, 10, walls, usePowerfailure);
	}
	
	/**
	 * The size of the grid returned by
	 * {@link GridBuilder#getPredefinedTestGrid(boolean)} Used for testing
	 * purposes, should not be used in game play.
	 */
	public static final int	PREDIFINED_GRID_SIZE	= 10;
	
}
