package grid.builder;

import grid.Coordinate;
import item.IItem;
import item.identitydisk.ChargedIdentityDisk;
import item.identitydisk.UnchargedIdentityDisk;
import item.lightgrenade.LightGrenade;
import item.teleporter.Teleporter;
import java.util.List;
import square.ASquare;
import square.Square;
import square.WallPart;
import ObjectronExceptions.builderExceptions.CannotPlaceItemException;

/**
 * Builder for the grid objects of a board game. The board consists of
 * {@link ASquare abstract squares} , each square can be a {@link WallPart wall}
 * or a {@link Square square}. {@link IItem Items} can be placed on these
 * squares.
 * 
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
	 */
	void addPlayerStartingPosition(Coordinate coordinate);
	
	/**
	 * Place a {@link LightGrenade light grenade} at the specified coordinate.
	 * 
	 * @param coordinate
	 *        the coordinate where the new light grenade will be placed.
	 * @throws CannotPlaceItemException
	 *         When the item cannot be placed at the specified coordinate, i.e.
	 *         <code>!{@link #canPlaceItem(Coordinate)}</code>.
	 */
	void placeLightGrenade(Coordinate coordinate) throws CannotPlaceItemException;
	
	/**
	 * Place a new {@link UnchargedIdentityDisk uncharged identity disc} on the
	 * specified coordinate.
	 * 
	 * @param coordinate
	 *        the coordinate where the new identity disc will be placed
	 * @throws CannotPlaceItemException
	 *         When the item cannot be placed at the specified coordinate, i.e.
	 *         <code>!{@link #canPlaceItem(Coordinate)}</code>.
	 */
	void placeUnchargedIdentityDisc(Coordinate coordinate) throws CannotPlaceItemException;
	
	/**
	 * Place a new {@link ChargedIdentityDisk charged identity disc} on the
	 * specified coordinate.
	 * 
	 * @param coordinate
	 *        the coordinate where the new identity disc will be placed
	 * @throws CannotPlaceItemException
	 *         When the item cannot be placed at the specified coordinate, i.e.
	 *         <code>!{@link #canPlaceItem(Coordinate)}</code>.
	 */
	void placeChargedIdentityDisc(Coordinate coordinate) throws CannotPlaceItemException;
	
	/**
	 * Place a new {@link Teleporter teleporter} at the specified coordinate.
	 * Note that building a grid with teleporters can result in build errors
	 * when no destination is set for one of the teleporters on the grid. A
	 * teleporter can be connected to an other teleporter using
	 * {@link #connectTeleporters(Coordinate, Coordinate)
	 * connectTeleporters(from, to)}.
	 * 
	 * @param coordinate
	 *        the coordinate where the new teleporter will be placed
	 * @throws CannotPlaceItemException
	 *         When the item cannot be placed at the specified coordinate, i.e.
	 *         <code>!{@link #canPlaceItem(Coordinate)}</code>.
	 */
	void placeTeleporter(Coordinate coordinate) throws CannotPlaceItemException;
	
	/**
	 * Connect two placed teleporters on the grid. The teleporter on the
	 * specified coordinate <code>to</code> will be set as the destination for
	 * the teleporter on the specified coordinate <code>from</code>.
	 * 
	 * @param from
	 *        The coordinate of the teleporter which destination will be set
	 * @param To
	 *        The coordinate of the teleporter which will be set as the
	 *        destination
	 */
	void connectTeleporters(Coordinate from, Coordinate To);
	
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
	
	/**
	 * Returns all neighbours of a specified coordinate, where one can directly
	 * move to (i.e. teleporting is not included).
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