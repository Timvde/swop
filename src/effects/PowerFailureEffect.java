package effects;

import square.TronObject;

/**
 * The effect a power failure has on an {@link TronObject object}
 * 
 */
public class PowerFailureEffect extends AbstractEffect {
	
	private boolean	execute;
	
	PowerFailureEffect() {
		this.execute = true;
	}
	
	@Override
	public void preExecutionHook(TronObject object) {
		if (execute && object.asAffectedByPowerFailure() != null) {
			object.asAffectedByPowerFailure().damageByPowerFailure();
		}
	}
	
	@Override
	public void addEffect(Effect effect) {
		
		// test if we can modify the effect
		if (effect instanceof ExplodeEffect) {
			((ExplodeEffect) effect).increaseStrength();
			this.execute = false;
		}
		
		super.addEffect(effect);
	}
}
