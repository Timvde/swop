package scenariotests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import game.CTFMode;
import grid.Coordinate;
import grid.builder.DeterministicGridBuilderDirector;
import item.IItem;
import item.identitydisk.IdentityDisk;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import player.IPlayer;
import square.Direction;
import square.Square;
import square.SquareContainer;

/**
 * This test class tests all cases when a flag should be dropped, and whether it
 * happened correctly.
 */
@SuppressWarnings("javadoc")
public class DropFlagTest extends SetUpTestGrid {
	
	@Before
	public void setUp() {
		super.setUp(new CTFMode(DeterministicGridBuilderDirector.NUMBER_OF_PLAYERS_ON_TEST_GRID));
	}
	
	@Test
	public void hitByLightGrenadeTest() {
		// Player 1 actions
		IPlayer player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH); // Player 1 teleports
		endTurnCont.endTurn();
		
		// Player 2 actions
		assertNotSame(player1, playerDB.getCurrentPlayer());
		IPlayer player2 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		List<IItem> items1 = playerDB.getCurrentPlayer().getCurrentLocation().getCarryableItems();
		IItem lightGrenade1 = items1.get(0);
		pickUpCont.pickUpItem(lightGrenade1);
		useItemCont.useItem(lightGrenade1);
		
		// Player 1 actions
		assertSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// Player 2 actions
		assertSame(player2, playerDB.getCurrentPlayer());
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		
		// Turn ended, light trail should be gone from the square with the
		// active light grenade
		
		// Player 1 actions
		assertSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.SOUTH);
		List<IItem> items2 = playerDB.getCurrentPlayer().getCurrentLocation().getCarryableItems();
		IItem flag = items2.get(0);
		pickUpCont.pickUpItem(flag);
		
		assertTrue(playerDB.getCurrentPlayer().getInventoryContent().contains(flag));
		
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		
		assertFalse(playerDB.getCurrentPlayer().getInventoryContent().contains(flag));
		assertTrue(itemInSurroundingSquaresOf(flag, playerDB.getCurrentPlayer()
				.getCurrentLocation()));
	}
	
	@Test
	public void passThroughTeleporterTest() {
		// Player 1 actions
		IPlayer player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH); // Player 1 teleports
		endTurnCont.endTurn();
		
		// Player 2 actions
		assertNotSame(player1, playerDB.getCurrentPlayer());
		IPlayer player2 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		
		// Player 1 actions
		assertSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		List<IItem> items = playerDB.getCurrentPlayer().getCurrentLocation().getCarryableItems();
		IItem flag = items.get(0);
		pickUpCont.pickUpItem(flag);
		assertTrue(playerDB.getCurrentPlayer().getInventoryContent().contains(flag));
		
		// Now move around to get the light trail out of the way
		moveCont.move(Direction.EAST);
		
		// .. But wait for Player 2 first
		assertSame(player2, playerDB.getCurrentPlayer());
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// Player 1
		assertSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.WEST); // Flag should be dropped here
		
		assertFalse(playerDB.getCurrentPlayer().getInventoryContent().contains(flag));
		assertTrue(itemInSurroundingSquaresOf(flag, grid.getSquareAt(new Coordinate(0, 7))));
	}
	
	@Test
	public void hitByIdentityDisk() {
		// Player 1 actions
		IPlayer player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH); // Player 1 teleports
		endTurnCont.endTurn();
		
		// Player 2 actions
		assertNotSame(player1, playerDB.getCurrentPlayer());
		IPlayer player2 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		List<IItem> items1 = playerDB.getCurrentPlayer().getCurrentLocation().getCarryableItems();
		IItem identityDisk = items1.get(0);
		pickUpCont.pickUpItem(identityDisk);
		moveCont.move(Direction.EAST);
		
		// Player 1 actions
		assertSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		List<IItem> items2 = playerDB.getCurrentPlayer().getCurrentLocation().getCarryableItems();
		IItem flag = items2.get(0);
		pickUpCont.pickUpItem(flag);
		assertTrue(playerDB.getCurrentPlayer().getInventoryContent().contains(flag));
		moveCont.move(Direction.NORTHEAST);
		
		// Player 2 actions
		assertSame(player2, playerDB.getCurrentPlayer());
		moveCont.move(Direction.NORTH);
		((IdentityDisk) identityDisk).setDirection(Direction.WEST);
		useItemCont.useItem(identityDisk);
		endTurnCont.endTurn();
		
		// Player 1
		assertSame(player1, playerDB.getCurrentPlayer());
		assertFalse(playerDB.getCurrentPlayer().getInventoryContent().contains(flag));
		assertTrue(itemInSurroundingSquaresOf(flag, playerDB.getCurrentPlayer()
				.getCurrentLocation()));
	}
	
	private boolean itemInSurroundingSquaresOf(IItem flag, Square square) {
		SquareContainer container = (SquareContainer) square;
		for (Direction direction : Direction.values()) {
			if (container.getNeighbourIn(direction) != null
					&& container.getNeighbourIn(direction).contains(flag))
				return true;
		}
		return false;
	}
	
}
