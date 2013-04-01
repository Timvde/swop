package player;

import square.ISquare;

/**
 * PlayerState controls the actions of a player. A player can only perform
 * actions when he is in the {@link #ACTIVE} state.
 */
public enum PlayerState {
	/**
	 * A {@link Player player} is in the active state when it is his turn. If
	 * this is the case, he is free to perform actions.
	 */
	ACTIVE,
	
	/**
	 * A {@link Player player} is in the waiting state when he is waiting for
	 * the turn of other player to end. When a player is in the waiting state,
	 * he cannot perform actions.
	 */
	WAITING,
	
	/**
	 * A {@link Player player} is in the finished state when he has reached his
	 * target {@link ISquare square}. When a player has reached this position,
	 * he cannot perform actions.
	 */
	FINISHED,
	
	/**
	 * A {@link Player player} is in the lost state when he has performed an
	 * action or a series of actions that caused the player to lose the game.
	 */
	LOST,
	
}
