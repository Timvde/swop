package item.lightgrenade;

import item.IItem;
import item.Item;
import square.SquareContainer;
import ObjectronExceptions.CannotPlaceLightGrenadeException;

/**
 * Light grenades are items that can be picked up and used by a player. There
 * are three states for a light grenade: <li>{@link LightGrenadeState#ACTIVE
 * ACTIVE}</li> <li>{@link LightGrenadeState#INACTIVE INACTIVE}</li> <li>
 * {@link LightGrenadeState#EXPLODED EXPLODED}.</li>
 * 
 * A light grenade always starts in the {@link LightGrenadeState#INACTIVE
 * INACTIVE} state. When the light grenade then gets used by a player the
 * internal state of this item will be changed to
 * {@link LightGrenadeState#ACTIVE ACTIVE}. The light grenade can now explode.
 * After exploding the light grenade converts to it's last state
 * {@link LightGrenadeState#EXPLODED EXPLODED}, this will convert the item to a
 * immutable object.
 * 
 */
public class LightGrenade extends Item {
	
	private LightGrenadeState	state;
	
	/**
	 * create a new light grenade, the state of this light grenade will be
	 * inactive.
	 */
	public LightGrenade() {
		state = LightGrenadeState.INACTIVE;
	}
	
	/**
	 * This method returns the current {@link LightGrenadeState} the grenade is
	 * in. (For testing purposes and GUI drawing)
	 * 
	 * @return the current {@link LightGrenadeState} the grenade is in.
	 */
	public LightGrenadeState getState() {
		return this.state;
	}
	
	/**
	 * This method sets the state of the grenade to {@link LightGrenadeState}
	 * .ACTIVE. It cannot be called until the player who dropped the
	 * lightgrenade and triggered it, left the square.
	 * 
	 * @throws IllegalStateException
	 *         The transition to the ACTIVE state must be valid from the current
	 *         state:
	 *         <code>this.getState().isAllowedTransistionTo(LightGrenadeState.ACTIVE)</code>
	 */
	private void enable() throws IllegalStateException {
		if (!this.state.isAllowedTransistionTo(LightGrenadeState.ACTIVE))
			throw new IllegalStateException("Illegal transition from " + this.state.toString()
					+ " to 'enabled'");
		
		this.state = LightGrenadeState.ACTIVE;
		System.out.println("state: " + this.state);
	}
	
	/**
	 * This method sets the state of the grenade to {@link LightGrenadeState}
	 * .EXPLODED
	 * 
	 * @throws IllegalStateException
	 *         The transition to the EXPLODED state must be valid from the
	 *         current state:
	 *         <code>this.getState().isAllowedTransistionTo(LightGrenadeState.EXPLODED)</code>
	 */
	void explode() throws IllegalStateException {
		if (!this.state.isAllowedTransistionTo(LightGrenadeState.EXPLODED))
			throw new IllegalStateException("Illegal transition from " + this.state.toString()
					+ " to 'exploded'");
		this.state = LightGrenadeState.EXPLODED;
	}
	
	@Override
	public String toString() {
		return "LightGrenade." + getId();
	}
	
	@Override
	public boolean isCarriable() {
		return this.state == LightGrenadeState.INACTIVE;
	}
	
	@Override
	public void use(SquareContainer square) throws CannotPlaceLightGrenadeException {
		// check if this light grenade can be added to the square
		for (IItem item : square.getAllItems())
			if (item instanceof LightGrenade)
				throw new CannotPlaceLightGrenadeException(
						"There is already a light grenade on the square");
		
		// try and add this light grenade to the square
		square.addItem(this);
		
		this.enable();
		
		System.out.println("Lightgrenade enabled.");
	}
	
	@Override
	public char toChar() {
		return 'l';
	}
	
	@Override
	public ExplodeEffect getEffect() {
		return new ExplodeEffect(this);
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
}
