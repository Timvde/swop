package scenariotests;

import static org.junit.Assert.*;
import junit.framework.Assert;
import game.Game;
import grid.Coordinate;
import grid.Grid;
import grid.GridBuilder;
import org.junit.Before;
import org.junit.Test;
import ObjectronExceptions.IllegalMoveException;
import player.PlayerDataBase;
import square.Direction;
import controllers.EndTurnController;
import controllers.MoveController;

/**
 * This class will test scenarios concerning the movement of players.
 * 
 * Tests: - No two players on one square - Cannot move on wall - Cannot leave
 * grid - Cannot cross lightrail - Player must always do a move action in turn
 * 
 * @author Tom
 */
@SuppressWarnings("javadoc")
public class MovePlayerTest {
	
	private static MoveController		moveCont;
	private static EndTurnController	endTurnCont;
	private static Grid					grid;
	private static PlayerDataBase		playerDB;
	
	@Before
	public void setUp() {
		Game game = new Game();
		playerDB = new PlayerDataBase(grid);
		grid = new GridBuilder().getPredefinedTestGrid(false);
		
		Coordinate[] startingCoords = new Coordinate[2];
		startingCoords[0] = new Coordinate(grid.getWidth() - 1, 0);
		startingCoords[1] = new Coordinate(0, grid.getHeight() - 1);
		
		playerDB.createNewDB(startingCoords, grid);
		game.setGrid(grid);
		
		game.start();
		
		moveCont = new MoveController(playerDB);
		endTurnCont = new EndTurnController(playerDB);
	}
	
	@Test
	public void testDefaultCase() throws IllegalStateException, IllegalArgumentException,
			IllegalMoveException {
		assertEquals(playerDB.getCurrentPlayer(),
				grid.getSquareAt(new Coordinate(grid.getWidth() - 1, 0)).getPlayer());
		moveCont.move(Direction.SOUTH);
		assertEquals(playerDB.getCurrentPlayer(),
				grid.getSquareAt(new Coordinate(grid.getWidth() - 1, 1)).getPlayer());
		assertNull(grid.getSquareAt(new Coordinate(grid.getWidth() - 1, 0)).getPlayer());
	}
	
	@Test
	public void testNoTwoPlayersOnOneSquare() throws IllegalStateException,
			IllegalArgumentException, IllegalMoveException {
		
		// Player 1 actions
		
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.SOUTHWEST);
		// Player 2 actions
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTHEAST);
		// Player 1 actions
		moveCont.move(Direction.SOUTHWEST);
		moveCont.move(Direction.SOUTHWEST);
		moveCont.move(Direction.SOUTHWEST);
		
		// Player 2 actions
		moveCont.move(Direction.NORTHEAST);
		
		boolean exceptionThrown = false;
		try {
			// This causes two players to be on the same square:
			moveCont.move(Direction.NORTHEAST);
		}
		catch (IllegalMoveException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
	}
	
	@Test
	public void testCannotMoveOnWall() throws IllegalStateException, IllegalArgumentException,
			IllegalMoveException {
		
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 2 actions
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// Player 2 actions
		boolean exceptionThrown = false;
		try {
			// This is a move on a wall:
			moveCont.move(Direction.NORTHEAST);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
	}
	
	@Test(expected = IllegalMoveException.class)
	public void testCannotLeaveGrid() throws IllegalStateException, IllegalArgumentException,
			IllegalMoveException {
		
		// Player 1 actions
		moveCont.move(Direction.NORTH);
	}
	
	// TODO specifieker specifiren, ook met try catch
	@Test(expected = IllegalMoveException.class)
	public void testCannotCrossLightrail() throws IllegalStateException, IllegalArgumentException,
			IllegalMoveException {
		
		// Player 1 actions
		moveCont.move(Direction.SOUTHWEST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.NORTHWEST);
	}
	
	@Test (expected = IllegalMoveException.class)
	public void testArgumentNull() throws IllegalStateException, IllegalArgumentException, IllegalMoveException {
		moveCont.move(null);
	}
	
	@Test (expected = IllegalMoveException.class)
	public void testMoveOnLightTrail() throws IllegalStateException, IllegalArgumentException, IllegalMoveException {
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.NORTH);
	}
}