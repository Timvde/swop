package player;

import item.IItem;
import item.lightgrenade.Explodable;
import item.teleporter.Teleportable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import player.actions.Action;
import powerfailure.AffectedByPowerFailure;
import square.AbstractSquare;
import square.Square;
import square.SquareContainer;

/**
 * Main character of the Tron game. A player carries an {@link Inventory
 * inventory} and is trailed by a {@link LightTrail light trail}. During the
 * game a player can perform
 * {@value PlayerDataBase#MAX_NUMBER_OF_ACTIONS_PER_TURN} {@link Action actions}
 * during a turn.
 */
public class TronPlayer implements Player, Teleportable, AffectedByPowerFailure, Explodable {
	
	/**
	 * The id of the player, not really used, but hey ... let's do something
	 * crazy FIXME DO we stil need the id? in GUI?
	 */
	private int						id;
	private static AtomicInteger	nextID	= new AtomicInteger();
	
	/** A boolean representing whether the player has moved */
	private boolean					hasMoved;
	/** The starting square of this player */
	private SquareContainer			startSquare;
	/** The square where the player is currently standing */
	private SquareContainer			currentSquare;
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
	TronPlayer(PlayerDataBase playerDB, SquareContainer startingPosition)
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
	public SquareContainer getStartingPosition() {
		return this.startSquare;
	}
	
	@Override
	public SquareContainer getCurrentLocation() {
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
		return playerDB.getAllowedNumberOfActions(this);
	}
	
	@Override
	public void skipNumberOfActions(int numberOfActionsToSkip) {
		playerDB.skipNumberOfActions(this, numberOfActionsToSkip);
	}
	
	/**
	 * Indicate that this player has to skip his next turn.
	 */
	public void skipNextTurn() {
		this.playerDB.skipNextTurn(this);
	}
	
	@Override
	public boolean hasMovedYet() {
		return this.hasMoved;
	}
	
	/**
	 * To be called when the player performed a move-action succesfully.
	 * 
	 * PostCondtion: {@link #hasMovedYet() this.hasMoved()}= true
	 */
	public void setHasMoved() {
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
	
	public void performAction(Action action) {
		action.execute(this);
		
		// end a players action
		playerDB.actionPerformed(this);
		lightTrail.updateLightTrail(currentSquare);
	}

	/**
	 * Let the player loose the current game
	 */
	public void looseGame() {
		setPlayerState(PlayerState.LOST);
		playerDB.reportGameLost(this);
	}
}
