package player.actions;

import player.TronPlayer;
import square.Direction;
import square.SquareContainer;
import ObjectronExceptions.IllegalActionException;
import ObjectronExceptions.IllegalMoveException;
import ObjectronExceptions.IllegalStepException;

/**
 * Move action lets a player move on the grid in a direction.
 * 
 */
public class MoveAction implements Action {
	
	private Direction	direction;
	
	/**
	 * Create a new move action to move the player in a specified direction.
	 * This direction cannot be null.
	 * 
	 * @param direction
	 *        the direction in which the player will move
	 */
	public MoveAction(Direction direction) {
		if (direction == null)
			throw new IllegalArgumentException();
		this.direction = direction;
	}
	
	/**
	 * This method moves the specified player one {@link SquareContainer square}
	 * in a specified {@link Direction}. More formally the postconditions are:
	 * 
	 * <li>The new location of this player is
	 * <code>{@link TronPlayer#getCurrentLocation() 
	 * currentLocationBefore}{@link SquareContainer#getNeighbourIn(Direction) 
	 * .getNeighbour(direction)}</code>.</li>
	 * 
	 * <li>The player will have done a move during this turn:
	 * <code>{@link TronPlayer#hasMovedYet()} = true</code>
	 * 
	 * @throws IllegalActionException
	 *         This player must be allowed to perform an action, i.e. <code>
	 *         {@link player#canPerformAction()}</code>.
	 * @throws IllegalMoveException
	 *         The player must be allowed to perform a move in the specified
	 *         direction.
	 */
	public void execute(TronPlayer player) {
		SquareContainer square = (SquareContainer) player.getCurrentLocation();
		if (!player.canPerformAction(this))
			throw new IllegalActionException("The player must be allowed to perform an action.");
		if (!isValidDirection(direction))
			throw new IllegalArgumentException("The specified direction is not valid.");
		if (!canMoveInDirection(square, direction))
			throw new IllegalStepException("The player cannot move in given direction on the grid.");
		
		// Update the player's square
		square.remove(player);
		SquareContainer oldSquare = square;
		square = square.getNeighbourIn(direction);
		player.setSquare(square);
		
		try {
			square.addPlayer(player);
		}
		catch (IllegalArgumentException e) {
			// Rollback before rethrowing an exception
			square.remove(player);
			oldSquare.addPlayer(player);
			player.setSquare(oldSquare);
			throw new IllegalMoveException(
					"The player can't be added to the square in the specified direction.");
		}
		
		// Moving succeeded. Update other stuff.
		player.setHasMoved();
	}
	
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
	private boolean isValidDirection(Direction direction) {
		return direction != null;
	}
	
	/**
	 * Test whether the player can move in the specified direction on the grid.
	 * 
	 * @param direction
	 *        the direction in which the player wants to move
	 * @return whether the player can move in the specified direction.
	 */
	private boolean canMoveInDirection(SquareContainer square, Direction direction) {
		if (square.getNeighbourIn(direction) == null)
			return false;
		else if (!square.getNeighbourIn(direction).canAddPlayer())
			return false;
		else if (crossesLightTrail(square, direction))
			return false;
		// darn, we could not stop the player moving
		// better luck next time
		return true;
	}
	
	/**
	 * Returns whether the player would cross a light trail if he moved in the
	 * specified direction
	 * 
	 * @param direction
	 *        The direction to test
	 * @return True if the player crosses a light trail, otherwise false
	 */
	private boolean crossesLightTrail(SquareContainer square, Direction direction) {
		// test if we are moving sideways
		if (direction.getPrimaryDirections().size() != 2)
			return false;
		
		// test if the square has a neighbour in both directions
		else if (square.getNeighbourIn(direction.getPrimaryDirections().get(0)) == null)
			return false;
		else if (square.getNeighbourIn(direction.getPrimaryDirections().get(1)) == null)
			return false;
		
		// test if both of the neighbours have a light trail
		else if (!square.getNeighbourIn(direction.getPrimaryDirections().get(0)).hasLightTrail())
			return false;
		else if (!square.getNeighbourIn(direction.getPrimaryDirections().get(1)).hasLightTrail())
			return false;
		
		// it looks like the player crosses a light trail
		// he will not get away with this ...
		return true;
	}

	@Override
	public int getCost() {
		return 1;
	}
}
