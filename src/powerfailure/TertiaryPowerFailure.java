package powerfailure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import player.TurnEvent;
import square.Direction;
import square.SquareContainer;
import effects.EffectFactory;

/**
 * This class represents a tertiary power failure.
 */
public class TertiaryPowerFailure extends PowerFailure {
	
	private static final int		TIME_TO_LIVE	= 1;
	private SecondaryPowerFailure	secondaryPowerFailure;
	
	/**
	 * Create a power failure for a given square.
	 * 
	 * @param secondaryPowerFailure
	 *        Its secondary power failure
	 * @param factory
	 *        The EffectFactory to use to create effects.
	 */
	public TertiaryPowerFailure(SecondaryPowerFailure secondaryPowerFailure, EffectFactory factory) {
		this.secondaryPowerFailure = secondaryPowerFailure;
		updateSquare();
		this.effectFactory = factory;
	}
	
	@Override
	public void updateStatus(TurnEvent event) {
		decreaseTimeToLive();
		
		// rotate the power failure
		if (timeToLive <= 0) {
			timeToLive = TIME_TO_LIVE;
			if (square != null)
				square.removeProperty(this);
			updateSquare();
		}
	}
	
	/**
	 * Calculates a new square for the tertiary power failure, and returns this.
	 * <code>null</code> is returned if the square falls of the grid.
	 * 
	 * @return the new square.
	 */
	private SquareContainer calculateSquare() {
		List<Direction> possibleDirections = new ArrayList<Direction>(3);
		Direction direction = secondaryPowerFailure.getDirection();
		possibleDirections.add(direction);
		
		possibleDirections.addAll(direction.getAdjacentDirections());
		
		Direction randomDirection = possibleDirections.get(new Random().nextInt(possibleDirections
				.size()));
		
		if (secondaryPowerFailure.getSquare() != null)
			return secondaryPowerFailure.getSquare().getNeighbourIn(randomDirection);
		else
			return null;
	}
	
	private void updateSquare() {
		square = calculateSquare();
		if (square != null)
			square.addProperty(this);
	}
}
