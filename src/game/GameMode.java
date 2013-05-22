package game;

import player.PlayerDataBase;
import player.TurnEvent;
import effects.EffectFactory;

/**
 * A game mode is a strategy a {@link Game} can follow. It encapsulates the
 * rules for winning and losing and the number of players participating in the
 * game.
 */
public interface GameMode {
	
	/**
	 * This method will check whether the
	 * {@link PlayerDataBase#getCurrentPlayer() current player} has won using
	 * the rules for this gamemode. When a player has won the game, the given
	 * database will be {@link PlayerDataBase#clearDataBase() cleared}.
	 * 
	 * This method should be called each time the player has performed an
	 * action, as the actions he performs may result in winning/loosing.
	 * 
	 * @param playerDB
	 *        the database appointing the current player
	 * @param turnEvent
	 *        The turnevent corresponding to action the player just performed.
	 * @return Whether or not the current player has won.
	 */
	boolean checkCurrentPlayerHasWon(PlayerDataBase playerDB, TurnEvent turnEvent);
	
	/**
	 * This method will check whether the
	 * {@link PlayerDataBase#getCurrentPlayer() current player} has lost using
	 * the rules for this gamemode.
	 * 
	 * This method should be called each time the player has performed an
	 * action, as the actions he performs may result in winning/loosing.
	 * 
	 * @param playerDB
	 *        the database appointing the current player
	 * @param turnEvent
	 *        The turnevent corresponding to action the player just performed.
	 * @return Whether or not the current player has won.
	 */
	boolean checkCurrentPlayerHasLost(PlayerDataBase playerDB, TurnEvent turnEvent);
	
	/**
	 * Returns the number of players participating in the game at the start.
	 * 
	 * @return the number of players initially participating in the game
	 */
	int getNumberOfPlayers();
	
	/**
	 * Returns an effectfactory for this gamemode.
	 * 
	 * @return an effectfactory for this gamemode.
	 */
	EffectFactory getEffectFactory();
}
