package powerfailure;

import player.TurnEvent;
import square.SquareContainer;
import effects.EffectFactory;

/**
 * This class represents a primary power failure. It is the root of <s>all
 * evil</s> a secondary and tertiary power failure.
 */
public class PrimaryPowerFailure extends PowerFailure {
	
	private static final int	TIME_TO_LIVE	= 2;
	
	/**
	 * Create a new primary power failure on a specified square
	 * 
	 * @param square
	 *        the square this power failure will affect
	 * @param factory
	 *        The EffectFactory to use to create effects.
	 */
	public PrimaryPowerFailure(SquareContainer square, EffectFactory factory) {
		if (square == null || factory == null)
			throw new IllegalArgumentException("Arguments cannot be null");
		this.square = square;
		square.addProperty(this);
		this.effectFactory = factory;
		
		timeToLive = TIME_TO_LIVE;
		
		createSecondaryPowerFailure();
	}
	
	@Override
	public void updateStatus(TurnEvent event) {
		if (event == TurnEvent.END_TURN)
			decreaseTimeToLive();
		
		if (timeToLive <= 0 && square != null)
			square.removeProperty(this);
	}
	
	private void createSecondaryPowerFailure() {
		new SecondaryPowerFailure(this, effectFactory);
	}
}
