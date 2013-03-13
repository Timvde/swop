package grid;

import java.util.ArrayList;
import java.util.List;

/**
 * A Direction enumeration class.
 * 
 * @author tom
 * 
 */
public enum Direction {
	NORTH, SOUTH, EAST, WEST, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST;
	
	/** A list of the primary directions */
	private List<Direction>	primaryDirections;
	
	/**
	 * Get a list of the primary directions.
	 */
	public List<Direction> getPrimeryDirections() {
		return primaryDirections;
	}
	
	/**
	 * Set the primary directions.
	 */
	private void setPrimerydirections(List<Direction> directions) {
		this.primaryDirections = directions;
	}
	
	static {
		List<Direction> directions = new ArrayList<Direction>();
		directions.add(NORTH);
		NORTH.setPrimerydirections(directions);
		directions.add(EAST);
		NORTHEAST.setPrimerydirections(directions);
		directions.remove(NORTH);
		EAST.setPrimerydirections(directions);
		directions.add(SOUTH);
		SOUTHEAST.setPrimerydirections(directions);
		directions.remove(EAST);
		SOUTH.setPrimerydirections(directions);
		directions.add(WEST);
		SOUTHWEST.setPrimerydirections(directions);
		directions.remove(SOUTH);
		WEST.setPrimerydirections(directions);
		directions.add(NORTH);
		NORTHWEST.setPrimerydirections(directions);
	}
	
}
