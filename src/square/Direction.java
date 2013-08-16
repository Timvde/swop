package square;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A Direction enumeration class. Source for comments:
 * {@link "http://en.wikipedia.org"}
 */
public enum Direction {
	
	/**
	 * North is a noun, adjective, or adverb indicating direction or geography.
	 * 
	 * North is one of the four cardinal directions or compass points. It is the
	 * opposite of south and is perpendicular to east and west.
	 * 
	 * By convention, the top side of a map is often north.
	 */
	NORTH(true),
	
	/**
	 * Northeast or north east is the ordinal direction halfway between north
	 * and east. It is the opposite of southwest.
	 */
	NORTHEAST(false),
	
	/**
	 * East is a noun, adjective, or adverb indicating direction or geography.
	 * 
	 * East is one of the four cardinal directions or compass points. It is the
	 * opposite of west and is perpendicular to north and south.
	 * 
	 * By convention, the right hand side of a map is often east.
	 */
	EAST(true),
	
	/**
	 * Southeast or south east is the ordinal direction halfway between south
	 * and east. It is the opposite of northwest.
	 */
	SOUTHEAST(false),
	
	/**
	 * South is a noun, adjective, or adverb indicating direction or geography.
	 * 
	 * South is one of the four cardinal directions or compass points. It is the
	 * polar opposite of north and is perpendicular to east and west.
	 * 
	 * By convention, the bottom side of a map is often south.
	 */
	SOUTH(true),
	
	/**
	 * Southwest or south west is the ordinal direction halfway between south
	 * and west on a compass. It is the opposite of northeast.
	 */
	SOUTHWEST(false),
	
	/**
	 * West is a noun, adjective, or adverb indicating direction or geography.
	 * 
	 * West is one of the four cardinal directions or compass points. It is the
	 * opposite of east and is perpendicular to north and south.
	 * 
	 * By convention, the left hand side of a map is often west.
	 */
	WEST(true),
	
	/**
	 * Northwest or north west is the ordinal direction halfway between north
	 * and west on a compass. It is the opposite of southeast.
	 */
	NORTHWEST(false);
	
	private boolean	isPrimaryDirection;
	
	private Direction(boolean isPrimaryDirection) {
		this.isPrimaryDirection = isPrimaryDirection;
	}
	
	/**
	 * Returns whether or not this direction is primary. Primary directions are:
	 * are NORTH, EAST, SOUTH, WEST.
	 * 
	 * @return whether or not this direction is primary.
	 */
	public boolean isPrimaryDirection() {
		return isPrimaryDirection;
	}
	
	/**
	 * Returns the primary directions neighbouring this direction, or the
	 * direction itself if it is a {@link #isPrimaryDirection primary direction}.
	 * 
	 * @return the primary directions neighbouring this direction or itself if
	 *         it a primary direction
	 */
	@SuppressWarnings("javadoc")
	public List<Direction> getPrimaryDirections() {
		List<Direction> primaryDirections = new ArrayList<Direction>();
		if (isPrimaryDirection)
			primaryDirections.add(this);
		else
			primaryDirections.addAll(getAdjacentDirections());
		return primaryDirections;
	}
	
	/**
	 * Return the direction that is the next direction seen clockwise.
	 * 
	 * @return The next clockwise direction
	 */
	public Direction getNextClockwiseDirection() {
		return Direction.values()[(this.ordinal() + Direction.values().length + 1)
				% Direction.values().length];
	}
	
	/**
	 * Return the directions that are adjacent to this direction.
	 * 
	 * @return The adjacent directions
	 */
	public List<Direction> getAdjacentDirections() {
		List<Direction> adjacentDirections = new ArrayList<Direction>();
		adjacentDirections.add(getNextClockwiseDirection());
		adjacentDirections.add(getNextCounterClockwiseDirection());
		return adjacentDirections;
	}
	
	/**
	 * Return the direction that is the next direction seen counterclockwise.
	 * 
	 * @return The next counterclockwise direction
	 */
	public Direction getNextCounterClockwiseDirection() {
		return Direction.values()[(this.ordinal() + Direction.values().length - 1)
				% Direction.values().length];
	}
	
	/**
	 * Returns the opposite direction to this direction, e.g.
	 * <code> NORTH.getOppositeDirection() == SOUTH</code>
	 * 
	 * @return the opposite direction
	 */
	public Direction getOppositeDirection() {
		int size = Direction.values().length;
		return Direction.values()[(this.ordinal() + size / 2) % size];
	}
	
	/**
	 * Return a random direction.
	 * 
	 * @return A random Direction.
	 */
	public static Direction getRandomDirection() {
		Random rnd = new Random();
		return Direction.values()[rnd.nextInt(Direction.values().length)];
	}
}
