package square;

import player.TurnEvent;

/**
 * This interface represents a creator of properties.
 */
public interface PropertyCreator {
	
	/**
	 * Create a property and add its effects to the specified square
	 * 
	 * @param square
	 *        The square to affect
	 */
	public void affect(SquareContainer square);
	
	/**
	 * Gets the update event on which affect() should be called.
	 * 
	 * @return The turn event this {@link PropertyCreator} wants to listen to
	 */
	// TODO: Volgens mij moet de verantwoordelijkheid om dit te checken niet in
	// SquareContainer liggen, maar hier. Als extra argument bij affect()?
	public TurnEvent getUpdateEvent();
}
