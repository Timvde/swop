package grid;

import java.util.ArrayList;
import java.util.Random;

/**
 * an immutable coordinate class
 * 
 * @author Bavo Mees
 */
public class Coordinate {
	
	/** The x and y values of this coordinate */
	private final int				x;
	private final int				y;
	
	/** The origin of the grid */
	public final static Coordinate	ORIGIN	= new Coordinate(0, 0);
	
	/**
	 * create a new coordinate with a specified x and y coordinate
	 * 
	 * @param x
	 *        the x part of the new coordinate
	 * @param y
	 *        the y part of the new coordinate
	 */
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * returns the x coordinate of this element
	 * 
	 * @return the x coordinate
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * returns the y coordinate of this element
	 * 
	 * @return the y coordinate
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * returns a new coordinate in relative to this coordinate in a specified
	 * direction. The direction NORTH maps to a negative y-coordinate. The
	 * direction EAST maps to a positive x-coordinate. e.g.: <code>
	 * Coordinate.ORIGIN.getCoordinateInDirection(Direction.NORTH).equals(new Coordinate(0, -1)) == true
	 * </code> and <code>
	 * Coordinate.ORIGIN.getCoordinateInDirection(Direction.EAST).equals(new Coordinate(1, 0)) == true
	 * </code>
	 * 
	 * @param direction
	 *        the direction in which a new coordinate is returned
	 * @return a new coordinate relative to this coordinate
	 */
	public Coordinate getCoordinateInDirection(Direction direction) {
		switch (direction) {
			case NORTH:
				return new Coordinate(x, y - 1);
			case EAST:
				return new Coordinate(x + 1, y);
			case WEST:
				return new Coordinate(x - 1, y);
			case SOUTH:
				return new Coordinate(x, y + 1);
			case NORTHEAST:
				return new Coordinate(x + 1, y - 1);
			case NORTHWEST:
				return new Coordinate(x - 1, y - 1);
			case SOUTHEAST:
				return new Coordinate(x + 1, y + 1);
			case SOUTHWEST:
				return new Coordinate(x - 1, y + 1);
			default:
				// this can never happen
				throw new IllegalStateException();
		}
	}
	
	/**
	 * Return a string representation of this coordinate.
	 */
	@Override
	public String toString() {
		return "Coordinate(" + x + ", " + y + ")";
	}
	
	/**
	 * Return a hashcode of this coordinate.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	
	/**
	 * Check if this coordinate equals another.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	/**
	 * Return a random coordinate.
	 */
	public static Coordinate random(int x, int y) {
		return new Coordinate(new Random().nextInt(x), new Random().nextInt(y));
	}
	
	/**
	 * Return a random coordinate with a given distance from this coordinate.
	 * @param distance
	 * 			The distance of the new coordinate.
	 * @return
	 * 			A random coordinate that has a certain distance from this coordinate.
	 */
	public Coordinate getRandomCoordinateWithDistance(int distance) {
		Direction[] directions = { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
		Direction direction = directions[new Random().nextInt(directions.length)];
		switch (direction) {
			case NORTH:
				return new Coordinate(x, y - distance);
			case EAST:
				return new Coordinate(x + distance, y);
			case SOUTH:
				return new Coordinate(x, y + distance);
			case WEST:
				return new Coordinate(x - distance, y);
			default:
				throw new IllegalStateException("Something went terribly wrong :(");
		}
	}
}
