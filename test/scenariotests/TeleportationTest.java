package scenariotests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import grid.Coordinate;
import grid.Grid;
import grid.builder.DeterministicGridBuilderDirector;
import grid.builder.TronGridBuilder;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import player.PlayerDataBase;
import square.Direction;
import square.PlayerStartingPosition;
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
