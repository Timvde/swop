package grid.builder;

import effects.EffectFactory;
import grid.Coordinate;
import grid.Grid;
import item.IItem;
import item.forcefieldgenerator.ForceFieldGenerator;
import item.identitydisk.ChargedIdentityDisk;
import item.identitydisk.UnchargedIdentityDisk;
import item.lightgrenade.LightGrenade;
import item.teleporter.Teleporter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import player.PlayerState;
import powerfailure.PowerFailureCreator;
import square.Direction;
import square.NormalSquare;
import square.PropertyType;
import square.SquareContainer;
import square.StartingPositionProperty;
import square.WallPart;
import ObjectronExceptions.builderExceptions.GridBuildException;

/**
 * A {@link GridBuilder builder} specifically for the Tron game. This builder
 * offers a method {@link #getResult()} which returns the {@link Grid}
 * corresponding to the preceding construction-calls.
 */
public class TronGridBuilder implements GridBuilder {
	
	private Map<Coordinate, SquareContainer>	grid;
	private HashMap<Coordinate, Teleporter>		teleporters;
	private int									numberOfSquares;
	private Map<Integer, Coordinate>			startingPositions;
	private EffectFactory						effectFactory;
	
	/**
	 * Create a new builder with an empty grid.
	 * 
	 * @param effectFactory
	 *        The effect factory that the items will get.
	 */
	public TronGridBuilder(EffectFactory effectFactory) {
		if (effectFactory == null)
			throw new IllegalArgumentException("the factory cannot be null");
		
		this.effectFactory = effectFactory;
		this.createNewEmptyGrid();
	}
	
	@Override
	public void createNewEmptyGrid() {
		this.grid = new HashMap<Coordinate, SquareContainer>();
		this.teleporters = new HashMap<Coordinate, Teleporter>();
		this.numberOfSquares = 0;
		this.startingPositions = new HashMap<Integer, Coordinate>();
	}
	
	@Override
	public void addSquare(Coordinate coordinate) {
		if (coordinate == null)
			throw new IllegalArgumentException("The specified coordinate cannot be null");
		
		Map<Direction, SquareContainer> neighbours = getNeigboursFor(coordinate);
		SquareContainer square = new SquareContainer(neighbours, new NormalSquare());
		square.addPropertyCreator(new PowerFailureCreator(effectFactory));
		if (grid.put(coordinate, square) == null)
			numberOfSquares++;
	}
	
	@Override
	public void addWall(Coordinate coordinate) {
		if (coordinate == null)
			throw new IllegalArgumentException("The specified coordinate cannot be null");
		
		Map<Direction, SquareContainer> neighbours = getNeigboursFor(coordinate);
		SquareContainer wall = new SquareContainer(neighbours, new WallPart());
		wall.addPropertyCreator(new PowerFailureCreator(effectFactory));
		if (grid.put(coordinate, wall) != null)
			numberOfSquares--;
	}
	
	@Override
	public void addPlayerStartingPosition(Coordinate coordinate, int number) {
		if (coordinate == null)
			throw new IllegalArgumentException("The specified coordinate cannot be null");
		if (startingPositions.keySet().contains(number)) {
			if (startingPositions.get(number).equals(coordinate))
				return; // the starting position is already added
			else
				throw new IllegalArgumentException(
						"the startingposition number is already used with another coordinate");
		}
		
		if (!grid.containsKey(coordinate))
			addSquare(coordinate);
		startingPositions.put(number, coordinate);
		grid.get(coordinate).addProperty(new StartingPositionProperty());
	}
	
	@Override
	public void placeLightGrenade(Coordinate coordinate) throws GridBuildException {
		if (!canPlaceItem(coordinate))
			throw new GridBuildException("The item cannot be placed at the specified coordinate");
		
		grid.get(coordinate).addItem(new LightGrenade(effectFactory));
	}
	
	@Override
	public void placeUnchargedIdentityDisc(Coordinate coordinate) throws GridBuildException {
		if (!canPlaceItem(coordinate))
			throw new GridBuildException("The item cannot be placed at the specified coordinate");
		
		grid.get(coordinate).addItem(new UnchargedIdentityDisk());
	}
	
	@Override
	public void placeChargedIdentityDisc(Coordinate coordinate) throws GridBuildException {
		if (!canPlaceItem(coordinate))
			throw new GridBuildException("The item cannot be placed at the specified coordinate");
		
		grid.get(coordinate).addItem(new ChargedIdentityDisk());
	}
	
	@Override
	public void placeForceFieldGenerator(Coordinate coordinate) throws GridBuildException {
		if (!canPlaceItem(coordinate))
			throw new GridBuildException("The item cannot be placed at the specified coordinate");
		
		grid.get(coordinate).addItem(new ForceFieldGenerator());
	}
	
