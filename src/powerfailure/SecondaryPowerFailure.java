package powerfailure;

import player.TurnEvent;
import square.SquareContainer;

/**
 * This class represents a secondary power failure.
 */
public class SecondaryPowerFailure extends PowerFailure {
	
	private static final int	TIME_TO_LIVE	= 2;
	
	/**
	 * Create a secondary power failure for a given square.
	 * 
	 * @param square
	 *        The square that is impacted by this power failure.
	 */
	public SecondaryPowerFailure(SquareContainer square) {
		super(square);
		
		timeToLive = TIME_TO_LIVE;
	}
	
	@Override
	public void updateStatus(TurnEvent event) {
		/*
		 * Does nothing for the secondary power failure. The secondary power
		 * failure stays alive as long as the primary power failure.
		 */
	}
	
}
