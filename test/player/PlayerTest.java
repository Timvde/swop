package player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import grid.Grid;
import grid.builder.DeterministicGridBuilderDirector;
import grid.builder.TronGridBuilder;
import item.DummyEffectFactory;
import item.lightgrenade.LightGrenade;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import org.junit.Before;
import org.junit.Test;
import player.actions.EndTurnAction;
import player.actions.MoveAction;
import player.actions.PickupItemAction;
import player.actions.UseAction;
import square.Direction;
import square.NormalSquare;
import square.SquareContainer;
import ObjectronExceptions.IllegalActionException;
import ObjectronExceptions.IllegalMoveException;

@SuppressWarnings("javadoc")
public class PlayerTest implements Observer {
	
	private TronPlayer		player;
	private PlayerDataBase	db;
	private TurnEvent		notifiedWithTurnEvent;
	private Grid			grid;
	
	@Before
	public void setUp() {
		TronGridBuilder builder = new TronGridBuilder(new DummyEffectFactory());
		new DeterministicGridBuilderDirector(builder, false).construct();
		grid = builder.getResult();
		
		db = new PlayerDataBase();
		db.createNewDB(grid.getAllStartingPositions());
		// make this class an observer for testing purposes
		db.addObserver(this);
		
		player = (TronPlayer) db.getCurrentPlayer();
	}
	
	/* ######################### CONSTRUCTOR TESTS ######################### */
	
