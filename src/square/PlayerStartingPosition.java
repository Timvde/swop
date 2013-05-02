package square;

import item.IItem;
import java.util.Map;

/**
 * A special kind of {@link NormalSquare}. Where a player can stand on when the game
 * starts. The starting position of a player cannot contain an item.
 */
public class PlayerStartingPosition extends NormalSquare {
	
	/**
	 * Create a new player startingposition square.
	 * 
	 * @param neighbours
	 *        the squares adjacent to this square
	 */
	public PlayerStartingPosition(Map<Direction, AbstractSquare> neighbours) {
		super(neighbours);
	}
	
	@Override
	public boolean canBeAdded(IItem item) {
		// The starting position of a player cannot contain an item
		return false;
	}
	
}
