package player;

/*
 * NOTE: Only Game holds a reference to the PlayerDB-object (for the NewGame use case). 
 * The controllers hold references to an IPlayerDB (and thus can only ask the current
 * player)
 */

/**
 * An {@link IPlayerDataBase} object keeps track of the current player allowed
 * to play.
 * 
 */
public interface IPlayerDataBase {
	
	/**
	 * Returns the player who is currently allowed to play.
	 * 
	 * @return the {@link IPlayer} who is currently allowed to play.
	 * 
	 * @throws IllegalStateException
	 *         When the database is empty.
	 */
	public IPlayer getCurrentPlayer() throws IllegalStateException;

	/**
	 * TODO remove this method...
	 */
	void endCurrentPlayerTurn();
	
}
