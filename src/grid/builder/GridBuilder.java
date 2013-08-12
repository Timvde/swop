package grid.builder;

import grid.Coordinate;
import item.IItem;
import item.forcefieldgenerator.ForceFieldGenerator;
import item.identitydisk.ChargedIdentityDisk;
import item.identitydisk.UnchargedIdentityDisk;
import item.lightgrenade.LightGrenade;
import item.teleporter.Teleporter;
import java.util.List;
import square.Square;
import square.WallPart;
import ObjectronExceptions.builderExceptions.GridBuildException;

/**
 * Builder for the grid objects of a board game. The board consists of
 * {@link AbstractSquare abstract squares} , each square can be a
 * {@link WallPart wall} or a {@link Square square}. {@link IItem Items} can be
 * placed on these squares.
 */
public interface GridBuilder {
	
	/**
	 * Make a new empty grid.
	 */
	void createNewEmptyGrid();
	
	/**
	 * Add a {@link Square square} at the specified coordinate.
	 * 
	 * @param coordinate
	 *        the coordinate where the new square will be placed
	 */
	void addSquare(Coordinate coordinate);
	
	/**
	 * Add a {@link WallPart wall} at the specified coordinate.
	 * 
	 * @param coordinate
	 *        the coordinate where the new square will be placed
	 */
	void addWall(Coordinate coordinate);
	
	/**
	 * Add a player startingposition at the specified coordinate.
	 * 
	 * @param coordinate
	 *        the coordinate where the new player will be placed.
	 * @param number
	 *        the number corresponding to this starting position
	 * @throws IllegalArgumentException
	 *         The number cannot be already used earlier
	 */
	void addPlayerStartingPosition(Coordinate coordinate, int number)
			throws IllegalArgumentException;
	
	/**
	 * Place a flag with a certain owner ID at the specified coordinate.
	 * 
	 * @param coordinate
	 * 			The coordinate where the new flag will be added.
	 * @param id
	 * 			The owner ID of the flag.
	 */
    void placeFlag(Coordinate coordinate, int id);
	
	/**
	 * Place a {@link LightGrenade light grenade} at the specified coordinate.
	 * 
	 * @param coordinate
	 *        the coordinate where the new light grenade will be placed.
	 * @throws GridBuildException
	 *         When the item cannot be placed at the specified coordinate, i.e.
	 *         <code>!{@link #canPlaceItem(Coordinate)}</code>.
	 */
	void placeLightGrenade(Coordinate coordinate) throws GridBuildException;
	
	/**
	 * Place a new {@link UnchargedIdentityDisk uncharged identity disc} on the
	 * specified coordinate.
	 * 
	 * @param coordinate
	 *        the coordinate where the new identity disc will be placed
	 * @throws GridBuildException
	 *         When the item cannot be placed at the specified coordinate, i.e.
	 *         <code>!{@link #canPlaceItem(Coordinate)}</code>.
	 */
	void placeUnchargedIdentityDisc(Coordinate coordinate) throws GridBuildException;
	
	/**
	 * Place a new {@link ChargedIdentityDisk charged identity disc} on the
	 * specified coordinate.
	 * 
	 * @param coordinate
	 *        the coordinate where the new identity disc will be placed
	 * @throws GridBuildException
	 *         When the item cannot be placed at the specified coordinate, i.e.
	 *         <code>!{@link #canPlaceItem(Coordinate)}</code>.
	 */
	void placeChargedIdentityDisc(Coordinate coordinate) throws GridBuildException;
	
	/**
	 * Place a new {@link Teleporter teleporter} at the specified coordinate
	 * <code>from</code>. The destination of this teleporter will be the
	 * specified coordinate <code>to</code>. If there's no teleporter on the
	 * {@link Coordinate} <code>to</code> a new one (withouth a destination)
	 * will be created.
	 * 
	 * Note that building a grid with teleporters can result in build errors
	 * when no destination is set for one of the teleporters on the grid.
	 * 
	 * @param from
	 *        The coordinate where the new teleporter will be placed
	 * @param to
	 *        The coordinate which will be set as the destination
	 * @throws GridBuildException
	 *         When the item cannot be placed at the specified coordinate, i.e.
	 *         <code>!{@link #canPlaceItem(Coordinate)}</code>.
	 */
	void placeTeleporter(Coordinate from, Coordinate to) throws GridBuildException;
	
	/**
	 * Place a new {@link ForceFieldGenerator force field generator} on the
	 * specified coordinate.
	 * 
	 * @param coordinate
	 *        the coordinate where the new generator will be placed
	 * @throws GridBuildException
	 *         When the item cannot be placed at the specified coordinate, i.e.
	 *         <code>!{@link #canPlaceItem(Coordinate)}</code>.
	 */
	void placeForceFieldGenerator(Coordinate coordinate) throws GridBuildException;
	
	/**
	 * Returns whether an {@link IItem item} can be placed on the grid. This
	 * method can only account for limitations by the grid invariants. If other
	 * constraints are to be forced, then these must be checked by the client.
	 * 
	 * @param coordinate
	 *        the coordinate to test
	 * @return True if the item can be placed on the specified coordinate, else
	 *         false
	 */
	boolean canPlaceItem(Coordinate coordinate);
	
	// TODO maybe we need a canPlaceIDdisk method... instead of doing the
	// "coordinate already contains" check at the director
	
	/**
	 * Returns all neighbours of a specified coordinate, where one can directly
	 * move to (i.e. teleporting is not included).
	 * 
	 * TODO include teleporting?
	 * 
	 * @param coordinate
	 *        The coordinate to get the neighbours from
	 * @return A list of neighbouring coordinates
	 */
	List<Coordinate> getAllReachableNeighboursOf(Coordinate coordinate);
	
	/**
	 * Returns the number of {@link Square squares} on the currently constructed
	 * Grid.
	 * 
	 * @return number of squares on the grid
	 */
	int getNumberOfSquares();
}
