package powerfailure;

import player.TurnEvent;
import square.SquareContainer;

/**
 * This class represents a power failure.
 */
public class TertiaryPowerFailure extends PowerFailure {
	
	private static final int TIME_TO_LIVE = 1;
	
	/**
	 * Create a power failure for a given square.
	 * 
	 * @param square
	 *        The square that is impacted by this power failure.
	 */
	public TertiaryPowerFailure(SquareContainer square) {
		super(square);
		
		timeToLive = TIME_TO_LIVE;
	}
	
	@Override
	public void updateStatus(TurnEvent event) {
		if (event == TurnEvent.END_ACTION) {
			decreaseTimeToLive();
		}
	}
	
}
