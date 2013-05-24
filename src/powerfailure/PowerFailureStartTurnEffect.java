package powerfailure;

import player.TronPlayer;
import square.TronObject;
import effects.AbstractEffect;

/**
 * This class represents an effect to be executed on a player when he resides on
 * a power failured square at the start of his turn.
 */
public class PowerFailureStartTurnEffect extends AbstractEffect {
	
	/**
	 * The number of actions that a player loses if he is standing on a power
	 * failured square at the start of his turn.
	 */
	private static final int	POWER_FAILURE_PENALTY_AT_START_TURN	= 1;
	
	@Override
	public void execute(TronObject object) {
		if (object instanceof TronPlayer) {
			TronPlayer player = (TronPlayer) object;
			player.skipNumberOfActions(POWER_FAILURE_PENALTY_AT_START_TURN);
		}
		super.execute(object);
	}
	
}
