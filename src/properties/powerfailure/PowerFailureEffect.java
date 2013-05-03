package properties.powerfailure;

import square.TronObject;
import item.Effect;
import item.lightgrenade.ExplodeEffect;

/**
 * The effect a power failure has on an {@link TronObject object}
 * 
 */
public class PowerFailureEffect implements Effect {
	
	private boolean	execute;
	private Effect	next;
	
	PowerFailureEffect() {
		this.execute = true;
	}
	
	@Override
	public void execute(TronObject object) {
		if (execute && object.asAffectedByPowerFailure() != null) {
			object.asAffectedByPowerFailure().damageByPowerFailure();
		}
		
		if (next != null)
			next.execute(object);
	}
	
	@Override
	public void addEffect(Effect effect) {
		
		// test if we can modify the effect
		if (effect instanceof ExplodeEffect) {
			((ExplodeEffect) effect).increaseStrength();
			this.execute = false;
		}
		
		if (next == null)
			this.next = effect;
		else
			next.addEffect(effect);
	}
}
