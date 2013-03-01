package player;

import grid.Square;

import java.util.LinkedList;

/**
 * Every player leaves behind a lighttrail. A lighttrail consist of multiple
 * squares and has a maximum length. A lighttrailpart (one square) remains
 * active during NUMBER_OF_ACTIONS_DELAY actions of the player.
 * 
 */
public class LightTrail {

	/**
	 * The number of player-actions the lighttrailpart (one square) remains
	 * visible.
	 */
	public static final int NUMBER_OF_ACTIONS_DELAY = 2;

	/**
	 * The maximum lenght of a lightrail (in squares). At this moment fixed.
	 */
	private static final int MAX_LENGTH = 3;

	//linked list with the first item = the square the player came from
	private LinkedList<Square> lightTrailList;

	/**
	 * Returns the maximum length of this lightrail (in squares)
	 * 
	 * @return the maximum length of this lightrail (in squares)
	 */
	public int getMaxLength() {
		return MAX_LENGTH;
	}

	/**
	 * Returns the current length of this lightrail (in squares)
	 * 
	 * @return the current length of this lightrail (in squares)
	 */
	public int getLightTrailLenght() {
		return this.lightTrailList.size();
	}

	/**
	 * Returns an array with all the {@link Square}s that are part of this
	 * lighttrail.
	 * 
	 * @return an array of length <code>getLightTrailLength()</code> with all
	 *         the {@link Square}s that are part of this lightrail.
	 */
	public Square[] getLightTrail() {
		return (Square[]) this.lightTrailList.toArray();
	}

	/**
	 * This method updates the lightTrail after a one-square-move of the player
	 * in a given direction.
	 * 
	 * @param direction
	 *            The direction the player just moved in
	 * 
	 * @note Do not call this method manually. Lighttrails are
	 *       updated automatically as the player (and its lighttrail) move around the grid.
	 */
	public void updateLightTrail(Square newSquare) {
		if (this.getLightTrailLenght() >= this.getMaxLength()) {
			Square last = this.lightTrailList.removeLast();
			last.setHasLightTrail(false);
		}
		this.lightTrailList.addFirst(newSquare);
		newSquare.setHasLightTrail(true);
	}
}
