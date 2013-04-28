package grid.builder;

import grid.Coordinate;
import grid.Grid;
import item.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * A RandomItemGridBuilderDirector offers a method to its subclasses to
 * <i>randomly</i> (i.e. as specified by the Tron game constraints) place the
 * items on the grid being constructed.
 */
public abstract class RandomItemGridBuilderDirector extends GridBuilderDirector {
	
	// The numbers are percentages of the number of squares on the grid
	private static final double	PERCENTAGE_OF_GRENADES				= 0.02;
	private static final double	PERCENTAGE_OF_TELEPORTERS			= 0.03;
	private static final double	PERCENTAGE_OF_IDENTITY_DISKS		= 0.02;
	private static final int	NUMBER_OF_CHARGED_IDENTITY_DISKS	= 1;
	
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
	 * and number of the different item-types depends on the Tron game
	 * constraints. The placement of items on the board is also limited by the
	 * so called {@link GridBuilder#canPlaceItem(Coordinate) <i>hard
	 * constraints</i>} (i.e. the invariants of the grid).
	 * 
	 * This method will also put the specified player starting positions on the
	 * grid.
	 * 
	 * <br>
	 * <b>This method should only be called when the adding of squares to the
	 * grid under construction is finished.</b>
	 * 
	 * @param startingCoordinates
	 *        a list with the starting coordinates of the players. This is
	 *        needed as some Tron-constraints depend on the players starting
	 *        positions.
	 * @param maxX
	 *        The maximal w coordinate of a square inside the constructed grid
	 *        (i.e. the maximal width).
	 * @param maxY
	 *        The maximal y coordinate of a square inside the constructed grid
	 *        (i.e. the maximal height).
	 */
	protected void placeItemsOnBoard(List<Coordinate> startingCoordinates, int maxX, int maxY) {
		for (Coordinate coordinate : startingCoordinates) {
			builder.addPlayerStartingPosition(coordinate);
		}
		placeLightGrenades(startingCoordinates, maxX, maxY);
		Map<Coordinate, Coordinate> teleporters = placeTeleporters(maxX, maxY);
		placeIdentityDisks(startingCoordinates, teleporters, maxX, maxY);
	}
	
	/**
	 * Places all the light grenades on the grid. Their number is limited by a
	 * rounded percentage ({@value #PERCENTAGE_OF_GRENADES}) of the number of
	 * squares on the grid. There will be a light grenade within a 3x3 square of
	 * each starting position.
	 */
	private void placeLightGrenades(List<Coordinate> startingCoordinates, int maxX, int maxY) {
		int numberOfPlacedLightGrenades = 0;
		
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
			} while (!builder.canPlaceItem(position));
			builder.placeLightGrenade(position);
			numberOfPlacedLightGrenades++;
		}
		
		// place other light grenades
		while (((double) numberOfPlacedLightGrenades) / builder.getNumberOfSquares() < PERCENTAGE_OF_GRENADES) {
			Coordinate position = Coordinate.random(maxX + 1, maxY + 1);
			if (builder.canPlaceItem(position)) {
				builder.placeLightGrenade(position);
				numberOfPlacedLightGrenades++;
			}
		}
	}
	
	/**
	 * Places the teleporters on the grid. Their number is limited by a rounded
	 * percentage ({@value #PERCENTAGE_OF_TELEPORTERS}) of the number of squares
	 * on the grid.
	 * 
	 * @return a map of teleporter locations and their destionations (because
	 *         they are needed to calculate the shortest path).
	 */
	private Map<Coordinate, Coordinate> placeTeleporters(int maxX, int maxY) {
		int numberOfPlacedTeleporters = 0;
		List<Coordinate> teleporters = new ArrayList<Coordinate>();
		while (((double) numberOfPlacedTeleporters) / builder.getNumberOfSquares() < PERCENTAGE_OF_TELEPORTERS) {
			Coordinate position = Coordinate.random(maxX + 1, maxY + 1);
			if (builder.canPlaceItem(position)) {
				builder.placeTeleporter(position);
				teleporters.add(position);
				numberOfPlacedTeleporters++;
			}
		}
		return setDestinationOfTeleporters(teleporters);
	}
	
	/**
	 * Randomly interconnect the teleporters in a specified list.
	 */
	private Map<Coordinate, Coordinate> setDestinationOfTeleporters(List<Coordinate> teleporters) {
		Map<Coordinate, Coordinate> result = new HashMap<Coordinate, Coordinate>();
		for (Coordinate teleporterLocation : teleporters) {
			Coordinate teleporterDestination = teleporters.get(new Random().nextInt(teleporters
					.size()));
			builder.connectTeleporters(teleporterLocation, teleporterDestination);
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
		int numberOfPlacedIDdisks = 0;
		while (((double) numberOfPlacedIDdisks) / builder.getNumberOfSquares() < PERCENTAGE_OF_IDENTITY_DISKS) {
			Coordinate position = Coordinate.random(maxX + 1, maxY + 1);
			if (builder.canPlaceItem(position)) {
				builder.placeUnchargedIdentityDisc(position);
				numberOfPlacedIDdisks++;
			}
		}
		
		// place charged identity disk
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
