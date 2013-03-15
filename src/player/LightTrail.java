package player;

import grid.Coordinate;
import grid.Square;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import grid.ASquare;

/**
 * LightTrail is an impenetrable wall (for other players) which trails the
 * player. A lightTrail cannot cover more than three squares and has to be
 * updated every with every action the player performs. The each player is
 * responsible for keeping his own lightTrail consistent with his actions. It is
 * not allowed for a lightTrail to occupy the same position twice.
 * 
 * @author Bavo Mees
 */
public class LightTrail {
	
	/** the lightTrail of the player */
	private LinkedList<Square>	lightTrail;
	
	/** the maximum length of this light trail */
	private static final int	MAXIMUM_LENGTH	= 3;
	
	/**
	 * Create a new lightTrail behind a player. The length of the newly created
	 * lightTrail will be zero.
	 */
	public LightTrail() {
		lightTrail = new LinkedList<Square>();
	}
	
	/**
	 * Update the lightTrail to a specified coordinate. This method will trim
	 * the lightTrail if this appears to be necessary. When the method returns
	 * the lightTrail will cover a square with the specified coordinate if the
	 * previous length of the square was less than MAXIMUM LENGTH,
	 * otherwise the lightTrail will have shifted to cover the new Coordinate
	 * and the last coordinate of the square will be cleared of this lightTrail.
	 * This method should be called whenever the player makes a move action on
	 * the board.
	 * 
	 * @param square
	 *        the new square which the light trail should cover
	 * @throws IllegalArgumentException
	 *         if the specified coordinate is not a
	 *         {@link #isValidNewSquare(Square) valid} new coordinate for this
	 *         lightTrail
	 */
	public void updateLightTrail(Square square) throws IllegalArgumentException {
		if (!isValidNewSquare(square))
			throw new IllegalArgumentException(
					"the specified square could not be added to the lightTrail!");
		lightTrail.addFirst(square);
		square.placeLightTrail();
		
		if (lightTrail.size() > MAXIMUM_LENGTH)
			lightTrail.removeLast().removeLightTrail();
	}
	
	/**
	 * Test whether a coordinate is valid as a new part of the lightTrail. This
	 * will return false if the lightTrail already contains the specified
	 * coordinate or if the specified coordinate is not a
	 * {@link Coordinate#isNeighbour(Coordinate) neighbor} of the first
	 * coordinate of this lightTrail.
	 * 
	 * @param square
	 *        the square to test
	 * @return whether the new coordinate can be set as a new part of the
	 *         lightTrail
	 */
	public boolean isValidNewSquare(Square square) {
		// null cannot be added to the list
		if (square == null)
			return false;
		// if the lightTrail is empty, any coordinate is valid
		if (lightTrail.isEmpty())
			return true;
		// test whether the lightTrail already contains the coordinate
		else if (lightTrail.contains(square))
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
			lightTrail.removeLast().removeLightTrail();
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
	 * returns a list of the squares in this light trail
	 * 
	 * @return a list of the squares
	 */
	public List<ASquare> getLightTrailSquares() {
		return new ArrayList<ASquare>(lightTrail);
	}
}
