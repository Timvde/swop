package player;

import item.IItem;
import item.lightgrenade.Explodable;
import item.teleporter.Teleportable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import square.ASquare;
import square.AffectedByPowerFailure;
import square.Direction;
import square.ISquare;
import square.Square;
import square.WallPart;
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
 * game a player can perform {@value #MAX_NUMBER_OF_ACTIONS_PER_TURN} actions
 * during a turn. These actions are {@link #moveInDirection(Direction) move},
 * {@link #pickUpItem(IItem) pickup} an item, {@link #useItem(IItem) use} an
 * item and {@link #endTurn() end} the turn.
 */
public class Player implements IPlayer, Teleportable, AffectedByPowerFailure, Explodable {
	
	/** The maximum number of actions a Player is allowed to do in one turn */
	public static final int			MAX_NUMBER_OF_ACTIONS_PER_TURN		= 3;
	
	/**
	 * The number of actions that a player loses if he is standing on a power
	 * failured square at the start of his turn.
	 */
	private static final int		POWER_FAILURE_PENALTY_AT_START_TURN	= 1;
	
	/**
	 * The id of the player, not really used, but hey ... let's do something
	 * crazy FIXME DO we stil need the id? in GUI?
	 */
	private int						id;
	private static AtomicInteger	nextID								= new AtomicInteger();
	
	/** The number of actions the player has left during this turn */
	private int						allowedNumberOfActionsLeft;
	/** A boolean representing whether the player has moved */
	private boolean					hasMoved;
	/** The starting square of this player */
	private ASquare					startSquare;
	/** The square where the player is currently standing */
	private ASquare					currentSquare;
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
	 * {@value #MAX_NUMBER_OF_ACTIONS_PER_TURN}.The default state of a player is
	 * {@link PlayerState#WAITING}. One has to call
	 * {@link #setStartingPosition(ASquare)} to set the starting position. Until
	 * then and until the state is set to {@link PlayerState#ACTIVE} it will not
	 * be able to perform any action.
	 */
	// User cannot create players himself. This is the responsability of
	// the PlayerDB --> constructor package access
	Player(PlayerDataBase playerDB) throws IllegalArgumentException {
		if (playerDB == null)
			throw new IllegalArgumentException("The database cannot be null");
		
		this.id = nextID.incrementAndGet();
		this.inventory = new Inventory();
		this.lightTrail = new LightTrail();
		this.hasMoved = false;
		this.allowedNumberOfActionsLeft = MAX_NUMBER_OF_ACTIONS_PER_TURN;
		this.state = PlayerState.WAITING;
		this.playerDB = playerDB;
	}
	
	@Override
	public int getID() {
		return id;
	}
	
	/**
	 * This method is used to initiate the starting position of the player. It
	 * can be called only once.
	 * 
	 * @param square
	 *        The square that should be the starting position of this player
	 * @throws IllegalStateException
	 *         When the player already has a starting position set
	 */
	public void setStartingPosition(ASquare square) throws IllegalStateException {
		if (getStartingPosition() != null)
			throw new IllegalStateException("This player already has a starting position set");
		this.startSquare = square;
		this.currentSquare = square;
	}
	
	@Override
	public ISquare getStartingPosition() {
		return this.startSquare;
	}
	
	@Override
	public ASquare getCurrentLocation() {
		return this.currentSquare;
	}
	
	@Override
	public List<IItem> getInventoryContent() {
		return inventory.getItems();
	}
	
	/* ############## ActionHistory related methods ############## */
	
	/**
	 * This method will under normal circumstances increase the allowed number
	 * of actions left with {@link #MAX_NUMBER_OF_ACTIONS_PER_TURN}.
	 * 
	 * When this player is on a power failured square, the number of actions
	 * lost will be {@value #POWER_FAILURE_PENALTY_AT_START_TURN}.
	 * 
	 * This method is called by the {@link PlayerDataBase} when it assigns the
	 * next player.
	 */
	public void assignNewTurn() {
		// rest the turn related properties
		this.resetHasMoved();
		this.setPlayerState(PlayerState.ACTIVE);
		
		// increase the number of actions left by the number of actions per turn
		// this cannot be more then the max number of actions
		this.allowedNumberOfActionsLeft = Math.min(allowedNumberOfActionsLeft
				+ MAX_NUMBER_OF_ACTIONS_PER_TURN, MAX_NUMBER_OF_ACTIONS_PER_TURN);
		
		// If the player is on a square with a power failure, it receives a
		// penalty
		if (this.getCurrentLocation().hasPowerFailure()) {
			/*
			 * decrease the allowed number of actions by the right amount (do
			 * not use the method skipNumberOfActions() as this will call
			 * checkEndTurn() and thus the checkEndTurn() below might throw an
			 * exception
			 */
			this.allowedNumberOfActionsLeft -= POWER_FAILURE_PENALTY_AT_START_TURN;
		}
		
		// allowed nb actions could be <= 0 because of penalties
		checkEndTurn();
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
		skipNumberOfActions(1);
	}
	
	@Override
	public int getAllowedNumberOfActions() {
		return this.allowedNumberOfActionsLeft;
	}
	
	@Override
	public void skipNumberOfActions(int numberOfActionsToSkip) {
		this.allowedNumberOfActionsLeft -= numberOfActionsToSkip;
		checkEndTurn();
	}
	
	/**
	 * This method checks if a player has any actions left. If not, it ends its
	 * turn.
	 */
	private void checkEndTurn() {
		if (getAllowedNumberOfActions() <= 0) {
			// this method will return if it's not this player's turn
			playerDB.endPlayerTurn(this);
		}
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
	private void resetHasMoved() {
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
					"The player is not allowed to switch his state from the current state "
							+ this.state.name() + " to the specified state " + state.name());
		}
		
		// set the player state
		this.state = state;
	}
	
	/**
	 * Returns the current state the player is in. (For testing purposes)
	 * 
	 * @return The current state the player is in.
	 */
	PlayerState getPlayerState() {
		return this.state;
	}
	
	/* #################### Move methods #################### */
	
	@Override
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
	public void moveInDirection(Direction direction) throws IllegalActionException,
			IllegalMoveException, IllegalStepException {
		if (!canPerformAction())
			throw new IllegalActionException("The player must be allowed to perform an action.");
		if (!isValidDirection(direction))
			throw new IllegalArgumentException("The specified direction is not valid.");
		if (!canMoveInDirection(direction))
			throw new IllegalStepException("The player cannot move in given direction on the grid.");
		
		// remove this player from his current square
		currentSquare.remove(this);
		
		// set new position
		ASquare oldSquare = currentSquare;
		currentSquare = currentSquare.getNeighbour(direction);
		
		try {
			// add the player to the new square
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
		
		// update the light trail of this player
		this.lightTrail.updateLightTrail(oldSquare);
		
		// the player is moving
		this.setHasMoved();
		
		// end the players action ...
		decreaseAllowedNumberOfActions();
	}
	
	@Override
	public boolean isValidDirection(Direction direction) {
		return direction != null;
	}
	
	@Override
	public boolean canMoveInDirection(Direction direction) {
		// test whether a square in the specified direction exists
		if (currentSquare.getNeighbour(direction) == null)
			return false;
		// test if there is a player in the specified direction
		else if (currentSquare.getNeighbour(direction).hasPlayer())
			return false;
		// test if the square in the specified direction is a wallpart
		else if (currentSquare.getNeighbour(direction) instanceof WallPart)
			return false;
		// test if the square in the specified direction has a light trail
		else if (currentSquare.getNeighbour(direction).hasLightTrail())
			return false;
		// test if the player would cross a light trail by moving in the
		// specified direction
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
	 *        the direction to test
	 * @return true if the player crosses a light trail, else false
	 */
	private boolean crossesLightTrail(Direction direction) {
		// test if we are moving sideways
		if (direction.getPrimeryDirections().size() != 2)
			return false;
		
		// test if the square has a neighbour in both directions
		else if (currentSquare.getNeighbour(direction.getPrimeryDirections().get(0)) == null)
			return false;
		else if (currentSquare.getNeighbour(direction.getPrimeryDirections().get(1)) == null)
			return false;
		
		// test if both of the neighbours have a light trail
		else if (!currentSquare.getNeighbour(direction.getPrimeryDirections().get(0))
				.hasLightTrail())
			return false;
		else if (!currentSquare.getNeighbour(direction.getPrimeryDirections().get(1))
				.hasLightTrail())
			return false;
		
		// it looks like the player crosses a light trail
		// he will not get away with this ...
		return true;
	}
	
	/* #################### PickUp method #################### */
	
	@Override
	public void pickUpItem(IItem item) throws IllegalActionException, IllegalArgumentException,
			ItemNotOnSquareException, InventoryFullException {
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
		decreaseAllowedNumberOfActions();
		this.lightTrail.updateLightTrail();
	}
	
	/* #################### UseItem method #################### */
	
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
		this.decreaseAllowedNumberOfActions();
		lightTrail.updateLightTrail();
	}
	
	@Override
	public String toString() {
		return "Player [id=" + id + ", allowedNumberOfActionsLeft=" + allowedNumberOfActionsLeft
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
	
	@Override
	public void teleportTo(ASquare destination) {
		if (!canTeleportTo(destination))
			throw new IllegalArgumentException("Player could not teleport to destination: "
					+ destination);
		currentSquare.remove(this);
		currentSquare = (Square) destination;
		destination.addPlayer(this);
	}
	
	/**
	 * Returns whether the player can teleport to the specified square.
	 * 
	 * @param destination
	 *        the destination to test
	 * @return true if the player can teleport to the specified square, else
	 *         false
	 */
	public boolean canTeleportTo(ASquare destination) {
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
		this.endTurn();
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
		this.currentSquare.remove(this);
		this.currentSquare = null;
		this.id = -1;
		this.inventory = null;
		this.lightTrail = null;
		this.state = null;
	}
}
