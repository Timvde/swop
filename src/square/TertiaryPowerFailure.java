package square;

/**
 * This class represents a power failure.
 */
public class TertiaryPowerFailure extends PowerFailure {
	
	/**
	 * Create a power failure for a given square.
	 * 
	 * @param square
	 *        The square that is impacted by this power failure.
	 */
	public TertiaryPowerFailure(ASquare square) {
		super(square);
		
		timeToLive = 2;
	}
	
}
