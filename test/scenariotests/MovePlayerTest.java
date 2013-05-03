package scenariotests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import grid.Coordinate;
import grid.builder.DeterministicGridBuilderDirector;
import org.junit.Test;
import player.IPlayer;
import player.Player;
import player.PlayerDataBase;
import player.PlayerState;
import square.Direction;
import ObjectronExceptions.IllegalMoveException;

/**
 * Tests the "Move" use case. This class will test scenarios concerning the
 * movement of players.
 * 
 * Tests: - No two players on one square - Cannot move on wall - Cannot leave
 * grid - Cannot cross lightrail - Player must always do a move action in turn
 */
@SuppressWarnings("javadoc")
public class MovePlayerTest extends SetupTestGrid {
	
	/**
	 * Move player 1 to the south
	 */
	@Test
	public void testMove_success() {
		Coordinate start = DeterministicGridBuilderDirector.PLAYER1_START_POS;
		assertEquals(playerDB.getCurrentPlayer(), grid.getSquareAt(start).getPlayer());
		
		moveCont.move(Direction.SOUTH);
		
		// system moves the character of the player 1 square in the selected
		// direction
		assertEquals(grid.getSquareAt(start.getCoordinateInDirection(Direction.SOUTH)), playerDB
				.getCurrentPlayer().getCurrentLocation());
		assertNull(grid.getSquareAt(start).getPlayer());
		
		// The system adds 1 to the number of actions that the player has
		// performed during this turn.
		assertEquals(PlayerDataBase.MAX_NUMBER_OF_ACTIONS_PER_TURN - 1,
				playerDB.getAllowedNumberOfActions(playerDB.getCurrentPlayer()));
	}
	
	@Test
	public void testCannotMoveOnWall() {
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// Player 2 actions
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		endTurnCont.endTurn();
		
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
		assertTrue(exceptionThrown);
	}
	
	@Test(expected = IllegalMoveException.class)
	public void testCannotLeaveGrid() {
		// Player 1 actions
		moveCont.move(Direction.NORTH);
	}
	
	@Test
	public void testNoTwoPlayersOnOneSquare() {
		// Player 1 actions
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.SOUTHWEST);
		endTurnCont.endTurn();
		
		// Player 2 actions
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		
		// Player 1 actions
		moveCont.move(Direction.SOUTHWEST);
		moveCont.move(Direction.SOUTHWEST);
		moveCont.move(Direction.SOUTHWEST);
		endTurnCont.endTurn();
		
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
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void testCannotCrossLightrail() {
		// Player 1 actions
		moveCont.move(Direction.SOUTHWEST);
		moveCont.move(Direction.EAST);
		
		boolean exceptionthrown = false;
		try {
			// this will cross a lighttrail
			moveCont.move(Direction.NORTHWEST);
		}
		catch (IllegalMoveException e) {
			exceptionthrown = true;
		}
		assertTrue(exceptionthrown);
	}
	
	@Test(expected = IllegalMoveException.class)
	public void testMoveOnLightTrail() {
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.NORTH);
	}
	
	@Test
	public void testPlayerWin() {
		//Player 1
		IPlayer player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		// now player 1 is teleported
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		//player 2
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		//player 1
		moveCont.move(Direction.SOUTH);
		
		assertEquals(PlayerState.FINISHED, ((Player) player1).getPlayerState());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testArgumentNull() {
		moveCont.move(null);
	}
}
