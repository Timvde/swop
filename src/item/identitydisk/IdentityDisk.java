package item.identitydisk;

import item.Effect;
import item.Item;
import item.teleporter.Teleportable;
import square.Direction;
import square.SquareContainer;

/**
 * Charged Identity disk can be launched by players on the grid. The disk will
 * then travel until it hits a player, wall or reaches the end of the board. An
 * identity disk cannot travel trough walls or outside of the grid. When either
 * of those cases crosses the path of this disk, the disk will stop before the
 * wall or the end of the grid. When an identity disk hits a player that player
 * loses its turn, and the disk will stop at the position of the player that has
 * been hit by the disk.
 * 
 */
public abstract class IdentityDisk extends Item implements Teleportable {
	
	private SquareContainer	currentSquare;
	private Direction		direction;
	
	@Override
	public void use(SquareContainer square) {
		if (direction == null)
			throw new IllegalStateException(
					"The disk cannot be used when there is no direction set!");
		
		setSquare(square);
		
		if (!canMoveDisk(direction))
			// We can't move any further, so we'll have to drop here
			currentSquare.addItem(this);
		else
			while (canMoveDisk(direction)) {
				moveDisk(direction);
				// test if we have hit a player (and hit him if we have)
				if (currentSquare.hasPlayer()) {
					Effect effect = new IdentityDiskEffect();
					effect.execute(currentSquare.getPlayer());
					break;
				}
			}
		
		// reset the direction field
		direction = null;
		currentSquare = null;
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
		if (currentSquare.getNeighbourIn(direction) == null)
			return false;
		else if (!currentSquare.getNeighbourIn(direction).canBeAdded(this))
			return false;
		return true;
	}
	
	/**
	 * move the identity disk in the specified direction
	 * 
	 * @param direction
	 *        the direction in which this disk should win
	 */
	protected void moveDisk(Direction direction) {
		if (!canMoveDisk(direction))
			throw new IllegalStateException("something happend, good look finding it ...");
		currentSquare.remove(this);
		currentSquare = currentSquare.getNeighbourIn(direction);
		currentSquare.addItem(this);
	}
	
	@Override
	public boolean isCarriable() {
		return true;
	}
	
	@Override
	public String toString() {
		return "ChargedIdentityDisk." + getId();
	}
	
	/**
	 * returns this instance as a {@link Teleportable} object.
	 */
	@Override
	public Teleportable asTeleportable() {
		return this;
	}
	
	/**
	 * Check if the given direction is a valid direction in which an ID can be
	 * shot in.
	 */
	private boolean isValidDirection(Direction direction) {
		if (direction == null)
			return false;
		
		if (direction == Direction.NORTHEAST)
			return false;
		
		if (direction == Direction.SOUTHEAST)
			return false;
		
		if (direction == Direction.SOUTHWEST)
			return false;
		
		if (direction == Direction.NORTHWEST)
			return false;
		
		return true;
	}
	
	/**
	 * Set the direction in which the disk will be fired.
	 * 
	 * @param direction
	 *        the direction in which the disk will be fired.
	 * @throws IllegalArgumentException
	 *         If the given direction is not a valid direction.
	 */
	public void setDirection(Direction direction) {
		if (!isValidDirection(direction))
			throw new IllegalArgumentException("The direction is not a valid direction.");
		
		this.direction = direction;
	}
	
	public void setSquare(SquareContainer square) {
		this.currentSquare = square;
	}
	
}
