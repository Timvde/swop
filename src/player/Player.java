package player;

import grid.ASquare;
import grid.AffectedByPowerFailure;
import grid.Coordinate;
import grid.Direction;
import grid.IGrid;
import grid.Square;
import grid.TronObject;
import item.IItem;
import item.lightgrenade.Explodable;
import item.teleporter.Teleportable;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicInteger;
import ObjectronExceptions.IllegalMoveException;
import notnullcheckweaver.NotNull;

/**
 * Main character of the Tron game. A player carries an {@link Inventory
 * inventory} and is trailed by a {@link LightTrail light trail}. During the
 * game a player can perform {@value #MAX_NUMBER_OF_ACTIONS_PER_TURN} actions
 * during a turn. These actions are {@link #moveInDirection(Direction) move},
 * {@link #pickUpItem(IItem) pickup} an item, {@link #useItem(IItem) use} an
 * item and {@link #endTurn() end} the turn.
 */
public class Player extends Observable implements IPlayer, Teleportable, AffectedByPowerFailure,
		Explodable {
	
	/**
	 * The maximum number of actions a Player is allowed to do in one turn.
	 */
	public static final int			MAX_NUMBER_OF_ACTIONS_PER_TURN	= 3;
	
	private int						id;
	private static AtomicInteger	nextID							= new AtomicInteger();
	
	private int						allowedNumberOfActionsLeft;
	private boolean					hasMoved;
	private final Square			startSquare;
	private Square					currentSquare;
	private Inventory				inventory;
	private LightTrail				lightTrail;
	private IGrid					grid;
	
	/*
	 * // FIXME bij aanmaak van de players in PlayerDb is de coord onbekend
	 * 
	 * @Deprecated public Player(@NotNull Coordinate targetPosition) {
	 * this.targetPosition = targetPosition; this.id = nextID.incrementAndGet();
	 * this.inventory = new Inventory(); this.lightTrail = new LightTrail(); }
	 */
	
	/**
	 * Creates a new Player object, with an empty inventory, who has not yet
	 * moved and has an allowed nb of actions of
	 * {@value #MAX_NUMBER_OF_ACTIONS_PER_TURN}. The specified coordinate is the
	 * starting position of the player.
	 * 
	 * @param startSquare
	 *        The starting position of the player
	 * @param grid
	 *        The grid the player will move on
	 * @throws IllegalArgumentException
	 *         The given arguments cannot be null.
	 * @throws IllegalStateException
	 *         The given coordinate must exist on the given grid
	 */
	public Player(Square startSquare, IGrid grid)
			throws IllegalArgumentException, IllegalStateException {
		if (startSquare == null || grid == null) {
			throw new IllegalArgumentException("The given arguments cannot be null");
		}
//		if (grid.getSquareAt(startSquare) == null) {
//			throw new IllegalStateException("The given coordinate must exist on the given grid");
//		}
		
		this.id = nextID.incrementAndGet();
		this.inventory = new Inventory();
		this.lightTrail = new LightTrail();
		this.hasMoved = false;
		this.allowedNumberOfActionsLeft = MAX_NUMBER_OF_ACTIONS_PER_TURN;
		this.startSquare = startSquare;
		this.currentSquare = startSquare;
		this.grid = grid;
	}
	
	private IGrid getGrid() {
		return grid;
	}
	
	@Override
	public int getID() {
		return id;
	}
	
	@Override
	public ASquare getStartingPosition() {
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
	
	// TODO: change the literal 2 into something better.
	/**
	 * This method will under normal circumstances increase the allowed number
	 * of actions left with {@link #MAX_NUMBER_OF_ACTIONS_PER_TURN}. When the
	 * Player is on a power failured square, the number of actions lost will be
	 * 2. It is called when a Player calls {@link #endTurn()} to reset his turn
	 * related fields.
	 */
	private void increaseAllowedNumberOfActions() {
		// increase the number of actions left by the number of actions per turn
		// this cannot be more then the max number of actions
		this.allowedNumberOfActionsLeft = Math.min(allowedNumberOfActionsLeft
				+ MAX_NUMBER_OF_ACTIONS_PER_TURN, MAX_NUMBER_OF_ACTIONS_PER_TURN);
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
			this.setChanged();
			this.notifyObservers();
			// We need to increase it again to prepare for this player's next
			// turn.
			this.increaseAllowedNumberOfActions();
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
	
	/* #################### User methods #################### */
	
	@Override
	public void endTurn() throws IllegalStateException {
		if (!isPreconditionEndTurnSatisfied()) {
			throw new IllegalStateException("The endTurn-preconditions are not satisfied.");
		}
		
		if (this.hasMovedYet()) {
			// this player's turn will end; reset the turn-related properties
			this.resetHasMoved();
			resetNumberOfActionsLeft();
			lightTrail.updateLightTrail();
		}
		else {
			// TODO player loses the game
			System.out.println("Player " + getID() + " loses the game!");
		}
	}
	
	/**
	 * This sets the number of actions a player has left to zero. This will end
	 * a player's turn.
	 */
	private void resetNumberOfActionsLeft() {
		skipNumberOfActions(getAllowedNumberOfActions());
	}
	
	@Override
	public boolean isPreconditionEndTurnSatisfied() {
		return getAllowedNumberOfActions() > 0;
	}
	
	@Override
	public void moveInDirection(Direction direction) throws IllegalMoveException {
		if (!isPreconditionMoveSatisfied()) {
			throw new IllegalMoveException("The move-preconditions are not satisfied.");
		}
		if (!isValidDirection(direction)) {
			throw new IllegalMoveException("The specified direction is not valid.");
		}
		if (!getGrid().canMoveFromCoordInDirection(this.currentCoord, direction)) {
			throw new IllegalMoveException("The player cannot move in given direction on the grid.");
		}
		
		// remove this player form his current square
		currentSquare.removePlayer();
		
		// set new position
		// FIXME see issue#39
		Square newSquare = (Square) currentSquare.getNeighbour(direction);
		
		// the player is moving 
		this.setHasMoved();
		
		// add the player to the new square
		newSquare.addPlayer(this);
		
		// update the light trail of this player 
		this.lightTrail.updateLightTrail(currentSquare);
		
		// set the new square of the player
		currentSquare = newSquare;
		
		// end the players action ... 
		decreaseAllowedNumberOfActions();
	}
	
	@Override
	public boolean isValidDirection(Direction direction) {
		return direction != null;
	}
	
	@Override
	public boolean isPreconditionMoveSatisfied() {
		return getAllowedNumberOfActions() > 0;
	}
	
	@Override
	public void pickUpItem(IItem item) {
		if (item == null || !currentSquare.contains(item))
			throw new IllegalArgumentException("The item does not exist on the square");
		
		// remove the item from the square
		currentSquare.remove(item);
		
		try {
			// add the item to the inventory
			inventory.addItem(item);
		}
		catch (IllegalArgumentException e) {
			// the inventory is full, re-add the item
			currentSquare.addItem(item);
			throw e;
		}
		
		// end the players action ... 
		decreaseAllowedNumberOfActions();
		this.lightTrail.updateLightTrail();
	}
	
	@Override
	public void useItem(IItem i) {
		if (!inventory.hasItem(i))
			throw new IllegalArgumentException("The item is not in the inventory");
		// TODO are there any other exceptions?
		
		// remove the item from the inventory
		inventory.removeItem(i);
		
		// try and use the item 
		try {
			i.use(currentSquare);
		}
		catch (IllegalStateException e) {
			// re-add the item to the inventory and re-throw the exception
			inventory.addItem(i);
			throw e;
		}
		
		// end the players action ... 
		this.decreaseAllowedNumberOfActions();
		lightTrail.updateLightTrail();
	}
	
	/**
	 * resets the player for a new game. The inventory and the lightTrail will
	 * be reinitialized. The number of actions left is set to
	 * {@link #MAX_NUMBER_OF_ACTIONS_PER_TURN}. Also {@link #hasMovedYet()} will
	 * return false.oldSquare
	 */
	public void reset() {
		inventory = new Inventory();
		lightTrail = new LightTrail();
		
		allowedNumberOfActionsLeft = MAX_NUMBER_OF_ACTIONS_PER_TURN;
		hasMoved = false;
	}
	
	@Override
	public String toString() {
		return "ID = " + this.id;
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
		currentSquare.remove(this);
		currentSquare = (Square) destination;
		destination.addPlayer(this);
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
}
