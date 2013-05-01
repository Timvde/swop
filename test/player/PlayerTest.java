package player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import grid.Grid;
import grid.AGridBuilder;
import grid.RandomGridBuilder;
import item.lightgrenade.LightGrenade;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import square.Direction;
import ObjectronExceptions.CannotPlaceLightGrenadeException;
import ObjectronExceptions.IllegalMoveException;

@SuppressWarnings("javadoc")
public class PlayerTest implements Observer {
	
	private Player			player;
	private PlayerDataBase	db;
	private PlayerState		notifiedWithPlayerState;
	private Grid			grid;
	
	@Before
	public void setUp() {
		db = new PlayerDataBase();
		List<Player> players = db.createNewDB();
		// make this class an observer for testing purposes
		db.addObserver(this);
		grid = new RandomGridBuilder(db.createNewDB()).getPredefinedTestGrid(false);
		
		player = (Player) db.getCurrentPlayer();
		player.assignNewTurn();
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
		new Player(null);
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
	 * Warning: this method is just a quick hack to make sure the player has
	 * done a move (as the iterator order of a {@link HashSet} is not
	 * deterministic and this is used to appoint the first player in the db)
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
		assertEquals(PlayerState.LOST, this.notifiedWithPlayerState);
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
		
		assertEquals(PlayerState.WAITING, db.getNextPlayer().getPlayerState());
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
