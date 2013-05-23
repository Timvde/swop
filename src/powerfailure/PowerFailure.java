package powerfailure;

import player.TurnEvent;
import square.AbstractSquare;
import square.AbstractSquareDecorator;
import square.Property;
import square.SquareContainer;
import effects.Effect;
import effects.PowerFailureEffect;

/**
 * This class represents a power failure. This is a property that affects
 * squares:
 * 
 * <ul>
 * <li>If a player starts on a power failured square, he will have one action
 * less to perform</li>
 * <li>If a player moves onto a power failured square, his turn will end</li>
 * <li>A light grenade exploding on a power failured square will have its
 * strength increased</li>
 * </ul>
 */
public abstract class PowerFailure implements Property {
	
	/**
	 * The time this power failure still has to live. This is counted as actions
	 * or turns, depending on what kind of power failure.
	 */
	protected int				timeToLive;
	
	/**
	 * The square where this PowerFailure resides
	 */
	protected SquareContainer	square;
	
	/**
	 * When a turn ends, a PowerFailure has to decrease the number of turns or
	 * actions left it is power failured. When it is set to zero, the power
	 * failure will be released.
	 */
	protected void decreaseTimeToLive() {
		timeToLive--;
	}
	
	/**
	 * Return the time to live for this power failure. This can be counted as
	 * actions or turns, depending on the kind of power failure.
	 * 
	 * @return the time to live
	 */
	public int getTimeToLive() {
		return this.timeToLive;
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
	
	/**
	 * Let the powerfailure know about a new turn event.
	 * 
	 * @param event
	 *        The turn event to let the powerfailure know about.
	 */
	public abstract void updateStatus(TurnEvent event);
	
	/**
	 * Return the square this power failure is located on.
	 * 
	 * @return The square this power failure influences.
	 */
	protected SquareContainer getSquare() {
		return square;
	}
	
	/**
	 * Return the effect that should be executed when a player enters a square
	 * with a power failure.
	 * 
	 * @return The effect to be executed
	 */
	public Effect getStartTurnEffect() {
		return new PowerFailureStartTurnEffect();
	}
}