	@Test
	public void testConstructor() {
		// check init turn stuff and basic fields
		assertEquals(false, player.hasMovedYet());
		assertEquals(PlayerActionManager.MAX_NUMBER_OF_ACTIONS_PER_TURN,
				player.getAllowedNumberOfActions());
		assertNotNull(player.getStartingPosition());
		assertEquals(player.getStartingPosition(), player.getCurrentPosition());
		assertEquals(0, player.getInventoryContent().size());
		
		// test whether the player is appointed by the db as the current player
		assertIsCurrentPlayerTurn();
		assertTrue(player.canPerformAction(new EndTurnAction()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullDB() {
		new TronPlayer(null, new SquareContainer(
				Collections.<Direction, SquareContainer> emptyMap(), new NormalSquare()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullArgumentSquare() {
		new TronPlayer(db, null);
	}
	
	/* ######################### TURN TESTS ######################### */
	
	@Test
	public void testEndTurnWithMove() throws IllegalMoveException {
		doMove();
		
		player.performAction(new EndTurnAction());
		assertIsNotCurrentPlayerTurn();
	}
	
	/**
	 * This method will move the player one square in the first direction
	 * possible.
	 * 
	 * This method is used to make sure the player has done a move so he doesn't
	 * loose the game.
	 */
	private void doMove() {
		boolean moved = false;
		for (Direction dir : Direction.values()) {
			try {
				player.performAction(new MoveAction(dir));
				moved = true;
				break;
			}
			catch (IllegalMoveException e) {
				; // do nothing try the next direction
			}
		}
		assertTrue(moved);
	}
	
	@Test
	public void testEndTurnWithoutMove() {
		player.performAction(new EndTurnAction());
		
		// test whether player sets itself lost and reported it
		assertEquals(PlayerState.FINISHED, player.getPlayerState());
		assertEquals(TurnEvent.END_TURN, this.getTurnEventOfNotify());
	}
	
	@Test
	public void testCanPerformAction() {
		assertIsCurrentPlayerTurn();
		switchPlayers();
		// now it's not this player's turn. It should not be able to execute
		// actions
		boolean exceptionThrown = false;
		
		try {
			player.performAction(new EndTurnAction());
		}
		catch (IllegalActionException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		try {
			player.performAction(new MoveAction(Direction.NORTH));
		}
		catch (IllegalActionException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		try {
			player.performAction(new UseAction(new LightGrenade(new DummyEffectFactory())));
		}
		catch (IllegalActionException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		try {
			player.performAction(new PickupItemAction(new LightGrenade(new DummyEffectFactory())));
		}
		catch (IllegalActionException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void testNumberOfActions() {
		assertIsCurrentPlayerTurn();
		
		// test the initial number of actions,
		// Player.MAX_NUMBER_OF_ACTIONS_PER_TURN = 4
		assertEquals(PlayerActionManager.MAX_NUMBER_OF_ACTIONS_PER_TURN,
				player.getAllowedNumberOfActions());
		assertIsCurrentPlayerTurn();
		
		// decrease by one
		player.skipNumberOfActions(1);
		assertIsCurrentPlayerTurn();
		
		// test again
		assertEquals(PlayerActionManager.MAX_NUMBER_OF_ACTIONS_PER_TURN - 1,
				player.getAllowedNumberOfActions());
		assertIsCurrentPlayerTurn();
		
		// subtract another two: nb of actions will become zero
		// --> player must have asked the db to switch players
		player.skipNumberOfActions(2);
		assertEquals(PlayerActionManager.MAX_NUMBER_OF_ACTIONS_PER_TURN - 3,
				player.getAllowedNumberOfActions());
		assertIsNotCurrentPlayerTurn();
		assertEquals(TurnEvent.END_TURN, getTurnEventOfNotify());
		
		// simulate player switch
		switchPlayers();
		assertIsCurrentPlayerTurn();
		
		assertEquals(PlayerActionManager.MAX_NUMBER_OF_ACTIONS_PER_TURN,
				player.getAllowedNumberOfActions());
		
		// subtract four at once
		player.skipNumberOfActions(4);
		assertIsNotCurrentPlayerTurn();
		switchPlayers();
		// the result should now be two ( 3 - 4 + 3 )
		assertEquals(2, player.getAllowedNumberOfActions());
	}
	
	@Test
	public void testAssignNewTurn() {
		assertIsCurrentPlayerTurn();
		db.assignNewTurn(player);
		assertEquals(PlayerActionManager.MAX_NUMBER_OF_ACTIONS_PER_TURN,
				player.getAllowedNumberOfActions());
		assertFalse(player.hasMovedYet());
		assertIsCurrentPlayerTurn();
		
		player.skipNumberOfActions(PlayerActionManager.MAX_NUMBER_OF_ACTIONS_PER_TURN + 4);
		assertEquals(-4, player.getAllowedNumberOfActions());
		assertFalse(player.hasMovedYet());
		// player should have switched turns (allowed nb actions < 0)
		assertIsNotCurrentPlayerTurn();
		assertEquals(TurnEvent.END_TURN, getTurnEventOfNotify());
		
		switchPlayers();
		assertIsNotCurrentPlayerTurn();
		assertEquals(-1, player.getAllowedNumberOfActions());
		assertFalse(player.hasMovedYet());
		assertIsNotCurrentPlayerTurn();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof PlayerDataBase && arg instanceof TurnEvent) {
			this.notifiedWithTurnEvent = (TurnEvent) arg;
		}
	}
	
	/* ############ PlayerDb methods (private) ############# */
	
	private void switchPlayers() {
		db.endPlayerTurn((TronPlayer) db.getCurrentPlayer());
		assertEquals(TurnEvent.END_TURN, getTurnEventOfNotify());
	}
	
	private void assertIsCurrentPlayerTurn() {
		assertEquals(player, db.getCurrentPlayer());
		assertEquals(PlayerState.ACTIVE, player.getPlayerState());
		
		assertEquals(PlayerState.WAITING, db.getNextPlayer().getPlayerState());
	}
	
	private void assertIsNotCurrentPlayerTurn() {
		assertFalse(player.equals(db.getCurrentPlayer()));
		assertEquals(PlayerState.WAITING, player.getPlayerState());
		
		assertEquals(PlayerState.ACTIVE, ((TronPlayer) db.getCurrentPlayer()).getPlayerState());
	}
	
	/**
	 * Returns the turnevent of the last observer call and clears it.
	 */
	private TurnEvent getTurnEventOfNotify() {
		TurnEvent result = this.notifiedWithTurnEvent;
		this.notifiedWithTurnEvent = null;
		return result;
	}
}
