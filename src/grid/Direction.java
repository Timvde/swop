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
	NORTH {
		
		@Override
		public Coordinate getCrossingCoordinateOnXAxis(Coordinate coord) {
			return coord;
		}
		
		@Override
		public Coordinate getCrossingCoordinateOnYAxis(Coordinate coord) {
			return coord;
		}
	},
	SOUTH {
		
		@Override
		public Coordinate getCrossingCoordinateOnXAxis(Coordinate coord) {
			return coord;
		}
		
		@Override
		public Coordinate getCrossingCoordinateOnYAxis(Coordinate coord) {
			return coord;
		}
	},
	EAST {
		
		@Override
		public Coordinate getCrossingCoordinateOnXAxis(Coordinate coord) {
			return coord;
		}
		
		@Override
		public Coordinate getCrossingCoordinateOnYAxis(Coordinate coord) {
			return coord;
		}
	},
	WEST {
		
		@Override
		public Coordinate getCrossingCoordinateOnXAxis(Coordinate coord) {
			return coord;
		}
		
		@Override
		public Coordinate getCrossingCoordinateOnYAxis(Coordinate coord) {
			return coord;
		}
	},
	NORTHEAST {
		
		@Override
		public Coordinate getCrossingCoordinateOnXAxis(Coordinate coord) {
			return coord.getCoordinateInDirection(WEST);
		}
		
		@Override
		public Coordinate getCrossingCoordinateOnYAxis(Coordinate coord) {
			return coord.getCoordinateInDirection(SOUTH);
		}
	},
	NORTHWEST {
		
		@Override
		public Coordinate getCrossingCoordinateOnXAxis(Coordinate coord) {
			return coord.getCoordinateInDirection(EAST);
		}
		
		@Override
		public Coordinate getCrossingCoordinateOnYAxis(Coordinate coord) {
			return coord.getCoordinateInDirection(SOUTH);
		}
	},
	SOUTHEAST {
		
		@Override
		public Coordinate getCrossingCoordinateOnXAxis(Coordinate coord) {
			return coord.getCoordinateInDirection(WEST);
		}
		
		@Override
		public Coordinate getCrossingCoordinateOnYAxis(Coordinate coord) {
			return coord.getCoordinateInDirection(NORTH);
		}
	},
	SOUTHWEST {
		
		@Override
		public Coordinate getCrossingCoordinateOnXAxis(Coordinate coord) {
			return coord.getCoordinateInDirection(EAST);
		}
		
		@Override
		public Coordinate getCrossingCoordinateOnYAxis(Coordinate coord) {
			return coord.getCoordinateInDirection(NORTH);
		}
	};
	
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
	private void setPrimarydirections(List<Direction> directions) {
		this.primaryDirections = directions;
	}
	
	static {
		List<Direction> directions = new ArrayList<Direction>();
		directions.add(NORTH);
		NORTH.setPrimarydirections(directions);
		directions.add(EAST);
		NORTHEAST.setPrimarydirections(directions);
		directions.remove(NORTH);
		EAST.setPrimarydirections(directions);
		directions.add(SOUTH);
		SOUTHEAST.setPrimarydirections(directions);
		directions.remove(EAST);
		SOUTH.setPrimarydirections(directions);
		directions.add(WEST);
		SOUTHWEST.setPrimarydirections(directions);
		directions.remove(SOUTH);
		WEST.setPrimarydirections(directions);
		directions.add(NORTH);
		NORTHWEST.setPrimarydirections(directions);
	}
	
	/**
	 * Returns the {@link Coordinate} on the perpendicular axis on the line
	 * oriented in this Direction, through the specified neighboring coordinate.
	 * The x-axis means the horizontal axis relative to the specified
	 * coordinate. If the direction is in {NORTH, EAST, SOUTH, WEST}, the method
	 * returns the specified coord.
	 * 
	 * @param coord
	 *        the given coordinate
	 * @return the crossing coordinate or the given coordinate if the direction
	 *         is one of the 4 main directions
	 */
	public abstract Coordinate getCrossingCoordinateOnXAxis(Coordinate coord);
	
	/**
	 * Returns the {@link Coordinate} on the perpendicular axis on the line
	 * oriented in this Direction, through the specified neighboring coordinate.
	 * The y-axis means the vertical axis relative to the specified coordinate.
	 * If the direction is in {NORTH, EAST, SOUTH, WEST}, the method returns the
	 * specified coord.
	 * 
	 * @param coord
	 *        the given coordinate
	 * @return the crossing coordinate or the given coordinate if the direction
	 *         is one of the 4 main directions
	 */
	public abstract Coordinate getCrossingCoordinateOnYAxis(Coordinate coord);
}
