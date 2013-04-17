package scenariotests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import game.Game;
import grid.Coordinate;
import grid.Grid;
import grid.GridBuilder;
import org.junit.Before;
import org.junit.Test;
import player.PlayerDataBase;
import square.Direction;
import ObjectronExceptions.IllegalMoveException;
import controllers.EndTurnController;
import controllers.MoveController;

@SuppressWarnings("javadoc")
public class TeleportationTest {
	
	private MoveController		moveCont;
	private EndTurnController	endTurnCont;
	private Grid				grid;
	private PlayerDataBase		playerDB;
	
	@Before
	public void setUp() throws Exception {
		Game game = new Game();
		playerDB = new PlayerDataBase();
		grid = new GridBuilder(playerDB.createNewDB()).getPredefinedTestGrid(false);
		game.setGrid(grid);
		
		game.start();
		
		moveCont = new MoveController(playerDB);
		endTurnCont = new EndTurnController(playerDB);
	}
	
	@Test
	public void testTeleportation() throws IllegalStateException, IllegalArgumentException,
			IllegalMoveException {
		
		// move player 1 three squares down
		
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		
		// check whether the destination square of the teleporter contains a player
		assertTrue(null != grid.getSquareAt(new Coordinate(0, 8)).getPlayer());
		
		// end the turn of player 2 asap
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		//move player 1 and check whether the light trail travels through the teleporter
		moveCont.move(Direction.NORTH);
		assertTrue(grid.getSquareAt(new Coordinate(0, 8)).hasLightTrail());
		assertTrue(grid.getSquareAt(new Coordinate(8, 1)).hasLightTrail());
		assertTrue(grid.getSquareAt(new Coordinate(8, 2)).hasLightTrail());
		assertFalse(grid.getSquareAt(new Coordinate(8, 0)).hasLightTrail());
		
		moveCont.move(Direction.NORTH);
		assertTrue(grid.getSquareAt(new Coordinate(0, 8)).hasLightTrail());
		assertTrue(grid.getSquareAt(new Coordinate(0, 7)).hasLightTrail());
		assertTrue(grid.getSquareAt(new Coordinate(8, 2)).hasLightTrail());
		assertFalse(grid.getSquareAt(new Coordinate(8, 1)).hasLightTrail());
		
		moveCont.move(Direction.NORTH);
	}
	
	@Test
	public void testTeleportation_PlayerCoversDestination() throws IllegalStateException,
			IllegalArgumentException, IllegalMoveException {
		
		// move player 1
		
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.SOUTH);
		
		// move player 2
		
		moveCont.move(Direction.NORTH);
		endTurnCont.endTurn();
		
		// move player 1 onto destination
		
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// try to move player 2 onto the teleporter
		
		boolean exceptionThrown = false;
		
		try {
			moveCont.move(Direction.NORTH);
		}
		catch (IllegalMoveException e) {
			exceptionThrown = true;
		}
		
		// check whether the teleportation did not happen ..
		assertTrue(exceptionThrown);
	}
}
