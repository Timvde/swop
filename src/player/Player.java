package player;

import item.IItem;
import item.lightgrenade.Explodable;
import item.lightgrenade.LightGrenade;
import item.teleporter.Teleportable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import powerfailure.AffectedByPowerFailure;
import square.AbstractSquare;
import square.Direction;
import square.NormalSquare;
import square.Square;
import square.SquareContainer;
import ObjectronExceptions.CannotPlaceLightGrenadeException;
import ObjectronExceptions.IllegalActionException;
import ObjectronExceptions.IllegalMoveException;
import ObjectronExceptions.IllegalStepException;
import ObjectronExceptions.IllegalUseException;
import ObjectronExceptions.InventoryFullException;
import ObjectronExceptions.ItemNotOnSquareException;

/**
 * Main character of the Tron game. A player carries an {@link Inventory
 * inventory} and is trailed by a {@link LightTrail light trail}. During the
 * game a player can perform
 * {@value PlayerDataBase#MAX_NUMBER_OF_ACTIONS_PER_TURN} actions during a turn.
 * These actions are {@link #moveInDirection(Direction) move},
 * {@link #pickUpItem(IItem) pickup} an item, {@link #useItem(IItem) use} an
 * item and {@link #endTurn() end} the turn.
 */
public class Player implements IPlayer, Teleportable, AffectedByPowerFailure, Explodable {
	
	/**
	 * The id of the player, not really used, but hey ... let's do something
	 * crazy FIXME DO we stil need the id? in GUI?
	 */
	private int						id;
	private static AtomicInteger	nextID	= new AtomicInteger();
	
	/** A boolean representing whether the player has moved */
	private boolean					hasMoved;
	/** The starting square of this player */
	private SquareContainer	startSquare;
	/** The square where the player is currently standing */
	private SquareContainer					currentSquare;
	/** The inventory of the player */
	private Inventory				inventory;
	/** The light trail of the player */
	private LightTrail				lightTrail;
	/** The state of the player */
	private PlayerState				state;
	/** The player database in which the player is placed */
	private PlayerDataBase			playerDB;
	
	/**
	 * Creates a new Player object, with an empty inventory, who has not yet
	 * moved and has an allowed number of actions of
	 * {@value PlayerDataBase#MAX_NUMBER_OF_ACTIONS_PER_TURN}. The default state
	 * of a player is {@link PlayerState#WAITING}. Until the state is set to
	 * {@link PlayerState#ACTIVE} the player will not be able to perform any
	 * action.
	 */
	// User cannot create players himself. This is the responsibility of
	// the PlayerDB --> constructor package access
	Player(PlayerDataBase playerDB, SquareContainer startingPosition)
			throws IllegalArgumentException {
		if (playerDB == null)
			throw new IllegalArgumentException("The database cannot be null");
		
		this.id = nextID.incrementAndGet();
		this.inventory = new Inventory();
		this.lightTrail = new LightTrail();
		this.hasMoved = false;
		this.state = PlayerState.WAITING;
		this.playerDB = playerDB;
		this.setStartingPosition(startingPosition);
	}
	
	/**
	 * This method is used to initiate the starting position of the player.
	 * 
	 * @param square
	 *        The square that should be the starting position of this player
	 */
	private void setStartingPosition(SquareContainer square) {
		if (square == null) {
			throw new IllegalArgumentException("the given square cannot be null");
		}
		this.startSquare = square;
		this.startSquare.addPlayer(this);
		this.currentSquare = square;
		
	}
	
	
	@Override 
	public void setSquare(SquareContainer square) {
		this.currentSquare = square;
	}
	
	@Override
	
	public int getID() {
		return id;
	}
	
	@Override
	public Square getStartingPosition() {
		return this.startSquare;
	}
	
	@Override
	public Square getCurrentLocation() {
		return this.currentSquare;
	}
	
	@Override
	public List<IItem> getInventoryContent() {
		return inventory.getItems();
	}
	
	/* ############## ActionHistory related methods ############## */
	
	
	@Override
	public int getAllowedNumberOfActions() {
		return playerDB.getAllowedNumberOfActions(this);
	}
	
	@Override
	public void skipNumberOfActions(int numberOfActionsToSkip) {
		playerDB.skipNumberOfActions(this, numberOfActionsToSkip);
	}
	
	@Override
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
	void resetHasMoved() {
		this.hasMoved = false;
	}
	
