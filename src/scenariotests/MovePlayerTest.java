package scenariotests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import game.Game;
import grid.Direction;
import grid.Grid;
import org.junit.BeforeClass;
import org.junit.Test;
import player.IPlayer;
import player.Player;
import player.PlayerDataBase;
import controllers.EndTurnController;
import controllers.MoveController;

/** TODO in this class: catch exceptions / do assertions
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
	
	@BeforeClass
	public static void setUpBeforeClass() {
		Game game = new Game();
		
		playerDB = new PlayerDataBase();
		List<IPlayer> playerList = playerDB.createNewDB();
		
		grid = Grid.getPredefinedTestGrid(playerList);
		game.setGrid(grid);
		
		game.start();

		moveCont = new MoveController(playerDB);
		endTurnCont = new EndTurnController(playerDB);
	}
	
	@Test
	public void testNoTwoPlayersOnOneSquare() {
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
		moveCont.move(Direction.WEST);
	}
	
	@Test
	public void testCannotMoveOnWall() {
		// Player 1 actions
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		// Player 2 actions
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		// Player 1 actions
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		// Player 2 actions
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		// Player 1 actions
		moveCont.move(Direction.NORTHEAST);
	}
	
	@Test
	public void testCannotLeaveGrid() {
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
	}
	
	@Test
	public void testCannotCrossLightrail() {
		// Player 1 actions
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.SOUTHEAST);
	}
	
	@Test
	public void testAlwaysMoveActionInTurn() {
		// Player 1 actions
		endTurnCont.endTurn();
	}
}
