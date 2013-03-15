package grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * This class represents a power failure. This is a state a square can be in
 * with the following properties: - If a Square is power failured, its
 * neighbours are, too -
 * 
 */
public class PowerFailure {
	
	private List<ASquare>	squares		= new ArrayList<ASquare>();
	
	// The time to live is 3 on creation
	private int				timeToLive	= 3;
	
	/**
	 * Create a power failure for a given list of Squares.
	 * 
	 * @param squares
	 *        The squares that are impacted by this power failure.
	 */
	public PowerFailure(List<ASquare> squares) {
		// We have no way to check if the right conditions are met (i.e. the
		// squares are neighbours), so we just have to trust the system. This is
		// not that bad at all, as PowerFailure objects should never get to the
		// outside.
		
		for (ASquare square : squares) {
			try {
				square.addPowerFailure(this);
			}
			catch (IllegalStateException e) {
				squares.remove(square);
			}
		}
		
		this.squares = squares;
	}
	
	/**
	 * When a turn ends, a PowerFailure has to decrease the number of turns left
	 * it is power failured. When it is set to zero, the power failure will be
	 * released.
	 */
	public void decreaseTimeToLive() {
		if (timeToLive > 0)
			timeToLive--;
		if (timeToLive == 0)
			for (ASquare square : squares)
				square.removePowerFailure(this);
	}
}
