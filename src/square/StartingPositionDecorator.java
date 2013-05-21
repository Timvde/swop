package square;

import item.IItem;

/**
 * A special kind of {@link Square}. Where a player can stand on when the game
 * starts. The starting position of a player cannot contain an item.
 */
public class StartingPositionDecorator extends AbstractSquareDecorator {
	
	private int	number;
	
	/**
	 * Create a new starting position decorator for a specified square
	 * 
	 * @param square
	 *        the square who will be decorated
	 * @param number
	 *        the number of this startingposition
	 * @throws IllegalArgumentException the number of a startingposition must be > 0
	 */
	public StartingPositionDecorator(AbstractSquare square, int number) throws IllegalArgumentException{
		super(square);
		if (number < 1)
			throw new IllegalArgumentException(
					"the number of a startingposition must be in the valid range");
		
		this.number = number;
	}
	
	/**
	 * Returns the number of this starting position.
	 */
	@Override
	public int getStartingPosition() {
		return number;
	}
	
	/**
	 * Returns <code>false</code>. The starting position of a player cannot
	 * contain an item
	 */
	@Override
	public boolean canBeAdded(IItem item) {
		return false;
	}
}
