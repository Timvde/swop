package square;

import player.TurnEvent;
import item.Effect;

/**
 * This class represents a powerfailure. A powerfailure can either be a primary
 * powerfailure, a secondary powerfailure and a tertiary powerfailure.
 * 
 * @author tom
 * 
 */
public abstract class PowerFailure {
	
	/** The square this powerfailure affects */
	private ASquare	square;
	
	/**
	 * The time this powerfailure still has to live. This is counted as actions
	 * or turns, depending on what kind of powerfailure.
	 */
	protected int	timeToLive;
	
	/**
	 * Create a new powerfailure that is active on the given square.
	 * 
	 * @param square
	 *        The square this powerfailure will affect.
	 */
	public PowerFailure(ASquare square) {
		if (square instanceof Square) {
			square.addPowerFailure(this);
			this.square = square;
		}
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
		if (timeToLive == 0) {
			this.square.removePowerFailure(this);
			this.square = null;
		}
	}
	
	/**
	 * Return the time to live for this powerfailure. This can be counted as
	 * actions or turns, depending on the kind of powerfailure.
	 */
	@SuppressWarnings("javadoc")
	public int getTimeToLive() {
		return this.timeToLive;
	}
	
	/**
	 * Return the square this powerfailure is located on.
	 */
	protected ASquare getSquare() {
		return this.square;
	}
	
	/**
	 * Set the square this powerfailure is located on.
	 */
	protected void setSquare(ASquare square) {
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
	
	/**
	 * Let the powerfailure know about a new turn event.
	 * 
	 * @param event
	 *        The turn event to let the powerfailure know about.
	 */
	abstract void updateStatus(TurnEvent event);
}
