package square;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A Direction enumeration class.
 * 
 * 
 */
public enum Direction {
	
	@SuppressWarnings("javadoc")
	NORTH,
	
	@SuppressWarnings("javadoc")
	SOUTH,
	
	@SuppressWarnings("javadoc")
	EAST,
	
	@SuppressWarnings("javadoc")
	WEST,
	
	@SuppressWarnings("javadoc")
	NORTHEAST,
	
	@SuppressWarnings("javadoc")
	NORTHWEST,
	
	@SuppressWarnings("javadoc")
	SOUTHEAST,
	
	@SuppressWarnings("javadoc")
	SOUTHWEST;
	
	/** A list of the primary directions */
	private List<Direction>	primaryDirections;
	/** the opposite direction */
	private Direction		oppositeDirection;
	
	private Direction		nextClockwiseDirection;
	private Direction		nextCounterClockwiseDirection;
	
	/**
	 * Get a list of the primary directions. These are NORTH, EAST, SOUTH, WEST.
	 * 
	 * @return the primary directions
	 */
	public List<Direction> getPrimaryDirections() {
		return primaryDirections;
	}
	
	private void setNextClockwiseDirection(Direction d) {
		this.nextClockwiseDirection = d;
	}
	
	private void setNextCounterClockwiseDirection(Direction d) {
		this.nextCounterClockwiseDirection = d;
	}
	
	/**
	 * Return the direction that is the next direction seen clockwise.
	 */
	@SuppressWarnings("javadoc")
	public Direction getNextClockwiseDirection() {
		return this.nextClockwiseDirection;
	}
	
	/**
	 * Return the direction that is the next direction seen counterclockwise.
	 */
	@SuppressWarnings("javadoc")
	public Direction getNextCounterClockwiseDirection() {
		return this.nextCounterClockwiseDirection;
	}
	
	/**
	 * Set the primary directions.
	 */
	private void setPrimarydirections(List<Direction> directions) {
		this.primaryDirections = directions;
	}
	
	/**
	 * returns the opposite direction to this direction, e.g.
	 * <code> NORTH.getOppositeDirection() == SOUTH</code>
	 * 
	 * @return the opposite direction
	 */
	public Direction getOppositeDirection() {
		return oppositeDirection;
	}
	
	/**
	 * Return a random direction.
	 * 
	 * @return A random Direction.
	 */
	public static Direction getRandomDirection() {
		Random rnd = new Random();
		return Direction.values()[rnd.nextInt(8)];
	}
	
	/**
	 * Return adjacent directions of the given direction.
	 * 
	 * @param d
	 *        The direction we want the adjacent direction of.
	 * @return An array of length 2 that has the adjacent directions of the
	 *         given direction.
	 */
	public static Direction[] getAdjacentDirections(Direction d) {
		Direction[] result = new Direction[2];
		
		if (d == Direction.NORTH) {
			result[0] = Direction.NORTHWEST;
			result[1] = Direction.NORTHEAST;
		}
		if (d == Direction.EAST) {
			result[0] = Direction.NORTHEAST;
			result[1] = Direction.SOUTHEAST;
		}
		if (d == Direction.SOUTH) {
			result[0] = Direction.SOUTHEAST;
			result[1] = Direction.SOUTHWEST;
		}
		if (d == Direction.WEST) {
			result[0] = Direction.SOUTHWEST;
			result[1] = Direction.NORTHWEST;
		}
		if (d == Direction.NORTHEAST) {
			result[0] = Direction.NORTH;
			result[1] = Direction.EAST;
		}
		if (d == Direction.SOUTHEAST) {
			result[0] = Direction.EAST;
			result[1] = Direction.SOUTH;
		}
		if (d == Direction.SOUTHWEST) {
			result[0] = Direction.SOUTH;
			result[1] = Direction.WEST;
		}
		if (d == Direction.NORTHWEST) {
			result[0] = Direction.WEST;
			result[1] = Direction.NORTH;
		}
		
		return result;
	}
	
	private void setOppositeDirection(Direction direction) {
		this.oppositeDirection = direction;
	}
	
	// initialize the fields of the enum
	static {
		List<Direction> directions = new ArrayList<Direction>();
		directions.add(NORTH);
		NORTH.setPrimarydirections(new ArrayList<Direction>(directions));
		directions.add(EAST);
		NORTHEAST.setPrimarydirections(new ArrayList<Direction>(directions));
		directions.remove(NORTH);
		EAST.setPrimarydirections(new ArrayList<Direction>(directions));
		directions.add(SOUTH);
		SOUTHEAST.setPrimarydirections(new ArrayList<Direction>(directions));
		directions.remove(EAST);
		SOUTH.setPrimarydirections(new ArrayList<Direction>(directions));
		directions.add(WEST);
		SOUTHWEST.setPrimarydirections(new ArrayList<Direction>(directions));
		directions.remove(SOUTH);
		WEST.setPrimarydirections(new ArrayList<Direction>(directions));
		directions.add(NORTH);
		NORTHWEST.setPrimarydirections(new ArrayList<Direction>(directions));
		
		NORTH.setOppositeDirection(SOUTH);
		EAST.setOppositeDirection(WEST);
		SOUTH.setOppositeDirection(NORTH);
		WEST.setOppositeDirection(EAST);
		
		NORTHEAST.setOppositeDirection(SOUTHWEST);
		NORTHWEST.setOppositeDirection(SOUTHEAST);
		SOUTHEAST.setOppositeDirection(NORTHWEST);
		SOUTHWEST.setOppositeDirection(NORTHEAST);
		
		NORTH.setNextClockwiseDirection(NORTHEAST);
		NORTH.setNextCounterClockwiseDirection(NORTHWEST);
		NORTHEAST.setNextClockwiseDirection(EAST);
		NORTHEAST.setNextCounterClockwiseDirection(NORTH);
		EAST.setNextClockwiseDirection(SOUTHEAST);
		EAST.setNextCounterClockwiseDirection(NORTHEAST);
		SOUTHEAST.setNextClockwiseDirection(SOUTH);
		SOUTHEAST.setNextCounterClockwiseDirection(EAST);
		SOUTH.setNextClockwiseDirection(SOUTHWEST);
		SOUTH.setNextCounterClockwiseDirection(SOUTHEAST);
		SOUTHWEST.setNextClockwiseDirection(WEST);
		SOUTHWEST.setNextCounterClockwiseDirection(SOUTH);
		WEST.setNextClockwiseDirection(NORTHWEST);
		WEST.setNextCounterClockwiseDirection(SOUTHWEST);
		NORTHWEST.setNextClockwiseDirection(NORTH);
		NORTHWEST.setNextCounterClockwiseDirection(WEST);
	}
}