	/* ############## PlayerState related methods ############## */
	
	@Override
	public boolean canPerformAction() {
		return this.state == PlayerState.ACTIVE && getStartingPosition() != null
				&& getAllowedNumberOfActions() > 0;
	}
	
	/**
	 * Set the state of the player to a new specified state
	 * 
	 * @param state
	 *        the new state of the player
	 * 
	 * @throws IllegalArgumentException
	 *         The player must be allowed to switch his state from his current
	 *         state to the specified state. I.e.
	 *         <code>this.getState().isAllowedTransistionTo(state)</code>
	 */
	void setPlayerState(PlayerState state) throws IllegalArgumentException {
		if (!this.state.isAllowedTransistionTo(state)) {
			throw new IllegalArgumentException(
					"The player is not allowed to switch from the current state ("
							+ this.state.name() + ") to the specified state (" + state.name() + ")");
		}
		
		this.state = state;
	}
	
	/**
	 * Returns the current state the player is in.
	 * 
	 * @return The current state the player is in.
	 */
	public PlayerState getPlayerState() {
		return this.state;
	}
	
	/* #################### Move methods #################### */
	
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
	public void endTurn() throws IllegalActionException {
		if (!canPerformAction())
			throw new IllegalActionException("The player must be allowed to perform an action.");
		
		if (this.hasMovedYet()) {
			// this player's turn will end;
			lightTrail.updateLightTrail();
			playerDB.endPlayerTurn(this);
		}
		else {
			// setPlayerState will check if we can transition to the LOST state
			this.setPlayerState(PlayerState.LOST);
			this.playerDB.reportGameLost(this);
		}
	}
	
	@Override
	/**
	 * @throws IllegalStepException
	 *         The player must be able to move in the given direction on the
	 *         grid, i.e. {@link #canMoveInDirection(Direction)}.
	 * @throws IllegalMoveException
	 *         When the player can't be added to the square in the specified
	 *         direction, i.e. {@link Square#canAddPlayer()}.
	 */
	public void moveInDirection(Direction direction) throws IllegalActionException,
			IllegalMoveException {
		if (!canPerformAction())
			throw new IllegalActionException("The player must be allowed to perform an action.");
		if (!isValidDirection(direction))
			throw new IllegalArgumentException("The specified direction is not valid.");
		if (!canMoveInDirection(direction))
			throw new IllegalStepException("The player cannot move in given direction on the grid.");
		
		// Update the player's square
		currentSquare.remove(this);
		SquareContainer oldSquare = currentSquare;
		currentSquare = currentSquare.getNeighbourIn(direction);
		
		try {
			currentSquare.addPlayer(this);
		}
		catch (IllegalArgumentException e) {
			// Rollback before rethrowing an exception
			currentSquare.remove(this);
			oldSquare.addPlayer(this);
			currentSquare = oldSquare;
			throw new IllegalMoveException(
					"The player can't be added to the square in the specified direction.");
		}
		
		// Moving succeeded. Update other stuff.
		this.lightTrail.updateLightTrail(oldSquare);
		this.setHasMoved();
		playerDB.decreaseAllowedNumberOfActions(this);
	}
	
	@Override
	public boolean isValidDirection(Direction direction) {
		return direction != null;
	}
	
