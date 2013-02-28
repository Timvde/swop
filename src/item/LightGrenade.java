package item;

import com.sun.istack.internal.NotNull;

public class LightGrenade extends Item implements ILightGrenade {

	@NotNull
	private LightGrenadeState state = LightGrenadeState.INACTIVE; // initial
																	// state

	@Override
	public LightGrenadeState getState() {
		return this.state;
	}

	public void explode() throws IllegalStateException {
		if (!this.state.isAllowedTransistionTo(LightGrenadeState.EXPLODED))
			throw new IllegalStateException();
		this.state = LightGrenadeState.EXPLODED;
	}

	public void enable() throws IllegalStateException {
		if (!this.state.isAllowedTransistionTo(LightGrenadeState.ACTIVE))
			throw new IllegalStateException();
		this.state = LightGrenadeState.ACTIVE;
	}

	public void use() throws IllegalStateException {
		if (!this.state
				.isAllowedTransistionTo(LightGrenadeState.WAITING_FOR_PLAYER_LEAVE))
			throw new IllegalStateException();
		this.state = LightGrenadeState.WAITING_FOR_PLAYER_LEAVE;
	}

	@Override
	public boolean isCarriable() {
		// exploded grenades cannot be carried
		return this.state != LightGrenadeState.EXPLODED;
	}
	
	/************************* LigthGrenadeEnum *************************/

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
				return (toState == this)
						|| (toState == WAITING_FOR_PLAYER_LEAVE);
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
		 * Returns whether or not a transistion from this state to a give state
		 * is allowed.
		 * 
		 * @param toState
		 *            the given other state
		 * @return whether or not a transistion from this state to a give state
		 *         is allowed.
		 */
		public abstract boolean isAllowedTransistionTo(LightGrenadeState toState);
	}
}
