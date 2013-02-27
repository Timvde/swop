package item;

/**
 * An enumeration of the states a {@link LightGrenade} can have.
 */
public enum LightGrenadeState {
	
	/**
	 * Initial state, the grenade is disarmed (before pickup)
	 */
	DISABLED, 
	/**
	 * the grenade is armed (after dropdown)
	 */
	ENABLED, 
	/**
	 * the grenade is exploded. (It is worthless now)
	 */
	EXPLODED;

}
