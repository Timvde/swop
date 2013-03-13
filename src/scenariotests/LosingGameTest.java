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
 * 
 * @author Tom
 */
public class LosingGameTest {
	
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
}
