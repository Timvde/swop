package player;


import static org.junit.Assert.*;
import java.util.Collections;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import square.ASquare;
import square.Direction;
import square.Square;

@SuppressWarnings("javadoc")
public class PlayerTest {
	
	private Player	player;
	private Square	randomSquare;
	private PlayerDataBase db;
	
	@Before
	public void setUp() {
		randomSquare = new Square(Collections.<Direction, ASquare> emptyMap());
		db = new PlayerDataBase();
		db.createNewDB(new Square[] {randomSquare, new Square(Collections.<Direction, ASquare> emptyMap())});
		player = (Player) db.getCurrentPlayer();
	}
	
	@Test
	public void testConstructor() {
		// test basic fields
		assertEquals(player.getCurrentLocation(), randomSquare);
		
		// check the turn stuff
		assertEquals(false, player.hasMovedYet());
		assertEquals(1, player.getID());
		assertEquals(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN, player.getAllowedNumberOfActions());
		
		// check empty inventory
		assertEquals(0, player.getInventoryContent().size());
		Assert.assertNotNull(player.getStartingPosition());
		
		// test whether the player is appointed by the db as the current player
		assertIsCurrentPlayerTurn();
	}

	private void assertIsCurrentPlayerTurn() {
		assertEquals(player, db.getCurrentPlayer());
		assertEquals(PlayerState.ACTIVE, player.getPlayerState());
	}
	
	private void assertIsNotCurrentPlayerTurn() {
		assertFalse(player.equals(db.getCurrentPlayer()));
		assertEquals(PlayerState.WAITING, player.getPlayerState());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullArgumentSquare() {
		new Player(null, db);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructor_nullArgumentPlayerDB() {
		new Player(randomSquare, null);
	}
	
	@Test (expected = IllegalArgumentException.class) 
	public void testConstructor_nullArguments() {
		new Player(null, null);
	}
	
	@Test
	public void testEndTurn() {
		// TODO
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
	
	private void switchPlayers() {
		db.endPlayerTurn((Player) db.getCurrentPlayer());
	}

	@Test
	public void testIsValidDirection() {
		assertTrue(player.isValidDirection(Direction.NORTH));
		assertFalse(player.isValidDirection(null));
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
		
		switchPlayers();
		assertIsNotCurrentPlayerTurn();
		assertEquals(-1, player.getAllowedNumberOfActions());
		assertFalse(player.hasMovedYet());
		assertIsNotCurrentPlayerTurn();
	}
}
