package item.lightgrenade;

import square.TronObject;
import item.IItem;
import item.lightgrenade.LightGrenade.LightGrenadeState;

/**
 * A light grenade always is in a {@link LightGrenadeState}. Initially in the
 * INACTIVE state. Only inactive grenades can be carried.
 * 
 * A player may only 'trigger' a light grenade, which means the grenade will
 * become 'active', once the player leaves the square. When a player enters a
 * square with an active light grenade, it will go in the 'exploded' state.
 * 
 * TODO remove this interface?
 */
public interface ILightGrenade extends IItem {

	/**
	 * This method returns the current {@link LightGrenadeState} the grenade is
	 * in.
	 * 
	 * @return the current {@link LightGrenadeState} the grenade is in.
	 */
	public LightGrenadeState getState();

//	/**
//	 * This method sets the state of the grenade to {@link LightGrenadeState}
//	 * .TRIGGERED
//	 * 
//	 * @throws IllegalStateException
//	 *             The transition to the TRIGGERED state must be valid from the
//	 *             current state:
//	 *             <code>this.getState().isAllowedTransistionTo(LightGrenadeState.WAITING_FOR_PLAYER_LEAVE)</code>
//	 * 
//	 */
//	public void trigger() throws IllegalStateException;

	
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
	 * Let the light grenade explode. This will set the internal state of the light grenade to 
	 * exploded. If the specified element can be affected by an {@link Explodable explosion}, the 
	 * object will suffer from this explosion. 
	 * 
	 * @throws IllegalStateException
	 *             The transition to the EXPLODED state must be valid from the
	 *             current state:
	 *             <code>this.getState().isAllowedTransistionTo(LightGrenadeState.EXPLODED)</code>
	 */
	public void execute(TronObject object) throws IllegalStateException;

}
