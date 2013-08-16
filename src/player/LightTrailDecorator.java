package player;

import square.AbstractSquare;
import square.AbstractSquareDecorator;
import square.PropertyType;

/**
 * 
 * Decorates a square so it is affected by a light trail. Light trails prohibit
 * the players from entering this square.
 * 
 */
public class LightTrailDecorator extends AbstractSquareDecorator {
	
	/**
	 * create a new light trail decorator to affect a specified square.
	 * 
	 * @param square
	 *        the square to decorate
	 */
	public LightTrailDecorator(AbstractSquare square) {
		super(square);
	}
	
	@Override
	public boolean canAddPlayer() {
		return false;
	}
	
	@Override 
	public boolean hasProperty(PropertyType property) {
		return property == PropertyType.LIGHT_TRAIL ? true : square.hasProperty(property);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof LightTrailDecorator))
			return false;
		
		LightTrailDecorator other = (LightTrailDecorator) obj;
		
		return this.square.equals(other.square);
	}
}
