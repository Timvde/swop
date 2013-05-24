package scenariotests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import game.CTFMode;
import game.GameMode;
import game.RaceMode;
import grid.builder.DeterministicGridBuilderDirector;
import item.IItem;
import item.identitydisk.UnchargedIdentityDisk;
import java.util.List;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theory;
import player.IPlayer;
import square.Direction;

@SuppressWarnings("javadoc")
public class IdentityDiskTest extends SetUpTestGrid {
	
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
	
	/**
	 * Player 1 will pickup the ID west of him and we will check if it is picked
	 * up successfully
	 */
	@Test
	public void testPickup_ID_Success() {
		// Player 1 actions
		IPlayer player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		List<IItem> itemsList = playerDB.getCurrentPlayer().getCurrentLocation()
				.getCarryableItems();
		
		assertEquals(1, itemsList.size());
		assertTrue(itemsList.get(0) instanceof UnchargedIdentityDisk);
		
		UnchargedIdentityDisk ID = (UnchargedIdentityDisk) itemsList.get(0);
		pickUpCont.pickUpItem(ID);
		
		// The system removes the item from the gameboard
		assertFalse(player1.getCurrentLocation().getCarryableItems().contains(ID));
		// and puts it in the inventory of the player
		player1.getInventoryContent().contains(ID);
	}
	
	/**
	 * Player 1 will pickup the ID west of him and shoot it at the grid boundary
	 * east of him. We will then check if the ID is on the correct square and no
	 * longer in the player inventory.
	 */
	@Test
	public void testShoot_ID_GridBorder() {
		// Player 1 actions
		IPlayer player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		List<IItem> itemsList = playerDB.getCurrentPlayer().getCurrentLocation()
				.getCarryableItems();
		
		assertEquals(1, itemsList.size());
		assertTrue(itemsList.get(0) instanceof UnchargedIdentityDisk);
		
		UnchargedIdentityDisk ID = (UnchargedIdentityDisk) itemsList.get(0);
		pickUpCont.pickUpItem(ID);
		
		moveCont.move(Direction.SOUTH);
		
		// Player 2 actions
		assertNotSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.NORTH);
		endTurnCont.endTurn();
		
		// Player 1 actions
		assertSame(player1, playerDB.getCurrentPlayer());
		ID.setDirection(Direction.EAST);
		// we do not use the controller here because it will give a nullpointer
		// after asking the gui for the direction:
		playerDB.getCurrentPlayer().useItem(ID);
		// Check if the ID is no longer in the inventory
		assertFalse(player1.getInventoryContent().contains(ID));
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		
		// See if the ID landed on the correct square
		List<IItem> itemsList2 = playerDB.getCurrentPlayer().getCurrentLocation()
				.getCarryableItems();
		
