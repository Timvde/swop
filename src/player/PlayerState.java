package player;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import square.ISquare;

/**
 * PlayerState controls the actions of a player. A player can only perform
 * actions when he is in the {@link #ACTIVE} state. Although it is not possible
 * to force ENUM transitions in java, it is highly encouraged to check each transition
 * between state with the {@link #canTransistionTo(PlayerState)} method.
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
	 * Returns whether or not a transition from this state to a give state is
	 * allowed.
	 * 
	 * @param toState
	 *        the given other state
	 * @return whether or not a transition from this state to a give state is
	 *         allowed.
	 */
	public abstract boolean isAllowedTransistionTo(PlayerState toState);
	
	private static EnumMap<PlayerState, Set<PlayerState>>	transitionTable;
	
	/**
	 * Returns whether a transition between the current state and a specified
	 * state is allowed. Note that this transition table is not symmetrical,
	 * e.g. if x and y are both states and <code> x.canTransitionTo(y) </code>
	 * returns <code> true </code> it is not implied that
	 * <code> y.canTransitionTo(x) </code> returns <code> true </code> as well.
	 * 
	 * @param state
	 *        the state to test
	 * @return true if a transition from this state to the specified state is
	 *         possible, else false
	 */
	public boolean canTransistionTo(PlayerState state) {
		return transitionTable.get(this).contains(state);
	}
	
	static {
		transitionTable = new EnumMap<PlayerState, Set<PlayerState>>(PlayerState.class);
		
		// put the transitions for ACTIVE in the table
		Set<PlayerState> active = new HashSet<PlayerState>();
		active.add(ACTIVE);
		active.add(FINISHED);
		active.add(LOST);
		active.add(WAITING);
		transitionTable.put(ACTIVE, active);
		
		// put the transitions for FINISHED in the table
		transitionTable.put(FINISHED, Collections.<PlayerState> emptySet());
		
		// put the transitions for LOST in the table
		transitionTable.put(LOST, Collections.<PlayerState> emptySet());
		
		// put the transitions for WAITING in the table
		Set<PlayerState> lost = new HashSet<PlayerState>();
		lost.add(ACTIVE);
		transitionTable.put(WAITING, lost);
	}
}
