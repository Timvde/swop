package grid.builder;

import grid.Coordinate;
import grid.Grid;
import item.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

/**
 * A RandomItemGridBuilderDirector offers a method to its subclasses to
 * <i>randomly</i> (i.e. as specified by the Tron game constraints) place the
 * items on the grid being constructed.
 * 
 * TODO Constraints for the randomness of items
 */
public abstract class RandomItemGridBuilderDirector extends GridBuilderDirector {
	
	// The numbers are percentages of the number of squares on the grid
	// (package access for testing puposes)
	static final double	PERCENTAGE_OF_GRENADES				= 0.02;
	static final double	PERCENTAGE_OF_TELEPORTERS			= 0.03;
	static final double	PERCENTAGE_OF_IDENTITY_DISKS		= 0.02;
	static final double	PERCENTAGE_OF_GENERATORS			= 0.02;
	static final int	NUMBER_OF_CHARGED_IDENTITY_DISKS	= 1;
	static final int	MAX_CID_SHORTEST_PATH_DISTANCE		= 2;
	
	/**
	 * Create a new RandomItemGridBuilderDirector which will use the specified
	 * builder to build the {@link Grid}
	 * 
	 * @param builder
	 *        The builder this director will use to build the grid.
	 */
	public RandomItemGridBuilderDirector(GridBuilder builder) {
		super(builder);
	}
	
	/**
	 * Place all the {@link Item items} on the grid constructed. The placement
	 * and number of the different item-types are specified by the Tron game
	 * constraints. The placement of items on the board is also limited by the
	 * so called {@link GridBuilder#canPlaceItem(Coordinate) <i>hard
	 * constraints</i>} (i.e. the invariants of the grid).
	 * 
	 * This method will also put the specified player starting positions on the
	 * grid.
	 * 
	 * <br>
	 * <b>This method should only be called when the adding of squares and walls
	 * to the grid under construction is finished.</b>
	 * 
	 * @param startingCoordinates
	 *        a set with the starting numbers and corresponding coordinates of
	 *        the players. This is needed as some Tron-constraints depend on the
	 *        players starting positions.
	 * @param maxX
	 *        The maximal w coordinate of a square inside the constructed grid
	 *        (i.e. the maximal width).
	 * @param maxY
	 *        The maximal y coordinate of a square inside the constructed grid
	 *        (i.e. the maximal height).
	 */
	protected void placeItemsOnBoard(Map<Integer, Coordinate> startingCoordinates, int maxX,
			int maxY) {
		for (Integer number : startingCoordinates.keySet())
			builder.addPlayerStartingPosition(startingCoordinates.get(number), number);
		
		List<Coordinate> startCoords = new ArrayList<Coordinate>(startingCoordinates.values());
		
		placeLightGrenades(startCoords, maxX, maxY);
		Map<Coordinate, Coordinate> teleporters = placeTeleporters(startCoords, maxX, maxY);
		placeIdentityDisks(startCoords, teleporters, maxX, maxY);
		placeForceFieldGenerators(startCoords, maxX, maxY);
	}
	
	/**
	 * Returns a random distribution of coordinates where items can be placed on
	 * the grid. The number of coordinates is a percentage relative to the total
	 * number of squares on the grid.
	 * 
	 * @param startingCoordinates
	 *        The starting coordinates of the player
	 * @param maxX
	 *        Upper x bound of the grid
	 * @param maxY
	 *        Upper y bound of the grid
	 * @param percentage
	 *        The percentage of the grid that has to be filled
	 * @param itemLocations
	 *        The coordinates that already contain an item of this kind, due to
	 *        some precondition. This list will be filled with new coordinates.
	 */
	private void addItemCoordinates(List<Coordinate> startingCoordinates, int maxX, int maxY,
			double percentage, Set<Coordinate> itemLocations) {
		int numberOfItemsToPlace = (int) Math.ceil(builder.getNumberOfSquares() * percentage);
		while (itemLocations.size() < numberOfItemsToPlace) {
			Coordinate position = Coordinate.random(maxX + 1, maxY + 1);
			if (builder.canPlaceItem(position) && !itemLocations.contains(position)
					&& !startingCoordinates.contains(position)) {
				itemLocations.add(position);
			}
		}
	}
	
