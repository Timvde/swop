package scenariotests;

import static org.junit.Assert.*;
import game.Game;
import grid.Coordinate;
import grid.Direction;
import grid.Grid;
import grid.GridBuilder;
import item.IItem;
import java.util.List;
import org.junit.Test;
import ObjectronExceptions.IllegalMoveException;
import player.PlayerDataBase;
import controllers.EndTurnController;
import controllers.MoveController;
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
@SuppressWarnings("javadoc")
public class UseItemTest {
	
	private static PickUpItemController	pickUpCont;
	private static MoveController		moveCont;
	private static EndTurnController	endTurnCont;
	private static UseItemController	useItemCont;
	private static Grid					grid;
	private static PlayerDataBase		playerDB;
	
	private void newGame() {
		Game game = new Game();
		
		playerDB = new PlayerDataBase(grid);
		
		GridBuilder builder = new GridBuilder();
		grid = builder.getPredefinedTestGrid(false);
		
		Coordinate[] startingCoords = new Coordinate[2];
		startingCoords[0] = new Coordinate(grid.getWidth() - 1, 0);
		startingCoords[1] = new Coordinate(0, grid.getHeight() - 1);
		
		playerDB.createNewDB(startingCoords, grid);
		
		game.setGrid(grid);
		
		game.start();
		
		moveCont = new MoveController(playerDB);
		pickUpCont = new PickUpItemController(playerDB);
		endTurnCont = new EndTurnController(playerDB);
		useItemCont = new UseItemController(playerDB);
	}
	
	@Test 
	public void testMaximumOneLightGrenadePerSquare() throws IllegalStateException, IllegalArgumentException, IllegalMoveException {
		newGame();
		
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 2 actions
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 2 actions
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 2 actions
		List<IItem> items1 = grid.getSquareAt(playerDB.getCurrentPlayer().getCurrentLocation())
				.getCarryableItems();
		IItem lightGrenade1 = items1.get(0);
		pickUpCont.pickUpItem(lightGrenade1);
		moveCont.move(Direction.EAST);
		List<IItem> items2 = grid.getSquareAt(playerDB.getCurrentPlayer().getCurrentLocation())
				.getCarryableItems();
		IItem lightGrenade2 = items2.get(0);
		pickUpCont.pickUpItem(lightGrenade2);
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 2 actions
		useItemCont.useItem(lightGrenade1);
		
		boolean assertionThrown = false;
		try {
			useItemCont.useItem(lightGrenade2);
		}
		catch (Exception e) {
			assertionThrown = true;
		}
		assertTrue(assertionThrown);
	}
	
	@Test
	public void testCannotPlaceLightGrenadeOnStartPositions() throws IllegalStateException, IllegalArgumentException, IllegalMoveException {
		newGame();
		
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 2 actions
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		List<IItem> items1 = grid.getSquareAt(playerDB.getCurrentPlayer().getCurrentLocation())
				.getCarryableItems();
		IItem lightGrenade1 = items1.get(0);
		pickUpCont.pickUpItem(lightGrenade1);
		// player 1 Actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 2 actions
		moveCont.move(Direction.NORTHWEST);
		moveCont.move(Direction.SOUTHWEST);
		moveCont.move(Direction.SOUTH);
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 2 actions:
		moveCont.move(Direction.SOUTH);
		
		boolean assertionThrown = false;
		try {
			useItemCont.useItem(lightGrenade1);
		} // TODO specifieker
		catch (Exception e) {
			assertionThrown = true;
			e.printStackTrace();
		}
		assertTrue(assertionThrown);
	}
}
