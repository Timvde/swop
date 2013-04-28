package grid.builder;

import grid.Coordinate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ObjectronExceptions.builderExceptions.CannotPlaceItemException;

/**
 * An implementation of the {@link GridBuilder} interface to test the
 * {@link GridBuilderDirector directors}. This builder offers a method
 * {@link #gridHasValidWallsAndItems()} which returns whether or not the grid
 * created by the preceding build calls meets the constraints on walls and
 * items.
 */
public class TestGridBuilder implements GridBuilder {
	
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
	public TestGridBuilder() {
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
		if (!this.wallParts.add(coordinate))
			throw new IllegalArgumentException("There's already a wall at this coordinate");
		if (playerStartingPositions.contains(coordinate))
			throw new IllegalArgumentException("This coordinate is already used");
		// a wall may overwrite a square
		this.squares.remove(coordinate);
	}
	
	@Override
	public void addPlayerStartingPosition(Coordinate coordinate) {
		if (!playerStartingPositions.add(coordinate))
			throw new IllegalArgumentException("There's already a playerstart at this coordinate");
		if (squares.contains(coordinate) || wallParts.contains(coordinate))
			throw new IllegalArgumentException("This coordinate is already used");
	}
	
	@Override
	public void placeLightGrenade(Coordinate coordinate) throws CannotPlaceItemException {
		if (!canPlaceItem(coordinate))
			throw new CannotPlaceItemException();
		if (!LGList.add(coordinate))
			throw new IllegalArgumentException("There's already a LG at this coordinate");
	}
	
	@Override
	public void placeUnchargedIdentityDisc(Coordinate coordinate) throws CannotPlaceItemException {
		if (!canPlaceItem(coordinate))
			throw new CannotPlaceItemException();
		if (!IDList.add(coordinate))
			throw new IllegalArgumentException("There's already an IDdisk at this coordinate");
	}
	
	@Override
	public void placeChargedIdentityDisc(Coordinate coordinate) throws CannotPlaceItemException {
		if (!canPlaceItem(coordinate))
			throw new CannotPlaceItemException();
		if (!CIDList.add(coordinate))
			throw new IllegalArgumentException("There's already an CIDdisk at this coordinate");
	}
	
	@Override
	public void placeTeleporter(Coordinate from, Coordinate to) throws CannotPlaceItemException {
		if (!canPlaceItem(from) || !canPlaceItem(to))
			throw new CannotPlaceItemException();
		if (teleporters.put(from, to) != null)
			throw new IllegalArgumentException("Theres already a teleporter on this coordinates");
	}
	
	@Override
	public boolean canPlaceItem(Coordinate coordinate) {
		return coordinate != null
				&& (this.squares.contains(coordinate) || this.playerStartingPositions
						.contains(coordinate));
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
	 * Returns whether or not the grid as build by the preceding
	 * construction-calls adheres the constraints on walls and items.
	 * 
	 * This method is the same as
	 * <code>return {@link #hasValidLightGrenades()} && {@link #hasValidIDdisks()} && {@link #hasValidTeleporters()}
				&& {@link #hasValidWalls()}</code>
	 * 
	 * @return Whether the created grid is valid.
	 */
	public boolean gridHasValidWallsAndItems() {
		return hasValidLightGrenades() && hasValidIDdisks() && hasValidTeleporters()
				&& hasValidWalls();
	}
	
	/**
	 * Returns whether or not the grid as build by the preceding
	 * construction-calls adheres the constraints on lightgrenade placement.
	 * 
	 * @return Whether the created grid's lightgrenade positions are valid.
	 */
	public boolean hasValidLightGrenades() {
		if (Math.ceil(getNumberOfSquares() * RandomGridBuilderDirector.PERCENTAGE_OF_GRENADES) != LGList
				.size())
			return false;
		
		// The starting position of a player cannot contain a light grenade.
		if (!Collections.disjoint(playerStartingPositions, LGList))
			return false;
		
		return true;
	}
	
	/**
	 * Returns whether or not the grid as build by the preceding
	 * construction-calls adheres the constraints on identity disk placement.
	 * 
	 * @return Whether the created grid's identity disk positions are valid.
	 */
	public boolean hasValidIDdisks() {
		if (Math.ceil(getNumberOfSquares() * RandomGridBuilderDirector.PERCENTAGE_OF_IDENTITY_DISKS) != IDList
				.size())
			return false;
		
		// The starting position of a player cannot contain an identity disc.
		if (!Collections.disjoint(playerStartingPositions, IDList))
			return false;
		
		if (Math.ceil(getNumberOfSquares()
				* RandomGridBuilderDirector.NUMBER_OF_CHARGED_IDENTITY_DISKS) <= CIDList.size())
			return false;
		
		// The starting position of a player cannot contain a charged identity
		// disc.
		if (!Collections.disjoint(playerStartingPositions, CIDList))
			return false;
		
		return true;
	}
	
	/**
	 * Returns whether or not the grid as build by the preceding
	 * construction-calls adheres the constraints on teleporter placement.
	 * 
	 * @return Whether the created grid's teleporter positions are valid.
	 */
	public boolean hasValidTeleporters() {
		if (Math.ceil(getNumberOfSquares() * RandomGridBuilderDirector.PERCENTAGE_OF_TELEPORTERS) != teleporters
				.size())
			return false;
		
		// The starting position of a player cannot contain a teleporter.
		if (!Collections.disjoint(playerStartingPositions, teleporters.values()))
			return false;
		
		return true;
	}
	
	/**
	 * Returns whether or not the grid as build by the preceding
	 * construction-calls adheres the constraints on wall placement.
	 * 
	 * @return Whether the created grid's wall positions are valid.
	 */
	public boolean hasValidWalls() {
		if (wallParts.size() < RandomGridBuilderDirector.MINIMUM_WALL_LENGHT)
			return false;
		if (wallParts.size() > Math.ceil((getNumberOfSquares() + wallParts.size())
				* RandomGridBuilderDirector.MAXIMUM_WALL_NUMBER_PERCENTAGE))
			return false;
		
		if (!Collections.disjoint(playerStartingPositions, wallParts))
			return false;

		return true;
	}
}