	/**
	 * Places all the light grenades on the grid. Their number is limited by a
	 * rounded percentage ({@value #PERCENTAGE_OF_GRENADES}) of the number of
	 * squares on the grid. There will be a light grenade within a 3x3 square of
	 * each starting position.
	 */
	private void placeLightGrenades(List<Coordinate> startingCoordinates, int maxX, int maxY) {
		Set<Coordinate> placedLGCoordinates = new HashSet<Coordinate>();
		
		/*
		 * A light grenade should be within a 3x3 square of each starting
		 * position. In general, this is a 5x5 square with the starting
		 * positions in the middle.
		 */
		Random rand = new Random();
		for (Coordinate startCoord : startingCoordinates) {
			Coordinate position = null;
			do {
				int x = startCoord.getX() - 2 + rand.nextInt(5);
				int y = startCoord.getY() - 2 + rand.nextInt(5);
				position = new Coordinate(x, y);
			} while (!builder.canPlaceItem(position) || placedLGCoordinates.contains(position));
			placedLGCoordinates.add(position);
		}
		
		// place all light grenades
		addItemCoordinates(startingCoordinates, maxX, maxY, PERCENTAGE_OF_GRENADES,
				placedLGCoordinates);
		for (Coordinate coord : placedLGCoordinates)
			builder.placeLightGrenade(coord);
	}
	
	/**
	 * Places all the force field generators on the grid. Their number is
	 * limited by a rounded percentage ({@value #PERCENTAGE_OF_GENERATORS}) of
	 * the number of squares on the grid.
	 * 
	 * @param startingCoordinates
	 *        the starting coordinates of the players
	 * @param maxX
	 *        max x coordinate
	 * @param maxY
	 *        max y coordinate
	 */
	private void placeForceFieldGenerators(List<Coordinate> startingCoordinates, int maxX, int maxY) {
		Set<Coordinate> placedGeneratorCoordinates = new HashSet<Coordinate>();
		
		// place other force field generators
		addItemCoordinates(startingCoordinates, maxX, maxY, PERCENTAGE_OF_GENERATORS,
				placedGeneratorCoordinates);
		for (Coordinate coord : placedGeneratorCoordinates)
			builder.placeForceFieldGenerator(coord);
	}
	
	/**
	 * Places the teleporters on the grid. Their number is limited by a rounded
	 * percentage ({@value #PERCENTAGE_OF_TELEPORTERS}) of the number of squares
	 * on the grid.
	 * 
	 * @return a map of teleporter locations and their destinations (because
	 *         they are needed to calculate the shortest path).
	 */
	private Map<Coordinate, Coordinate> placeTeleporters(List<Coordinate> startingCoordinates,
			int maxX, int maxY) {
		Set<Coordinate> teleporterLocations = new HashSet<Coordinate>();
		addItemCoordinates(startingCoordinates, maxX, maxY, PERCENTAGE_OF_TELEPORTERS,
				teleporterLocations);
		return addTeleportersToGrid(new ArrayList<Coordinate>(teleporterLocations));
	}
	
	/**
	 * This method will add teleporters to the grid on all the coordinates in a
	 * specified list. It will randomly interconnect the specified teleporters.
	 * 
	 * @return a map of teleporter locations and their destinations
	 */
	private Map<Coordinate, Coordinate> addTeleportersToGrid(List<Coordinate> teleporters) {
		Map<Coordinate, Coordinate> result = new HashMap<Coordinate, Coordinate>();
		for (Coordinate teleporterLocation : teleporters) {
			Coordinate teleporterDestination;
			do {
				teleporterDestination = teleporters.get(new Random().nextInt(teleporters.size()));
			} while (teleporterDestination.equals(teleporterLocation));
			
			builder.placeTeleporter(teleporterLocation, teleporterDestination);
			result.put(teleporterLocation, teleporterDestination);
		}
		return result;
	}
	
