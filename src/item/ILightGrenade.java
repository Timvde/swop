package item;

import item.LightGrenade.LightGrenadeState;

/**
 * A lightgrenade always is in a {@link LightGrenadeState}. Initially in the
 * INACTIVE state. Only inactive grenades can be carried.
 * 
 * A player may only 'trigger' a lightgrenade, which means the grenade will
 * become 'active', once the player leaves the square. When a player enters a
 * square with an active lightgrenade, it will go in the 'exploded' state.
 */
public interface ILightGrenade {

	/**
	 * This method returns the current {@link LightGrenadeState} the grenade is
	 * in.
	 * 
	 * @return the current {@link LightGrenadeState} the grenade is in.
	 */
	public LightGrenadeState getState();

	/**
	 * This method sets the state of the grenade to {@link LightGrenadeState}
	 * .TRIGGERED
	 * 
	 * @throws IllegalStateException
	 *             The transition to the TRIGGERED state must be valid from the
	 *             current state:
	 *             <code>this.getState().isAllowedTransistionTo(LightGrenadeState.WAITING_FOR_PLAYER_LEAVE)</code>
	 * 
	 * FIXME <br><br><b>NOTE: Do not call this method directly. Instead use</b> 
	 */
	public void trigger() throws IllegalStateException;

	/**
	 * This method sets the state of the grenade to {@link LightGrenadeState}
	 * .ACTIVE. It cannot be called until the player who dropped the
	 * lightgrenade and triggered it, left the square.
	 * 
	 * @throws IllegalStateException
	 *             The transition to the ACTIVE state must be valid from the
	 *             current state:
	 *             <code>this.getState().isAllowedTransistionTo(LightGrenadeState.ACTIVE)</code>
	 */
	public void enable() throws IllegalStateException;

	/**
	 * This method sets the state of the grenade to {@link LightGrenadeState}
	 * .EXPLODED
	 * 
	 * @throws IllegalStateException
	 *             The transition to the EXPLODED state must be valid from the
	 *             current state:
	 *             <code>this.getState().isAllowedTransistionTo(LightGrenadeState.EXPLODED)</code>
	 */
	public void explode() throws IllegalStateException;

}
