package game;

import item.EffectFactory;

/**
 * A game mode is a strategy a {@link Game} can follow. It encapsulates the
 * rules for winning and losing and the number of players participating in the
 * game.
 */
public interface GameMode {
	
	/**
	 * This method will check whether a player has won/loose using the rules for
	 * this gamemode.
	 */
	void checkWinner();
	
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
