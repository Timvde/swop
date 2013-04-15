package scenariotests;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import game.Game;
import grid.Grid;
import grid.AGridBuilder;
import gui.DummyGUI;
import item.IItem;
import item.identitydisk.IdentityDisk;
import item.lightgrenade.LightGrenade;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import player.PlayerDataBase;
import square.Direction;
import ObjectronExceptions.CannotPlaceLightGrenadeException;
import ObjectronExceptions.IllegalMoveException;
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
	
	@Before
	public void setUp() {
		Game game = new Game();
		playerDB = new PlayerDataBase();
		grid = new AGridBuilder(playerDB.createNewDB()).getPredefinedTestGrid(false);
		
		game.start();
		game.setGrid(grid);
		
		moveCont = new MoveController(playerDB);
		endTurnCont = new EndTurnController(playerDB);
		pickUpCont = new PickUpItemController(playerDB);
		useItemCont = new UseItemController(playerDB);
	}
	
	@Test
	public void testMaximumOneLightGrenadePerSquare() throws IllegalStateException,
			IllegalArgumentException, IllegalMoveException, CannotPlaceLightGrenadeException {
		
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
	
	@Test(expected = IllegalArgumentException.class)
	public void testUseItem_itemNotInInventory() throws IllegalStateException,
			IllegalArgumentException, IllegalMoveException, CannotPlaceLightGrenadeException {
		moveCont.move(Direction.SOUTH);
		
		useItemCont.useItem(new LightGrenade());
	}
	
	@Test
	public void testUseIdentityDisc() throws IllegalStateException, IllegalArgumentException,
			IllegalMoveException, CannotPlaceLightGrenadeException {
		// set a DummyGUI, so we have control over the returned direction by
		// DummyGUI#getBasicDirection()
		useItemCont.setGUI(new DummyGUI());
		
		
		// player 1
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		IdentityDisk id = (IdentityDisk) playerDB.getCurrentPlayer().getCurrentLocation()
				.getAllItems().get(0);
		pickUpCont.pickUpItem(id);
		
		
		// player 2
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// player 1
		useItemCont.useItem(id);
		
		assertFalse(playerDB.getCurrentPlayer().getCurrentLocation().contains(id));
		assertFalse(playerDB.getCurrentPlayer().getInventoryContent().contains(id));
	}
	
}
