package scenariotests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import game.Game;
import gui.GUI;
import item.IItem;
import item.identitydisk.IdentityDisk;
import item.lightgrenade.LightGrenade;
import item.lightgrenade.LightGrenadeState;
import java.util.List;
import org.junit.Test;
import controllers.EndTurnController;
import controllers.GUIDataController;
import controllers.MoveController;
import controllers.NewGameController;
import controllers.PickUpItemController;
import controllers.UseItemController;
import square.Direction;
import ObjectronExceptions.CannotPlaceLightGrenadeException;
import ObjectronExceptions.IllegalMoveException;
import ObjectronExceptions.IllegalUseException;
import ObjectronExceptions.ItemNotOnSquareException;

/**
 * Tests the "Use An Item From The Inventory" use case.
 * 
 * Tests: - Place maximum one lightgrenade on a square - Cannot place light
 * grenade on start positions
 * 
 * @author Tom
 */
@SuppressWarnings("javadoc")
public class UseItemTest extends SetupTestGrid {
	
	@Test
	public void testSuccess_LG() {
		// first player 1 move to lightgrenade
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH); // teleported
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		
		// player 2
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// player 1 pick up LG
		LightGrenade LG = (LightGrenade) playerDB.getCurrentPlayer().getCurrentLocation()
				.getCarryableItems().get(0);
		pickUpCont.pickUpItem(LG);
		useItemCont.useItem(LG);
		
		/*
		 * TODO: We can't get an entire list of items anymore. The exploded
		 * light grenade is not carriable, and therefore I can't replace this
		 * with getCarriableItems(), like the rest
		 */
		assertTrue(playerDB.getCurrentPlayer().getCurrentLocation().contains(LG));
		assertEquals(LightGrenadeState.ACTIVE, LG.getState());
		
	}
	
	@Test
	public void testMaximumOneLightGrenadePerSquare() {
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 2 actions
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// Player 1 actions
		moveCont.move(Direction.WEST);
		endTurnCont.endTurn();
		
		// Player 2 actions
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
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
		endTurnCont.endTurn();
		
		// Player 1 actions
		moveCont.move(Direction.EAST);
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
	public void testUseIdentityDisc() throws IllegalStateException, IllegalArgumentException,
			IllegalMoveException, CannotPlaceLightGrenadeException {
		// set a DummyGUI, so we have control over the returned direction by
		// DummyGUI#getBasicDirection()
		useItemCont.setGUI(new DummyGUI());
		
		// player 1
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		IdentityDisk id = (IdentityDisk) playerDB.getCurrentPlayer().getCurrentLocation()
				.getCarryableItems().get(0);
		pickUpCont.pickUpItem(id);
		assertFalse(playerDB.getCurrentPlayer().getCurrentLocation().getCarryableItems()
				.contains(id));
		endTurnCont.endTurn();
		
		// player 2
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// player 1
		useItemCont.useItem(id);
		assertFalse(playerDB.getCurrentPlayer().getInventoryContent().contains(id));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testArgumentNull() {
		useItemCont.useItem(null);
	}
	
	@Test(expected = IllegalUseException.class)
	public void testUse_NotInInventory() {
		useItemCont.useItem(new LightGrenade());
	}
	
	/**
	 * set a DummyGUI, so we have control over the returned direction by
	 * DummyGUI#getBasicDirection()
	 */
	private class DummyGUI extends GUI {
		
		public DummyGUI() {
			super(null, null, null, null, null, null);
		}
		
		@Override
		public Direction getBasicDirection() {
			return Direction.NORTH;
		}
		
	}
}
