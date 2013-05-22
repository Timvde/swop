package square;

import item.Flag;

/**
 * Objects having the flagkeeper property can carry a {@link Flag}.
 */
public interface FlagKeeper {
	
	/**
	 * This method returns the flag the flagkeeper is carrying (or
	 * <code>null</code> if he carries none). The flagkeeper must realease the
	 * flag he is carrying (i.e. remove it from his inventory).
	 * 
	 * @return Flag he flag the flagkeeper is carrying or <code>null</code> if
	 *         he carries none
	 */
	Flag giveFlag();
	
	/**
	 * Returns the current position the flagkeeper is standing on.
	 * 
	 * @return the current position the flagkeeper is standing on.
	 */
	SquareContainer getCurrentPosition();
	
}
