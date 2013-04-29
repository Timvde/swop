package grid.builder;

import grid.Coordinate;
import grid.Grid;
import item.IItem;
import item.identitydisk.ChargedIdentityDisk;
import item.identitydisk.UnchargedIdentityDisk;
import item.lightgrenade.LightGrenade;
import item.teleporter.Teleporter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import square.ASquare;
import square.Direction;
import square.PlayerStartingPosition;
import square.Square;
import square.WallPart;
import ObjectronExceptions.builderExceptions.GridBuildException;

/**
 * A {@link GridBuilder builder} specifically for the Tron game. This builder
 * offers a method {@link #getResult()} which returns the {@link Grid}
 * corresponding to the preceding construction-calls.
 */
public class TronGridBuilder implements GridBuilder {
	
	private Map<Coordinate, ASquare>		grid;
	private HashMap<Coordinate, Teleporter>	teleporters;
	private int						numberOfSquares;
	
	/**
	 * Create a new builder with an empty grid.
	 */
	public TronGridBuilder() {
		this.createNewEmptyGrid();
	}
	
	@Override
	public void createNewEmptyGrid() {
		this.grid = new HashMap<Coordinate, ASquare>();
		this.teleporters = new HashMap<Coordinate, Teleporter>();
		this.numberOfSquares = 0;
	}
	
	@Override
	public void addSquare(Coordinate coordinate) {
		if (coordinate == null)
			throw new IllegalArgumentException("The specified coordinate cannot be null");
		
		Map<Direction, ASquare> neighbours = getNeigboursFor(coordinate);
		grid.put(coordinate, new Square(neighbours));
		numberOfSquares++;
	}
	
	@Override
	public void addWall(Coordinate coordinate) {
		if (coordinate == null)
			throw new IllegalArgumentException("The specified coordinate cannot be null");
		
		Map<Direction, ASquare> neighbours = getNeigboursFor(coordinate);
		grid.put(coordinate, new WallPart(neighbours));
	}
	
	@Override
	public void addPlayerStartingPosition(Coordinate coordinate) {
		if (coordinate == null)
			throw new IllegalArgumentException("The specified coordinate cannot be null");
		
		Map<Direction, ASquare> neighbours = getNeigboursFor(coordinate);
		grid.put(coordinate, new PlayerStartingPosition(neighbours));
		numberOfSquares++;
	}
	
	@Override
	public void placeLightGrenade(Coordinate coordinate) throws GridBuildException {
		if (!canPlaceItem(coordinate))
			throw new GridBuildException("The item cannot be placed at the specified coordinate");
		
		grid.get(coordinate).addItem(new LightGrenade());
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
	public void placeTeleporter(Coordinate from, Coordinate to) throws GridBuildException {
		Teleporter start = getTeleporter(from);
		Teleporter destination = getTeleporter(to);
		
		if (start.getDestination() != null)
			throw new IllegalArgumentException("Teleporter on coordinate " + from
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
		
		ASquare square = grid.get(coordinate);
		Teleporter rv = new Teleporter(null, square);
		teleporters.put(coordinate, rv);
		square.addItem(rv);
		return rv;
	}
	
	/**
	 * Returns whether an {@link IItem item} can be placed on the grid. The
	 * constraints for a <i>TronGrid</i> are: <li>One cannot place an item when
	 * there's no square on that coordinate. <li>One cannot place an item on a
	 * {@link WallPart}. <li>One cannot place an item on a
	 * {@link PlayerStartingPosition}.
	 */
	@Override
	public boolean canPlaceItem(Coordinate coordinate) {
		if (coordinate == null)
			return false;
		if (!grid.containsKey(coordinate))
			return false;
		else if (grid.get(coordinate) instanceof WallPart)
			return false;
		else if (grid.get(coordinate) instanceof PlayerStartingPosition)
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
	private Map<Direction, ASquare> getNeigboursFor(Coordinate coordinate) {
		Map<Direction, ASquare> neighbours = new HashMap<Direction, ASquare>();
		
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
		
		return new Grid(grid);
	}
	
}
