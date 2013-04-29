package scenariotests;

import static org.junit.Assert.*;
import game.Game;
import grid.Coordinate;
import grid.Grid;
import grid.AGridBuilder;
import grid.RandomGridBuilder;
import item.IItem;
import item.lightgrenade.LightGrenade;
import java.util.List;
import org.junit.Test;
import ObjectronExceptions.IllegalMoveException;
import player.PlayerDataBase;
import square.Direction;
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
		
		playerDB = new PlayerDataBase();
		
		grid = new RandomGridBuilder(playerDB.createNewDB()).getPredefinedTestGrid(false);
		
		game.start();
		game.setGrid(grid);
		
		moveCont = new MoveController(playerDB);
		endTurnCont = new EndTurnController(playerDB);
		pickUpCont = new PickUpItemController(playerDB);
	}
	
	@Test
	public void testPickup() throws IllegalStateException, IllegalArgumentException,
			IllegalMoveException {
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
		List<IItem> items1 = playerDB.getCurrentPlayer().getCurrentLocation().getCarryableItems();
		IItem lightGrenade1 = items1.get(0);
		pickUpCont.pickUpItem(lightGrenade1);
		
		assertTrue(playerDB.getCurrentPlayer().getInventoryContent().contains(lightGrenade1));
		assertFalse(playerDB.getCurrentPlayer().getCurrentLocation().contains(lightGrenade1));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testItemNotOnSquare() throws IllegalStateException, IllegalArgumentException, IllegalMoveException {
		newGame();
		
		moveCont.move(Direction.SOUTH);
		pickUpCont.pickUpItem(new LightGrenade());
	}
	
	@Test
	public void testPlayerCanCarryMaximum6Items() throws IllegalStateException,
			IllegalArgumentException, IllegalMoveException {
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
		List<IItem> items1 = playerDB.getCurrentPlayer().getCurrentLocation().getCarryableItems();
		IItem lightGrenade1 = items1.get(0);
		pickUpCont.pickUpItem(lightGrenade1);
		moveCont.move(Direction.EAST);
		List<IItem> items2 = playerDB.getCurrentPlayer().getCurrentLocation().getCarryableItems();
		IItem lightGrenade2 = items2.get(0);
		pickUpCont.pickUpItem(lightGrenade2);
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 2 actions
		moveCont.move(Direction.EAST);
		List<IItem> items3 = playerDB.getCurrentPlayer().getCurrentLocation().getCarryableItems();
		IItem lightGrenade3 = items3.get(0);
		pickUpCont.pickUpItem(lightGrenade3);
		moveCont.move(Direction.EAST);
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 2 actions
		List<IItem> items4 = playerDB.getCurrentPlayer().getCurrentLocation().getCarryableItems();
		IItem lightGrenade4 = items4.get(0);
		pickUpCont.pickUpItem(lightGrenade4);
		moveCont.move(Direction.NORTH);
		List<IItem> items5 = playerDB.getCurrentPlayer().getCurrentLocation().getCarryableItems();
		IItem lightGrenade5 = items5.get(0);
		pickUpCont.pickUpItem(lightGrenade5);
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 2 actions
		moveCont.move(Direction.NORTHWEST);
		List<IItem> items6 = playerDB.getCurrentPlayer().getCurrentLocation().getCarryableItems();
		IItem lightGrenade6 = items6.get(0);
		pickUpCont.pickUpItem(lightGrenade6);
		moveCont.move(Direction.WEST);
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 2 actions
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 2 actions
		moveCont.move(Direction.SOUTHWEST);
		List<IItem> items7 = playerDB.getCurrentPlayer().getCurrentLocation().getCarryableItems();
		IItem lightGrenade7 = items7.get(0);
		
		boolean assertionThrown = false;
		try {
			pickUpCont.pickUpItem(lightGrenade7);
		}
		catch (IllegalArgumentException e) {
			assertionThrown = true;
		}
		assertTrue(assertionThrown);
	}
}
