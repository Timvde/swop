package effects;

import item.lightgrenade.LightGrenade;
import item.teleporter.Teleporter;

/**
 * An abstract implementation of the {@link EffectFactory} interface to minimize
 * the effort required to implement this interface. All the methods in this
 * class will return the standard effects. Extending classes should override the
 * methods they want to change the behavior of.
 * 
 */
public class AbtractEffectFactory implements EffectFactory {
	
	@Override
	public ExplodeEffect getExplodeEffect(LightGrenade lightGrenade) {
		return new ExplodeEffect(lightGrenade);
	}
	
	@Override
	public TeleportationEffect getTeleportationEffect(Teleporter teleporter) {
		return new TeleportationEffect(teleporter);
	}
	
	@Override
	public PowerFailureEffect getPowerFailureEffect() {
		return new PowerFailureEffect();
	}
	
	@Override
	public EmptyEffect getEmptyEffect() {
		return new EmptyEffect();
	}
	
}
