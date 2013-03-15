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
 * Test if the pick up action works correctly.
 * 
 * Tests: - Player can carry maximum 6 items
 * 
 * @author Tom
 */
@SuppressWarnings("javadoc")
public class PickUpTest {
	
	private static PickUpItemController	pickUpCont;
	private static EndTurnController	endTurnCont;
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
		endTurnCont = new EndTurnController(playerDB);
		pickUpCont = new PickUpItemController(playerDB);
	}
	
	@Test
	public void testPlayerCanCarryMaximum6Items() {
		newGame();
		
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
		moveCont.move(Direction.EAST);
		List<IItem> items3 = grid.getSquareAt(playerDB.getCurrentPlayer().getCurrentLocation())
				.getCarryableItems();
		IItem lightGrenade3 = items3.get(0);
		pickUpCont.pickUpItem(lightGrenade3);
		moveCont.move(Direction.EAST);
		// Player 2 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 1 actions
		List<IItem> items4 = grid.getSquareAt(playerDB.getCurrentPlayer().getCurrentLocation())
				.getCarryableItems();
		IItem lightGrenade4 = items4.get(0);
		pickUpCont.pickUpItem(lightGrenade4);
		moveCont.move(Direction.NORTH);
		List<IItem> items5 = grid.getSquareAt(playerDB.getCurrentPlayer().getCurrentLocation())
				.getCarryableItems();
		IItem lightGrenade5 = items5.get(0);
		pickUpCont.pickUpItem(lightGrenade5);
		// Player 2 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 1 actions
		moveCont.move(Direction.WEST);
		List<IItem> items6 = grid.getSquareAt(playerDB.getCurrentPlayer().getCurrentLocation())
				.getCarryableItems();
		IItem lightGrenade6 = items6.get(0);
		pickUpCont.pickUpItem(lightGrenade6);
		moveCont.move(Direction.WEST);
		// Player 2 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 1 actions
		List<IItem> items7 = grid.getSquareAt(playerDB.getCurrentPlayer().getCurrentLocation())
				.getCarryableItems();
		IItem lightGrenade7 = items7.get(0);
		pickUpCont.pickUpItem(lightGrenade7);
		
		// TODO check if inventory full error
	}
}
