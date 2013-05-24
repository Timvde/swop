package scenariotests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import game.CTFMode;
import game.GameMode;
import game.RaceMode;
import grid.builder.DeterministicGridBuilderDirector;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theory;
import player.Player;
import player.PlayerActionManager;
import player.TronPlayer;
import player.actions.EndTurnAction;
import square.Direction;
import ObjectronExceptions.IllegalActionException;

/**
 * Test the "End Turn" use case.
 */
@SuppressWarnings("javadoc")
public class EndTurnTest extends SetUpTestGrid {
	
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
	public void testEndTurn_success() {
		Player player = playerDB.getCurrentPlayer();
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// The system lets the character of the player perform an empty action
		// for each remaining action in this turn.
		assertEquals(0, player.getAllowedNumberOfActions());
		assertFalse(player.equals(playerDB.getCurrentPlayer()));
	}
	
	@Test(expected = IllegalActionException.class)
	public void testEndTurn_NotHisTurn() {
		Player player = playerDB.getCurrentPlayer();
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		assertEquals(0, player.getAllowedNumberOfActions());
		assertFalse(player.equals(playerDB.getCurrentPlayer()));
		
		// cast to a player to try to break the preconditions
		TronPlayer playerNotHisTurn = (TronPlayer) player;
		playerNotHisTurn.performAction(new EndTurnAction());;
	}
	
	@Test(expected = IllegalActionException.class)
	public void testEndTurn_ToManyActions() {
		Player player = playerDB.getCurrentPlayer();
		for (int i = 0; i < PlayerActionManager.MAX_NUMBER_OF_ACTIONS_PER_TURN; i++) {
			moveCont.move(Direction.WEST);
		}
		
		// now the db should have changed the players and reseted his actions
		assertEquals(0, player.getAllowedNumberOfActions());
		assertFalse(player.equals(playerDB.getCurrentPlayer()));
		
		// cast to a player to try to break the preconditions
		TronPlayer playerNotHisTurn = (TronPlayer) player;
		playerNotHisTurn.performAction(new EndTurnAction());;
	}
}
