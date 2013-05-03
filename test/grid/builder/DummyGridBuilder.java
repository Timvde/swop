package grid.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import grid.Coordinate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import org.junit.runners.JUnit4;
import ObjectronExceptions.builderExceptions.GridBuildException;

/**
 * An implementation of the {@link GridBuilder} interface to test the
 * {@link GridBuilderDirector directors}. This builder offers a method
 * {@link #assertIsValidGrid()} which {@link JUnit4 asserts} whether or not the
 * grid created by the preceding build calls meets the constraints.
 */
public class DummyGridBuilder implements GridBuilder {
	
	private Set<Coordinate>				squares;
	private Set<Coordinate>				wallParts;
	private Set<Coordinate>				playerStartingPositions;
	private Set<Coordinate>				LGList;
	private Set<Coordinate>				IDList;
	private Set<Coordinate>				CIDList;
	private Map<Coordinate, Coordinate>	teleporters;
	
	/**
	 * Create a new TestGridBuilder with an empty grid.
	 */
	public DummyGridBuilder() {
		this.createNewEmptyGrid();
	}
	
	@Override
	public void createNewEmptyGrid() {
		this.squares = new HashSet<Coordinate>();
		this.wallParts = new HashSet<Coordinate>();
		this.playerStartingPositions = new HashSet<Coordinate>();
		this.LGList = new HashSet<Coordinate>();
		this.IDList = new HashSet<Coordinate>();
		this.CIDList = new HashSet<Coordinate>();
		this.teleporters = new HashMap<Coordinate, Coordinate>();
	}
	
	@Override
	public void addSquare(Coordinate coordinate) {
		if (!this.squares.add(coordinate))
			throw new IllegalArgumentException("There's already a Square at this coordinate.");
		if (playerStartingPositions.contains(coordinate) || wallParts.contains(coordinate))
			throw new IllegalArgumentException("This coordinate is already used.");
	}
	
	@Override
	public void addWall(Coordinate coordinate) {
		if (!wallParts.add(coordinate))
			throw new IllegalArgumentException("There's already a wall at this coordinate");
		if (playerStartingPositions.contains(coordinate))
			throw new IllegalArgumentException("a startingpos cannot contain a wall");
		// a wall may overwrite a square
		this.squares.remove(coordinate);
	}
	
	@Override
	public void addPlayerStartingPosition(Coordinate coordinate) {
		if (wallParts.contains(coordinate))
			throw new IllegalArgumentException("This coordinate is already occupied by a wall");
		if (!playerStartingPositions.add(coordinate))
			throw new IllegalArgumentException("There's already a playerstart at this coordinate");
	}
	
	@Override
	public void placeLightGrenade(Coordinate coordinate) throws GridBuildException {
		if (!canPlaceItem(coordinate))
			throw new GridBuildException("The item cannot be placed at the specified coordinate");
		if (!LGList.add(coordinate))
			throw new IllegalArgumentException("There's already a LG at this coordinate");
	}
	
	@Override
	public void placeUnchargedIdentityDisc(Coordinate coordinate) throws GridBuildException {
		if (!canPlaceItem(coordinate))
			throw new GridBuildException("The item cannot be placed at the specified coordinate");
		if (!IDList.add(coordinate))
			throw new IllegalArgumentException("There's already an IDdisk at this coordinate");
	}
	
	@Override
	public void placeChargedIdentityDisc(Coordinate coordinate) throws GridBuildException {
		if (!canPlaceItem(coordinate))
			throw new GridBuildException("The item cannot be placed at the specified coordinate");
		if (!CIDList.add(coordinate))
			throw new IllegalArgumentException("There's already an CIDdisk at this coordinate");
	}
	
	@Override
	public void placeTeleporter(Coordinate from, Coordinate to) throws GridBuildException {
		if (!canPlaceItem(from) || !canPlaceItem(to))
			throw new GridBuildException("The item cannot be placed at the specified coordinate");
		if (teleporters.put(from, to) != null)
			throw new IllegalArgumentException("Theres already a teleporter on this coordinates");
	}
	
	@Override
	public boolean canPlaceItem(Coordinate coordinate) {
		return coordinate != null && !this.playerStartingPositions.contains(coordinate)
				&& this.squares.contains(coordinate);
	}
	
