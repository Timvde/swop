package player;

/**
 * Every player leaves behind a lighttrail. A lighttrail consist of multiple
 * squares and has a maximum length. A lighttrailpart (one square) remains
 * active during NUMBER_OF_ACTIONS_DELAY actions of the player.
 * 
 */
public interface ILightTrail {
	/**
	 * The number of player-actions the lighttrailpart (one square) remains
	 * visible.
	 */
	public static final int NUMBER_OF_ACTIONS_DELAY = 2;

	/**
	 * Returns the maximum length of this lightrail (in squares)
	 * 
	 * @return the maximum length of this lightrail (in squares)
	 */
	public int getMaxLength();
	
	/**
	 * Returns the current length of this lightrail (in squares)
	 * 
	 * @return the current length of this lightrail (in squares)
	 */
	public int getLenght();

}
