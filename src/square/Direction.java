package square;

import java.util.ArrayList;
import java.util.List;

/**
 * A Direction enumeration class.
 * 
 * 
 */
public enum Direction {
	
	@SuppressWarnings("javadoc")
	NORTH ,
	
	@SuppressWarnings("javadoc")
	SOUTH,
	
	@SuppressWarnings("javadoc")
	EAST,
	
	@SuppressWarnings("javadoc")
	WEST,
	
	@SuppressWarnings("javadoc")
	NORTHEAST ,
	
	@SuppressWarnings("javadoc")
	NORTHWEST,
	
	@SuppressWarnings("javadoc")
	SOUTHEAST,
	
	@SuppressWarnings("javadoc")
	SOUTHWEST;
	
	/** A list of the primary directions */
	private List<Direction>	primaryDirections;
	/** the opposite direction */
	private Direction oppositeDirection;
	
	/**
	 * Get a list of the primary directions. These are NORTH, EAST, SOUTH, WEST. 
	 * @return the primary directions
	 */
	public List<Direction> getPrimaryDirections() {
		return primaryDirections;
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
	 * @return the opposite direction
	 */
	public Direction getOppositeDirection() {
		return oppositeDirection;
	}
	
	private void setOppositeDirection(Direction direction) {
		this.oppositeDirection = direction;
	}
	
	
	// initialize the fields of the enum
	static {
		List<Direction> directions = new ArrayList<Direction>();
		directions.add(NORTH);
		NORTH.setPrimarydirections(new ArrayList<Direction> (directions));
		directions.add(EAST);
		NORTHEAST.setPrimarydirections(new ArrayList<Direction> (directions));
		directions.remove(NORTH);
		EAST.setPrimarydirections(new ArrayList<Direction> (directions));
		directions.add(SOUTH);
		SOUTHEAST.setPrimarydirections(new ArrayList<Direction> (directions));
		directions.remove(EAST);
		SOUTH.setPrimarydirections(new ArrayList<Direction> (directions));
		directions.add(WEST);
		SOUTHWEST.setPrimarydirections(new ArrayList<Direction> (directions));
		directions.remove(SOUTH);
		WEST.setPrimarydirections(new ArrayList<Direction> (directions));
		directions.add(NORTH);
		NORTHWEST.setPrimarydirections(new ArrayList<Direction> (directions));
		
		NORTH.setOppositeDirection(SOUTH);
		EAST.setOppositeDirection(WEST);
		SOUTH.setOppositeDirection(NORTH);
		WEST.setOppositeDirection(EAST);
		
		NORTHEAST.setOppositeDirection(SOUTHWEST);
		NORTHWEST.setOppositeDirection(SOUTHEAST);
		SOUTHEAST.setOppositeDirection(NORTHWEST);
		SOUTHWEST.setOppositeDirection(NORTHEAST);
	}
}
