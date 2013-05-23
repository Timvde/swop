package effects;

import item.lightgrenade.LightGrenade;
import item.teleporter.Teleporter;
import effects.Effect;
import effects.EmptyEffect;
import effects.ExplodeEffect;
import effects.PowerFailureEffect;
import effects.TeleportationEffect;

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
	Effect getExplodeEffect(LightGrenade lightGrenade);
	
	/**
	 * Returns a new {@link TeleportationEffect}.
	 * 
	 * @param teleporter
	 *        the teleporter for the teleportation effect
	 * 
	 * @return A new Teleportation effect.
	 */
	Effect getTeleportationEffect(Teleporter teleporter);
	
	/**
	 * Returns a new {@link PowerFailureEffect}.
	 * 
	 * @return A new Powerfailure effect.
	 */
	Effect getPowerFailureEffect();
	
	/**
	 * Returns a new {@link EmptyEffect}.
	 * 
	 * @return A new empty effect.
	 */
	Effect getEmptyEffect();
	
}
