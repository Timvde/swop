package square;

import item.lightgrenade.LightGrenadeEffect;
import java.util.ArrayList;
import java.util.List;
import effect.Effect;

/**
 * This class represents a power failure. This is a state a square can be in
 * with the following properties: - If a Square is power failured, its
 * neighbours are, too -
 * 
 */
public class PowerFailure {
	
	private List<ASquare>	squares	= new ArrayList<ASquare>();
	
	// The time to live is 3 on creation
	private int				timeToLive;
	
	/**
	 * Create a power failure for a given square. This will also impact the
	 * neigbhours of the specified square.
	 * 
	 * @param square
	 *        The square that is impacted by this power failure.
	 */
	public PowerFailure(ASquare square) {
		squares = new ArrayList<ASquare>();
		
		if (square instanceof Square) {
			square.addPowerFailure(this);
			squares.add(square);
		}
		
		// add a power failure for the neighbours of the square
		for (Direction direction : Direction.values())
			if (square.getNeighbour(direction) != null) {
				if (square.getNeighbour(direction) instanceof Square) {
					square.getNeighbour(direction).addPowerFailure(this);
					squares.add(square.getNeighbour(direction));
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
			for (ASquare square : squares)
				square.removePowerFailure(this);
	}
	
	/**
	 * Returns the effect this power failure has on an object.
	 * 
	 * @param effect
	 *        A light grenade effect that can be modified
	 * @return the effect of this power failure
	 */
	public Effect getEffect(LightGrenadeEffect effect) {
		return new PowerFailureEffect(effect);
	}
}
