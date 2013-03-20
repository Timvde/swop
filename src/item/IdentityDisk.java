package item;

import item.teleporter.Teleportable;
import grid.square.ASquare;
import grid.square.AffectedByPowerFailure;

/**
 * Identity disk can be launched by players on the grid. The disk will then
 * travel 4 squares in a specified direction. If this disk travels through a
 * square affected by a power failure, the disk will decrease its range. An
 * identity disk cannot travel trough walls or outside of the grid. When either
 * of those cases crosses the path of this disk, the disk will stop before the
 * wall or the end of the grid. When an identity disk hits a player that player
 * loses its turn, and the disk will stop at the position of the player that has
 * been hit by the disk.
 * 
 * @author Bavo Mees
 */
public class IdentityDisk extends Item implements Teleportable, AffectedByPowerFailure {
	
	private int range;
	
	/**
	 * create a new identity disk
	 */
	public IdentityDisk() {
		range = 4;
	}
	
	@Override
	public void use(ASquare square) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean isCarriable() {
		return true;
	}
	
	@Override
	public void teleportTo(ASquare destination) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		return "IdentityDisk." + getId();
	}
	
	/**
	 * returns this instance as a {@link Teleportable} object.
	 */
	@Override
	public Teleportable asTeleportable() {
		return this;
	}
	
	/**
	 * returns this instance as a {@link AffectedByPowerFailure} object.
	 */
	@Override
	public AffectedByPowerFailure asAffectedByPowerFailure() {
		return this;
	}

	@Override
	public void damageByPowerFailure() {
		range--;
	}
	
}
