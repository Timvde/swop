package square;

import item.lightgrenade.LightGrenadeEffect;
import effect.Effect;

/**
 * The effect a power failure has on an {@link TronObject object}
 *
 */
public class PowerFailureEffect implements Effect {
	
	private LightGrenadeEffect	effect;


	PowerFailureEffect(LightGrenadeEffect effect) {
		this.effect = effect;
	}
	
	
	@Override
	public void execute(TronObject object) {
		if (effect == null) {
			if (object.asAffectedByPowerFailure() != null)
				object.asAffectedByPowerFailure().damageByPowerFailure();
		}
		else {
			effect.increaseStrength();
			effect.execute(object);
		}
	}
}
