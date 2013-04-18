package grid;

import item.IItem;
import item.identitydisk.ChargedIdentityDisk;
import item.identitydisk.UnchargedIdentityDisk;
import item.lightgrenade.LightGrenade;
import item.teleporter.Teleporter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import player.Player;
import square.ASquare;
import square.Direction;
import square.Square;
import square.Wall;
import square.WallPart;

/**
 * A builder used for building the grid. This builder contains all the
 * functionality for configuring and building the grid.
 * 
 * @author Tom
 */
public abstract class AGridBuilder {
	
	private static final double	NUMBER_OF_GRENADES			= 0.02;
	private static final double	NUMBER_OF_TELEPORTERS		= 0.03;
	private static final double	NUMBER_OF_IDENTITY_DISKS	= 0.02;
	
	protected int					width;
	protected int					height;
	protected List<Player>		players;
	
	// Constraints
	private int					minimumGridWidth			= 10;
	private int					minimumGridHeight			= 10;
	
	protected HashMap<Coordinate, ASquare>	grid; 
	protected ArrayList<Wall>					walls;
	protected Map<Teleporter, Coordinate>		teleporterCoords;
	
	/**
	 * Create a new builder for the grid.
	 * 
	 * @param players
	 *        The players to be placed on this grid.
	 * @throws IllegalArgumentException
	 *         When the number of players is not two.
	 */
	public AGridBuilder(List<Player> players) throws IllegalArgumentException {
		if (players.size() != 2)
			throw new IllegalArgumentException(
					"At this moment, only a fixed number of two players is supported.");
		this.players = players;
		this.width = 10;
		this.height = 10;
	}
	
	/**
	 * Build the new grid.
	 * @return The newly constructed grid.
	 */
	public abstract Grid build();
	
	/**
	 * set the width of the grid. This must be a strictly positive integer, and
	 * at least 10.
	 * 
	 * @param width
	 *        the width of the grid
	 * @return this
	 */
	// @Requires("width > 0")
	public AGridBuilder setGridWidth(int width) {
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
	public AGridBuilder setGridHeigth(int height) {
		if ((width <= 3 && height <= 3) || height < minimumGridHeight)
			throw new IllegalArgumentException("False grid dimensions!");
		this.height = height;
		return this;
	}
	
	/**
	 * Will return the ASquare at the specified coordinate if there is any,
	 * otherwise it will create a new Square.
	 */
	protected ASquare getSquare(Coordinate coordinate) {
		if (grid.containsKey(coordinate))
			return grid.get(coordinate);
		
		// initialize the neighbours
		Map<Direction, ASquare> neighbours = new HashMap<Direction, ASquare>();
		
		// find the neighbours of the new square
		for (Direction direction : Direction.values())
			if (grid.containsKey(coordinate.getCoordinateInDirection(direction)))
				neighbours.put(direction, grid.get(coordinate.getCoordinateInDirection(direction)));
		
		// return the new square with its neighbours
		return new Square(neighbours);
	}
	
	protected WallPart getWallPart(Coordinate coordinate) {
		// initialize the neighbours
		Map<Direction, ASquare> neighbours = new HashMap<Direction, ASquare>();
		
		// find the neighbours of the new square
		for (Direction direction : Direction.values())
			if (grid.containsKey(coordinate.getCoordinateInDirection(direction)))
				neighbours.put(direction, grid.get(coordinate.getCoordinateInDirection(direction)));
		
		// return the new square with its neighbours
		return new WallPart(neighbours);
	}
	
	/* -------------------- grid building methods ------------------------ */
	
	
	
	/**  
	 * Place a random number of items on the board. The number of items will be
	 * a rounded percentage ({@value #NUMBER_OF_GRENADES}) of the total size of
	 * the board. The items will be placed on the board with the following
	 * {@link #canPlaceItem(Coordinate) constraints}.
	 */
	protected void placeItemsOnBoard(List<Coordinate> startingCoordinates) {
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
				((Square) getSquare(position)).addItem(new UnchargedIdentityDisk());
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
	protected List<Coordinate> calculateStartingPositionsOfPlayers() {
		List<Coordinate> startingCoordinates = new ArrayList<Coordinate>();
		
		// The starting positions are hardcoded at this moment, we can change
		// this into an algorithm if at some point multiple players need to be
		// supported.
		
		startingCoordinates.add(new Coordinate(width - 1, 0));
		startingCoordinates.add(new Coordinate(0, height - 1));
		
		return startingCoordinates;
	}
	
}
