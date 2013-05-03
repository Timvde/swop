package scenariotests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import grid.Coordinate;
import grid.Grid;
import grid.builder.DeterministicGridBuilderDirector;
import grid.builder.TronGridBuilder;
import java.util.LinkedHashSet;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import player.PlayerDataBase;
import square.Direction;
import square.PlayerStartingPosition;
import ObjectronExceptions.IllegalMoveException;
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
		TronGridBuilder builder = new TronGridBuilder();
		DeterministicGridBuilderDirector director = new DeterministicGridBuilderDirector(builder,
				false);
		director.construct();
		grid = builder.getResult();
		
		//make a set with the startingpostions in a deterministic order
		Set<PlayerStartingPosition> playerstartingpositions = new LinkedHashSet<PlayerStartingPosition>();
		playerstartingpositions.add((PlayerStartingPosition) grid
				.getSquareAt(DeterministicGridBuilderDirector.PLAYER1_START_POS));
		playerstartingpositions.add((PlayerStartingPosition) grid
				.getSquareAt(DeterministicGridBuilderDirector.PLAYER2_START_POS));
		
		playerDB = new PlayerDataBase();
		playerDB.createNewDB(playerstartingpositions);
		assertEquals(1, playerDB.getCurrentPlayer().getID());
		
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
		catch (IllegalMoveException e) {
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
	
	@Test (expected = IllegalMoveException.class)
	public void testCrossLightTrail() throws IllegalStateException, IllegalArgumentException, IllegalMoveException {
		moveCont.move(Direction.SOUTHWEST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.NORTHWEST);
	}
}
