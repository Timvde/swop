package scenariotests;

import static org.junit.Assert.*;
import game.Game;
import grid.Grid;
import org.junit.BeforeClass;
import org.junit.Test;
import player.PlayerDatabase;
import controllers.GUIDataController;
import controllers.MoveController;
import controllers.PickUpItemController;

/**
 * Test if a player loses the game in correct circumstances.
 * 
 * To Test:
 * - Player trapped (not by grenade): lose
 * - Player's opponent wins: lose
 * 
 * @author Tom
 */
public class LosingGameTest {
	
	private static GUIDataController	guiDataCont;
	private static PickUpItemController	pickUpCont;
	private static MoveController		moveCont;
	private static Grid					grid;
	private static PlayerDatabase		playerDB;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		// TODO set up playerDB en predefined grid, en bij Game doe setGrid
		Game game = new Game();
		game.start();
		guiDataCont = new GUIDataController(playerDB, grid);
		pickUpCont = new PickUpItemController(playerDB);
		moveCont = new MoveController(playerDB);
	}
	
	@Test
	public void testPlayerTrappedButNotLosing() {
		
	}
	
	@Test
	public void testPlayerTrappedAndLosing() {
		
	}
}
