package game;

import java.util.List;
import player.PlayerDataBase;
import player.TurnEvent;
import player.Player;
import effects.EffectFactory;
import effects.RaceEffectFactory;

/**
 * In RaceMode the goal for each player is to reach the starting location of the
 * other player or to enclose the other player such that he cannot make a move.
 * 
 * The number of players at the start is exactly
 * {@value #NUMBER_OF_PLAYERS_IN_RACE_MODE}.
 */
public class RaceMode implements GameMode {
	
	/**
	 * The number of players at the start of RaceMode
	 */
	public static final int	NUMBER_OF_PLAYERS_IN_RACE_MODE	= 2;
	
	@Override
	public boolean checkCurrentPlayerHasWon(PlayerDataBase playerDB, TurnEvent turnEvent) {
		List<Player> playerList = playerDB.getAllPlayers();
		Player curPlayer = playerDB.getCurrentPlayer();
		
		// check whether the player has reached the start of another
		for (Player player : playerList)
			if ((!curPlayer.equals(player))
					&& curPlayer.getCurrentPosition().equals(player.getStartingPosition())) {
				playerDB.clearDataBase();
				return true;
			}
				
		return false;
	}
	
	@Override
	public boolean checkCurrentPlayerHasLost(PlayerDataBase playerDB, TurnEvent turnEvent) {
		Player curPlayer = playerDB.getCurrentPlayer();
		// check whether the current player ended his turn without moving
		if (turnEvent == TurnEvent.END_TURN && !curPlayer.hasMovedYet()) {
			playerDB.clearDataBase();
			return true;
		}
		return false;
	}
	
	@Override
	public int getNumberOfPlayers() {
		return NUMBER_OF_PLAYERS_IN_RACE_MODE;
	}
	
	@Override
	public EffectFactory getEffectFactory() {
		return new RaceEffectFactory();
	}
}
