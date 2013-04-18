package scenariotests;

import static org.junit.Assert.*;
import game.Game;
import grid.Grid;
import grid.AGridBuilder;
import grid.RandomGridBuilder;
import org.junit.Test;
import player.IPlayer;
import player.PlayerDataBase;
import square.Direction;
import ObjectronExceptions.IllegalMoveException;
import controllers.EndTurnController;
import controllers.MoveController;

/**
 * Test if the turns end correctly in the game.
 * 
 * Tests: - Player enters power failed square with no grenade: end turn
 * 
 * @author Tom
 */
@SuppressWarnings("javadoc")
public class EndTurnTest {
	
	private static EndTurnController	endTurnCont;
	private static MoveController		moveCont;
	private static Grid					grid;
	private static PlayerDataBase		playerDB;
	
	private void newGame() {
		Game game = new Game();
		
		playerDB = new PlayerDataBase();
		
		grid = new RandomGridBuilder(playerDB.createNewDB()).getPredefinedTestGrid(false);
		
		game.start();
		game.setGrid(grid);
		
		moveCont = new MoveController(playerDB);
		endTurnCont = new EndTurnController(playerDB);
	}
	
	@Test
	public void testEndTurn() throws IllegalStateException, IllegalArgumentException, IllegalMoveException {
		newGame();
		
		IPlayer player = playerDB.getCurrentPlayer();
		
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		assertFalse(player == playerDB.getCurrentPlayer());
	}
}
