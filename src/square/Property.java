package square;

/**
 * A property of a square. Objects who implement this interface, allow
 * themselves to modify the behavior of a square. The modifying will be done
 * with a decorator.
 * 
 */
public interface Property {
	
	/**
	 * Returns a Decorator that is wrapped around the specified square.
	 * 
	 * @param square
	 *        the square that will be wrapped
	 * @return A new square decorator
	 */
	AbstractSquareDecorator getDecorator(AbstractSquare square);
}
