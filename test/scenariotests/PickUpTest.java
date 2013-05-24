package scenariotests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import game.CTFMode;
import game.GameMode;
import game.RaceMode;
import grid.builder.DeterministicGridBuilderDirector;
import item.DummyEffectFactory;
import item.IItem;
import item.identitydisk.UnchargedIdentityDisk;
import item.lightgrenade.LightGrenade;
import java.util.List;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theory;
import player.Player;
import player.PlayerActionManager;
import square.Direction;
import ObjectronExceptions.InventoryFullException;
import ObjectronExceptions.ItemNotOnSquareException;

/**
 * Tests the "Pick Up An Item" use case.
 */
@SuppressWarnings("javadoc")
public class PickUpTest extends SetUpTestGrid {
	
	public static @DataPoints
	GameMode[]	candidates	= { new RaceMode(),
			new CTFMode(DeterministicGridBuilderDirector.NUMBER_OF_PLAYERS_ON_TEST_GRID) };
	
	/**
	 * This method will be called with all gamemodes.
	 */
	@Theory
	public void setUp(GameMode mode) {
		super.setUp(mode);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPickup_Null() {
		pickUpCont.pickUpItem(null);
	}
	
	@Test(expected = ItemNotOnSquareException.class)
	public void testPickup_NotOnSquare() {
		moveCont.move(Direction.SOUTH);
		pickUpCont.pickUpItem(new LightGrenade(new DummyEffectFactory()));
	}
	
	/**
	 * player 1 will pickup the Uncharged Identity disc west of him
	 */
	@Test
	public void testPickup_UncharchedID_Success() {
		Player player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		
		assertEquals(player1, playerDB.getCurrentPlayer());
		List<IItem> itemsList = player1.getCurrentPosition().getCarryableItems();
		assertEquals(1, itemsList.size());
		assertTrue(itemsList.get(0) instanceof UnchargedIdentityDisk);
		UnchargedIdentityDisk ID = (UnchargedIdentityDisk) itemsList.get(0);
		pickUpCont.pickUpItem(ID);
		
		// The system removes the item from the gameboard
		assertFalse(player1.getCurrentPosition().getCarryableItems().contains(ID));
		// and puts it in the inventory of the player
		player1.getInventoryContent().contains(ID);
		
		// The system adds 1 to the number of actions that the player has
		// performed during this turn.
		assertEquals(PlayerActionManager.MAX_NUMBER_OF_ACTIONS_PER_TURN - 3,
				player1.getAllowedNumberOfActions());
	}
	
	/**
	 * player 2 will pickup the LG NothEast of him
	 */
	@Test
	public void testPickup_LG_Success() {
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// Player 2 actions
		Player player2 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// Player 2 actions
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		
		List<IItem> itemsList = playerDB.getCurrentPlayer().getCurrentPosition()
				.getCarryableItems();
		assertEquals(1, itemsList.size());
		assertTrue(itemsList.get(0) instanceof LightGrenade);
		LightGrenade LG = (LightGrenade) itemsList.get(0);
		pickUpCont.pickUpItem(LG);
		
		// The system removes the item from the gameboard
		assertFalse(player2.getCurrentPosition().getCarryableItems().contains(LG));
		// and puts it in the inventory of the player
		player2.getInventoryContent().contains(LG);
		
		// The system adds 1 to the number of actions that the player has
		// performed during this turn.
		assertEquals(PlayerActionManager.MAX_NUMBER_OF_ACTIONS_PER_TURN - 4,
				player2.getAllowedNumberOfActions());
	}
	
	@Test
	public void testPlayerCanCarryMaximum6Items() {
		// Player 1 actions
		Player player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// Player 2 actions
		Player player2 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// Player 1 actions
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.WEST);
		endTurnCont.endTurn();
		
		// Player 2 actions
		assertEquals(player2, playerDB.getCurrentPlayer());
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// Player 1 actions
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// Player 2 actions
		assertEquals(player2, playerDB.getCurrentPlayer());
		List<IItem> items1 = playerDB.getCurrentPlayer().getCurrentPosition().getCarryableItems();
		IItem lightGrenade1 = items1.get(0);
		pickUpCont.pickUpItem(lightGrenade1);
		moveCont.move(Direction.EAST);
		List<IItem> items2 = playerDB.getCurrentPlayer().getCurrentPosition().getCarryableItems();
		IItem lightGrenade2 = items2.get(0);
		pickUpCont.pickUpItem(lightGrenade2);
		endTurnCont.endTurn();
		
		// Player 1 actions
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// Player 2 actions
		assertEquals(player2, playerDB.getCurrentPlayer());
		moveCont.move(Direction.EAST);
		List<IItem> items3 = playerDB.getCurrentPlayer().getCurrentPosition().getCarryableItems();
		IItem lightGrenade3 = items3.get(0);
		pickUpCont.pickUpItem(lightGrenade3);
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// Player 1 actions
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// Player 2 actions
		assertEquals(player2, playerDB.getCurrentPlayer());
		List<IItem> items4 = playerDB.getCurrentPlayer().getCurrentPosition().getCarryableItems();
		IItem lightGrenade4 = items4.get(0);
		pickUpCont.pickUpItem(lightGrenade4);
		moveCont.move(Direction.NORTH);
		List<IItem> items5 = playerDB.getCurrentPlayer().getCurrentPosition().getCarryableItems();
		IItem lightGrenade5 = items5.get(0);
		pickUpCont.pickUpItem(lightGrenade5);
		endTurnCont.endTurn();
		
		// Player 1 actions
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// Player 2 actions
		assertEquals(player2, playerDB.getCurrentPlayer());
		moveCont.move(Direction.NORTHWEST);
		List<IItem> items6 = playerDB.getCurrentPlayer().getCurrentPosition().getCarryableItems();
		IItem lightGrenade6 = items6.get(0);
		pickUpCont.pickUpItem(lightGrenade6);
		moveCont.move(Direction.WEST);
		endTurnCont.endTurn();
		
		// Player 1 actions
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// Player 2 actions
		assertEquals(player2, playerDB.getCurrentPlayer());
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		endTurnCont.endTurn();
		
		// Player 1 actions
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// Player 2 actions
		assertEquals(player2, playerDB.getCurrentPlayer());
		moveCont.move(Direction.SOUTHWEST);
		List<IItem> items7 = playerDB.getCurrentPlayer().getCurrentPosition().getCarryableItems();
		IItem lightGrenade7 = items7.get(0);
		
		boolean exceptionThrown = false;
		try {
			pickUpCont.pickUpItem(lightGrenade7);
		}
		catch (InventoryFullException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
	}
}
