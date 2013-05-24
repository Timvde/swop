package player.actions;

import ObjectronExceptions.IllegalActionException;
import player.TronPlayer;
import player.PlayerState;
import square.SquareContainer;

/**
 * EndTurnAction ends the turn of the player
 */
public class EndTurnAction implements Action {
	
	@Override
	public void execute(TronPlayer player) {
		if (!player.canPerformAction())
			throw new IllegalActionException("The player must be allowed to perform an action.");
		
		player.endTurn();
	}
	
}
