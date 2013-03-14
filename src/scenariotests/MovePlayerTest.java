package scenariotests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import game.Game;
import grid.Direction;
import grid.Grid;
import grid.GridBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import player.IPlayer;
import player.Player;
import player.PlayerDataBase;
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

	private void newGame() {
		Game game = new Game();
		
		playerDB = new PlayerDataBase();
		List<IPlayer> playerList = playerDB.createNewDB();
		
		
		GridBuilder builder = new GridBuilder(playerList);
		grid = builder.getPredefinedTestGrid(playerList);
		
		for (IPlayer p : playerList) {
			p.setGrid(grid);
		}
		
		game.setGrid(grid);
		
		game.start();

		moveCont = new MoveController(playerDB);
		endTurnCont = new EndTurnController(playerDB);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testNoTwoPlayersOnOneSquare() {
		newGame();
		
		// Player 1 actions
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		// Player 2 actions
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		// Player 1 actions
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		// Player 2 actions
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		// Player 1 actions
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		// Player 2 actions
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		
		// This causes two players to be on the same square:
		moveCont.move(Direction.WEST);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testCannotMoveOnWall() {
		newGame();
		
		// Player 1 actions
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		// Player 2 actions
		moveCont.move(Direction.WEST);
		endTurnCont.endTurn();
		// Player 1 actions
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		// Player 2 actions
		moveCont.move(Direction.WEST);
		endTurnCont.endTurn();
		// Player 1 actions
		// This is a move on a wall:
		moveCont.move(Direction.NORTHEAST);
		
	}
	
	@Test(expected = IllegalStateException.class)
	public void testCannotLeaveGrid() {
		newGame();
		
		// Player 1 actions
			moveCont.move(Direction.SOUTH);
		
	}
	
	// TODO specifieker specifiëren
	@Test(expected = Exception.class)
	public void testCannotCrossLightrail() {
		newGame();
		
		// Player 1 actions
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.SOUTHEAST);
	}
	
	// TODO specifieker specifiëren
	@Test(expected = Exception.class)
	public void testAlwaysMoveActionInTurn() {
		newGame();
		
		// Player 1 actions
		endTurnCont.endTurn();
	}
}
