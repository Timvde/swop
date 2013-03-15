package player;

import grid.Coordinate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * LightTrail is an impenetrable wall (for other players) which trails the
 * player. A lightTrail cannot cover more than three squares and has to be
 * updated every with every action the player performs. The each player is
 * responsible for keeping his own lightTrail consistent with his actions. Each
 * coordinate in the lightTrail is a neighbor of at least one other coordinate
 * in the lightTrail. Also it is not allowed for a lightTrail to occupy the same
 * position twice.
 * 
 * @author Bavo Mees
 */
public class LightTrail {
	
	/** the lightTrail of the player */
	private LinkedList<Coordinate>	lightTrail;
	
	/**
	 * Create a new lightTrail behind a player. The length of the newly created
	 * lightTrail will be zero.
	 */
	public LightTrail() {
		lightTrail = new LinkedList<Coordinate>();
	}
	
	/**
	 * Update the lightTrail to a specified coordinate. This method will trim
	 * the lightTrail if this appears to be necessary. When the method returns
	 * the lightTrail will cover a square with the specified coordinate if the
	 * previous length of the square was less than three, otherwise the
	 * lightTrail will have shifted to cover the new Coordinate and the last
	 * coordinate of the square will be cleared of this lightTrail. This method
	 * should be called whenever the player makes a move action on the board.
	 * 
	 * @param coordinate
	 *        the coordinate which the new lightTrail should cover
	 * @throws IllegalArgumentException
	 *         if the specified coordinate is not a
	 *         {@link #isValidNewCoordinate(Coordinate) valid} new coordinate
	 *         for this lightTrail
	 */
	public void updateLightTrail(Coordinate coordinate) throws IllegalArgumentException {
		if (!isValidNewCoordinate(coordinate))
			throw new IllegalArgumentException(
					"the specified coordinate could not be added to the lightTrail!");
		lightTrail.addFirst(coordinate);
		
		if (lightTrail.size() > 3)
			lightTrail.removeLast();
	}
	
	/**
	 * Test whether a coordinate is valid as a new part of the lightTrail. This
	 * will return false if the lightTrail already contains the specified
	 * coordinate or if the specified coordinate is not a
	 * {@link Coordinate#isNeighbour(Coordinate) neighbor} of the first
	 * coordinate of this lightTrail.
	 * 
	 * @param coordinate
	 *        the coordinate to test
	 * @return whether the new coordinate can be set as a new part of the
	 *         lightTrail
	 */
	public boolean isValidNewCoordinate(Coordinate coordinate) {
		// null cannot be added to the list
		if (coordinate == null)
			return false;
		// if the lightTrail is empty, any coordinate is valid
		if (lightTrail.isEmpty())
			return true;
		// test whether the lightTrail already contains the coordinate
		else if (lightTrail.contains(coordinate))
			return false;
		// test whether the coordinate is a neighbor of the first coordinate
		else if (!lightTrail.getFirst().isNeighbour(coordinate))
			return false;
		else
			return true;
	}
	
	/**
	 * Update the lightTrail to a new state. When the method returns the
	 * lightTrail will not cover the last element of the previous lightTrail.
	 * This method should be called whenever the player executes an action that
	 * does not include moving around on the grid.
	 */
	public void updateLightTrail() {
		if (!lightTrail.isEmpty())
			lightTrail.removeLast();
	}
	
	/**
	 * returns the size of this lightTrail.
	 * 
	 * @return the size of the lightTrail
	 */
	public int size() {
		return lightTrail.size();
	}
	
	/**
	 * Returns the coordinates of the current lightTrail.
	 * 
	 * @return coordinates of the lightTrail
	 */
	public List<Coordinate> getLightTrailCoordinates() {
		return new ArrayList<Coordinate>(lightTrail);
	}
}
