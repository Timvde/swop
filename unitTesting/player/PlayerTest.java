package player;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import grid.Grid;
import grid.GridBuilder;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import square.Direction;
import square.Square;

@SuppressWarnings("javadoc")
public class PlayerTest {
	
	private Player		player;
	private Square		randomSquare;
	
	@Before
	public void setUp() {
		Grid grid = new GridBuilder().getPredefinedTestGrid(false);
		randomSquare = (Square) grid.getSquareAt(GridBuilder.getRandomCoordOnTestGrid());
		player = new Player(randomSquare);
	}
	
	@Test
	public void testConstructor() {
		assertEquals(player.getCurrentLocation(), randomSquare);
		assertEquals(false, player.hasMovedYet());
		assertEquals(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN, player.getAllowedNumberOfActions());
		// check empty inventory
		assertEquals(0, player.getInventoryContent().size());
		Assert.assertNotNull(player.getStartingPosition());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructor_nullArgument() {
		new Player(null);
	}
	
	@Test
	public void testEndTurn() {
		// TODO	
	}
	
	@Test
	public void testNumberOfActions() {
		// test the initial number of actions
		assertEquals(3, player.getAllowedNumberOfActions());
		// decrease by one 
		player.skipNumberOfActions(1);
		// test again
		assertEquals(2, player.getAllowedNumberOfActions());
		// subtract another two 
		player.skipNumberOfActions(2);
		// i think the player should have three now 
		// but this might be something that will change
		assertEquals(3, player.getAllowedNumberOfActions());
		// subtract four at once 
		player.skipNumberOfActions(4);
		// the result should now be two ( 3 - 4 + 3 )
		assertEquals(2, player.getAllowedNumberOfActions());
	}
	
	@Test
	public void testIsValidDirection() {
		assertTrue(player.isValidDirection(Direction.NORTH));
		assertFalse(player.isValidDirection(null));
	}
}
