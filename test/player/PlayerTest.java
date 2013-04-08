package player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import grid.GridBuilder;
import item.lightgrenade.LightGrenade;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import square.ASquare;
import square.Direction;
import square.Square;
import ObjectronExceptions.CannotPlaceLightGrenadeException;
import ObjectronExceptions.IllegalMoveException;

@SuppressWarnings("javadoc")
public class PlayerTest implements Observer {
	
	private Player			player;
	private PlayerDataBase	db;
	private PlayerState		notifiedWithPlayerState;
	
	@Before
	public void setUp() {
		db = new PlayerDataBase();
		db.createNewDB(new GridBuilder().getPlayerStartingPositionsOnTestGrid());
		// make this class an observer for testing purposes
		db.addObserver(this);
		
		player = (Player) db.getCurrentPlayer();
	}
	
	/* ######################### CONSTRUCTOR TESTS ######################### */
	
	@Test
	public void testConstructor() {
		// check init turn stuff and basic fields
		assertEquals(false, player.hasMovedYet());
		assertEquals(1, player.getID());
		assertEquals(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN, player.getAllowedNumberOfActions());
		assertEquals(player.getStartingPosition(), player.getCurrentLocation());
		
		// check empty inventory
		assertEquals(0, player.getInventoryContent().size());
		Assert.assertNotNull(player.getStartingPosition());
		
		// test whether the player is appointed by the db as the current player
		assertIsCurrentPlayerTurn();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullArgumentSquare() {
		new Player(null, db);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullArgumentPlayerDB() {
		new Player(new Square(Collections.<Direction, ASquare> emptyMap()), null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullArguments() {
		new Player(null, null);
	}
	
	/* ######################### TURN TESTS ######################### */
	
	@Test
	public void testEndTurn() {
		// TODO
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
		catch (IllegalStateException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		try {
			player.moveInDirection(Direction.NORTH);
		}
		catch (IllegalStateException e) {
			exceptionThrown = true;
		}
		catch (IllegalMoveException e) {
			;
		}
		assertTrue(exceptionThrown);
		
		try {
			player.useItem(new LightGrenade());
		}
		catch (IllegalStateException e) {
			exceptionThrown = true;
		}
		catch (IllegalArgumentException e) {
			;
		}
		catch (CannotPlaceLightGrenadeException e) {
			;
		}
		assertTrue(exceptionThrown);
		
		try {
			player.pickUpItem(new LightGrenade());
		}
		catch (IllegalStateException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void testNumberOfActions() {
		assertIsCurrentPlayerTurn();
		
		// test the initial number of actions,
		// Player.MAX_NUMBER_OF_ACTIONS_PER_TURN = 3
		assertEquals(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN, player.getAllowedNumberOfActions());
		assertIsCurrentPlayerTurn();
		
		// decrease by one
		player.skipNumberOfActions(1);
		assertIsCurrentPlayerTurn();
		
		// test again
		assertEquals(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN - 1, player.getAllowedNumberOfActions());
		assertIsCurrentPlayerTurn();
		
		// subtract another two: nb of actions will become zero
		// --> player must have asked the db to switch players
		player.skipNumberOfActions(2);
		assertEquals(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN - 3, player.getAllowedNumberOfActions());
		assertIsNotCurrentPlayerTurn();
		assertObserversNotifiedOfPlayerSwitch();
		
		// simulate player switch
		switchPlayers();
		assertIsCurrentPlayerTurn();
		
		assertEquals(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN, player.getAllowedNumberOfActions());
		
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
		player.assignNewTurn();
		assertEquals(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN, player.getAllowedNumberOfActions());
		assertFalse(player.hasMovedYet());
		assertIsCurrentPlayerTurn();
		
		player.skipNumberOfActions(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN + 4);
		assertEquals(-4, player.getAllowedNumberOfActions());
		assertFalse(player.hasMovedYet());
		// player should have switched turns (allowed nb actions < 0)
		assertIsNotCurrentPlayerTurn();
		assertObserversNotifiedOfPlayerSwitch();
		
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
		if (o instanceof PlayerDataBase && arg instanceof PlayerState) {
			this.notifiedWithPlayerState = (PlayerState) arg;
		}
	}
	
	/* ############ PlayerDb methods (private) ############# */
	
	private void switchPlayers() {
		db.endPlayerTurn((Player) db.getCurrentPlayer());
		assertObserversNotifiedOfPlayerSwitch();
	}
	
	private void assertIsCurrentPlayerTurn() {
		assertEquals(player, db.getCurrentPlayer());
		assertEquals(PlayerState.ACTIVE, player.getPlayerState());
		
		assertEquals(PlayerState.WAITING, db.getOtherPlayer().getPlayerState());
	}
	
	private void assertIsNotCurrentPlayerTurn() {
		assertFalse(player.equals(db.getCurrentPlayer()));
		assertEquals(PlayerState.WAITING, player.getPlayerState());
		
		assertEquals(PlayerState.ACTIVE, ((Player) db.getCurrentPlayer()).getPlayerState());
	}
	
	private void assertObserversNotifiedOfPlayerSwitch() {
		// PlayerDB passes the PlayerState of the player whos turn is ended
		// as an argument
		assertEquals(PlayerState.WAITING, this.notifiedWithPlayerState);
		// ignore any other PlayerStates; only intrested in Player-turn ends
		
		this.notifiedWithPlayerState = null;
	}
}
