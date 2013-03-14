package scenariotests;

import static org.junit.Assert.*;
import java.util.List;
import game.Game;
import grid.Direction;
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
 * Test if a player loses the game in correct circumstances.
 * 
 * Tests: - Player trapped (not by item effect): lose - Player's opponent wins:
 * lose
 * 
 * @author Tom
 */
public class LosingGameTest {
	
	private static PickUpItemController	pickUpCont;
	private static MoveController		moveCont;
	private static EndTurnController	endTurnCont;
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
		endTurnCont = new EndTurnController(playerDB);
		pickUpCont = new PickUpItemController(playerDB);
	}
	
	@Test
	public void testPlayerTrappedButNotLosing() {
		newGame();
	}
	
	@Test
	public void testPlayerTrappedAndLosing() {
		newGame();
		
		// Player 1 actions
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		// Player 2 actions
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		// Player 1 actions
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		// Player 2 actions
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 1 actions
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		
		// TODO test if player 2 lost
	}
}
