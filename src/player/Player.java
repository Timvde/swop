package player;

import grid.Coordinate;
import grid.Direction;
import grid.Grid;
import item.Item;

import java.util.List;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicInteger;

import notnullcheckweaver.NotNull;

public class Player extends Observable implements IPlayer {

	public static final int MAX_NUMBER_OF_ACTIONS_PER_TURN = 3;

	@NotNull
	private int id;
	@NotNull
	private static AtomicInteger nextID = new AtomicInteger();
	@NotNull
	private Coordinate targetPosition;
	@NotNull
	private Inventory inventory = new Inventory();
	@NotNull
	private LightTrail lightTrail = new LightTrail();
	private int allowedNumberOfActionsLeft;
	@NotNull
	private Grid grid;

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

	/**
	 * This method will increase the allowed number of actions left with
	 * MAX_NUMBER_OF_ACTIONS_PER_TURN
	 */
	void increaseAllowedNumberOfActions() {
		this.allowedNumberOfActionsLeft = allowedNumberOfActionsLeft
				+ MAX_NUMBER_OF_ACTIONS_PER_TURN;
	}

	private void decreaseAllowedNumberOfActions() {
		this.allowedNumberOfActionsLeft--;
	}

	/**
	 * TODO
	 */
	public void endTurn() {

		// notify the PlayerDataBase to ask to end the player's turn
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * TODO
	 * 
	 * @param direction
	 */
	public void moveInDirection(Direction direction) {
		if (!isPreconditionMoveSatisfied()) {
			throw new IllegalStateException(
					"The move-preconditions are not satisfied.");
		}
		Coordinate updatedCoordinate = this.grid.movePlayerInDirection(this, direction);
		this.lightTrail.updateLightTrail(updatedCoordinate);
		this.decreaseAllowedNumberOfActions();
	}

	/**
	 * Returns whether this player has performed less then
	 * MAX_NUMBER_OF_ACTIONS_PER_TURN in his current turn.
	 * 
	 * @return whether this player has performed less then
	 *         MAX_NUMBER_OF_ACTIONS_PER_TURN in his current turn.
	 */
	private boolean isPreconditionMoveSatisfied() {
		return this.allowedNumberOfActionsLeft > 0;
	}

}
