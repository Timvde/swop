package player;

/**
 * The TurnEvent is a signal that is sent to every observer which is interested
 * in the execution of an action.
 */
public enum TurnEvent {
	/**
	 * This indicates the end of a player's action, while staying the active
	 * player.
	 */
	END_ACTION,
	/**
	 * This indicates the current player has ended his turn. This is sent before
	 * the database actually switches the players (i.e. the currentplayer is
	 * still the player that has ended his turn).
	 */
	END_TURN;
}
