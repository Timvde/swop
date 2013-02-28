package item;

import org.omg.PortableInterceptor.INACTIVE;

import ObjectronExceptions.IllegalGrenadeStateTranisitionException;

/**
 * A grenade-item that can be enabled en exploded. A lightgrenade always is in a
 * {@link LightGrenadeState}. Initially in the {@link INACTIVE} state. Exploded
 * grenades cannot be carried.
 * 
 */
public interface ILightGrenade {

	/**
	 * FIXME kopie returnen?? of verschillende boolean inspectors??
	 * 
	 * @return
	 */
	public LightGrenadeState getState();
	
	/**
	 * This method sets the state of the grenade to {@link LightGrenadeState}
	 * .EXPLODED
	 * 
	 * @throws IllegalGrenadeStateTranisitionException
	 *             The transition to the EXPLODED state must be valid from the
	 *             current state:
	 *             <code>this.getState().isAllowedTransistionTo(LightGrenadeState.EXPLODED)</code>
	 */
	public void explode() throws IllegalGrenadeStateTranisitionException;

	/**
	 * This method sets the state of the grenade to {@link LightGrenadeState}
	 * .ACTIVE
	 * 
	 * @throws IllegalGrenadeStateTranisitionException
	 *             The transition to the ACTIVE state must be valid from the
	 *             current state:
	 *             <code>this.getState().isAllowedTransistionTo(LightGrenadeState.ACTIVE)</code>
	 */
	public void enable() throws IllegalGrenadeStateTranisitionException;

	/**
	 *  This method sets the state of the grenade to {@link LightGrenadeState}
	 * .WAITING_FOR_PLAYER_LEAVE
	 * 
	 * @throws IllegalGrenadeStateTranisitionException
	 *             The transition to the WAITING_FOR_PLAYER_LEAVE state must be valid from the
	 *             current state:
	 *             <code>this.getState().isAllowedTransistionTo(LightGrenadeState.WAITING_FOR_PLAYER_LEAVE)</code>
	 */
	public void use() throws IllegalGrenadeStateTranisitionException;
}
