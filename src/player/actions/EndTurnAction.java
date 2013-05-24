package player.actions;

import player.TronPlayer;
import ObjectronExceptions.IllegalActionException;

/**
 * EndTurnAction ends the turn of the player
 */
public class EndTurnAction implements Action {
	
	@Override
	public void execute(TronPlayer player) {
		if (!player.canPerformAction(this))
			throw new IllegalActionException("The player must be allowed to perform an action.");
		
		player.endTurn();
	}

	@Override
	public int getCost() {
		return 0;
	}
	
}
