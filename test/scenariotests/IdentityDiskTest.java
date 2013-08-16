package scenariotests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import game.CTFMode;
import grid.builder.DeterministicGridBuilderDirector;
import item.IItem;
import item.UseArguments;
import item.identitydisk.DummyIDArgumentsHandler;
import item.identitydisk.UnchargedIdentityDisk;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import player.Player;
import player.actions.UseAction;
import square.Direction;

@SuppressWarnings("javadoc")
public class IdentityDiskTest extends SetUpTestGrid {
	
	@Before
	public void setUp() {
		super.setUp(new CTFMode(DeterministicGridBuilderDirector.NUMBER_OF_PLAYERS_ON_TEST_GRID));
	}
	
	/**
	 * Player 1 will pickup the ID west of him and we will check if it is picked
	 * up successfully
	 */
	@Test
	public void testPickup_ID_Success() {
		// Player 1 actions
		Player player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		List<IItem> itemsList = playerDB.getCurrentPlayer().getCurrentPosition()
				.getCarryableItems();
		
		assertEquals(1, itemsList.size());
		assertTrue(itemsList.get(0) instanceof UnchargedIdentityDisk);
		
		UnchargedIdentityDisk ID = (UnchargedIdentityDisk) itemsList.get(0);
		pickUpCont.pickUpItem(ID);
		
		// The system removes the item from the gameboard
		assertFalse(player1.getCurrentPosition().getCarryableItems().contains(ID));
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
		Player player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		List<IItem> itemsList = playerDB.getCurrentPlayer().getCurrentPosition()
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
		// USE THE ID TO THE EAST
		DummyIDArgumentsHandler idHandler = new DummyIDArgumentsHandler();
		idHandler.setChoice(2);
		UseArguments<?> arguments = ID.getUseArguments();
		if (arguments != null)
			idHandler.handleArguments(arguments);
		playerDB.getCurrentPlayer().performAction(new UseAction(ID, idHandler));
		
		// Check if the ID is no longer in the inventory
		assertFalse(player1.getInventoryContent().contains(ID));
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		
		// See if the ID landed on the correct square
		List<IItem> itemsList2 = playerDB.getCurrentPlayer().getCurrentPosition()
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
		Player player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		List<IItem> itemsList = playerDB.getCurrentPlayer().getCurrentPosition()
				.getCarryableItems();
		
		assertEquals(1, itemsList.size());
		assertTrue(itemsList.get(0) instanceof UnchargedIdentityDisk);
		
		UnchargedIdentityDisk ID = (UnchargedIdentityDisk) itemsList.get(0);
		pickUpCont.pickUpItem(ID);
		
		moveCont.move(Direction.SOUTH);
		
		// Player 2 actions
		assertNotSame(player1, playerDB.getCurrentPlayer());
		Player player2 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.NORTH);
		endTurnCont.endTurn();
		
		// Player 1 actions
		assertSame(player1, playerDB.getCurrentPlayer());
		// USE THE ID TO THE EAST
		DummyIDArgumentsHandler idHandler = new DummyIDArgumentsHandler();
		idHandler.setChoice(1);
		UseArguments<?> arguments = ID.getUseArguments();
		if (arguments != null)
			idHandler.handleArguments(arguments);
		playerDB.getCurrentPlayer().performAction(new UseAction(ID, idHandler));
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
		List<IItem> itemsList2 = playerDB.getCurrentPlayer().getCurrentPosition()
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
		Player player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		List<IItem> itemsList = playerDB.getCurrentPlayer().getCurrentPosition()
				.getCarryableItems();
		
		assertEquals(1, itemsList.size());
		assertTrue(itemsList.get(0) instanceof UnchargedIdentityDisk);
		
		UnchargedIdentityDisk ID = (UnchargedIdentityDisk) itemsList.get(0);
		pickUpCont.pickUpItem(ID);
		
		moveCont.move(Direction.SOUTH);
		
		// Player 2 actions
		assertNotSame(player1, playerDB.getCurrentPlayer());
		Player player2 = playerDB.getCurrentPlayer();
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
		// Shoot the ID at player 2:
		// USE THE ID TO THE EAST
		DummyIDArgumentsHandler idHandler = new DummyIDArgumentsHandler();
		idHandler.setChoice(1);
		UseArguments<?> arguments = ID.getUseArguments();
		if (arguments != null)
			idHandler.handleArguments(arguments);
		playerDB.getCurrentPlayer().performAction(new UseAction(ID, idHandler));
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		// Test if it is again player 1's turn (player 2 skipped turn):
		assertSame(player1, playerDB.getCurrentPlayer());
	}
	
}
