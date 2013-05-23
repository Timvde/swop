package scenariotests;

import static org.junit.Assert.*;
import item.IItem;
import item.identitydisk.UnchargedIdentityDisk;
import java.util.List;
import org.junit.Test;
import player.IPlayer;
import square.Direction;

@SuppressWarnings("javadoc")
public class IdentityDiskTest extends SetupTestGrid {
	
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
	 * Player 1 will pickup the ID west of him and shoot it at the grid boundary east
	 * of him. We will then check if the ID is on the correct square and no longer
	 * in the player inventory.
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
		moveCont.move(Direction.NORTH);
		endTurnCont.endTurn();
		
		// Player 1 actions
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
		
		assertTrue(itemsList2.get(0) == ID);
	}
	
	/**
	 * Player 1 will pickup the ID west of him and shoot it at the wall south
	 * of him. We will then check if the ID is on the correct square and no longer
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
		moveCont.move(Direction.NORTH);
		endTurnCont.endTurn();
		
		// Player 1 actions
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
		moveCont.move(Direction.NORTH);
		endTurnCont.endTurn();
		
		// Player 1 actions
		// See if the ID landed on the correct square
		List<IItem> itemsList2 = playerDB.getCurrentPlayer().getCurrentLocation()
				.getCarryableItems();
		
		assertTrue(itemsList2.get(0) == ID);
	}
	
	/**
	 * Player 1 will pickup the ID west of him and shoot it against player 2.
	 * We will then check if the turn of player 2 is skipped.
	 */
	@Test
	public void testShoot_ID_Player() {
		// Player 1 actions
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
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		endTurnCont.endTurn();
		
		// Player 1 actions
		moveCont.move(Direction.SOUTHWEST);
		moveCont.move(Direction.SOUTHWEST);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		
		// Player 2 actions
		moveCont.move(Direction.NORTH);
		endTurnCont.endTurn();
		
		// Player 1 actions
		// Shoot the ID at player 2:
		ID.setDirection(Direction.SOUTH);
		// we do not use the controller here because it will give a nullpointer
		// after asking the gui for the direction:
		playerDB.getCurrentPlayer().useItem(ID);
		
		moveCont.move(Direction.WEST);
		endTurnCont.endTurn();
		
		// Test if it is again player 1's turn (player 2 skipped turn):
		assertEquals(playerDB.getCurrentPlayer().getID(), 1);
	}
	
	/**
	 * We will set the direction of the identity disk to null and see if
	 * the correct exception is thrown.
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
		try {
			ID.setDirection(Direction.NORTHEAST);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		boolean exceptionThrown2 = false;
		try {
			ID.setDirection(Direction.SOUTHEAST);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown2 = true;
		}
		assertTrue(exceptionThrown2);
		
		boolean exceptionThrown3 = false;
		try {
			ID.setDirection(Direction.SOUTHWEST);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown3 = true;
		}
		assertTrue(exceptionThrown3);
		
		boolean exceptionThrown4 = false;
		try {
			ID.setDirection(Direction.NORTHWEST);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown4 = true;
		}
		assertTrue(exceptionThrown4);
	}
	
}
