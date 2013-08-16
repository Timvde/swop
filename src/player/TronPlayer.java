package player;

import game.GameMode;
import item.IItem;
import item.lightgrenade.Explodable;
import item.teleporter.Teleportable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import player.actions.Action;
import powerfailure.AffectedByPowerFailure;
import square.SquareContainer;
import ObjectronExceptions.IllegalActionException;

/**
 * Main character of the Tron game. A player carries an {@link Inventory
 * inventory} and is trailed by a {@link LightTrail light trail}. During the
 * game a player can perform 
 * {@value PlayerActionManager#MAX_NUMBER_OF_ACTIONS_PER_TURN} {@link Action
 * actions} during a turn.
 */
public class TronPlayer implements Player, Teleportable, AffectedByPowerFailure, Explodable {
	
	/**
	 * The id of the player.
	 */
	private int							id;
	private static AtomicInteger		nextID	= new AtomicInteger();
	private boolean isDead = false;
	
	/** The starting square of this player */
	private SquareContainer				startSquare;
	/** The square where the player is currently standing */
	private SquareContainer				currentSquare;
	/** The inventory of the player */
	private Inventory					inventory;
	/** The light trail of the player */
	private LightTrail					lightTrail;
	/** The state of the player */
	private PlayerState					state;
	/** The player database in which the player is placed */
	private PlayerDataBase				playerDB;
	/** The Action Manager that manages this player's actions */
	private final PlayerActionManager	actionManager;
	
	/**
	 * Creates a new Player object, with an empty inventory, who has not yet
	 * moved and has an allowed number of actions of
	 * {@value PlayerDataBase#MAX_NUMBER_OF_ACTIONS_PER_TURN}. The default state
	 * of a player is {@link PlayerState#WAITING}. Until the state is set to
	 * {@link PlayerState#ACTIVE} the player will not be able to perform any
	 * action.
	 */
	TronPlayer(PlayerDataBase playerDB, SquareContainer startingPosition)
			throws IllegalArgumentException {
		if (playerDB == null)
			throw new IllegalArgumentException("The database cannot be null");
		
		this.id = nextID.incrementAndGet();
		this.inventory = new Inventory();
		this.lightTrail = new LightTrail(startingPosition);
		this.state = PlayerState.WAITING;
		this.playerDB = playerDB;
		this.setStartingPosition(startingPosition);
		this.actionManager = new PlayerActionManager(this);
	}
	
	public boolean isDead() {
		return this.isDead;
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
	public SquareContainer getStartingPosition() {
		return this.startSquare;
	}
	
	@Override
	public SquareContainer getCurrentPosition() {
		return this.currentSquare;
	}
	
	@Override
	public List<IItem> getInventoryContent() {
		return inventory.getItems();
	}
	
	/**
	 * Returns the inventory of this player.
	 * 
	 * @return the inventory of the player
	 */
	public Inventory getInventory() {
		return inventory;
	}
	
	@Override
	public int getAllowedNumberOfActions() {
		return actionManager.getNumberOfActionsLeft();
	}
	
	@Override
	public void skipNumberOfActions(int numberOfActionsToSkip) {
		actionManager.decrementNumberOfActions(numberOfActionsToSkip);
	}
	
	/**
	 * This method ends the turn of this player. Note that some {@link GameMode
	 * game modes} let the player lose the game if this method is called before
	 * he did a move action, (i.e. if <code>{@link #hasMovedYet()}</code> is
	 * false when calling this method, this player loses the game).
	 * 
	 * @throws IllegalActionException
	 *         This player must be allowed to perform an EndTurnAction, i.e.
	 *         <code>{@link #canPerformAction()}</code>.
	 */
	public void endTurn() throws IllegalActionException {
		if (!canPerformAction())
			throw new IllegalActionException("The player must be allowed to perform an action.");

		actionManager.resetActionsToEndTurn();
	}
	
	/**
	 * Indicate that this player has to skip his next turn.
	 */
	public void skipNextTurn() {
		actionManager.skipNextNTurns(1);
	}
	
	@Override
	public boolean hasMovedYet() {
		return this.actionManager.hasMoved();
	}
	
	/**
	 * To be called when the player performed a move-action successfully.
	 * 
	 * PostCondtion: {@link #hasMovedYet() this.hasMoved()}= true
	 */
	public void setHasMoved() {
		actionManager.setHasMoved();
	}
	
	@Override
	public boolean canPerformAction() {
		return this.state == PlayerState.ACTIVE && getAllowedNumberOfActions() > 0;
	}
	
	/**
	 * Set the state of the player to a new specified state. It is not recommended to use
	 * this method from anywhere else than the PlayerDB. The visibility is set to public
	 * to use for testing reasons.
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
	
	@Override
	public String toString() {
		return "Player [id=" + id + ", allowedNumberOfActionsLeft=" + getAllowedNumberOfActions()
				+ ", hasMoved=" + actionManager.hasMoved() + ", currentSquare=" + currentSquare
				+ ", state=" + state + "]";
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
	 * Ends the turn of this player
	 */
	@Override
	public void damageByPowerFailure() {
		if (canPerformAction()) {
			this.endTurn();
		}
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
	 * game with an old player. The state of the player will be set to FINISHED.
	 * 
	 * <b>NOTE:</b> After calling this method one cannot use the player object
	 * anymore (as nullpointers will be thrown).
	 */
	void endPlayerLife() {
		this.isDead = true;
		this.playerDB = null;
		this.id = -1;
		this.inventory.removeAll();
		this.lightTrail.destroy();
		this.state = PlayerState.FINISHED;
		
		if (currentSquare != null)
			this.currentSquare.remove(this);
		
		this.currentSquare = null;
	}
	
	public void performAction(Action action) {
		SquareContainer oldSquare = currentSquare;
		action.execute(this);
		actionManager.performedAction();
		
		/*
		 * The current square should only be added to the light trail when the
		 * player has moved.
		 */
		if (!currentSquare.equals(oldSquare))
			lightTrail.updateLightTrail(currentSquare);
		else
			lightTrail.updateLightTrail();
		// end a players action
		playerDB.actionPerformed(this);
	}
	
	PlayerActionManager getActionManager() {
		return actionManager;
	}
}
