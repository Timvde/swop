package powerfailure;

import java.util.Random;
import player.TurnEvent;
import square.PropertyCreator;
import square.SquareContainer;
import effects.EffectFactory;

/**
 * This class is able to create a Power Failure. It will make sure the right
 * Effect Factory for the game mode is set.
 */
public class PowerFailureCreator implements PropertyCreator {
	
	private static final boolean	ENABLE_POWER_FAILURE	= true;
	private static final float		POWER_FAILURE_CHANCE	= 0.01F;
	
	private EffectFactory			effectFactory;
	
	/**
	 * Instantiates a PowerFailureCreator
	 * 
	 * @param factory
	 *        The EffectFactory power failures must use
	 */
	public PowerFailureCreator(EffectFactory factory) {
		this.effectFactory = factory;
	}
	
	/**
	 * This method has a chance of {@value #POWER_FAILURE_CHANCE} to create a
	 * PrimaryPowerFailure on the specified square.
	 */
	@SuppressWarnings("javadoc")
	public void affect(SquareContainer square) {
		if (ENABLE_POWER_FAILURE) {
			Random rand = new Random();
			if (rand.nextFloat() < POWER_FAILURE_CHANCE)
				new PrimaryPowerFailure(square, effectFactory);
		}
	}

	@Override
	public TurnEvent getUpdateEvent() {
		return TurnEvent.END_TURN;
	}
}
