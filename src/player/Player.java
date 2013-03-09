package player;

import grid.Coordinate;
import grid.Direction;
import grid.Grid;
import grid.Square;
import item.Item;

import java.util.List;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicInteger;

import notnullcheckweaver.NotNull;

/**
 * TODO: verantw voor: actions per TURN actions = move; pickup; use
 * 
 * en action history (boolean hasmovedyet)
 * 
 * 
 */
public class Player extends Observable implements IPlayer {

	public static final int MAX_NUMBER_OF_ACTIONS_PER_TURN = 3;

	@NotNull
	private int id; // TODO: hebben we id wel nodig??
	@NotNull
	private static AtomicInteger nextID = new AtomicInteger();
	@NotNull
	private Coordinate targetPosition; // TODO waar zetten?
	@NotNull
	private Inventory inventory = new Inventory();
	@NotNull
	private LightTrail lightTrail = new LightTrail();
	@NotNull
	private Grid grid; // TODO IGrid ofzo

	private int allowedNumberOfActionsLeft;
	private boolean hasMoved;

	// FIXME bij aanmaak van de players in PlayerDb is de coord onbekend
	@Deprecated
	public Player(@NotNull Coordinate targetPosition) {
		this.id = nextID.incrementAndGet();
		this.targetPosition = targetPosition;
	}

	/**
	 * Creates a new Player object with a unique id.
	 */
	public Player() {
		this.id = nextID.incrementAndGet();
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public Coordinate getTargetPosition() {
		return this.targetPosition;
	}

	@Override
	public List<Item> getInventory() {
		return inventory.getItems();
	}

	/* ############## ActionHistory related methods ############## */

	/**
	 * This method will increase the allowed number of actions left with
	 * {@link #MAX_NUMBER_OF_ACTIONS_PER_TURN} .
	 * 
	 * FIXME: mss al direct aanroepen bij endTurn, i.p.v. aan te roepen als
	 * appointed door PlayerDB ??
	 * 
	 * <> It is called when a Player is appointed the current Player by the
	 * {@link PlayerDataBase}.
	 */
	private void increaseAllowedNumberOfActions() {
		this.allowedNumberOfActionsLeft = allowedNumberOfActionsLeft
				+ MAX_NUMBER_OF_ACTIONS_PER_TURN;
	}

	/**
	 * Called when this player performed an action, his allowed number of
	 * actions must drop by one.
	 * 
	 * If after decreasing the allowed number of actions by one, this player has
	 * no more actions left, he will notify the {@link PlayerDataBase} to
	 * indicate he wants to end his turn.
	 */
	private void decreaseAllowedNumberOfActions() {
		this.allowedNumberOfActionsLeft--;

		// If no more actions left, notify the PlayerDataBase to ask to end this
		// player's turn
		if (allowedNumberOfActionsLeft <= 0) {
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * Return whether or not this player has already done a move action during
	 * this turn.
	 * 
	 * @return whether or not this player has already done a move action during
	 *         this turn.
	 */
	public boolean hasMovedYet() {
		return this.hasMoved;
	}

	/**
	 * To be called when the player performed a move-action succesfully.
	 * 
	 * PostCondtion: {@link #hasMoved this.hasMoved()}= true
	 */
	private void setHasMoved() {
		this.hasMoved = true;
	}

	/**
	 * To be called when the player's turn end, to reset the hasMoved history.
	 * 
	 * Postcondition: {@link #hasMoved this.hasMoved()} = false
	 */
	private void resetHasMoved() {
		this.hasMoved = false;
	}

	/* #################### User methods #################### */

	/**
	 * This method ends the turn of this player. This player will lose the game
	 * if this method is called before he did a move action, (i.e. if
	 * <code>this.hasMovedYet()</code> is false when calling this method, this
	 * player loses the game).
	 * 
	 * @throws IllegalStateException
	 *             The end turn preconditions must be satisfied, i.e.
	 *             {@link #isPreconditionEndTurnSatisfied()}
	 */
	public void endTurn() throws IllegalStateException {
		if (!isPreconditionEndTurnSatisfied()) {
			throw new IllegalStateException(
					"The endTurn-preconditions are not satisfied.");
		}

		if (this.hasMovedYet()) {
			// this player's turn will end; reset the turn-related properties
			this.resetHasMoved();
			this.increaseAllowedNumberOfActions();

			// notify the PlayerDataBase to ask to end this player's turn
			this.setChanged();
			this.notifyObservers();
		} else {
			// this player loses the game
			// FIXME de player moet nu aan de game vragen om te verliezen ??
			// dus een verwijzing naar Game ??
		}
	}

	/**
	 * Returns whether this player is allowed to perform an end turn action.
	 * 
	 * @return whether this player has performed less then
	 *         {@link #MAX_NUMBER_OF_ACTIONS_PER_TURN} in his current turn.
	 */
	public boolean isPreconditionEndTurnSatisfied() {
		return this.allowedNumberOfActionsLeft > 0;
	}

	/**
	 * This method moves the player one square in the specified
	 * {@link Direction}.
	 * 
	 * @param direction
	 *            the direction to move in
	 * @throws IllegalStateException
	 *             The move preconditions must be satisfied, i.e. this.
	 *             {@link #isPreconditionMoveSatisfied()}
	 */
	public void moveInDirection(Direction direction)
			throws IllegalStateException {
		if (!isPreconditionMoveSatisfied()) {
			throw new IllegalStateException(
					"The move-preconditions are not satisfied.");
		}
		Coordinate updatedCoordinate = this.grid.movePlayerInDirection(this,
				direction);
		this.lightTrail.updateLightTrail(updatedCoordinate);
		this.setHasMoved();
		this.decreaseAllowedNumberOfActions();
	}

	/**
	 * Returns whether this player is allowed to perform a move action.
	 * 
	 * @return whether this player has performed less then
	 *         {@link #MAX_NUMBER_OF_ACTIONS_PER_TURN} in his current turn.
	 */
	public boolean isPreconditionMoveSatisfied() {
		return this.allowedNumberOfActionsLeft > 0;
	}

	public void pickUpItem(Item item) {
		Square playerSq = this.grid.getSquareOfPlayer(this);
		playerSq.removeItem(item);
	}
	
}
