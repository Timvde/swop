package scenariotests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import player.IPlayer;
import player.Player;
import player.PlayerDataBase;
import square.Direction;
import ObjectronExceptions.IllegalActionException;

/**
 * Test the "End Turn" use case.
 */
@SuppressWarnings("javadoc")
public class EndTurnTest extends SetupTestGrid {
	
	@Test
	public void testEndTurn_success() {
		IPlayer player = playerDB.getCurrentPlayer();
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// The system lets the character of the player perform an empty action
		// for each remaining action in this turn.
		assertEquals(0, playerDB.getAllowedNumberOfActions(player));
		assertFalse(player.equals(playerDB.getCurrentPlayer()));
	}
	
	@Test(expected = IllegalActionException.class)
	public void testEndTurn_NotHisTurn() {
		IPlayer player = playerDB.getCurrentPlayer();
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		assertEquals(0, playerDB.getAllowedNumberOfActions(player));
		assertFalse(player.equals(playerDB.getCurrentPlayer()));
		
		// cast to a player to try to break the preconditions
		Player playerNotHisTurn = (Player) player;
		playerNotHisTurn.endTurn();
	}
	
	@Test(expected = IllegalActionException.class)
	public void testEndTurn_ToManyActions() {
		IPlayer player = playerDB.getCurrentPlayer();
		for (int i = 0; i < PlayerDataBase.MAX_NUMBER_OF_ACTIONS_PER_TURN; i++) {
			moveCont.move(Direction.WEST);
		}
		
		// now the db should have changed the players and reseted his actions
		assertEquals(0, playerDB.getAllowedNumberOfActions(player));
		assertFalse(player.equals(playerDB.getCurrentPlayer()));
		
		// cast to a player to try to break the preconditions
		Player playerNotHisTurn = (Player) player;
		playerNotHisTurn.endTurn();
	}
}
