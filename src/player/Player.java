package player;

import item.IItem;
import java.util.List;
import player.actions.Action;
import square.SquareContainer;
import square.TronObject;
 
/*
 * NOTE: Only PlayerDB holds a reference to the Player-objects. 
 * The controllers only get IPlayer references 
 * (using the getCurrentPlayer() method of the IPlayerDB class)
 */

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
	public boolean canPerformAction(Action action);
	
	/**
	 * Let the player perform a specified action.
	 * 
	 * @param action
	 *        the action to perform
	 */
	public void performAction(Action action);
}
