package item;

import item.LightGrenade.LightGrenadeState;

import org.omg.PortableInterceptor.INACTIVE;

/**
 * A grenade can be enabled en exploded. A lightgrenade always is in a
 * {@link LightGrenadeState}. Initially in the {@link INACTIVE} state. Only
 * inactive grenades can be carried.
 * 
 */
public interface ILightGrenade {

	/**
	 * FIXME kopie returnen?? of verschillende boolean inspectors??
	 * 
	 * @return
	 */
	LightGrenadeState getState();

	/**
	 * This method sets the state of the grenade to {@link LightGrenadeState}
	 * .TRIGGERED
	 * 
	 * @throws IllegalStateException
	 *             The transition to the TRIGGERED state must be valid from the
	 *             current state:
	 *             <code>this.getState().isAllowedTransistionTo(LightGrenadeState.WAITING_FOR_PLAYER_LEAVE)</code>
	 */
	public void trigger() throws IllegalStateException;
}
