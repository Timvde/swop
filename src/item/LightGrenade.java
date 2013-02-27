package item;

import com.sun.istack.internal.NotNull;

/**
 * A class representing a grenade-item that can be enabled en exploded.
 * 
 */
public class LightGrenade extends Item {
	
	@NotNull
	private LightGrenadeState state = LightGrenadeState.DISABLED;

	/**
	 * TODO kopie returnen??
	 * @return
	 */
	public LightGrenadeState getState() {
		return this.state;
	}

	/**
	 * This method sets the state of the grenade to {@link LightGrenadeState}
	 * .EXPLODED
	 */
	public void explode() {
		this.state = LightGrenadeState.EXPLODED;
	}

	/**
	 * This method sets the state of the grenade to {@link LightGrenadeState}
	 * .ENABLED
	 * 
	 * TODO invar: alleen enablen als op vakje neergelegd...
	 */
	public void enable() {
		this.state = LightGrenadeState.ENABLED;
	}

	@Override
	public boolean isCarriable() {
		//exploded grenades cannot be carried
		return this.state != LightGrenadeState.EXPLODED;
	}

}
