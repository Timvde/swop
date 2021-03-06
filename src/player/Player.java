package player;

import item.IItem;
import java.util.List;
import player.actions.Action;
import square.SquareContainer;
import square.TronObject;
 
/**
 * A Player is the main actor of the game. It can move round, pick up objects
 * and tries to win the game
 */
public interface Player extends TronObject {
	
	/**
	 * Returns the unique ID-number associated with this player.
	 * 
	 * @return the ID associated with this player.
	 */
	public int getID(); 
	
	/**
	 * Test whether a player is actually dead. This can be used to
	 * remove player references without causing exceptions.
	 * 
	 * @return True if the player is no longer part of the game
	 */
	public boolean isDead();
	
	/**
	 * Returns the square this player has to reach to win the game.
	 * 
	 * @return the square this player has to reach to win the game.
	 */
	public SquareContainer getStartingPosition();
	
	/**
	 * Returns the square this player currently stands on.
	 * 
	 * @return the square this player currently stands on.
	 */
	public SquareContainer getCurrentPosition();
	
	/**
	 * Returns the {@link Inventory}-content associated with this player.
	 * 
	 * @return the Inventory associated with this player.
	 */
	public List<IItem> getInventoryContent();
	
	/**
	 * Returns the allowed number of actions the player has left in this turn.
	 * 
	 * @return the allowed number of actions the player has left.
	 */
	public int getAllowedNumberOfActions();
	
	/**
	 * Return whether or not this player has already done a move action during
	 * this turn.
	 * 
	 * @return whether this player has already moved
	 */
	public boolean hasMovedYet();
	
	/**
	 * Returns whether this player can perform an action. A player can perform
	 * an action if: <li>his state is {@link PlayerState#ACTIVE}</li> <li>he has
	 * performed less then
	 * {@value PlayerActionManager#MAX_NUMBER_OF_ACTIONS_PER_TURN} in his current
	 * turn (i.e. <code>{@link #getAllowedNumberOfActions()} > 0</code> ).</li>
	 * 
	 * @return whether this player is allowed to perform an action.
	 */
	public boolean canPerformAction();
	
	/**
	 * Let the player perform a specified action.
	 * 
	 * @param action
	 *        the action to perform
	 */
	public void performAction(Action action);
}
