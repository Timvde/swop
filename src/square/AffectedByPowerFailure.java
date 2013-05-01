package square;


/**
 * Objects having the AffectedByPowerFailure property can be damaged as result of a 
 * {@link PrimaryPowerFailure}.
 * 
 * @author Bavo Mees
 */
public interface AffectedByPowerFailure {
	
	/**
	 * Inflict damage to the object as result of a power failure.
	 */
	public void damageByPowerFailure();
}
