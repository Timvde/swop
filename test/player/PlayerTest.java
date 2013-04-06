package player;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import grid.Coordinate;
import grid.Grid;
import grid.GridBuilder;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import square.ASquare;
import square.Direction;
import square.Square;

@SuppressWarnings("javadoc")
public class PlayerTest {
	
	private Player			player;
	private Square			randomSquare;
	private PlayerDataBase	playerDB;
	
	@Before
	public void setUp() {
		Grid grid = new GridBuilder().getPredefinedTestGrid(false);
		ASquare[] playerStartingCoordinates = new ASquare[] {
				grid.getGrid().get(new Coordinate(grid.getWidth() - 1, 0)),
				grid.getGrid().get(new Coordinate(0, grid.getHeight() - 1)) };
		
		playerDB = new PlayerDataBase();
		playerDB.createNewDB(playerStartingCoordinates);
		randomSquare = (Square) grid.getSquareAt(GridBuilder.getRandomCoordOnTestGrid());
		player = (Player) playerDB.getCurrentPlayer();
	}
	
	@Test
	public void testConstructor() {
		assertEquals(player.getCurrentLocation(), randomSquare);
		
		// check the turn stuff
		assertEquals(false, player.hasMovedYet());
		assertEquals(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN, player.getAllowedNumberOfActions());
		
		// check empty inventory
		assertEquals(0, player.getInventoryContent().size());
		Assert.assertNotNull(player.getStartingPosition());
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullArgumentSquare() {
		new Player(null, playerDB);
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
		// test the initial number of actions,
		// Player.MAX_NUMBER_OF_ACTIONS_PER_TURN = 3
		assertEquals(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN, player.getAllowedNumberOfActions());
		// decrease by one
		player.skipNumberOfActions(1);
		// test again
		assertEquals(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN - 1, player.getAllowedNumberOfActions());
		// subtract another two: nb of actions will become zero
		player.skipNumberOfActions(2);
		assertEquals(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN - 3, player.getAllowedNumberOfActions());
		
		// simulate the DB assigning new actions to the player
		player.assignNewTurn();
		assertEquals(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN, player.getAllowedNumberOfActions());
		
		// subtract four at once
		player.skipNumberOfActions(4);
		player.assignNewTurn();
		// the result should now be two ( 3 - 4 + 3 )
		assertEquals(2, player.getAllowedNumberOfActions());
	}
	
	@Test
	public void testIsValidDirection() {
		assertTrue(player.isValidDirection(Direction.NORTH));
		assertFalse(player.isValidDirection(null));
	}
	
	@Test
	public void testAssignNewTurn() {
		player.assignNewTurn();
		assertEquals(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN, player.getAllowedNumberOfActions());
		assertFalse(player.hasMovedYet());
		assertFalse(player.hasMovedYet());
		
		player.skipNumberOfActions(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN + 4);
		assertEquals(-4, player.getAllowedNumberOfActions());
		assertFalse(player.hasMovedYet());
		
		player.assignNewTurn();
		assertEquals(-1, player.getAllowedNumberOfActions());
		assertFalse(player.hasMovedYet());
	}
}
