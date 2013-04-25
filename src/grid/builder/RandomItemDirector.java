package grid.builder;

import grid.Coordinate;
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
public abstract class RandomItemDirector extends Director {
	
	private static final double	NUMBER_OF_GRENADES			= 0.02;
	private static final double	NUMBER_OF_TELEPORTERS		= 0.03;
	private static final double	NUMBER_OF_IDENTITY_DISKS	= 0.02;
	
	public RandomItemDirector(GridBuilder builder) {
		super(builder);
	}
	
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
		int numberOfItems = 0;
		
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
			} while (!builder.canPlaceItem(position));
			builder.placeLightGrenade(position);
			numberOfItems++;
		}
		
		// place other light grenades
		while (((double) numberOfItems) / builder.size() < NUMBER_OF_GRENADES) {
			Coordinate position = Coordinate.random(width, height);
			if (builder.canPlaceItem(position)) {
				builder.placeLightGrenade(position);
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
		List<Coordinate> teleporters = new ArrayList<Coordinate>();
		while (((double) numberOfItems) / builder.size() < NUMBER_OF_TELEPORTERS) {
			Coordinate position = Coordinate.random(builder.size(), builder.size());
			if (builder.canPlaceItem(position)) {
				builder.placeTeleporter(position);
				teleporters.add(position);
				numberOfItems++;
			}
		}
		
		setDestinationOfTeleporters(teleporters);
	}
	
	/** Set a destination for each of the teleporters */
	private void setDestinationOfTeleporters(List<Coordinate> teleporters) {
		for (Coordinate teleporter : teleporters)
			builder.connectTeleporters(teleporter,
					teleporters.get(new Random().nextInt(teleporters.size())));	
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
		while (((double) numberOfItems) / builder.size() < NUMBER_OF_IDENTITY_DISKS) {
			Coordinate position = Coordinate.random(width, height);
			if (builder.canPlaceItem(position)) {
				builder.placeUnchargedIdentityDisc(position);
				numberOfItems++;
			}
		}
		
		// place charged identity disk
		Random rand = new Random();
		List<Coordinate> CIDCoords = getPossibleCIDLocations(startingCoordinates);
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
			builder.placeUnchargedIdentityDisc(CIDCoord);
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
	
}