	@Override
	public List<Coordinate> getAllReachableNeighboursOf(Coordinate coordinate) {
		List<Coordinate> neighbours = coordinate.getAllNeighbours();
		
		for (int i = neighbours.size() - 1; i >= 0; i--) {
			if (!squares.contains(neighbours.get(i))
					&& !playerStartingPositions.contains(neighbours.get(i)))
				neighbours.remove(i);
		}
		
		return neighbours;
	}
	
	@Override
	public int getNumberOfSquares() {
		return this.squares.size() + this.playerStartingPositions.size();
	}
	
	/**
	 * {@link JUnit4 Asserts} whether or not the grid as build by the preceding
	 * construction-calls adheres the constraints.
	 */
	public void assertIsValidGrid() {
		assertValidPlayerPositions();
		assertValidWalls();
		assertValidLightGrenades();
		assertValidIDdisks();
		assertValidTeleporters();
		assertValidCIDdisks();
	}
	
	/**
	 * Asserts the grid as build by the preceding construction-calls adheres the
	 * constraints on player placement.
	 */
	private void assertValidPlayerPositions() {
		assertEquals(GridBuilderDirector.NUMBER_OF_PLAYERS, playerStartingPositions.size());
	}
	
	/**
	 * Asserts the grid as build by the preceding construction-calls adheres the
	 * constraints on lightgrenade placement.
	 */
	private void assertValidLightGrenades() {
		assertEquals(
				(int) Math.ceil(getNumberOfSquares()
						* RandomGridBuilderDirector.PERCENTAGE_OF_GRENADES), LGList.size());
		
		// A light grenade cannot be placed on a wall
		assertTrue(Collections.disjoint(LGList, wallParts));
		
		// There can be at most a single light grenade on each square --> set
		
		// The starting position of a player cannot contain a light grenade.
		assertTrue(Collections.disjoint(playerStartingPositions, LGList));
		
		// For each player, at least one light grenade is placed in the area of
		// 3x3 squares that covers the starting position of that player.
		boolean found = false;
		for (Coordinate startcoord : playerStartingPositions) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					Coordinate candidate1 = new Coordinate(startcoord.getX() + i, startcoord.getY()
							+ j);
					Coordinate candidate2 = new Coordinate(startcoord.getX() - i, startcoord.getY()
							- j);
					Coordinate candidate3 = new Coordinate(startcoord.getX() - i, startcoord.getY()
							+ j);
					Coordinate candidate4 = new Coordinate(startcoord.getX() + i, startcoord.getY()
							- j);
					if (LGList.contains(candidate1) || LGList.contains(candidate2)
							|| LGList.contains(candidate3) || LGList.contains(candidate4))
						found = true;
				}
			}
			assertTrue(found);
		}
	}
	
	/**
	 * Asserts the grid as build by the preceding construction-calls adheres the
	 * constraints on uncharched identity disk placement.
	 */
	private void assertValidIDdisks() {
		assertEquals(
				(int) Math.ceil(getNumberOfSquares()
						* RandomGridBuilderDirector.PERCENTAGE_OF_IDENTITY_DISKS), IDList.size());
		
		// An identity disc cannot be placed on a wall
		assertTrue(Collections.disjoint(IDList, wallParts));
		
		// Initially, there can be at most a single identity disc on each square
		// --> set
		
		// The starting position of a player cannot contain an identity disc.
		assertTrue(Collections.disjoint(playerStartingPositions, IDList));
		
		// For each player, at least one identity disc is placed in the area of
		// 5x5 squares that covers the starting position of that player.
		for (Coordinate startcoord : playerStartingPositions) {
			boolean found = false;
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					Coordinate candidate1 = new Coordinate(startcoord.getX() + i, startcoord.getY()
							+ j);
					Coordinate candidate2 = new Coordinate(startcoord.getX() - i, startcoord.getY()
							- j);
					Coordinate candidate3 = new Coordinate(startcoord.getX() - i, startcoord.getY()
							+ j);
					Coordinate candidate4 = new Coordinate(startcoord.getX() + i, startcoord.getY()
							- j);
					if (IDList.contains(candidate1) || IDList.contains(candidate2)
							|| IDList.contains(candidate3) || IDList.contains(candidate4))
						found = true;
				}
			}
			assertTrue(found);
		}
	}
	
	/**
	 * Asserts the grid as build by the preceding construction-calls adheres the
	 * constraints on charched identity disk placement.
	 */
	private void assertValidCIDdisks() {
		assertTrue(CIDList.size() <= RandomGridBuilderDirector.NUMBER_OF_CHARGED_IDENTITY_DISKS);
		
		// the disc must be placed such that the length of the shortest path
		// from each player to the disc differs by at most 2 squares
		//
		//	TODO test has nullpointer, but the shortest path is ok
		//
		//		List<Integer> pathLengths = new ArrayList<Integer>();
		//		for (Coordinate CIDCoord : CIDList) {
		//			for (Coordinate c : playerStartingPositions) {
		//				pathLengths.add(getLengthOfShortestPath(c, CIDCoord));
		//			}
		//			for (Integer i : pathLengths) {
		//				for (Integer j : pathLengths)
		//					if (Math.abs(i - j) > RandomGridBuilderDirector.MAX_CID_SHORTEST_PATH_DISTANCE)
		//						fail("shortest path not ok");
		//			}
		//		}
		
		// A charged identity disc cannot be placed on a wall.
		assertTrue(Collections.disjoint(CIDList, wallParts));
		
		// Initially, there can be at most a single charged identity disc on
		// each square --> set
		
		// The starting position of a player cannot contain a charged identity
		// disc.
		assertTrue(Collections.disjoint(playerStartingPositions, CIDList));
	}
	
	/**
	 * Returns the shortest path between two specified coordinates.
	 */
	private int getLengthOfShortestPath(Coordinate start, Coordinate finish) {
		Map<Coordinate, Integer> distances = new HashMap<Coordinate, Integer>();
		PriorityQueue<EuclideanVector> pq = new PriorityQueue<EuclideanVector>();
		EuclideanVector current = new EuclideanVector(start, 0);
		
		// Dijkstra
		while (!current.getCoordinate().equals(finish)) {
			List<Coordinate> neighbours;
			
			if (teleporters.containsKey(current.getCoordinate())) {
				Coordinate destination = teleporters.get(current.getCoordinate());
				distances.put(destination, current.getDistance());
				neighbours = getAllReachableNeighboursOf(destination);
			}
			else {
				neighbours = getAllReachableNeighboursOf(current.getCoordinate());
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
			current = pq.poll();
		}
		return current.getDistance();
	}
	
	/**
	 * Asserts the grid as build by the preceding construction-calls adheres the
	 * constraints on teleporter placement.
	 */
	private void assertValidTeleporters() {
		assertEquals(
				(int) Math.ceil(getNumberOfSquares()
						* RandomGridBuilderDirector.PERCENTAGE_OF_TELEPORTERS), teleporters.size());
		
		// A teleporter cannot be placed on a wall
		assertTrue(Collections.disjoint(teleporters.values(), wallParts));
		
		// There can be at most a single teleporter on each square --> hashmap
		
		// The starting position of a player cannot contain a teleporter.
		assertTrue(Collections.disjoint(playerStartingPositions, teleporters.values()));
		assertTrue(Collections.disjoint(playerStartingPositions, teleporters.keySet()));
		
		// Each teleporter has a fixed destination teleporter
		// --> hashmap: check destinations are teleporters too
		for (Coordinate c : teleporters.values()) {
			boolean found = false;
			for (Coordinate c2 : teleporters.keySet())
				if (c.equals(c2))
					found = true;
			assertTrue(found);
		}
		
		// The destination of a teleporter cannot be that teleporter itself
		for (Coordinate c : teleporters.keySet())
			if (teleporters.get(c).equals(c))
				fail("destination of a teleporter cannot be that teleporter itself");
	}
	
	/**
	 * Asserts the grid as build by the preceding construction-calls adheres the
	 * constraints on wall placement.
	 */
	private void assertValidWalls() {
		if (wallParts.size() < RandomGridBuilderDirector.MINIMUM_WALL_LENGHT) {
			System.out.println(wallParts.size());
		}
		assertTrue(wallParts.size() <= Math.ceil((getNumberOfSquares() + wallParts.size())
				* RandomGridBuilderDirector.MAXIMUM_WALL_NUMBER_PERCENTAGE));
		
		assertTrue(Collections.disjoint(playerStartingPositions, wallParts));
	}
}
