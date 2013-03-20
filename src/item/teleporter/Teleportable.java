package item.teleporter;

import grid.square.ASquare;

/**
 * Indicates an object or item that can be teleported by a {@link Teleporter}
 * object.
 * 
 * @author Bavo Mees
 */
public interface Teleportable {
	
	/**
	 * Teleports the given object to a new specified square.
	 * 
	 * @param destination
	 *        the destination where the object should teleport to
	 */
	public void teleportTo(ASquare destination);
}
