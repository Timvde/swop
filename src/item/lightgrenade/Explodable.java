package item.lightgrenade;

/**
 * Objects having the explodable property can be affected by the explosion of a
 * {@link LightGrenade}
 * 
 * @author Bavo Mees
 */
public interface Explodable {
	
	/**
	 * skip a number of a number of actions because of the explosion of a light
	 * grenade
	 * 
	 * @param number
	 *        the number of actions to skip
	 */
	public void skipNumberOfActions(int number);
}
