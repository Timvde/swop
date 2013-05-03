package item.lightgrenade;

import item.Effect;
import item.IItem;
import item.Item;
import square.AbstractSquare;
import square.TronObject;
import ObjectronExceptions.CannotPlaceLightGrenadeException;

/**
 * Light grenades are items that can be picked up and used by a player. There
 * are three states for a light grenade: {@link LightGrenadeState#ACTIVE ACTIVE}
 * , {@link LightGrenadeState#INACTIVE INACTIVE},
 * {@link LightGrenadeState#EXPLODED EXPLODED}. A light grenade always starts in
 * the {@link LightGrenadeState#INACTIVE INACTIVE} state. When the light grenade
 * then gets used by a player the internal state of this item will be changed to
 * {@link LightGrenadeState#ACTIVE ACTIVE}. The light grenade can now explode
 * whenever the {@link #execute(TronObject)} method is called. After exploding
 * the light grenade converts to it's last state
 * {@link LightGrenadeState#EXPLODED EXPLODED}, this will convert the item to a
 * immutable object.
 * 
 */
public class LightGrenade extends Item {
	
	private LightGrenadeState	state;
	private int					damage;
	
	/**
	 * The default strength of a light grenade: the number of actions to skip
	 * for {@link Explodable} objects when it explodes.
	 */
	static final int			DEFAULT_STRENGTH	= 3;
	/**
	 * The enforced damage, when {@link #increaseStrength()} is called.
	 */
	static final int			INCREASED_STRENGHT	= 4;
	
	/**
	 * create a new light grenade, the state of this light grenade will be
	 * inactive.
	 */
	public LightGrenade() {
		state = LightGrenadeState.INACTIVE;
		damage = DEFAULT_STRENGTH;
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
	public void use(AbstractSquare square) throws CannotPlaceLightGrenadeException {
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
	
	/**
	 * Increase the strength of a light grenade (the damage it does when it
	 * explodes). By default, when a light grenade explode it will decrease the
	 * number of actions left for {@link Explodable} objects by
	 * {@value #DEFAULT_STRENGTH}. This method will increase the strength of a
	 * light grenade to {@value #INCREASED_STRENGHT}.
	 */
	public void increaseStrength() {
		damage = INCREASED_STRENGHT;
		
		System.out.println("Lightgrenade set to increased strength.");
	}
	
	@Override
	public char toChar() {
		return 'l';
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

	@Override
	public Effect getEffect() {
		return new ExplodeEffect(this);
	}
}
