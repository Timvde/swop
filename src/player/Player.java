package player;

import grid.Coordinate;
import grid.Direction;
import grid.Grid;
import grid.Square;
import item.IItem;
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
	
	public static final int			MAX_NUMBER_OF_ACTIONS_PER_TURN	= 3;
	
	private int						id;
	
	private static AtomicInteger	nextID							= new AtomicInteger();
	
	//TODO targetpos weg bij player?
	@NotNull
	private Coordinate				targetPosition;
	@NotNull
	private Inventory				inventory;
	private LightTrail				lightTrail;
	//TODO IGrid
	@NotNull
	private Grid					grid;
																							
	private int						allowedNumberOfActionsLeft;
	private boolean					hasMoved;
	
	@NotNull
	private Coordinate				currentCoord;
	
	// FIXME bij aanmaak van de players in PlayerDb is de coord onbekend
	@Deprecated
	public Player(@NotNull Coordinate targetPosition) {
		this.targetPosition = targetPosition;
		this.id = nextID.incrementAndGet();
		this.inventory = new Inventory();
		this.lightTrail = new LightTrail();
	}
	
	/**
	 * Creates a new Player object, with an empty inventory and who has not yet
	 * moved and has an allowed nb of actions of
	 * {@link #MAX_NUMBER_OF_ACTIONS_PER_TURN}
	 */
	public Player() {
		this.hasMoved = false;
		this.allowedNumberOfActionsLeft = MAX_NUMBER_OF_ACTIONS_PER_TURN;
		this.id = nextID.incrementAndGet();
		this.inventory = new Inventory();
		this.lightTrail = new LightTrail();
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
	public List<IItem> getInventoryContent() {
		return inventory.getItems();
	}
	
	/* ############## ActionHistory related methods ############## */
	
	/**
	 * This method will increase the allowed number of actions left with
	 * {@link #MAX_NUMBER_OF_ACTIONS_PER_TURN}. It is called when a Player calls
	 * {@link #endTurn()} to reset his turn related fields.
	 */
	private void increaseAllowedNumberOfActions() {
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
			// The previous method will make the player lose its turn. We need
			// to increase it again to prepare for this player's next turn.
			this.increaseAllowedNumberOfActions();
			
		}
		else {
			// this player loses the game
			// FIXME de player moet nu aan de game vragen om te verliezen ??
			// dus een verwijzing naar Game ?? of observers
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
	public void moveInDirection(Direction direction) throws IllegalStateException {
		if (!isPreconditionMoveSatisfied()) {
			throw new IllegalStateException("The move-preconditions are not satisfied.");
		}
		if (!isValidDirection(direction)) {
			throw new IllegalArgumentException("The specified direction is not valid.");
		}
		
		if (!grid.canMoveFromCoordInDirection(this.currentCoord, direction)) {
			throw new IllegalStateException("The player cannot move in given direction on the grid.");
		}
		
		// remove this player form his current square
		((Square) this.grid.getSquareAt(this.currentCoord)).removePlayer();
		
		// set new position
		this.currentCoord = this.currentCoord.getCoordinateInDirection(direction);
		Square newSquare = (Square) this.grid.getSquareAt(this.currentCoord);
		newSquare.setPlayer(this);
		
		// update fields
		this.lightTrail.updateLightTrail(this.currentCoord);
		this.setHasMoved();
		this.decreaseAllowedNumberOfActions();
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
		// Square playerSq = this.grid.getSquareOfPlayer(this); TODO implement
		// playerSq.removeItem(item);
	}
	
	@Override
	public void useItem(IItem i) {
		// TODO Auto-generated method stub
		
	}
}
