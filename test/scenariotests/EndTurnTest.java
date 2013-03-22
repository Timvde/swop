package scenariotests;

import static org.junit.Assert.*;
import game.Game;
import grid.Coordinate;
import grid.Grid;
import grid.GridBuilder;
import org.junit.Test;
import ObjectronExceptions.IllegalMoveException;
import player.PlayerDataBase;
import square.Direction;
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
		
		playerDB = new PlayerDataBase(grid);
		
		GridBuilder builder = new GridBuilder();
		grid = builder.getPredefinedTestGrid(true);
		
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
	public void testPlayerEnterPowerFailedSquare() throws IllegalStateException,
			IllegalArgumentException, IllegalMoveException {
		newGame();
		
		// Player 1 actions
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		// Player 2 actions
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		// Player 1 actions
		moveCont.move(Direction.WEST);
		
		// Check if player 1 his turn ended because of the power failure.
		assertSame(2, playerDB.getCurrentPlayer().getID());
	}
}