	/**
	 * Places the identity disks on the grid. The number of normal idenity disks
	 * is limited by a rounded percentage (
	 * {@value #PERCENTAGE_OF_IDENTITY_DISKS}) of the number of squares on the
	 * grid. Each player will have at least one identity disk near him. There is
	 * {@value #NUMBER_OF_CHARGED_IDENTITY_DISKS} on the grid.
	 */
	private void placeIdentityDisks(List<Coordinate> startingCoordinates,
			Map<Coordinate, Coordinate> teleporters, int maxX, int maxY) {
		// place normal identity disks
		Set<Coordinate> placedIdentityDisksCoordinates = new HashSet<Coordinate>();
		
		/*
		 * A IDdisk should be within a 5x5 square of each starting position. In
		 * general, this is a 9x9 square with the starting positions in the
		 * middle.
		 */
		Random rand = new Random();
		for (Coordinate startCoord : startingCoordinates) {
			Coordinate position = null;
			do {
				int x = startCoord.getX() - 4 + rand.nextInt(9);
				int y = startCoord.getY() - 4 + rand.nextInt(9);
				position = new Coordinate(x, y);
			} while (!builder.canPlaceItem(position)
					|| placedIdentityDisksCoordinates.contains(position)
					|| startingCoordinates.contains(position));
			placedIdentityDisksCoordinates.add(position);
		}
		
		// place other identity disks
		addItemCoordinates(startingCoordinates, maxX, maxY, PERCENTAGE_OF_IDENTITY_DISKS,
				placedIdentityDisksCoordinates);
		for (Coordinate coord : placedIdentityDisksCoordinates)
			builder.placeUnchargedIdentityDisc(coord);
		
		placedCharchedIDdisks(startingCoordinates, teleporters);
	}
	
	private void placedCharchedIDdisks(List<Coordinate> startingCoordinates,
			Map<Coordinate, Coordinate> teleporters) {
		for (int i = 0; i < NUMBER_OF_CHARGED_IDENTITY_DISKS; i++) {
			Random rand = new Random();
			List<Coordinate> CIDCoords = getPossibleCIDLocations(startingCoordinates, teleporters);
			Coordinate CIDCoord = null;
			
			while (!CIDCoords.isEmpty()) {
				CIDCoord = CIDCoords.get(rand.nextInt(CIDCoords.size()));
				if (builder.canPlaceItem(CIDCoord))
					break;
				
				// We couldn't place it
				CIDCoords.remove(CIDCoord);
				CIDCoord = null;
			}
			
			if (CIDCoord != null)
				builder.placeChargedIdentityDisc(CIDCoord);
		}
	}
	
	/**
	 * Returns a list of all possible locations for the charged identity disk.
	 * To decide that, it compares the shortest path needed for both players to
	 * reach the squares.
	 * 
	 * @return A list of possible locations for the charged identity disk
	 */
	private List<Coordinate> getPossibleCIDLocations(List<Coordinate> startingCoordinates,
			Map<Coordinate, Coordinate> teleporters) {
		List<Map<Coordinate, Integer>> distances = new ArrayList<Map<Coordinate, Integer>>();
		List<Coordinate> CIDLocations = new ArrayList<Coordinate>();
		
		for (Coordinate coord : startingCoordinates)
			distances.add(getDistanceOfSquaresStartingAt(coord, teleporters));
		
		for (Coordinate coord : distances.get(0).keySet()) {
			if (!builder.canPlaceItem(coord))
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
			
			if ((max - min) <= MAX_CID_SHORTEST_PATH_DISTANCE)
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
	private Map<Coordinate, Integer> getDistanceOfSquaresStartingAt(Coordinate coordinate,
			Map<Coordinate, Coordinate> teleporters) {
		Map<Coordinate, Integer> distances = new HashMap<Coordinate, Integer>();
		PriorityQueue<EuclideanVector> pq = new PriorityQueue<EuclideanVector>();
		
		pq.add(new EuclideanVector(coordinate, 0));
		
		while (!pq.isEmpty()) {
			EuclideanVector current = pq.poll();
			List<Coordinate> neighbours;
			
			if (teleporters.containsKey(current.getCoordinate())) {
				Coordinate destination = teleporters.get(current.getCoordinate());
				distances.put(destination, current.getDistance());
				neighbours = builder.getAllReachableNeighboursOf(destination);
			}
			else {
				neighbours = builder.getAllReachableNeighboursOf(current.getCoordinate());
			}
			
			for (Coordinate neighbour : neighbours) {
				if (!distances.containsKey(neighbour)
						|| current.getDistance() + 1 < distances.get(neighbour)) {
					distances.put(neighbour, current.getDistance() + 1);
					EuclideanVector neighbourState = new EuclideanVector(neighbour,
							current.getDistance() + 1);
					pq.add(neighbourState);
				}
			}
		}
		
		return distances;
	}
}
