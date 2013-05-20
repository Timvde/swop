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
	
	private ArrayList<Direction>		adjacentDirections = new ArrayList<Direction>();
	
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
	public Direction getNextClockwiseDirection() {
		return this.nextClockwiseDirection;
	}
	
	/**
	 * Return the directions that are adjacent to this direction.
	 * This vector only has 2 elements.
	 */
	public ArrayList<Direction> getAdjacentDirections() {
		return this.adjacentDirections;
	}
	
	private void setFirstAdjacentDirection(Direction d) {
		this.adjacentDirections.add(0, d);
	}
	
	private void setSecondAdjacentDirection(Direction d) {
		this.adjacentDirections.add(1, d);
	}
	
	/**
	 * Return the direction that is the next direction seen counterclockwise.
	 */
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
	
	private void setOppositeDirection(Direction direction) {
		this.oppositeDirection = direction;
	}
	
	// Initialize the fields of the enum
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
		
		NORTH.setFirstAdjacentDirection(Direction.NORTHWEST);
		NORTH.setSecondAdjacentDirection(Direction.NORTHEAST);
		
		EAST.setFirstAdjacentDirection(Direction.NORTHEAST);
		EAST.setSecondAdjacentDirection(Direction.SOUTHEAST);
		
		SOUTH.setFirstAdjacentDirection(Direction.SOUTHEAST);
		SOUTH.setSecondAdjacentDirection(Direction.SOUTHWEST);
		
		WEST.setFirstAdjacentDirection(Direction.SOUTHWEST);
		WEST.setSecondAdjacentDirection(Direction.NORTHWEST);
		
		NORTHEAST.setFirstAdjacentDirection(Direction.NORTH);
		NORTHEAST.setSecondAdjacentDirection(Direction.EAST);
		
		SOUTHEAST.setFirstAdjacentDirection(Direction.EAST);
		SOUTHEAST.setSecondAdjacentDirection(Direction.SOUTH);
		
		SOUTHWEST.setFirstAdjacentDirection(Direction.SOUTH);
		SOUTHWEST.setSecondAdjacentDirection(Direction.WEST);
		
		NORTHWEST.setFirstAdjacentDirection(Direction.WEST);
		NORTHWEST.setSecondAdjacentDirection(Direction.NORTH);
	}
}
