package square;

import properties.powerfailure.PowerFailure;


/**
 * Objects having the AffectedByPowerFailure property can be damaged as result of a 
 * {@link PowerFailure}.
 * 
 * @author Bavo Mees
 */
public interface AffectedByPowerFailure {
	
	/**
	 * Inflict damage to the object as result of a power failure.
	 */
	public void damageByPowerFailure();
}
