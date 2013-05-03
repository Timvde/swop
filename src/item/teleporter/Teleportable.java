package item.teleporter;

import square.SquareContainer;

/**
 * Indicates an object or item that can be teleported by a {@link Teleporter}
 * object.
 * 
 */
public interface Teleportable {
	
	/**
	 * Set the square the object is currently placed on. If this is not done
	 * correctly inconsistencies will happen.
	 * 
	 * @param square
	 * 		the new square where the teleportable will stand on 
	 * 			
	 */
	void setSquare(SquareContainer square);
}