	@Override
	public boolean canMoveInDirection(Direction direction) {
		if (currentSquare.getNeighbourIn(direction) == null)
			return false;
		else if (!currentSquare.getNeighbourIn(direction).canAddPlayer())
			return false;
		else if (crossesLightTrail(direction))
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
	private boolean crossesLightTrail(Direction direction) {
		// test if we are moving sideways
		if (direction.getPrimaryDirections().size() != 2)
			return false;
		
		// test if the square has a neighbour in both directions
		else if (currentSquare.getNeighbourIn(direction.getPrimaryDirections().get(0)) == null)
			return false;
		else if (currentSquare.getNeighbourIn(direction.getPrimaryDirections().get(1)) == null)
			return false;
		
		// test if both of the neighbours have a light trail
		else if (!currentSquare.getNeighbourIn(direction.getPrimaryDirections().get(0))
				.hasLightTrail())
			return false;
		else if (!currentSquare.getNeighbourIn(direction.getPrimaryDirections().get(1))
				.hasLightTrail())
			return false;
		
		// it looks like the player crosses a light trail
		// he will not get away with this ...
		return true;
	}
	
	/* #################### PickUp method #################### */
	
	/**
	 * @throws ItemNotOnSquareException
	 *         The item must be {@link NormalSquare#contains(Object) on} the square
	 *         the player is currently on.
	 * @throws InventoryFullException
	 *         This players {@link Inventory} cannot be
	 *         {@link Inventory#getMaxNumberOfItems() full}.
	 */
	@Override
	public void pickUpItem(IItem item) throws IllegalActionException, IllegalArgumentException {
		if (!canPerformAction())
			throw new IllegalActionException("The player must be allowed to perform an action.");
		if (item == null)
			throw new IllegalArgumentException("The item cannot be null");
		if (!currentSquare.contains(item)) {
			throw new ItemNotOnSquareException("The specified item is not on the square.");
		}
		
		// remove the item from the square
		currentSquare.remove(item);
		
		try {
			// add the item to the inventory
			inventory.addItem(item);
		}
		catch (IllegalArgumentException e) {
			// the inventory is full, rollback
			currentSquare.addItem(item);
			throw new InventoryFullException("The item cannot be added to the inventory.");
		}
		
		// end the players action ...
		playerDB.decreaseAllowedNumberOfActions(this);
		this.lightTrail.updateLightTrail();
	}
	
	/* #################### UseItem method #################### */
	
	/**
	 * @throws IllegalUseException
	 *         The item must be in the {@link Inventory}.
	 * @throws CannotPlaceLightGrenadeException
	 *         When the specified item is a {@link LightGrenade} and it can't be
	 *         added to the square the player is currently standing on.
	 */
	@Override
	public void useItem(IItem i) throws IllegalStateException, IllegalArgumentException,
			CannotPlaceLightGrenadeException {
		if (!canPerformAction())
			throw new IllegalActionException("The player must be allowed to perform an action.");
		if (i == null) {
			throw new IllegalArgumentException("The specified item cannot be null.");
		}
		if (!inventory.hasItem(i))
			throw new IllegalUseException("The item is not in the inventory");
		
		// remove the item from the inventory
		inventory.removeItem(i);
		
		// try and use the item
		try {
			i.use(currentSquare);
		}
		catch (CannotPlaceLightGrenadeException e) {
			// re-add the item to the inventory and re-throw the exception
			inventory.addItem(i);
			throw e;
		}
		
		// end the players action ...
		playerDB.decreaseAllowedNumberOfActions(this);
		lightTrail.updateLightTrail();
	}
	
	@Override
	public String toString() {
		return "Player [id=" + id + ", allowedNumberOfActionsLeft=" + getAllowedNumberOfActions()
				+ ", hasMoved=" + hasMoved + ", currentSquare=" + currentSquare + ", state="
				+ state + "]";
	}
	
	/**
	 * Resets the unique Id counter. The next newly created player will have an
	 * ID of 0. This method should be called form the PlayerDb when it re-fills
	 * the database (package access).
	 */
	static void resetUniqueIdcounter() {
		nextID = new AtomicInteger();
	}
	
	@Override
	public Teleportable asTeleportable() {
		return this;
	}
	
	
	/**
	 * Returns whether the player can teleport to the specified square.
	 * 
	 * @param destination
	 *        the destination to test
	 * @return true if the player can teleport to the specified square, else
	 *         false
	 */
	public boolean canTeleportTo(AbstractSquare destination) {
		// test if the destination exists
		if (destination == null)
			return false;
		
		// test if the square accepts players
		else if (!destination.canAddPlayer())
			return false;
		// anything else?
		else
			return true;
	}
	
	/**
	 * Ends the turn of this player
	 */
	@Override
	public void damageByPowerFailure() {
		this.skipNumberOfActions(getAllowedNumberOfActions());
	}
	
	@Override
	public Explodable asExplodable() {
		return this;
	}
	
	@Override
	public AffectedByPowerFailure asAffectedByPowerFailure() {
		return this;
	}
	
	/**
	 * Sets all references of the player to null so that no-one can modify the
	 * game with an old player.
	 */
	void destroy() {
		this.playerDB = null;
		this.currentSquare = null;
		this.id = -1;
		this.inventory = null;
		this.lightTrail = null;
		this.state = null;
		
		if (currentSquare != null)
			this.currentSquare.remove(this);
	}
}
