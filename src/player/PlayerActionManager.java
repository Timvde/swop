package player;

import effects.Effect;

/**
 * This class manages the actions of a player. It makes sure they are always
 * right and keeps track of how many actions / turns a player must skip.
 */
public class PlayerActionManager {
	
	/** The maximum number of actions a Player is allowed to do in one turn */
	public static final int	MAX_NUMBER_OF_ACTIONS_PER_TURN	= 4;
	
	private int				actionsLeft;
	private int				numberOfTurnsToSkip;
	
	/**
	 * Create a {@link PlayerActionManager} with default values.
	 * 
	 * @param player
	 *        The player which this ActionManager manages
	 */
	public PlayerActionManager(Player player) {
		this.actionsLeft = 0;
		this.numberOfTurnsToSkip = 0;
	}
	
	/**
	 * Tell the {@link PlayerActionManager} to skip this player's next
	 * numberOfTurnsToSkip turns.
	 * 
	 * @param numberOfTurnsToSkip
	 *        The number of turns to skip
	 */
	void skipNextNTurns(int numberOfTurnsToSkip) {
		this.numberOfTurnsToSkip += numberOfTurnsToSkip;
	}
	
	/**
	 * Returns the number of actions left
	 * 
	 * @return The number of actions left
	 */
	int getNumberOfActionsLeft() {
		return actionsLeft;
	}
	
	/**
	 * Returns the number of turns this player has to skip
	 */
	private int getNumberOfTurnsToSkip() {
		return numberOfTurnsToSkip;
	}
	
	/**
	 * Makes this player skip a specified number of actions
	 * 
	 * @param numberOfActionsToSkip
	 *        The number of actions to skip
	 */
	void skipNumberOfActions(int numberOfActionsToSkip) {
		this.actionsLeft -= numberOfActionsToSkip;
	}
	
	/**
	 * Reset the number of actions left
	 */
	void resetNumberOfActions() {
		this.actionsLeft = 0;
	}
	
	/**
	 * Assign a new turn to a player. This can potentially execute an effect on
	 * a player.
	 * 
	 * @param player
	 */
	void assignNewTurn(Player player) {
		if (getNumberOfTurnsToSkip() > 0)
			numberOfTurnsToSkip--;
		else {
			Effect effect = player.getCurrentLocation().getStartTurnEffect();
			
			effect.execute(player);
			
			this.actionsLeft = Math.min(this.actionsLeft + MAX_NUMBER_OF_ACTIONS_PER_TURN,
					MAX_NUMBER_OF_ACTIONS_PER_TURN);
		}
	}
}
