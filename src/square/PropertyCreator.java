package square;

/**
 * This interface represents a creator of properties.
 */
public interface PropertyCreator {
	
	/**
	 * Create a property and add its effects to the specified square
	 * 
	 * @param square
	 *        The square to affect
	 */
	public void affect(SquareContainer square);
}
