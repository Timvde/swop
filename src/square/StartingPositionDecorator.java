package square;

import player.LightTrailDecorator;
import item.IItem;

/**
 * A special kind of {@link Square}. Where a player can stand on when the game
 * starts. The starting position of a player cannot contain an item.
 */
public class StartingPositionDecorator extends AbstractSquareDecorator {
	
	/**
	 * Create a new starting position decorator for a specified square
	 * 
	 * @param square
	 *        the square who will be decorated
	 */
	public StartingPositionDecorator(AbstractSquare square) {
		super(square);
	}
	
	@Override 
	public boolean hasProperty(PropertyType property) {
		return property == PropertyType.STARTING_POSITION ? true : square.hasProperty(property);
	}
	
	/**
	 * Returns <code>false</code>. The starting position of a player cannot
	 * contain an item
	 */
	@Override
	public boolean canBeAdded(IItem item) {
		return false;
		//FIXME dit mag wel at runtime?
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof StartingPositionDecorator))
			return false;
		
		StartingPositionDecorator other = (StartingPositionDecorator) obj;
		
		return this.square.equals(other.square);
	}
}
