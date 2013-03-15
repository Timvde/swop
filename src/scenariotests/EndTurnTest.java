package scenariotests;

import game.Game;
import grid.Coordinate;
import grid.Grid;
import grid.GridBuilder;
import org.junit.Test;
import player.PlayerDataBase;
import controllers.GUIDataController;
import controllers.MoveController;
import controllers.PickUpItemController;

/**
 * Test if the turns end correctly in the game.
 * 
 * Tests: 
 * - Player enters power failed square with no grenade: end turn
 * 
 * @author Tom
 */
@SuppressWarnings("javadoc")
public class EndTurnTest {
	
	private static GUIDataController	guiDataCont;
	private static PickUpItemController	pickUpCont;
	private static MoveController		moveCont;
	private static Grid					grid;
	private static PlayerDataBase		playerDB;
	
	private void newGame() {
		Game game = new Game();
		
		playerDB = new PlayerDataBase(grid);
		
		GridBuilder builder = new GridBuilder();
		grid = builder.getPredefinedTestGrid();
		
		Coordinate[] startingCoords = new Coordinate[2];
		startingCoords[0] = new Coordinate(grid.getWidth() - 1, 0);
		startingCoords[1] = new Coordinate(0, grid.getHeight()-1);
		
		playerDB.createNewDB(startingCoords, grid);
		
		game.setGrid(grid);
		
		game.start();

		moveCont = new MoveController(playerDB);
		pickUpCont = new PickUpItemController(playerDB);
	}
	
	@Test
	public void testPlayerEnterPowerFailedSquare() {
		newGame();
		
	}
}
