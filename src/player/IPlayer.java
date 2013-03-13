package player;

import grid.Coordinate;
import grid.Direction;
import item.IItem;
import java.util.List;

/*
 * NOTE: Only PlayerDB holds a reference to the Player-objects. 
 * The controllers only get IPlayer references 
 * (using the getCurrentPlayer() method of the IPlayerDB class)
 */

/**
 * A Player is the main actor of the game. It can move round, pick up objects
 * and tries to win the game
 */
public interface IPlayer {
	
	/**
	 * Returns the unique ID-number associated with this player.
	 * 
	 * @return the ID associated with this player.
	 */
	public int getID(); // TODO gaan we dit nog gebruiken??
	
	/**
	 * Returns the coordinate this player has to reach to win the game.
	 * 
	 * @return the coordinate this player has to reach to win the game.
	 */
	public Coordinate getTargetPosition();
	
	/**
	 * Returns the {@link Inventory}-content associated with this player.
	 * 
	 * @return the Inventory associated with this player.
	 */
	public List<IItem> getInventoryContent();
	
	/* ############## ActionHistory related methods ############## */
	
	/**
	 * Returns the allowed number of actions the player has left in this turn.
	 * 
	 * @return the allowed number of actions the player has left.
	 */
	public int getAllowedNumberOfActions();
	
	/**
	 * This method lets the player lose a specified number of actions.
	 * 
	 * @return The number of actions this player will skip
	 */
	public void skipNumberOfActions(int numberOfActionsToSkip);
	
	/**
	 * Return whether or not this player has already done a move action during
	 * this turn.
	 * 
	 * @return whether this player has already moved
	 */
	public boolean hasMovedYet();
	
	/* #################### User methods #################### */
	
	/**
	 * This method ends the turn of this player. This player will lose the game
	 * if this method is called before he did a move action, (i.e. if
	 * <code>this.hasMovedYet()</code> is false when calling this method, this
	 * player loses the game).
	 * 
	 * @throws IllegalStateException
	 *         The end turn preconditions must be satisfied, i.e.
	 *         {@link #isPreconditionEndTurnSatisfied()}
	 */
	public void endTurn() throws IllegalStateException;
	
	/**
	 * Returns whether this player is allowed to perform an end turn action. A
	 * player is allowed to perform an endturn action if he has performed less
	 * then {@link #MAX_NUMBER_OF_ACTIONS_PER_TURN} in his current turn,
	 * {@link #getAllowedNumberOfActions()} > 0.
	 * 
	 * @return whether this player is allowed to perform an end turn action.
	 */
	public boolean isPreconditionEndTurnSatisfied();
	
	/**
	 * This method moves the player one square in the specified
	 * {@link Direction}.
	 * 
	 * @param direction
	 *        the direction to move in
	 * @throws IllegalStateException
	 *         The move preconditions must be satisfied, i.e. this.
	 *         {@link #isPreconditionMoveSatisfied()}
	 */
	public void moveInDirection(Direction direction) throws IllegalStateException;
	
	/**
	 * Returns whether this player is allowed to perform a move action. A player
	 * is allowed to perform an move action if he has performedless then
	 * {@link #MAX_NUMBER_OF_ACTIONS_PER_TURN} in his current turn,
	 * {@link #getAllowedNumberOfActions()} > 0.
	 * 
	 * @return whether this player is allowed to perform a move action
	 */
	public boolean isPreconditionMoveSatisfied();
	
	/**
	 * TODO
	 * 
	 * @param item
	 */
	public void pickUpItem(IItem item);
	
	/**
	 * TODO
	 * 
	 * @param i
	 */
	public void useItem(IItem i);
}