	@Override
	public void placeTeleporter(Coordinate from, Coordinate to) throws GridBuildException {
		if (from == null || to == null)
			throw new GridBuildException("the coords cannot be null");
		
		Teleporter start = getTeleporter(from);
		Teleporter destination = getTeleporter(to);
		
		if (start.getDestination() != null)
			throw new GridBuildException("Teleporter on coordinate " + from
					+ " has already a destination!");
		
		start.setDestination(destination);
	}
	
	/**
	 * This method will return the {@link Teleporter} on a specified coordinate.
	 * When there's no teleporter on the specified coordinate, this method will
	 * create one.
	 * 
	 * @param coordinate
	 *        the coordinate to add the teleporter to
	 * @return The teleporter at the specified coordinate
	 */
	private Teleporter getTeleporter(Coordinate coordinate) {
		if (teleporters.containsKey(coordinate))
			return teleporters.get(coordinate);
		
		// teleporter doesn't exist yet; create new
		return setTeleporter(coordinate);
	}
	
	private Teleporter setTeleporter(Coordinate coordinate) throws GridBuildException {
		if (!canPlaceItem(coordinate))
			throw new GridBuildException("Cannot place a teleporter on " + coordinate);
		
		SquareContainer square = grid.get(coordinate);
		Teleporter rv = new Teleporter(null, square, effectFactory);
		teleporters.put(coordinate, rv);
		square.addItem(rv);
		return rv;
	}
	
	/**
	 * Returns whether an {@link IItem item} can be placed on the grid. The
	 * constraints for a <i>TronGrid</i> are: <li>One cannot place an item when
	 * there's no square on that coordinate. <li>One cannot place an item on a
	 * wall. <li>One cannot place an item on a starting position.
	 */
	@Override
	public boolean canPlaceItem(Coordinate coordinate) {
		if (coordinate == null)
			return false;
		if (!grid.containsKey(coordinate))
			return false;
		else if (grid.get(coordinate).hasProperty(PropertyType.WALL))
			return false;
		else if (grid.get(coordinate).hasProperty(PropertyType.STARTING_POSITION))
			return false;
		else
			return true;
	}
	
	/**
	 * Returns a map of all the neighbours for a specified coordinate. If the
	 * neighbour in a direction does not exist, the mapping will contain null.
	 * 
	 * @param coordinate
	 *        the coordinate of which the neighbours are returned
	 * @return the neighours of the specified coordinate
	 */
	private Map<Direction, SquareContainer> getNeigboursFor(Coordinate coordinate) {
		Map<Direction, SquareContainer> neighbours = new HashMap<Direction, SquareContainer>();
		
		for (Direction direction : Direction.values())
			if (grid.containsKey(coordinate.getCoordinateInDirection(direction)))
				neighbours.put(direction, grid.get(coordinate.getCoordinateInDirection(direction)));
		
		return neighbours;
	}
	
	@Override
	public List<Coordinate> getAllReachableNeighboursOf(Coordinate coordinate) {
		List<Coordinate> neighbours = coordinate.getAllNeighbours();
		
		for (int i = neighbours.size() - 1; i >= 0; i--) {
			// Grid doesn't contain this neighbour or a player can't be added to
			// this square
			if (!grid.containsKey(neighbours.get(i)))
				neighbours.remove(i);
			else if (!grid.get(neighbours.get(i)).canAddPlayer())
				neighbours.remove(i);
		}
		return neighbours;
	}
	
	@Override
	public int getNumberOfSquares() {
		return numberOfSquares;
	}
	
	/**
	 * Returns a new grid as build by the preceding construction-calls.
	 * 
	 * @return the constructed grid
	 * @throws GridBuildException
	 *         When a teleporter has no destination
	 */
	public Grid getResult() throws GridBuildException {
		for (Teleporter teleporter : this.teleporters.values()) {
			if (teleporter.getDestination() == null)
				throw new GridBuildException("Some teleporters have no destinations!");
		}
		
		return new Grid(grid, this.getAllStartingPositions());
	}
	
	/**
	 * Get all the starting positions. The list will be sorted by their
	 * startingposition number (from smaller to larger).
	 * 
	 * @return a sorted list of all the startingpositions on the grid.
	 */
	private List<SquareContainer> getAllStartingPositions() {
		List<Integer> startingNumbers = new ArrayList<Integer>(this.startingPositions.keySet());
		Collections.sort(startingNumbers);
		
		List<SquareContainer> result = new ArrayList<SquareContainer>();
		for (Integer startNumber : startingNumbers) {
			result.add(grid.get(startingPositions.get(startNumber)));
		}
		return result;
	}
}
