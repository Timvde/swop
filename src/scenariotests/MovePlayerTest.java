package scenariotests;

import static org.junit.Assert.*;
import game.Game;
import grid.Grid;
import org.junit.BeforeClass;
import org.junit.Test;
import player.PlayerDatabase;
import controllers.GUIDataController;
import controllers.NewGameController;

/**
 * This class will test scenarios concerning the movement of players.
 * 
 * To test:
 * - No two players on one square
 * - Cannot move on wall
 * - Cannot leave grid
 * - Cannot cross lightrail
 * - Player must always do a move action in turn
 * 
 * @author Tom
 */
public class MovePlayerTest {
	
	private static GUIDataController	guiDataCont;
	private static Grid					grid;
	private static PlayerDatabase		playerDB;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		// TODO set up playerDB en predefined grid, en bij Game doe setGrid
		Game game = new Game();
		game.start();
		guiDataCont = new GUIDataController(playerDB, grid);
	}
	
	@Test
	public void testNoTwoPlayersOnOneSquare() {
		
	}
	
	@Test
	public void testCannotMoveOnWall() {
		
	}
	
	@Test
	public void testCannotLeaveGrid() {
		
	}
	
	@Test
	public void testCannotCrossLightrail() {
		
	}
	
	@Test
	public void testAlwaysMoveActionInTurn() {
		
	}
}
