package scenariotests;

import static org.junit.Assert.*;
import java.util.List;
import game.Game;
import grid.Grid;
import grid.GridBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import player.IPlayer;
import player.PlayerDataBase;
import controllers.EndTurnController;
import controllers.GUIDataController;
import controllers.MoveController;
import controllers.NewGameController;
import controllers.PickUpItemController;
import controllers.UseItemController;

/**
 * Test if the use item mechanics work correctly.
 * 
 * Tests:
 * 
 * @author Tom
 */
public class UseItemTest {
	
	private static PickUpItemController	pickUpCont;
	private static MoveController		moveCont;
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
		pickUpCont = new PickUpItemController(playerDB);
	}
	
}
