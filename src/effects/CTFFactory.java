package effects;

import item.lightgrenade.LightGrenade;
import item.teleporter.Teleporter;
import game.CTFMode;

/**
 * A factory returning {@link Effect effects} for the {@link CTFMode CTF game
 * mode}.
 * 
 */
public class CTFFactory extends AbtractEffectFactory {
	
	/**
	 * Returns an explode effect chained with a drop flag effect.
	 */
	@Override
	public ExplodeEffect getExplodeEffect(LightGrenade lightGrenade) {
		ExplodeEffect explodeEffect = super.getExplodeEffect(lightGrenade);
		explodeEffect.addEffect(new DropFlagEffect());
		return explodeEffect;
	}
	
	/**
	 * Returns a teleport effect chained with a drop flag effect.
	 */
	@Override
	public TeleportationEffect getTeleportationEffect(Teleporter teleporter) {
		TeleportationEffect teleportEffect = super.getTeleportationEffect(teleporter);
		teleportEffect.addEffect(new DropFlagEffect());
		return teleportEffect;
	}
	
}
