package grid;

/**
 * an immutable coordinate class
 * 
 * @author Bavo Mees
 */
public class Coordinate {
	
	private final int	x;
	private final int	y;
	
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
	 * @return the x coordinate 
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * returns the y coordinate of this element
	 * @return the y coordinate
	 */
	public int getY() {
		return y;
	}
}
