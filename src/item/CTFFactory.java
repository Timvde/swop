package item;

import game.CTFMode;
import item.lightgrenade.ExplodeEffect;
import item.lightgrenade.LightGrenade;
import item.teleporter.TeleportationEffect;
import powerfailure.PowerFailureEffect;

/**
 * A factory returning {@link Effect effects} for the {@link CTFMode CTF game
 * mode}.
 * 
 */
public class CTFFactory implements EffectFactory {
	
	@Override
	public ExplodeEffect getExplodeEffect(LightGrenade lightGrenade) {
		return new ExplodeEffect(lightGrenade);
	}
	
	@Override
	public TeleportationEffect getTeleportationEffect() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public PowerFailureEffect getPowerFailureEffect() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public EmptyEffect getEmptyEffect() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
