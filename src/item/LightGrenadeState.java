package item;

/**
 * An enumeration of the states a {@link LightGrenade} can have and the allowed
 * transitions between them.
 */
public enum LightGrenadeState {

	/**
	 * Initial state, the grenade is disarmed (before pickup)
	 */
	INACTIVE {
		@Override
		public boolean isAllowedTransistionTo(LightGrenadeState toState) {
			return (toState == this) || (toState == WAITING_FOR_PLAYER_LEAVE);
		}
	},
	/**
	 * TODO
	 */
	WAITING_FOR_PLAYER_LEAVE {
		@Override
		public boolean isAllowedTransistionTo(LightGrenadeState toState) {
			return (toState == this) || (toState == ACTIVE);
		}
	},

	/**
	 * the grenade is armed (after dropdown)
	 */
	ACTIVE {
		@Override
		public boolean isAllowedTransistionTo(LightGrenadeState toState) {
			return (toState == this) || (toState == EXPLODED);
		}
	},
	/**
	 * the grenade is exploded. (It is worthless now)
	 */
	EXPLODED {
		@Override
		public boolean isAllowedTransistionTo(LightGrenadeState toState) {
			return toState == this;
		}
	};

	/**
	 * Returns whether or not a transistion from this state to a give state is
	 * allowed.
	 * 
	 * @param toState
	 *            the given other state
	 * @return whether or not a transistion from this state to a give state is
	 *         allowed.
	 */
	public abstract boolean isAllowedTransistionTo(LightGrenadeState toState);
}
