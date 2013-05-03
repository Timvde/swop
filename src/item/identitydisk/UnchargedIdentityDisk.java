package item.identitydisk;

import item.Effect;
import item.EmptyEffect;
import square.AbstractSquare;
import square.AffectedByPowerFailure;
import square.Direction;
import square.SquareContainer;

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
public class UnchargedIdentityDisk extends IdentityDisk implements AffectedByPowerFailure {
	
	private int	range;
	
	/**
	 * create a new identity disk
	 */
	public UnchargedIdentityDisk() {
		range = 4;
	}
	
	@Override
	public void use(SquareContainer square) {
		super.use(square);
		range = 4;
	}
	
	@Override
	protected void moveDisk(Direction direction) {
		super.moveDisk(direction);
		range--;
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
	public boolean canMoveDisk(Direction direction) {
		// test if there is any range left to move
		if (range <= 0)
			return false;
		
		return super.canMoveDisk(direction);
	}
	
	@Override
	public boolean isCarriable() {
		return true;
	}
	
	@Override
	public String toString() {
		return "IdentityDisk." + getId();
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
	public char toChar() {
		return 'u';
	}

	@Override
	public Effect getEffect() {
		return new EmptyEffect();
	}
}
