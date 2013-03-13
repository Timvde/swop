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
 * Test if the number of actions mechanics work correctly.
 * 
 * To Test: 
 * - Player enters a power failed square and a light grenade explodes.
 * - Player starts on a square with a power failure.
 * - Player stands on a square and a light grenade explodes.
 * 
 * @author Tom
 */
public class NumberOfActionsTest {
	
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
	public void testPlayerEntersPowerFailedSquareWithLightGrenadeExplosion() {
		
	}
	
	@Test
	public void testPlayerStartsOnPowerFailedSquare() {
		
	}
	
	@Test
	public void testPlayerSquareLightGrenade() {
		
	}
}
