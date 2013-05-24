package effects;

import game.CTFMode;
import item.lightgrenade.LightGrenade;
import item.teleporter.Teleporter;

/**
 * A factory returning {@link Effect effects} for the {@link CTFMode CTF game
 * mode}.
 * 
 */
public class CTFEffectFactory extends AbtractEffectFactory {
	
	/**
	 * Returns an explode effect chained with a drop flag effect.
	 */
	@Override
	public Effect getExplodeEffect(LightGrenade lightGrenade) {
		Effect explodeEffect = super.getExplodeEffect(lightGrenade);
		explodeEffect.addEffect(new DropFlagEffect());
		return explodeEffect;
	}
	
	/**
	 * Returns a teleport effect chained with a drop flag effect.
	 */
	@Override
	public Effect getTeleportationEffect(Teleporter teleporter) {
		/*
		 * flag should be dropped before going through the teleporter so we
		 * chain the dropflag first
		 */
		Effect result = new DropFlagEffect();
		result.addEffect(super.getTeleportationEffect(teleporter));
		return result;
	}
	
}
