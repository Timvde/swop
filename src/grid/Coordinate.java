package grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A immutable Cartesian coordinate in a two dimensional plane. Each coordinate
 * is represented by an X- and Y-coordinate. {@code Position.ORIGIN} represents
 * a position in the upper left corner of the screen. If {@code x} represents an
 * {@code Integer} then {@code new Coordinate(x, 0)} is a coordinate at the top
 * of the screen. Similarly, if {@code y} represents an {@code Integer} then
 * {@code new Coordinate(0, y)} is a coordinate on the left of the screen.
 * 
 * @author Bavo Mees
 */
public class Coordinate {
	
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
		if (direction == null)
			throw new IllegalArgumentException();
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
				throw new IllegalArgumentException();
		}
	}
	
	@Override
	public String toString() {
		return "Coordinate(" + x + ", " + y + ")";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	
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
	 * returns a new pseudorandom coordinate uniformly distributed between
	 * {@code new Coordinate(0,0)} (included) and {@code new Coordinate(x, y)}
	 * (excluded) within specified dimensions. The equality of the distribution
	 * is guaranteed by the the randomness of the {@link Random#nextInt(int n)
	 * nextInt(int)} method.
	 * 
	 * @param x
	 *        The maximum x value
	 * @param y
	 *        The maximum y value
	 * @return A random Coordinate between (0,0) and (x,y)
	 */
	public static Coordinate random(int x, int y) {
		return new Coordinate(new Random().nextInt(x), new Random().nextInt(y));
	}
	
	/**
	 * Get a random Coordinate with the specified distance from this Coordinate.
	 * This is used to build random walls in a grid.
	 * 
	 * @param distance
	 *        The distance the new Coordinate has to have from this Coordinate
	 * @return A Coordinate on the specified distance.
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
	
	/**
	 * returns whether a specified coordinate is a neighbor of this coordinate.
	 * More formally a coordinate is a neighbor of an other coordinate if and
	 * only if
	 * 
	 * <pre>
	 * {@code
	 * Math.abs(coord1.getX() - coord2.getX()) <= 1
	 * && Math.abs(coord1.getY() - coord2.getY()) <= 1
	 * }
	 * </pre>
	 * 
	 * @param coordinate
	 *        the coordinate to test
	 * @return True if the specified coordinate is a neighbor of this
	 *         coordinate, otherwise false
	 */
	public boolean isNeighbour(Coordinate coordinate) {
		for (Direction direction : Direction.values())
			if (this.getCoordinateInDirection(direction).equals(coordinate))
				return true;
		return false;
	}
	
	/**
	 * Returns a list of all the neighbours of this square. A square
	 * 
	 * @return A list of neighbours: all Coordinates in this list will have
	 *         {@link #isNeighbour(Coordinate) isNeighbour(this)} == true
	 */
	public List<Coordinate> getAllNeighbours() {
		List<Coordinate> neighbours = new ArrayList<Coordinate>();
		for (Direction direction : Direction.values())
			neighbours.add(getCoordinateInDirection(direction));
		return neighbours;
	}
}
