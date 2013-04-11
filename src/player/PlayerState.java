package player;

import square.ISquare;

/**
 * PlayerState controls the actions of a player. A player can only perform
 * actions when he is in the {@link #ACTIVE} state. It is highly encouraged to check each transition
 * between state with the {@link #isAllowedTransistionTo(PlayerState)} method.
 */
public enum PlayerState {
	/**
	 * A {@link Player player} is in the active state when it is his turn. If
	 * this is the case, he is free to perform actions.
	 */
	ACTIVE {
		
		@Override
		public boolean isAllowedTransistionTo(PlayerState toState) {
			if (toState == null) {
				return false;
			}
			// one can go anywhere from the active state
			return true;
		}
	},
	
	/**
	 * A {@link Player player} is in the waiting state when he is waiting for
	 * the turn of other player to end. When a player is in the waiting state,
	 * he cannot perform actions.
	 */
	WAITING {
		
		@Override
		public boolean isAllowedTransistionTo(PlayerState toState) {
			if (toState == null) {
				return false;
			}
			// one can only go to active from the waiting state
			return (toState == this) || (toState == ACTIVE);
		}
	},
	
	/**
	 * A {@link Player player} is in the finished state when he has reached his
	 * target {@link ISquare square}. When a player has reached this position,
	 * he cannot perform actions.
	 */
	FINISHED {
		
		@Override
		public boolean isAllowedTransistionTo(PlayerState toState) {
			if (toState == null) {
				return false;
			}
			// one can go nowhere from the finished state
			return (toState == this);
		}
	},
	
	/**
	 * A {@link Player player} is in the lost state when he has performed an
	 * action or a series of actions that caused the player to lose the game.
	 */
	LOST {
		
		@Override
		public boolean isAllowedTransistionTo(PlayerState toState) {
			if (toState == null) {
				return false;
			}
			// one can go nowhere from the lost state
			return (toState == this);
		}
	};
	
	/**
	 * Returns whether or not a transition from this state to a specified state is
	 * allowed.
	 * 
	 * @param toState
	 *        the given other state
	 * @return whether or not a transition from this state to a give state is
	 *         allowed.
	 */
	public abstract boolean isAllowedTransistionTo(PlayerState toState);
}