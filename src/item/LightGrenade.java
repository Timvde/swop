package item;

import com.sun.istack.internal.NotNull;

public class LightGrenade extends Item implements ILightGrenade {

	// initial state is inactive
	@NotNull
	private LightGrenadeState state = LightGrenadeState.INACTIVE;

	@Override
	public LightGrenadeState getState() {
		return this.state;
	}

	@Override
	public void trigger() throws IllegalStateException {
		if (!this.state.isAllowedTransistionTo(LightGrenadeState.ACTIVE))
			throw new IllegalStateException("Illegal transition from "
					+ this.state.toString() + " to 'triggered'");
		this.state = LightGrenadeState.TRIGGERED;
	}

	/**
	 * This method sets the state of the grenade to {@link LightGrenadeState}
	 * .ACTIVE. It cannot be called until the player who dropped the
	 * lightgrenade and triggered it, left the square.
	 * 
	 * @throws IllegalStateException
	 *             The transition to the ACTIVE state must be valid from the
	 *             current state:
	 *             <code>this.getState().isAllowedTransistionTo(LightGrenadeState.ACTIVE)</code>
	 */
	void enable() throws IllegalStateException {
		if (!this.state.isAllowedTransistionTo(LightGrenadeState.ACTIVE))
			throw new IllegalStateException("Illegal transition from "
					+ this.state.toString() + " to 'enabled'");
		this.state = LightGrenadeState.ACTIVE;
	}

	/**
	 * This method sets the state of the grenade to {@link LightGrenadeState}
	 * .EXPLODED
	 * 
	 * @throws IllegalStateException
	 *             The transition to the EXPLODED state must be valid from the
	 *             current state:
	 *             <code>this.getState().isAllowedTransistionTo(LightGrenadeState.EXPLODED)</code>
	 */
	void explode() throws IllegalStateException {
		if (!this.state.isAllowedTransistionTo(LightGrenadeState.EXPLODED))
			throw new IllegalStateException("Illegal transition from "
					+ this.state.toString() + " to 'exploded'");
		this.state = LightGrenadeState.EXPLODED;
	}

	/**
	 * only inactive grenades can be carried
	 */
	@Override
	public boolean isCarriable() {
		return this.state == LightGrenadeState.INACTIVE;
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
				return (toState == this) || (toState == TRIGGERED);
			}
		},
		/**
		 * The grenade is dropped on a square by a player and will become active
		 * once the player leaves the square
		 */
		TRIGGERED {
			@Override
			public boolean isAllowedTransistionTo(LightGrenadeState toState) {
				return (toState == this) || (toState == ACTIVE);
			}
		},
		/**
		 * the grenade is armed
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
