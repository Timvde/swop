package square;

import item.IItem;

/**
 * A special kind of {@link Square}. Where a player can stand on when the game
 * starts. The starting position of a player cannot contain an item.
 */
public class PlayerStartingPosition extends NormalSquare {
	
	@Override
	public boolean canBeAdded(IItem item) {
		// The starting position of a player cannot contain an item
		return false;
	}
}
