package grid;

import item.lightgrenade.Explodable;
import item.teleporter.Teleportable;

/**
 * An object that can be placed on the grid of a TRON game. These objects have
 * the ability to have several properties, and can be tested by special methods,
 * e.g. {@link #asTeleportable()}.
 * 
 * @author Bavo Mees
 */
public interface TronObject {
	
	/**
	 * Returns the same instance of the object as a {@link Teleportable} object
	 * or null if the object does not contain the teleportable property.
	 * 
	 * @return a teleportable
	 */
	public Teleportable asTeleportable();
	
	/**
	 * Returns the same instance of the object as an {@link Explodable} object
	 * or null if the object does not contain the explodable property.
	 * 
	 * @return an explodable
	 */
	public Explodable asExplodable();
	
	/**
	 * Returns the same instance of the object as an {@link AffectedByPowerFailure} object
	 * or null if the object does not contain the AffectedByPowerFailure property.
	 * 
	 * @return an AffectedByPowerFailure
	 */
	public AffectedByPowerFailure asAffectedByPowerFailure();
}
