package scenariotests;

import static org.junit.Assert.*;
import game.Game;
import grid.Grid;
import org.junit.BeforeClass;
import org.junit.Test;
import player.PlayerDatabase;
import controllers.GUIDataController;
import controllers.MoveController;
import controllers.NewGameController;
import controllers.PickUpItemController;

/**
 * Test if the use item mechanics work correctly.
 * 
 * To Test:
 * - Light grenade can not be placed on wall.
 * - Light grenade can not be placed on start positions.
 * - Multiple light grenades can not be used on one square.
 * 
 * @author Tom
 */
public class UseItemTest {
	
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
	public void testLightGrenadeCanNotBePlacedOnWall() {
		
	}
	
	@Test
	public void testLightGrenadeCanNotBePlacedOnStartPositions() {
		
	}
	
	@Test
	public void testCanNotUseMultipleLightGrenadesOnOneSquare() {
		
	}
}
