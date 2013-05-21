package powerfailure;

import player.TurnEvent;
import square.SquareContainer;

/**
 * This class represents a primary power failure. It is the root of <s>all
 * evil</s> a secondary and tertiary power failure.
 */
public class PrimaryPowerFailure extends PowerFailure {
	
	private static final int	TIME_TO_LIVE	= 2;
	private SecondaryPowerFailure secondaryPowerFailure;
	
	/**
	 * Create a new primary power failure on a specified square
	 * 
	 * @param square
	 *        the square this power failure will affect
	 */
	public PrimaryPowerFailure(SquareContainer square) {
		if (square == null)
			throw new IllegalArgumentException();
		this.square = square;
		square.addProperty(this);
		
		timeToLive = TIME_TO_LIVE;
		
		createSecondaryPowerFailure();
	}
	
	@Override
	public void updateStatus(TurnEvent event) {
		if (event == TurnEvent.END_TURN)
			decreaseTimeToLive();
		
		if (timeToLive <=0 && square != null)
				square.removeProperty(this);
		
		if (secondaryPowerFailure != null)
			secondaryPowerFailure.updateStatus(event); 
	}
	
	private void createSecondaryPowerFailure() {
		secondaryPowerFailure = new SecondaryPowerFailure(this);
	}

	@Override
	protected SquareContainer getSquare() {
		return square; 
	}
}
