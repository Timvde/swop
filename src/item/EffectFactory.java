package item;

import item.lightgrenade.ExplodeEffect;
import item.teleporter.TeleportationEffect;
import powerfailure.PowerFailureEffect;
import item.lightgrenade.LightGrenade;

/**
 * Items can use this factory to get the needed {@link Effect effects}.
 */
public interface EffectFactory {
	
	/**
	 * Returns a new {@link ExplodeEffect}.
	 * 
	 * @param lightGrenade
	 *        the light grenade for the explode effect
	 * 
	 * @return A new Explode effect.
	 */
	ExplodeEffect getExplodeEffect(LightGrenade lightGrenade);
	
	/**
	 * Returns a new {@link TeleportationEffect}.
	 * 
	 * @return A new Teleportation effect.
	 */
	TeleportationEffect getTeleportationEffect();
	
	/**
	 * Returns a new {@link PowerFailureEffect}.
	 * 
	 * @return A new Powerfailure effect.
	 */
	PowerFailureEffect getPowerFailureEffect();
	
	/**
	 * Returns a new {@link EmptyEffect}.
	 * 
	 * @return A new empty effect.
	 */
	EmptyEffect getEmptyEffect();
	
}
