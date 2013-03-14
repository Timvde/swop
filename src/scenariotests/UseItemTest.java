package scenariotests;

import static org.junit.Assert.*;
import item.IItem;
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
import controllers.NewGameController;
import controllers.PickUpItemController;
import controllers.UseItemController;

/**
 * Test if the use item mechanics work correctly.
 * 
 * Tests: - Place maximum one lightgrenade on a square - Cannot place light
 * grenade on start positions
 * 
 * @author Tom
 */
public class UseItemTest {
	
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
		pickUpCont = new PickUpItemController(playerDB);
		endTurnCont = new EndTurnController(playerDB);
	}
	
	@Test
	public void testMaximumOneLightGrenadePerSquare() {
		// Player 1 actions
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		// Player 2 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 1 actions
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		// Player 2 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 1 actions
		List<IItem> items1 = grid.getSquareOfPlayer(playerDB.getCurrentPlayer())
				.getCarryableItems();
		IItem lightGrenade1 = items1.get(0);
		pickUpCont.pickUpItem(lightGrenade1);
		moveCont.move(Direction.EAST);
		List<IItem> items2 = grid.getSquareOfPlayer(playerDB.getCurrentPlayer())
				.getCarryableItems();
		IItem lightGrenade2 = items2.get(0);
		pickUpCont.pickUpItem(lightGrenade2);
		// Player 2 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 1 actions
		// TODO twee keer onmiddelijk droppen? niet rond lopen
	}
	
	@Test
	public void testCannotPlaceLightGrenadeOnStartPositions() {
		// Player 1 actions
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		List<IItem> items1 = grid.getSquareOfPlayer(playerDB.getCurrentPlayer())
				.getCarryableItems();
		IItem lightGrenade1 = items1.get(0);
		pickUpCont.pickUpItem(lightGrenade1);
		// player 2 Actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 1 actions
		moveCont.move(Direction.NORTHWEST);
		moveCont.move(Direction.SOUTHWEST);
		moveCont.move(Direction.SOUTH);
		// Player 2 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 1 actions:
		moveCont.move(Direction.SOUTH);
		// TODO try to place LG
	}
}
