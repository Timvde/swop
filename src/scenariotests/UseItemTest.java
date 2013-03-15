package scenariotests;

import game.Game;
import grid.Coordinate;
import grid.Direction;
import grid.Grid;
import grid.GridBuilder;
import item.IItem;
import java.util.List;
import org.junit.Test;
import player.PlayerDataBase;
import controllers.EndTurnController;
import controllers.MoveController;
import controllers.PickUpItemController;

/**
 * Test if the use item mechanics work correctly.
 * 
 * Tests: - Place maximum one lightgrenade on a square - Cannot place light
 * grenade on start positions
 * 
 * @author Tom
 */
@SuppressWarnings("javadoc")
public class UseItemTest {
	
	private static PickUpItemController	pickUpCont;
	private static MoveController		moveCont;
	private static EndTurnController	endTurnCont;
	private static Grid					grid;
	private static PlayerDataBase		playerDB;
	
	private void newGame() {
		Game game = new Game();
		
		playerDB = new PlayerDataBase(grid);
		
		GridBuilder builder = new GridBuilder();
		grid = builder.getPredefinedTestGrid();
		
		Coordinate[] startingCoords = new Coordinate[2];
		startingCoords[0] = new Coordinate(grid.getWidth() - 1, 0);
		startingCoords[1] = new Coordinate(0, grid.getHeight() - 1);
		
		playerDB.createNewDB(startingCoords, grid);
		
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
		List<IItem> items1 = grid.getSquareAt(playerDB.getCurrentPlayer().getCurrentLocation())
				.getCarryableItems();
		IItem lightGrenade1 = items1.get(0);
		pickUpCont.pickUpItem(lightGrenade1);
		moveCont.move(Direction.EAST);
		List<IItem> items2 = grid.getSquareAt(playerDB.getCurrentPlayer().getCurrentLocation())
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
		List<IItem> items1 = grid.getSquareAt(playerDB.getCurrentPlayer().getCurrentLocation())
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
