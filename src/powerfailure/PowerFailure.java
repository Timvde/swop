package powerfailure;

import item.Effect;
import java.util.ArrayList;
import java.util.List;
import square.AbstractSquare;
import square.AbstractSquareDecorator;
import square.Direction;
import square.Property;
import square.SquareContainer;

/**
 * This class represents a power failure. This is a state a square can be in
 * with the following properties: - If a Square is power failured, its
 * neighbours are, too -
 * 
 */
public class PowerFailure implements Property {
	
	private List<SquareContainer>	squares;
	
	// The time to live is 3 on creation
	private int						timeToLive;
	
	/**
	 * Create a power failure for a given square. This will also impact the
	 * neigbhours of the specified square.
	 * 
	 * @param square
	 *        The square that is impacted by this power failure.
	 */
	public PowerFailure(SquareContainer square) {
		squares = new ArrayList<SquareContainer>();
		square.addProperty(this);
		squares.add(square);
		
		// add a power failure for the neighbours of the square
		for (Direction direction : Direction.values())
			if (square.getNeighbourIn(direction) != null) {
				if (square.getNeighbourIn(direction) instanceof SquareContainer) {
					square.getNeighbourIn(direction).addProperty(this);
					squares.add(square.getNeighbourIn(direction));
				}
			}
		
		timeToLive = 3;
	}
	
	/**
	 * When a turn ends, a PowerFailure has to decrease the number of turns left
	 * it is power failured. When it is set to zero, the power failure will be
	 * released.
	 */
	public void decreaseTimeToLive() {
		if (timeToLive > 0)
			timeToLive--;
		// No else if, timeToLive could be 1 before and 0 now.
		if (timeToLive == 0)
			for (SquareContainer square : squares)
				square.removeProperty(this);
	}
	
	/**
	 * Returns the effect this power failure has on an object.
	 * 
	 * @return the effect of this power failure
	 */
	public Effect getEffect() {
		return new PowerFailureEffect();
	}
	
	@Override
	public AbstractSquareDecorator getDecorator(AbstractSquare square) {
		return new PowerFailureDecorator(square, this);
	}
}
