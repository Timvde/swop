package player;

import effects.Effect;

/**
 * This class manages the actions of a player. It makes sure they are always
 * right and keeps track of how many actions / turns a player must skip.
 */
public class PlayerActionManager {
	
	/** The maximum number of actions a Player is allowed to do in one turn */
	public static final int	MAX_NUMBER_OF_ACTIONS_PER_TURN	= 4;
	
	private TronPlayer		player;
	private int				actionsLeft;
	private int				numberOfTurnsToSkip;
	private PlayerDataBase	playerDB;
	/** A boolean representing whether the player has moved */
	private boolean			hasMoved;
	
	/**
	 * Create a {@link PlayerActionManager} with default values.
	 * 
	 * @param player
	 *        The player which this ActionManager manages
	 * @param playerDB
	 *        The player database.
	 */
	public PlayerActionManager(TronPlayer player, PlayerDataBase playerDB) {
		this.player = player;
		this.playerDB = playerDB;
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
	 * Makes this player decrement his number of actions left with the specified
	 * amount.
	 * 
	 * @param numberOfActionsToSkip
	 *        The number of actions to skip
	 */
	void decrementNumberOfActions(int numberOfActionsToSkip) {
		this.actionsLeft -= numberOfActionsToSkip;
	}
	
	/**
	 * Indicates this player has performed an action. His action counter will be
	 * lowered.
	 */
	void performedAction() {
		decrementNumberOfActions(1);
		playerDB.actionPerformed(this.player);
	}
	
	/**
	 * Reset the number of actions left to one. This is done when the turn is
	 * being ended. The one actoin left will be substracted by the End Turn
	 * action.
	 */
	void resetActionsToEndTurn() {
		// this.actionsLeft = 0;
		this.actionsLeft = 1;
	}
	
	/**
	 * Assign a new turn to a player. This can potentially execute an effect on
	 * a player.
	 * 
	 * @param player
	 */
	void assignNewTurn() {
		if (getNumberOfTurnsToSkip() > 0)
			numberOfTurnsToSkip--;
		else {
			this.hasMoved = false;
			Effect effect = player.getCurrentPosition().getStartTurnEffect();
			
			effect.execute(player);
			
			this.actionsLeft = Math.min(this.actionsLeft + MAX_NUMBER_OF_ACTIONS_PER_TURN,
					MAX_NUMBER_OF_ACTIONS_PER_TURN);
			// this.actionsLeft = this.actionsLeft +
			// MAX_NUMBER_OF_ACTIONS_PER_TURN;
		}
	}
	
	/**
	 * @return Whether the player has moved yet
	 */
	public boolean hasMoved() {
		return hasMoved;
	}
	
	/**
	 * Indicate that this player has moved.
	 */
	public void setHasMoved() {
		this.hasMoved = true;
	}
}
