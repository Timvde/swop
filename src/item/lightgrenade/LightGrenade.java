package item.lightgrenade;

import item.Effect;
import item.IItem;
import item.Item;
import square.ASquare;
import square.TronObject;
import com.sun.istack.internal.NotNull;

/**
 * Light grenades are items that can be picked up and used by a player. There
 * are three states for a light grenade: {@link LightGrenadeState#ACTIVE ACTIVE}
 * , {@link LightGrenadeState#INACTIVE INACTIVE},
 * {@link LightGrenadeState#EXPLODED EXPLODED}. A light grenade always starts in
 * the {@link LightGrenadeState#INACTIVE INACTIVE} state. When the light grenade
 * then gets used by a player the internal state of this item will be changed to
 * {@link LightGrenadeState#ACTIVE ACTIVE}. The light grenade can now explode
 * whenever the {@link #execute(TronObject)} method is called. After exploding the light
 * grenade converts to it's last state {@link LightGrenadeState#EXPLODED
 * EXPLODED}, this will convert the item to a immutable object.
 * 
 */
public class LightGrenade extends Item implements ILightGrenade {
	
	@NotNull
	private LightGrenadeState	state;
	private int 				damage;
	
	/**
	 * create a new light grenade, the state of this light grenade will be
	 * inactive.
	 */
	public LightGrenade() {
		state = LightGrenadeState.INACTIVE;
		damage = 3;
	}
	
	@Override
	public LightGrenadeState getState() {
		return this.state;
	}
	
	@Override
	public void enable() throws IllegalStateException {
		if (!this.state.isAllowedTransistionTo(LightGrenadeState.ACTIVE))
			throw new IllegalStateException("Illegal transition from " + this.state.toString()
					+ " to 'enabled'");
		this.state = LightGrenadeState.ACTIVE;
	}
	
	@Override
	public void execute(TronObject object) throws IllegalStateException {
		if (!this.state.isAllowedTransistionTo(LightGrenadeState.EXPLODED))
			throw new IllegalStateException("Illegal transition from " + this.state.toString()
					+ " to 'exploded'");
		this.state = LightGrenadeState.EXPLODED;
		
		object.asExplodable().skipNumberOfActions(damage);
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
	public void use(ASquare square) {
		
		// check if this light grenade can be added to the square
		for (IItem item : square.getAllItems())
			if (item instanceof LightGrenade)
				throw new IllegalStateException("There is already a light grenade on the square");
		
		// try and add this light grenade to the square 
		square.addItem(this);
		
		this.enable();
	}
	
	@Override
	public void addToEffect(Effect effect) {
		if (this.getState() == LightGrenadeState.ACTIVE) {
			effect.addItem(this);
		}
	}
	
	/**
	 * Increase the strength of a light grenade for when it explodes. By default, when a light grenade explode it will
	 * decrease the number of actions left by {@link Explodable} objects by three. This method will increase the strength of 
	 * a light grenade to four.   
	 */
	public void increaseStrength() {
		damage = 4;
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