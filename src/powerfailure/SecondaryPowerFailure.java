package powerfailure;

import java.util.Random;
import player.TurnEvent;
import square.Direction;
import square.SquareContainer;

/**
 * This class represents a secondary power failure.
 */
public class SecondaryPowerFailure extends PowerFailure {
	
	private static final int		TIME_TO_LIVE	= 2;
	private Direction				direction;
	
	private PrimaryPowerFailure		primaryPowerFailure;
	private PowerFailure	tertiaryPowerFailure;
	private boolean					clockwise;
	
	/**
	 * Create a secondary power failure for a given square.
	 * 
	 * @param primaryPowerFailure
	 *        the primary power failure who created this power failure
	 * 
	 */
	public SecondaryPowerFailure(PrimaryPowerFailure primaryPowerFailure) {
		timeToLive = TIME_TO_LIVE;
		this.primaryPowerFailure = primaryPowerFailure;
		clockwise = new Random().nextBoolean();
		
		square = calculateSquare();
		if (square != null)
			square.addProperty(this);
		
		createTernaryPowerFailure();
	}
	
	private void createTernaryPowerFailure() {
		tertiaryPowerFailure = new TertiaryPowerFailure(this);
	}
	
	@Override
	public void updateStatus(TurnEvent event) {
		decreaseTimeToLive();
		
		// rotate the power failure
		if (timeToLive <= 0) {
			timeToLive = TIME_TO_LIVE;
			if (square != null)
				square.removeProperty(this);
			square = calculateSquare();
			if (square != null)
				square.addProperty(this);
		}
	}
	
	/**
	 * Returns the direction this power failure is positioned in relative to the
	 * primary power failure
	 * 
	 * @return the direction of this power failure
	 */
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * Calculates a square for this power failure. This power failure will
	 * rotate around the the primary power failure who induced this power
	 * failure . IF the chosen square is not on the grid, null will be returned.
	 * 
	 * @return the square for this power failure to affect
	 */
	private SquareContainer calculateSquare() {
		if (direction == null)
			direction = Direction.getRandomDirection();
		else if (clockwise)
			direction = direction.getNextClockwiseDirection();
		else
			direction = direction.getNextCounterClockwiseDirection();
		
		if (primaryPowerFailure.getSquare() != null)
			return primaryPowerFailure.getSquare().getNeighbourIn(direction);
		else
			return null;
	}
	
}
