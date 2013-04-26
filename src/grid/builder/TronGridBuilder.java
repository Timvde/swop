package grid.builder;

import grid.Coordinate;
import grid.Grid;
import item.IItem;
import item.identitydisk.ChargedIdentityDisk;
import item.identitydisk.UnchargedIdentityDisk;
import item.lightgrenade.LightGrenade;
import item.teleporter.Teleporter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import player.Player;
import square.ASquare;
import square.Direction;
import square.Square;
import square.WallPart;

/**
 * A {@link GridBuilder builder} specifically for the Tron game.
 * 
 */
public class TronGridBuilder implements GridBuilder {
	
	Map<Coordinate, ASquare>	grid;
	Set<Teleporter>				incompleteTeleporters;
	
	/**
	 * Create a new builder.
	 */
	public TronGridBuilder() {
		this.createNewGrid();
	}
	
	@Override
	public void createNewGrid() {
		this.grid = new HashMap<Coordinate, ASquare>();
		this.incompleteTeleporters = new HashSet<Teleporter>();
	}
	
	@Override
	public void addSquare(Coordinate coordinate) {
		if (coordinate == null)
			throw new IllegalArgumentException();
		
		Map<Direction, ASquare> neighbours = getNeigboursFor(coordinate);
		grid.put(coordinate, new Square(neighbours));
	}
	
	@Override
	public void addWall(Coordinate coordinate) {
		if (coordinate == null)
			throw new IllegalArgumentException();
		
		Map<Direction, ASquare> neighbours = getNeigboursFor(coordinate);
		grid.put(coordinate, new WallPart(neighbours));
	}
	
	@Override
	public void placePlayer(Coordinate coordinate) {
		//TODO implement
		//grid.get(coordinate).addPlayer(new Player());
	}
	
	@Override
	public void placeLightGrenade(Coordinate coordinate) {
		if (!canPlaceItem(coordinate))
			throw new IllegalArgumentException();
		
		grid.get(coordinate).addItem(new LightGrenade());
	}
	
	@Override
	public void placeUnchargedIdentityDisc(Coordinate coordinate) {
		if (!canPlaceItem(coordinate))
			throw new IllegalArgumentException();
		
		grid.get(coordinate).addItem(new UnchargedIdentityDisk());
	}
	
	@Override
	public void placeChargedIdentityDisc(Coordinate coordinate) {
		if (!canPlaceItem(coordinate))
			throw new IllegalArgumentException();
		
		grid.get(coordinate).addItem(new ChargedIdentityDisk());
	}
	
	@Override
	public void placeTeleporter(Coordinate coordinate) {
		if (!canPlaceItem(coordinate))
			throw new IllegalArgumentException();
		
		ASquare square = grid.get(coordinate);
		Teleporter teleporter = new Teleporter(null, square);
		
		square.addItem(teleporter);
		incompleteTeleporters.add(teleporter);
	}
	
	@Override
	public void connectTeleporters(Coordinate from, Coordinate to) {
		if (getTeleporterOnLocation(from) == null || getTeleporterOnLocation(to) == null)
			throw new IllegalArgumentException("Teleporter not found on the square");
		
		Teleporter start = getTeleporterOnLocation(from);
		Teleporter destination = getTeleporterOnLocation(to);
		
		if (!incompleteTeleporters.contains(start))
			throw new IllegalArgumentException("Teleporter has already a destination!");
		
		start.setDestination(destination);
		
		incompleteTeleporters.remove(start);
	}
	
	private Teleporter getTeleporterOnLocation(Coordinate coordinate) {
		if (grid.containsKey(coordinate)) {
			List<IItem> items = grid.get(coordinate).getAllItems();
			for (IItem item : items)
				if (item instanceof Teleporter)
					return (Teleporter) item;
		}
		return null;
	}
	
	@Override
	public boolean canPlaceItem(Coordinate coordinate) {
		if (!grid.containsKey(coordinate))
			return false;
		else if (grid.get(coordinate) instanceof WallPart)
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
			if (!(grid.containsKey(neighbours.get(i))))
				neighbours.remove(i);
			else if (!grid.get(neighbours.get(i)).canAddPlayer())
				neighbours.remove(i);
		}
		return neighbours;
	}
	
	@Override
	public int getNumberOfSquares() {
		return grid.size();
	}
	
	/**
	 * Returns a new grid as build by the preceding construction-calls.
	 * 
	 * @return the constructed grid
	 */
	public Grid getResult() {
		if (!incompleteTeleporters.isEmpty())
			throw new IllegalStateException("Some teleporters have no destinations!");
		
		return new Grid(grid);
	}
	
}
