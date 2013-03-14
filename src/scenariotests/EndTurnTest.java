package scenariotests;

import static org.junit.Assert.*;
import java.util.List;
import game.Game;
import grid.Coordinate;
import grid.Grid;
import grid.GridBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import player.IPlayer;
import player.PlayerDataBase;
import controllers.EndTurnController;
import controllers.GUIDataController;
import controllers.MoveController;
import controllers.PickUpItemController;
import controllers.UseItemController;

/**
 * Test if the turns end correctly in the game.
 * 
 * Tests: 
 * - Player enters power failed square with no grenade: end turn
 * 
 * @author Tom
 */
public class EndTurnTest {
	
	private static GUIDataController	guiDataCont;
	private static PickUpItemController	pickUpCont;
	private static MoveController		moveCont;
	private static Grid					grid;
	private static PlayerDataBase		playerDB;
	
	private void newGame() {
		Game game = new Game();
		
		playerDB = new PlayerDataBase();
		
		GridBuilder builder = new GridBuilder();
		grid = builder.getPredefinedTestGrid();
		
		Coordinate[] startingCoords = new Coordinate[2];
		startingCoords[0] = new Coordinate(grid.getWidth() - 1, 0);
		startingCoords[1] = new Coordinate(0, grid.getHeight()-1);
		
		List<IPlayer> playerList = playerDB.createNewDB(startingCoords);
		
		for (IPlayer p : playerList) {
			p.setGrid(grid);
		}
		
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
