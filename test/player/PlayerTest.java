package player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import grid.Grid;
import grid.builder.DeterministicGridBuilderDirector;
import grid.builder.TronGridBuilder;
import item.lightgrenade.LightGrenade;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import org.junit.Before;
import org.junit.Test;
import square.Direction;
import square.PlayerStartingPosition;
import square.SquareContainer;
import ObjectronExceptions.IllegalActionException;
import ObjectronExceptions.IllegalMoveException;

@SuppressWarnings("javadoc")
public class PlayerTest implements Observer {
	
	private Player			player;
	private PlayerDataBase	db;
	private TurnEvent		notifiedWithTurnEvent;
	private Grid			grid;
	
	@Before
	public void setUp() {
		TronGridBuilder builder = new TronGridBuilder();
		new DeterministicGridBuilderDirector(builder, false).construct();
		grid = builder.getResult();
		
		db = new PlayerDataBase();
		db.createNewDB(grid.getAllStartingPositions());
		// make this class an observer for testing purposes
		db.addObserver(this);
		
		player = (Player) db.getCurrentPlayer();
	}
	
	/* ######################### CONSTRUCTOR TESTS ######################### */
	
	@Test
	public void testConstructor() {
		// check init turn stuff and basic fields
		assertEquals(false, player.hasMovedYet());
		assertEquals(PlayerDataBase.MAX_NUMBER_OF_ACTIONS_PER_TURN,
				player.getAllowedNumberOfActions());
		assertNotNull(player.getStartingPosition());
		assertEquals(player.getStartingPosition(), player.getCurrentLocation());
		assertEquals(0, player.getInventoryContent().size());
		
		// test whether the player is appointed by the db as the current player
		assertIsCurrentPlayerTurn();
		assertTrue(player.canPerformAction());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullDB() {
		new Player(null, new SquareContainer(Collections.<Direction, SquareContainer> emptyMap(),
				new PlayerStartingPosition()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullArgumentSquare() {
		new Player(db, null);
	}
	
	/* ######################### TURN TESTS ######################### */
	
	@Test
	public void testEndTurnWithMove() throws IllegalMoveException {
		doMove();
		
		player.endTurn();
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
				player.moveInDirection(dir);
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
		player.endTurn();
		
		// test whether player sets itself lost and reported it
		assertEquals(PlayerState.LOST, player.getPlayerState());
		assertEquals(TurnEvent.END_GAME, this.getTurnEventOfNotify());
	}
	
	@Test
	public void testCanPerformAction() {
		assertIsCurrentPlayerTurn();
		switchPlayers();
		// now it's not this player's turn. It should not be able to execute
		// actions
		boolean exceptionThrown = false;
		
		try {
			player.endTurn();
		}
		catch (IllegalActionException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		try {
			player.moveInDirection(Direction.NORTH);
		}
		catch (IllegalActionException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		try {
			player.useItem(new LightGrenade());
		}
		catch (IllegalActionException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		try {
			player.pickUpItem(new LightGrenade());
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
		// Player.MAX_NUMBER_OF_ACTIONS_PER_TURN = 3
		assertEquals(PlayerDataBase.MAX_NUMBER_OF_ACTIONS_PER_TURN,
				player.getAllowedNumberOfActions());
		assertIsCurrentPlayerTurn();
		
		// decrease by one
		player.skipNumberOfActions(1);
		assertIsCurrentPlayerTurn();
		
		// test again
		assertEquals(PlayerDataBase.MAX_NUMBER_OF_ACTIONS_PER_TURN - 1,
				player.getAllowedNumberOfActions());
		assertIsCurrentPlayerTurn();
		
		// subtract another two: nb of actions will become zero
		// --> player must have asked the db to switch players
		player.skipNumberOfActions(2);
		assertEquals(PlayerDataBase.MAX_NUMBER_OF_ACTIONS_PER_TURN - 3,
				player.getAllowedNumberOfActions());
		assertIsNotCurrentPlayerTurn();
		assertEquals(TurnEvent.END_TURN, getTurnEventOfNotify());
		
		// simulate player switch
		switchPlayers();
		assertIsCurrentPlayerTurn();
		
		assertEquals(PlayerDataBase.MAX_NUMBER_OF_ACTIONS_PER_TURN,
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
		assertEquals(PlayerDataBase.MAX_NUMBER_OF_ACTIONS_PER_TURN,
				player.getAllowedNumberOfActions());
		assertFalse(player.hasMovedYet());
		assertIsCurrentPlayerTurn();
		
		player.skipNumberOfActions(PlayerDataBase.MAX_NUMBER_OF_ACTIONS_PER_TURN + 4);
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
	
	@Test
	public void testIsValidDirection() {
		assertTrue(player.isValidDirection(Direction.NORTH));
		assertFalse(player.isValidDirection(null));
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof PlayerDataBase && arg instanceof TurnEvent) {
			this.notifiedWithTurnEvent = (TurnEvent) arg;
		}
	}
	
	/* ############ PlayerDb methods (private) ############# */
	
	private void switchPlayers() {
		db.endPlayerTurn((Player) db.getCurrentPlayer());
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
		
		assertEquals(PlayerState.ACTIVE, ((Player) db.getCurrentPlayer()).getPlayerState());
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
