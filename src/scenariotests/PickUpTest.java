package scenariotests;

import static org.junit.Assert.*;
import item.IItem;
import java.util.List;
import game.Game;
import grid.Direction;
import grid.Grid;
import org.junit.BeforeClass;
import org.junit.Test;
import player.PlayerDataBase;
import controllers.EndTurnController;
import controllers.GUIDataController;
import controllers.MoveController;
import controllers.PickUpItemController;

/**
 * Test if the pick up action works correctly.
 * 
 * Tests: - Player can carry maximum 6 items
 * 
 * @author Tom
 */
public class PickUpTest {
	
	private static GUIDataController	guiDataCont;
	private static PickUpItemController	pickUpCont;
	private static EndTurnController	endTurnCont;
	private static MoveController		moveCont;
	private static Grid					grid;
	private static PlayerDataBase		playerDB;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		// TODO set up playerDB en predefined grid, en bij Game doe setGrid
		Game game = new Game();
		game.start();
		
		guiDataCont = new GUIDataController(playerDB, grid);
		pickUpCont = new PickUpItemController(playerDB);
		moveCont = new MoveController(playerDB);
		endTurnCont = new EndTurnController(playerDB);
	}
	
	@Test
	public void testPlayerCanCarryMaximum6Items() {
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
		moveCont.move(Direction.EAST);
		List<IItem> items3 = grid.getSquareOfPlayer(playerDB.getCurrentPlayer())
				.getCarryableItems();
		IItem lightGrenade3 = items3.get(0);
		pickUpCont.pickUpItem(lightGrenade3);
		moveCont.move(Direction.EAST);
		// Player 2 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 1 actions
		List<IItem> items4 = grid.getSquareOfPlayer(playerDB.getCurrentPlayer())
				.getCarryableItems();
		IItem lightGrenade4 = items4.get(0);
		pickUpCont.pickUpItem(lightGrenade4);
		moveCont.move(Direction.NORTH);
		List<IItem> items5 = grid.getSquareOfPlayer(playerDB.getCurrentPlayer())
				.getCarryableItems();
		IItem lightGrenade5 = items5.get(0);
		pickUpCont.pickUpItem(lightGrenade5);
		// Player 2 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 1 actions
		moveCont.move(Direction.WEST);
		List<IItem> items6 = grid.getSquareOfPlayer(playerDB.getCurrentPlayer())
				.getCarryableItems();
		IItem lightGrenade6 = items6.get(0);
		pickUpCont.pickUpItem(lightGrenade6);
		moveCont.move(Direction.WEST);
		// Player 2 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 1 actions
		List<IItem> items7 = grid.getSquareOfPlayer(playerDB.getCurrentPlayer())
				.getCarryableItems();
		IItem lightGrenade7 = items7.get(0);
		pickUpCont.pickUpItem(lightGrenade7);
		
		// TODO check if inventory full error
	}
}
