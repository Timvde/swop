package game;

import effects.EffectFactory;
import effects.RaceFactory;

/**
 * In RaceMode the goal for each player is to reach the starting location of the
 * other player or to enclose the other player such that he cannot make a move.
 * The number of players at the start is exactly
 * {@value #NUMBER_OF_PLAYERS_IN_RACE_MODE}.
 */
public class RaceMode implements GameMode {
	
	/**
	 * The number of players at the start of RaceMode
	 */
	public static final int	NUMBER_OF_PLAYERS_IN_RACE_MODE	= 2;
	
	@Override
	public void checkWinner() {
		
	}
	
	@Override
	public int getNumberOfPlayers() {
		return NUMBER_OF_PLAYERS_IN_RACE_MODE;
	}
	
	@Override
	public EffectFactory getEffectFactory() {
		return new RaceFactory();
	}
}
