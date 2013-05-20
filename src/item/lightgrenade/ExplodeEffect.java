package item.lightgrenade;

import item.AbstractEffect;
import square.TronObject;

/**
 * An explosion effect
 * 
 */
public class ExplodeEffect extends AbstractEffect {
	
	private static final int	DEFAULT_DAMAGE		= 3;
	private static final int	INCREASED_DAMAGE	= 4;
	
	private LightGrenade		lightGrenade;
	private int					damage;
	
	ExplodeEffect(LightGrenade lightGrenade) {
		this.lightGrenade = lightGrenade;
		this.damage = DEFAULT_DAMAGE;
	}
	
	@Override
	public void execute(TronObject object) {
		if (canExecuteEffect(object)) {
			lightGrenade.explode();
			object.asExplodable().skipNumberOfActions(damage);
		}
		
		super.execute(object);
	}
	
	/**
	 * Returns whether this effect can execute its effect on the specified
	 * object. The effect can be executed if the light grenade is active and if
	 * the object can {@link Explodable explode}.
	 * 
	 * @param object
	 *        The object on which the effect would be executed
	 * @return True if the effect can be executed on the specified object
	 */
	private boolean canExecuteEffect(TronObject object) {
		if (!lightGrenade.getState().isAllowedTransistionTo(LightGrenadeState.EXPLODED))
			return false;
		else if (object.asExplodable() == null)
			return false;
		else
			return true;
	}
	
	/**
	 * Increase the damage this effect will have on an object. 
	 */
	public void increaseStrength() {
		this.damage = INCREASED_DAMAGE;
	}
}
