package powerfailure;

import player.TurnEvent;
import square.AbstractSquare;
import square.AbstractSquareDecorator;
import square.Property;
import square.SquareContainer;
import effects.Effect;
import effects.PowerFailureEffect;

/**
 * This class represents a power failure. This is a property that affects squares:
 * 
 * <ul>
 * <li> If a player starts on a power failured square, he will have one action less to perform </li>
 * <li> If a player moves onto a power failured square, his turn will end </li>
 * <li> A light grenade exploding on a power failured square will have its strength increased </li>
 * </ul>
 */
public abstract class PowerFailure implements Property {
	
	/** The square this power failure affects */
	private SquareContainer	square;
	
	/**
	 * The time this power failure still has to live. This is counted as actions
	 * or turns, depending on what kind of power failure.
	 */
	protected int			timeToLive;
	
	/**
	 * Create a new power failure that is active on the given square.
	 * 
	 * @param square
	 *        The square this power failure will affect.
	 */
	public PowerFailure(SquareContainer square) {
		square.addProperty(this);
		this.square = square;
	}
	
	/**
	 * When a turn ends, a PowerFailure has to decrease the number of turns or
	 * actions left it is power failured. When it is set to zero, the power
	 * failure will be released.
	 */
	protected void decreaseTimeToLive() {
		if (timeToLive > 0)
			timeToLive--;
		// No else if, timeToLive could be 1 before and 0 now.
		if (timeToLive <= 0 && square != null) {
			this.square.removeProperty(this);
			this.square = null;
		}
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
	 * Return the square this power failure is located on.
	 * 
	 * @return The square this power failure influences.
	 */
	protected SquareContainer getSquare() {
		return this.square;
	}
	
	/**
	 * Set the square this power failure is located on.
	 */
	protected void setSquare(SquareContainer square) {
		this.square = square;
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
}
