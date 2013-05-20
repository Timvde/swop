package square;

import item.IItem;

/**
 * A special kind of {@link Square}. Where a player can stand on when the game
 * starts. The starting position of a player cannot contain an item.
 */
public class StartingPositionDecorator extends AbstractSquareDecorator {

	/**
	 *Create a new starting position decorator for a specified square
	 * 
	 * @param square
	 *        the square who will be decorated
	 */
	public StartingPositionDecorator(AbstractSquare square) {
		super(square);
	}
	
	@Override
	public boolean isStartingPosition() {
		return true;
	}
	
	@Override
	public boolean canBeAdded(IItem item) {
		// The starting position of a player cannot contain an item
		return false;
	}
}
