package player.actions;

import item.forcefieldgenerator.ForceField;
import item.forcefieldgenerator.ForceFieldState;
import player.TronPlayer;
import square.Direction;
import square.Property;
import square.PropertyType;
import square.SquareContainer;
import ObjectronExceptions.IllegalActionException;
import ObjectronExceptions.IllegalMoveException;
import ObjectronExceptions.IllegalStepException;
import ObjectronExceptions.IllegalTeleportException;

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
	 * <code>{@link TronPlayer#getCurrentPosition() 
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
		SquareContainer square = (SquareContainer) player.getCurrentPosition();
		if (!player.canPerformAction()) {
			throw new IllegalActionException("The player must be allowed to perform an action.");
		}
		if (!isValidDirection(direction))
			throw new IllegalArgumentException("The specified direction is not valid.");
		if (!canMoveInDirection(square, direction))
			throw new IllegalStepException("The player cannot move in given direction on the grid.");
		
		// Update the player's square
		try {
			square.remove(player);
		}
		catch (IllegalStateException e) {
			throw new IllegalMoveException("The player can't move away from his square. "
					+ e.getMessage());
		}
		
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
		catch (IllegalTeleportException e) {
			// Rollback before rethrowing an exception
			square.remove(player);
			oldSquare.addPlayer(player);
			player.setSquare(oldSquare);
			
			// Throw the exception as a move exception from here, so the GUI can
			// notify
			// the players.
			throw new IllegalMoveException(e.getMessage());
		}
		catch (IllegalMoveException e) {
			// Rollback before rethrowing an exception
			oldSquare.addPlayer(player);
			player.setSquare(oldSquare);
			
			throw e;
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
		if (square.getNeighbourIn(direction) == null) {
			return false;
		}
		else if (!square.getNeighbourIn(direction).canAddPlayer()) {
			return false;
		}
		else if (crossesLightTrail(square, direction)) {
			return false;
		}
		else if (crossesForceField(square, direction)) {
			return false;
		}
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
		
		boolean neighbour1HasLightTrail = false;
		boolean neighbour2HasLightTrail = false;
		boolean neighbour1HasPlayer = false;
		boolean neighbour2HasPlayer = false;
		
		// test if both of the neighbours have a light trail or contain the
		// player whose LT
		// that is
		if (square.getNeighbourIn(direction.getPrimaryDirections().get(0)).hasProperty(
				PropertyType.LIGHT_TRAIL))
			neighbour1HasLightTrail = true;
		
		if (square.getNeighbourIn(direction.getPrimaryDirections().get(0)).hasPlayer())
			neighbour1HasPlayer = true;
		
		if (square.getNeighbourIn(direction.getPrimaryDirections().get(1)).hasProperty(
				PropertyType.LIGHT_TRAIL))
			neighbour2HasLightTrail = true;
		
		if (square.getNeighbourIn(direction.getPrimaryDirections().get(1)).hasPlayer())
			neighbour2HasPlayer = true;
		
		// it looks like the player crosses a light trail
		// he will not get away with this ...
		return ((neighbour1HasLightTrail && neighbour2HasLightTrail) 
				|| (neighbour1HasLightTrail && neighbour2HasPlayer) 
				|| (neighbour1HasPlayer && neighbour2HasLightTrail));
	}
	
	/**
	 * Returns whether the player would cross a force field if he moved in the
	 * specified direction
	 * 
	 * @param direction
	 *        The direction to test
	 * @return True if the player crosses a force field, otherwise false
	 */
	private boolean crossesForceField(SquareContainer square, Direction direction) {
		// test if we are moving sideways
		if (direction.getPrimaryDirections().size() != 2)
			return false;
		
		// test if the square has a neighbour in both directions
		else if (square.getNeighbourIn(direction.getPrimaryDirections().get(0)) == null)
			return false;
		else if (square.getNeighbourIn(direction.getPrimaryDirections().get(1)) == null)
			return false;
		
		SquareContainer neighbour1 = square.getNeighbourIn(direction.getPrimaryDirections().get(0));
		SquareContainer neighbour2 = square.getNeighbourIn(direction.getPrimaryDirections().get(1));
		
		boolean neighbour1HasForcefield = false;
		boolean neighbour2HasForcefield = false;
		
		for (Property property : neighbour1.getProperties())
			if (property instanceof ForceField
					&& ((ForceField) property).getState() == ForceFieldState.ACTIVE)
				neighbour1HasForcefield = true;
		
		for (Property property : neighbour2.getProperties())
			if (property instanceof ForceField
					&& ((ForceField) property).getState() == ForceFieldState.ACTIVE)
				neighbour2HasForcefield = true;
		
		return (neighbour1HasForcefield && neighbour2HasForcefield);
	}
}
