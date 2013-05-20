package item.lightgrenade;

/**
 * An enumeration of the states a {@link LightGrenade} can have and the
 * allowed transitions between them.
 */
public enum LightGrenadeState {
	
	/**
	 * Initial state, the grenade is disarmed (before pickup)
	 */
	INACTIVE {
		
		@Override
		public boolean isAllowedTransistionTo(LightGrenadeState toState) {
			return (toState == this) || (toState == ACTIVE);
		}
	},
	/**
	 * The grenade is armed. Hide your kids, hide your wives!
	 */
	ACTIVE {
		
		@Override
		public boolean isAllowedTransistionTo(LightGrenadeState toState) {
			return (toState == this) || (toState == EXPLODED);
		}
	},
	/**
	 * The grenade is exploded. (It is worthless now)
	 */
	EXPLODED {
		
		@Override
		public boolean isAllowedTransistionTo(LightGrenadeState toState) {
			return false;
		}
	};
	
	/**
	 * Returns whether or not a transition from this state to a give state
	 * is allowed.
	 * 
	 * @param toState
	 *        the given other state
	 * @return whether or not a transition from this state to a give state
	 *         is allowed.
	 */
	public abstract boolean isAllowedTransistionTo(LightGrenadeState toState);
}