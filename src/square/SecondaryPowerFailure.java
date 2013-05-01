package square;

/**
 * This class represents a secondary powerfailure.
 */
public class SecondaryPowerFailure extends PowerFailure {
	
	/**
	 * Create a secondary powerfailure for a given square.
	 * 
	 * @param square
	 *        The square that is impacted by this power failure.
	 */
	public SecondaryPowerFailure(ASquare square) {
		super(square);
		
		timeToLive = 2;
	}
	
}
