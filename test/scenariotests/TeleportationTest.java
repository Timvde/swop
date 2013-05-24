package scenariotests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import game.CTFMode;
import game.GameMode;
import game.RaceMode;
import grid.Coordinate;
import grid.builder.DeterministicGridBuilderDirector;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theory;
import square.Direction;
import ObjectronExceptions.IllegalMoveException;

@SuppressWarnings("javadoc")
public class TeleportationTest extends SetUpTestGrid {
	
	public static @DataPoints
	GameMode[]	candidates	= { new RaceMode(),
			new CTFMode(DeterministicGridBuilderDirector.NUMBER_OF_PLAYERS_ON_TEST_GRID) };
	
	/**
	 * This method will be called with all gamemodes.
	 */
	@Theory
	public void setUp(GameMode mode) {
		super.setUp(mode);
	}
	
	@Test
	public void testTeleportation() {
		// move player 1 two squares down
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// check whether the destination square of the teleporter contains a
		// player
		assertTrue(null != grid.getSquareAt(new Coordinate(0, 7)).getPlayer());
		
		// end the turn of player 2 asap
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// move player 1 and check whether the light trail travels through the
		// teleporter
		moveCont.move(Direction.NORTH);
		assertTrue(grid.getSquareAt(new Coordinate(0, 7)).hasLightTrail());
		assertTrue(grid.getSquareAt(new Coordinate(9, 2)).hasLightTrail());
		
		moveCont.move(Direction.NORTH);
		assertTrue(grid.getSquareAt(new Coordinate(0, 8)).hasLightTrail());
		assertTrue(grid.getSquareAt(new Coordinate(0, 7)).hasLightTrail());
		assertTrue(grid.getSquareAt(new Coordinate(8, 2)).hasLightTrail());
		assertFalse(grid.getSquareAt(new Coordinate(8, 1)).hasLightTrail());
		
		moveCont.move(Direction.NORTH);
	}
	
	@Test
	public void testTeleportation_PlayerCoversDestination() {
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
