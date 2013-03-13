package scenariotests;

import static org.junit.Assert.*;
import game.Game;
import grid.Grid;
import org.junit.BeforeClass;
import org.junit.Test;
import player.PlayerDataBase;
import controllers.GUIDataController;
import controllers.MoveController;
import controllers.PickUpItemController;

/**
 * Test if the pick up action works correctly.
 * 
 * Tests: 
 * - Player can carry maximum 6 items
 * 
 * @author Tom
 */
public class PickUpTest {
	
	private static GUIDataController	guiDataCont;
	private static PickUpItemController	pickUpCont;
	private static MoveController		moveCont;
	private static Grid					grid;
	private static PlayerDataBase		playerDB;
	
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
	public void testPlayerCanCarryMaximum6Items() {
		
	}
}
