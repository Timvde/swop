package item.identitydisk;

import item.Item;
import item.teleporter.Teleportable;
import square.ASquare;
import square.AffectedByPowerFailure;
import square.Direction;
import square.Square;
import square.TronObject;

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
	
	private int		range;
	private ASquare	currentSquare;
	
	/**
	 * create a new identity disk
	 */
	public IdentityDisk() {
		range = 4;
	}
	
	@Override
	public void use(ASquare square, Direction direction) {
		// set the location of the current square
		currentSquare = square;
		
		// move the disk as long as it can ...
		while (canMoveDisk(direction)) {
			moveDisk(direction);
			// test if we have hit a player (and hit him if we have)
			if (currentSquare.hasPlayer()) {
				currentSquare.getPlayer().asExplodable().skipNumberOfActions(3);
				break;
			}
		}
	}
	
	/**
	 * Test whether the disk can be moved in the specified direction, a square
	 * can be moved in a direction when it has any range left, if the current
	 * square has a neighbour in the specified direction and the disk can be
	 * added to the neighbour of the specified direction.
	 * 
	 * @param direction
	 *        the direction to test
	 * @return true if the disk can move in the specified direction, else false
	 */
	private boolean canMoveDisk(Direction direction) {
		// test if there is any range left to move
		if (range <= 0)
			return false;
		// test whether the square has a neighbour in the given direction
		else if (currentSquare.getNeighbour(direction) == null)
			return false;
		// test whether the disk can be added to the neighbour
		else if (!currentSquare.getNeighbour(direction).canBeAdded(this))
			return false;
		// looks like the item can be added
		return true;
	}
	
	private void moveDisk(Direction direction) {
		if (!canMoveDisk(direction))
			throw new IllegalStateException("something happend, good look finding it ...");
		// remove the item from the current square
		currentSquare.remove(this);
		// set the neighbour as the current square
		currentSquare = currentSquare.getNeighbour(direction);
		// add the square to the (new) current square
		currentSquare.addItem(this);
	}
	
	@Override
	public boolean isCarriable() {
		return true;
	}
	
	@Override
	public void teleportTo(ASquare destination) {
		// remove the disk from the current square
		if (currentSquare != null)
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
		// the object does nothing when an object/player steps on an identity
		// disk
	}	
}
