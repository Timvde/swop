package player;

/*
 * NOTE: Only Game holds a reference to the PlayerDB-object (for the NewGame use case). The controllers
 * hold references to an IPlayerDB (and thus can only ask the current
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
	 * @return the player who is currently allowed to play.
	 */
	public IPlayer getCurrentPlayer();

}
