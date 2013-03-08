package player;

/**
 * An {@link IPlayerDataBase} object always has pointer to the current player
 * allowed to play.
 * 
 */
public interface IPlayerDataBase {

	/**
	 * Returns the player who is currently allowed to play.
	 * 
	 * @return the player who is currently allowed to play.
	 */
	public Player getCurrentPlayer();

}
