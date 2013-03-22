package item.identitydisk;

import square.ASquare;
import square.AffectedByPowerFailure;
import square.ISquare;
import square.Square;
import square.TronObject;
import item.Item;
import item.teleporter.Teleportable;

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
	private Square currentSquare;
	
	/**
	 * create a new identity disk
	 */
	public IdentityDisk() {
		range = 4;
	}
	
	@Override
	public void use(ISquare square) {
		// set the location of the current square
		currentSquare = (Square) square;
		
		// move the disk as long as it can ... 
		while (range > 0) {
			//TODO 
		}
	}
	
	@Override
	public boolean isCarriable() {
		return true;
	}
	
	@Override
	public void teleportTo(ASquare destination) {
		// remove the disk from the current square
		currentSquare.remove(this);
		// set the destination to the current square
		currentSquare = (Square) destination;
		// add the disk to a new square 
		currentSquare.addItem(this);
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

	@Override
	public void execute(TronObject object) {
		// the object does nothing when an object/player steps on an identity disk
	}
	
}