		assertTrue(itemsList2.contains(ID));
	}
	
	/**
	 * Player 1 will pickup the ID west of him and shoot it at the wall south of
	 * him. We will then check if the ID is on the correct square and no longer
	 * in the player inventory.
	 */
	@Test
	public void testShoot_ID_Wall() {
		// Player 1 actions
		IPlayer player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		List<IItem> itemsList = playerDB.getCurrentPlayer().getCurrentLocation()
				.getCarryableItems();
		
		assertEquals(1, itemsList.size());
		assertTrue(itemsList.get(0) instanceof UnchargedIdentityDisk);
		
		UnchargedIdentityDisk ID = (UnchargedIdentityDisk) itemsList.get(0);
		pickUpCont.pickUpItem(ID);
		
		moveCont.move(Direction.SOUTH);
		
		// Player 2 actions
		assertNotSame(player1, playerDB.getCurrentPlayer());
		IPlayer player2 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.NORTH);
		endTurnCont.endTurn();
		
		// Player 1 actions
		assertSame(player1, playerDB.getCurrentPlayer());
		ID.setDirection(Direction.SOUTH);
		// we do not use the controller here because it will give a nullpointer
		// after asking the gui for the direction:
		playerDB.getCurrentPlayer().useItem(ID);
		// Check if the ID is no longer in the inventory
		assertFalse(player1.getInventoryContent().contains(ID));
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		
		// Player 2 actions
		assertSame(player2, playerDB.getCurrentPlayer());
		moveCont.move(Direction.NORTH);
		endTurnCont.endTurn();
		
		// Player 1 actions
		assertSame(player1, playerDB.getCurrentPlayer());
		// See if the ID landed on the correct square
		List<IItem> itemsList2 = playerDB.getCurrentPlayer().getCurrentLocation()
				.getCarryableItems();
		
		assertTrue(itemsList2.contains(ID));
	}
	
	/**
	 * Player 1 will pickup the ID west of him and shoot it against player 2. We
	 * will then check if the turn of player 2 is skipped.
	 */
	@Test
	public void testShoot_ID_Player() {
		// Player 1 actions
		IPlayer player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		List<IItem> itemsList = playerDB.getCurrentPlayer().getCurrentLocation()
				.getCarryableItems();
		
		assertEquals(1, itemsList.size());
		assertTrue(itemsList.get(0) instanceof UnchargedIdentityDisk);
		
		UnchargedIdentityDisk ID = (UnchargedIdentityDisk) itemsList.get(0);
		pickUpCont.pickUpItem(ID);
		
		moveCont.move(Direction.SOUTH);
		
		// Player 2 actions
		assertNotSame(player1, playerDB.getCurrentPlayer());
		IPlayer player2 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		endTurnCont.endTurn();
		
		// Player 1 actions
		assertSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.SOUTHWEST);
		moveCont.move(Direction.SOUTHWEST);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		
		// Player 2 actions
		assertSame(player2, playerDB.getCurrentPlayer());
		moveCont.move(Direction.NORTH);
		endTurnCont.endTurn();
		
		// Player 1 actions
		assertSame(player1, playerDB.getCurrentPlayer());
		// Shoot the ID at player 2:
		ID.setDirection(Direction.SOUTH);
		// we do not use the controller here because it will give a nullpointer
		// after asking the gui for the direction:
		playerDB.getCurrentPlayer().useItem(ID);
		endTurnCont.endTurn();
		
		// Test if it is again player 1's turn (player 2 skipped turn):
		assertSame(player1, playerDB.getCurrentPlayer());
	}
	
	/**
	 * We will set the direction of the identity disk to null and see if the
	 * correct exception is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testDirection_ID_Null() {
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		List<IItem> itemsList = playerDB.getCurrentPlayer().getCurrentLocation()
				.getCarryableItems();
		
		assertEquals(1, itemsList.size());
		assertTrue(itemsList.get(0) instanceof UnchargedIdentityDisk);
		
		UnchargedIdentityDisk ID = (UnchargedIdentityDisk) itemsList.get(0);
		pickUpCont.pickUpItem(ID);
		
		ID.setDirection(null);
	}
	
	/**
	 * We will set the direction of the identity disk to invalid ones and see if
	 * the correct exception is thrown.
	 */
	@Test
	public void testDirection_ID_Wrong() {
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		List<IItem> itemsList = playerDB.getCurrentPlayer().getCurrentLocation()
				.getCarryableItems();
		
		assertEquals(1, itemsList.size());
		assertTrue(itemsList.get(0) instanceof UnchargedIdentityDisk);
		
		UnchargedIdentityDisk ID = (UnchargedIdentityDisk) itemsList.get(0);
		pickUpCont.pickUpItem(ID);
		
		boolean exceptionThrown = false;
		for (Direction direction : Direction.values()) {
			if (!direction.isPrimaryDirection()) {
				try {
					ID.setDirection(direction);
				}
				catch (IllegalArgumentException e) {
					exceptionThrown = true;
				}
				assertTrue(exceptionThrown);
				exceptionThrown = false;
			}
		}
	}
	
}
