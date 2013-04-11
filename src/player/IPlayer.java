package player;

import item.IItem;
import java.util.List;
import square.ASquare;
import square.Direction;
import square.ISquare;
import square.Square;
import square.TronObject;
import item.Effect;
import ObjectronExceptions.CannotPlaceLightGrenadeException;
import ObjectronExceptions.IllegalActionException;
import ObjectronExceptions.IllegalMoveException;
import ObjectronExceptions.IllegalStepException;

/*
 * NOTE: Only PlayerDB holds a reference to the Player-objects. 
 * The controllers only get IPlayer references 
 * (using the getCurrentPlayer() method of the IPlayerDB class)
 */

/**
 * A Player is the main actor of the game. It can move round, pick up objects
 * and tries to win the game
 */
public interface IPlayer extends TronObject {
	
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
	public ISquare getStartingPosition();
	
	/**
	 * Returns the square this player currently stands on.
	 * 
	 * @return the square this player currently stands on.
	 */
	public ASquare getCurrentLocation();
	
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
	 * @param numberOfActionsToSkip
	 *        The number of actions this player will skip
	 */
	public void skipNumberOfActions(int numberOfActionsToSkip);
	
	/**
	 * Return whether or not this player has already done a move action during
	 * this turn.
	 * 
	 * @return whether this player has already moved
	 */
	public boolean hasMovedYet();
	
	/* ############## PlayerState related methods ############## */
	
	/**
	 * Returns whether this player can perform an action. A player can perform
	 * an action if: <li>his state is {@link PlayerState#ACTIVE}</li> <li>he is
	 * assigned a starting position (i.e.
	 * <code>{@link #getStartingPosition()} != null</code>)</li> <li>he has
	 * performed less then {@value Player#MAX_NUMBER_OF_ACTIONS_PER_TURN} in his
	 * current turn (i.e. <code>{@link #getAllowedNumberOfActions()} > 0</code>
	 * ).</li>
	 * 
	 * @return whether this player is allowed to perform an action.
	 */
	public boolean canPerformAction();
	
	/* #################### User methods #################### */
	
	/**
	 * This method ends the turn of this player. This player will lose the game
	 * if this method is called before he did a move action, (i.e. if
	 * <code>{@link #hasMovedYet()}</code> is false when calling this method,
	 * this player loses the game).
	 * 
	 * @throws IllegalActionException
	 *         This player must be allowed to perform an action, i.e.
	 *         <code>{@link #canPerformAction()}</code>.
	 */
	public void endTurn() throws IllegalActionException;
	
	/**
	 * This method moves the player one {@link Square} in a specified
	 * {@link Direction}. More formally the postconditions are:
	 * 
	 * <li>The new location of this player is <code>{@link #getCurrentLocation() 
	 * currentLocationBefore}{@link ASquare#getNeighbour(Direction) 
	 * .getNeighbour(direction)}</code>.</li>
	 * 
	 * <li>The {@link Effect effects} of any items on the new Square will
	 * be executed (as described in {@link ASquare#addPlayer(IPlayer)}).</li>
	 * 
	 * <li>The {@link LightTrail} of the player will be
	 * {@link LightTrail#updateLightTrail(ASquare) updated}.</li>
	 * 
	 * <li>The player will have done a move during this turn:
	 * <code>{@link #hasMovedYet()} = true</code>
	 * 
	 * @param direction
	 *        The direction to move in
	 * 
	 * @throws IllegalActionException
	 *         This player must be allowed to perform an action, i.e. <code>
	 *         {@link #canPerformAction()}</code>.
	 * @throws IllegalArgumentException
	 *         The specified direction must be valid, i.e.
	 *         {@link #isValidDirection(Direction)}.
	 * @throws IllegalStepException
	 *         The player must be able to move in the given direction on the
	 *         grid, i.e. {@link #canMoveInDirection(Direction)}.
	 * @throws IllegalMoveException
	 *         When the player can't be added to the square in the specified
	 *         direction, i.e. {@link Square#canAddPlayer()}.
	 */
	public void moveInDirection(Direction direction) throws IllegalActionException,
			IllegalArgumentException, IllegalMoveException, IllegalStepException;
	
	/**
	 * Returns whether a specified direction is a valid direction for this
	 * player to try to move in. A player can try to move in every non-null
	 * direction.
	 * 
	 * @param direction
	 *        the direction the player wants to check
	 * @return whether a specified direction is a valid direction for this
	 *         player to try to move in
	 */
	public boolean isValidDirection(Direction direction);
	
	/**
	 * Test whether the player can move in the specified direction on the grid.
	 * 
	 * @param direction
	 *        the direction in which the player wants to move
	 * @return whether the player can move in the specified direction.
	 */
	public boolean canMoveInDirection(Direction direction);
	
	/**
	 * Pick up the given item. The item must be on the square the player is
	 * currently on.
	 * 
	 * @param item
	 *        The item to pick up.
	 * @throws IllegalArgumentException
	 *         The item must be on the square the player is currently on and
	 *         cannot be null.
	 */
	public void pickUpItem(IItem item) throws IllegalArgumentException;
	
	/**
	 * Use the given item. The item must be in the inventory of the player.
	 * 
	 * @param i
	 *        The item that will be used.
	 * @throws IllegalArgumentException
	 *         The item is not in the inventory
	 * @throws CannotPlaceLightGrenadeException
	 *         The lightgrenade cannot be placed here.
	 */
	public void useItem(IItem i) throws IllegalArgumentException, CannotPlaceLightGrenadeException;
}
