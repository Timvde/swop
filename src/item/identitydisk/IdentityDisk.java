package item.identitydisk;

import item.Item;
import item.UseArguments;
import item.teleporter.Teleportable;
import java.util.ArrayList;
import java.util.List;
import square.Direction;
import square.SquareContainer;
import effects.DropFlagEffect;
import effects.Effect;

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
	
	@Override
	public void use(SquareContainer square, UseArguments<?> arguments) {
		if (!isValidUseArguments(square, arguments))
			throw new IllegalStateException(
					"The disk cannot be used when there is no direction set!");
		
		Direction direction = (Direction) arguments.getUserChoise();
		setSquare(square);
		
		if (!canMoveDisk(direction))
			// We can't move any further, so we'll have to drop here
			currentSquare.addItem(this);
		else
			while (canMoveDisk(direction)) {
				moveDisk(direction);
				// test if we have hit a player (and hit him if we have).
				// Also add a drop flag effect, which will take care of the
				// flag dropping
				if (currentSquare.hasPlayer()) {
					Effect effect = new IdentityDiskEffect();
					effect.addEffect(new DropFlagEffect());
					effect.execute(currentSquare.getPlayer());
					break;
				}
			}
		
		currentSquare = null;
	}
	
	/**
	 * Checks whether the arguments are valid.
	 */
	private boolean isValidUseArguments(SquareContainer square, UseArguments<?> arguments) {
		if (square == null)
			return false;
		if (arguments == null)
			return false;
		else if (arguments.getUserChoise() == null
				|| !(arguments.getUserChoise() instanceof Direction))
			return false;
		else
			return isValidDirection((Direction) arguments.getUserChoise());
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
		else if (!currentSquare.getNeighbourIn(direction).canAddItem(this))
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
		return direction.isPrimaryDirection();
	}
	
	public void setSquare(SquareContainer square) {
		this.currentSquare = square;
	}
	
	@Override
	public UseArguments<Direction> getUseArguments() {
		List<Direction> choices = new ArrayList<Direction>();
		choices.add(Direction.NORTH);
		choices.add(Direction.SOUTH);
		choices.add(Direction.EAST);
		choices.add(Direction.WEST);
		
		String question = "Please specify the direction in which the disc will be fired.";
		
		return new UseArguments<Direction>(choices, question);
	}
}
