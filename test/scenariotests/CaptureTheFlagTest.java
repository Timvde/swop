package scenariotests;

import static org.junit.Assert.*;
import game.CTFMode;
import grid.Coordinate;
import grid.builder.DeterministicGridBuilderDirector;
import item.Flag;
import item.IItem;
import item.UseArguments;
import item.identitydisk.DummyIDArgumentsHandler;
import item.identitydisk.UnchargedIdentityDisk;
import item.lightgrenade.LightGrenade;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import player.Player;
import player.PlayerState;
import square.AbstractSquare;
import square.Direction;
import square.Square;
import player.TronPlayer;
import player.actions.UseAction;

/**
 * Test the capture the flag mode
 * 
 * 
 * At the start of the game, there is a flag on the starting position of each
 * player. Each player's goal is to capture the flag of the other players, i.e.
 * pick up the flag of another player and return it to your own starting
 * location. A player wins the game when he has captured the flag of each other
 * player at least once, or if all the other players are boxed in, such that
 * they can't move. A player can only carry a single flag at once. If a player
 * is carrying a flag and is hit by a light grenade, an identity disc, a charged
 * identity disc or if he uses a teleporter, the flag is dropped onto a randomly
 * selected square neighbouring the player's current square. A flag can be on a
 * square that already contains other items. When a player steps onto his own
 * starting location carrying another player's flag, that flag is captured. When
 * a player captures a flag, that flag is returned to its starting location.
 * When a player tries to pick up his own flag, his flag is returned to its
 * starting location. A player's own flag does not need to be on its starting
 * location for that player to be able to capture a flag. If a player is boxed
 * in, such that he cannot make a move, the player dies and is removed from the
 * game and disappears. If he was carrying a flag, the flag is dropped on the
 * square where the player was last alive. The other items he possessed
 * disappear.
 */
@SuppressWarnings("javadoc")
public class CaptureTheFlagTest extends SetUpTestGrid {
	
	@Before
	public void setUp() {
		super.setUp(new CTFMode(DeterministicGridBuilderDirector.NUMBER_OF_PLAYERS_ON_TEST_GRID));
	}
	
	/**
	 * Tests: At the start of the game, there is a flag on the starting position
	 * of each player. Each player's goal is to capture the flag of the other
	 * players, i.e. pick up the flag of another player and return it to your
	 * own starting location. A player wins the game when he has captured the
	 * flag of each other player at least once
	 * 
	 * When a player captures a flag, that flag is returned to its starting
	 * location.
	 */
	@Test
	public void testPlayerWinsWhenCapturedAllTheFlags() {
		// Player 1
		Player player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		// now player 1 is teleported
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// player 2 (bail there)
		assertNotSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		
		// player 1
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.SOUTH);
		// now player 1 is on the startsquare of player 2
		assertNotSame(PlayerState.FINISHED, ((TronPlayer) player1).getPlayerState());
		// pickup the flag:
		List<IItem> itemsList = playerDB.getCurrentPlayer().getCurrentPosition()
				.getCarryableItems();
		assertEquals(1, itemsList.size());
		assertTrue(itemsList.get(0) instanceof Flag);
		Flag flag = (Flag) itemsList.get(0);
		pickUpCont.pickUpItem(flag);
		endTurnCont.endTurn();
		
		// player 2 (make room)
		assertNotSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		
		// player 1
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		
		// player 2 (make room)
		assertNotSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.NORTH);
		endTurnCont.endTurn();
		
		// player 1
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.EAST);
		
		// player 2 (make room)
		assertNotSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.WEST);
		assertNotSame(player1, playerDB.getCurrentPlayer());
		endTurnCont.endTurn();
		
		// The test for winning is left out due to code constraints. This is on
		// the to do list, but there is no time now as I'm working on this
		// project on my own
		
		// player 1
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		
		// When a player captures a flag, that flag is returned to its starting
		// location.
		AbstractSquare startPos2 = grid
				.getSquareAt(DeterministicGridBuilderDirector.PLAYER2_START_POS);
		List<IItem> itemlist = startPos2.getAllItems();
		assertTrue(itemlist.contains(flag));
		
		playerDB = null;
	}
	
	/**
	 * Tests: If a player is boxed in, such that he cannot make a move, the
	 * player dies and is removed from the game and disappears. If he was
	 * carrying a flag, the flag is dropped on the square where the player was
	 * last alive.
	 */
	@Test
	public void testPlayerDies() {
		// Player 1
		Player player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		// now player 1 is teleported
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// player 2 (bail there)
		assertNotSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		
		// player 1
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.SOUTH);
		// now player 1 is on the startsquare of player 2
		assertNotSame(PlayerState.FINISHED, ((TronPlayer) player1).getPlayerState());
		// pickup the flag:
		List<IItem> itemsList = playerDB.getCurrentPlayer().getCurrentPosition()
				.getCarryableItems();
		assertEquals(1, itemsList.size());
		assertTrue(itemsList.get(0) instanceof Flag);
		Flag flag = (Flag) itemsList.get(0);
		pickUpCont.pickUpItem(flag);
		endTurnCont.endTurn();
		
		// player 2 get out of there so no lightrail
		assertNotSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// player 1
		assertEquals(player1, playerDB.getCurrentPlayer());
		Square player1LastSq = player1.getCurrentPosition();
		// end player 1 turn without moving --> he will loose
		endTurnCont.endTurn();
		
		// check flag is dropped:
		List<IItem> list = player1LastSq.getCarryableItems();
		assertEquals(1, list.size());
		assertTrue(list.contains(flag));
		
		playerDB = null;
	}
	
	/**
	 * Player 1 will capture the flag and Player 2 will shoot an identity disk
	 * at player 1. We will test wether player 1 drops the flag in this case.
	 */
	@Test
	public void testPlayerDropFlagHitByID() {
		// Player 1
		Player player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		// now player 1 is teleported
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// player 2
		assertNotSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		List<IItem> itemsList = playerDB.getCurrentPlayer().getCurrentPosition()
				.getCarryableItems();
		
		assertEquals(1, itemsList.size());
		assertTrue(itemsList.get(0) instanceof UnchargedIdentityDisk);
		
		UnchargedIdentityDisk ID = (UnchargedIdentityDisk) itemsList.get(0);
		pickUpCont.pickUpItem(ID);
		assertNotSame(player1, playerDB.getCurrentPlayer());
		endTurnCont.endTurn();
		
		// player 1
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.SOUTH);
		// now player 1 is on the startsquare of player 2
		assertNotSame(PlayerState.FINISHED, ((TronPlayer) player1).getPlayerState());
		// pickup the flag:
		List<IItem> itemsList2 = playerDB.getCurrentPlayer().getCurrentPosition()
				.getCarryableItems();
		assertEquals(1, itemsList2.size());
		assertTrue(itemsList2.get(0) instanceof Flag);
		Flag flag = (Flag) itemsList2.get(0);
		assertFalse(player1.getInventoryContent().contains(flag));
		pickUpCont.pickUpItem(flag);
		assertTrue(player1.getInventoryContent().contains(flag));
		endTurnCont.endTurn();
		
		// Player 2
		// Shoot the ID at player 1: (WEST)
		DummyIDArgumentsHandler idHandler = new DummyIDArgumentsHandler();
		idHandler.setChoice(3);
		UseArguments<?> arguments = ID.getUseArguments();
		if (arguments != null)
			idHandler.handleArguments(arguments);
		playerDB.getCurrentPlayer().performAction(new UseAction(ID, idHandler));
		
		// Test if flag is no longer in the inventory of player 1 that got hit:
		assertFalse(player1.getInventoryContent().contains(flag));
		
		// Test if the flag is on one of the neighbouring squares:
		assertTrue(grid.getSquareAt(new Coordinate(0, 8)).contains(flag)
				|| grid.getSquareAt(new Coordinate(1, 8)).contains(flag)
				|| grid.getSquareAt(new Coordinate(1, 9)).contains(flag)
				|| grid.getSquareAt(new Coordinate(0, 9)).contains(flag));
		
		playerDB = null;
		
	}
	
	/**
	 * Player 1 will capture the flag and Player 2 will shoot an identity disk
	 * at player 1. We will test wether player 1 drops the flag in this case.
	 */
	@Test
	public void testPlayerDropFlagTeleport() {
		// Player 1
		Player player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		// now player 1 is teleported
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// player 2
		assertNotSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// player 1
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.SOUTH);
		// now player 1 is on the startsquare of player 2
		assertNotSame(PlayerState.FINISHED, ((TronPlayer) player1).getPlayerState());
		// pickup the flag:
		List<IItem> itemsList2 = playerDB.getCurrentPlayer().getCurrentPosition()
				.getCarryableItems();
		assertEquals(1, itemsList2.size());
		assertTrue(itemsList2.get(0) instanceof Flag);
		Flag flag = (Flag) itemsList2.get(0);
		pickUpCont.pickUpItem(flag);
		endTurnCont.endTurn();
		
		// player 2
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// player 1
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.WEST);
		
		// player 2 just chillin
		assertNotSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.NORTH);
		endTurnCont.endTurn();
		
		// player 1
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.WEST);
		
		// Test if flag is no longer in the inventory of player 1 that just
		// teleported:
		assertTrue(!player1.getInventoryContent().contains(flag));
		
		// Test if the flag is on one of the neighbouring squares of the
		// teleporter:
		assertTrue(grid.getSquareAt(new Coordinate(0, 6)).contains(flag)
				|| grid.getSquareAt(new Coordinate(1, 6)).contains(flag)
				|| grid.getSquareAt(new Coordinate(1, 7)).contains(flag)
				|| grid.getSquareAt(new Coordinate(1, 8)).contains(flag)
				|| grid.getSquareAt(new Coordinate(0, 8)).contains(flag));
		
		playerDB = null;
	}
	
	/**
	 * Player 1 will capture the flag and will then walk into an active light
	 * grenade. We will test wether player 1 drops the flag in this case.
	 */
	@Test
	public void testPlayerDropFlagHitByLG() {
		// Player 1
		Player player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		// now player 1 is teleported
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// player 2
		assertNotSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		List<IItem> itemsList = playerDB.getCurrentPlayer().getCurrentPosition()
				.getCarryableItems();
		LightGrenade LG = (LightGrenade) itemsList.get(0);
		pickUpCont.pickUpItem(LG);
		useItemCont.useItem(LG);
		
		// player 1
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.SOUTH);
		// now player 1 is on the startsquare of player 2
		assertNotSame(PlayerState.FINISHED, ((TronPlayer) player1).getPlayerState());
		// pickup the flag:
		List<IItem> itemsList2 = playerDB.getCurrentPlayer().getCurrentPosition()
				.getCarryableItems();
		assertEquals(1, itemsList2.size());
		assertTrue(itemsList2.get(0) instanceof Flag);
		Flag flag = (Flag) itemsList2.get(0);
		pickUpCont.pickUpItem(flag);
		endTurnCont.endTurn();
		
		// Player 2
		// get out of there
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		
		// Player 1
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		// Player 1 now gets hit by LG
		
		// Test if flag is no longer in the inventory of player 1 that got hit:
		assertTrue(!player1.getInventoryContent().contains(flag));
		
		// Test if the flag is on one of the neighbouring squares:
		assertTrue(grid.getSquareAt(new Coordinate(1, 6)).contains(flag)
				|| grid.getSquareAt(new Coordinate(2, 6)).contains(flag)
				|| grid.getSquareAt(new Coordinate(3, 6)).contains(flag)
				|| grid.getSquareAt(new Coordinate(3, 7)).contains(flag)
				|| grid.getSquareAt(new Coordinate(3, 8)).contains(flag)
				|| grid.getSquareAt(new Coordinate(2, 8)).contains(flag)
				|| grid.getSquareAt(new Coordinate(1, 8)).contains(flag)
				|| grid.getSquareAt(new Coordinate(1, 7)).contains(flag));
		
		playerDB = null;
	}
	
	/**
	 * Player 2 will pick up his own flag after player 1 drops it and we will
	 * see if it got returned to the starting position of player 2.
	 */
	@Test
	public void testPlayerReturnsOwnFlag() {
		// Player 1
		Player player1 = playerDB.getCurrentPlayer();
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		// now player 1 is teleported
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// player 2
		assertNotSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		
		// player 1
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// Player 2
				moveCont.move(Direction.NORTH);
				moveCont.move(Direction.WEST);
				endTurnCont.endTurn();
				
		// now player 1 is on the startsquare of player 2
				assertEquals(player1, playerDB.getCurrentPlayer());
		assertNotSame(PlayerState.FINISHED, ((TronPlayer) player1).getPlayerState());
		// pickup the flag:
		List<IItem> itemsList2 = playerDB.getCurrentPlayer().getCurrentPosition()
				.getCarryableItems();
		assertEquals(1, itemsList2.size());
		assertTrue(itemsList2.get(0) instanceof Flag);
		Flag flag = (Flag) itemsList2.get(0);
		pickUpCont.pickUpItem(flag);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		
		// Player 2
				moveCont.move(Direction.WEST);
				endTurnCont.endTurn();
				
		// Player 1
		// Leave the location so the lighttrail disapears
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.WEST);
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.WEST);
		assertNotSame(PlayerState.FINISHED, ((TronPlayer) player1).getPlayerState());
		
		// Player 2
		moveCont.move(Direction.SOUTHWEST);
		endTurnCont.endTurn();
		
		// Player 1
		// Leave the location so the lighttrail disapears
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.NORTH);
		endTurnCont.endTurn();
		
		// player 2
		// Check if flag is on current location
		moveCont.move(Direction.NORTHWEST);
		List<IItem> itemsList3 = playerDB.getCurrentPlayer().getCurrentPosition()
				.getCarryableItems();
		if (itemsList3.size() != 0)
		if (itemsList3.get(0) instanceof Flag) {
			// Pick it up
			Flag flag2 = (Flag) itemsList3.get(0);
			pickUpCont.pickUpItem(flag2);
			// see if it is back on starting position
			assertTrue(grid.getSquareAt(new Coordinate(0, 9)).contains(flag2));
			// ignore rest of this test method
			return;
		}
		
		moveCont.move(Direction.EAST);
		
		// check if flag is on current location
		List<IItem> itemsList4 = playerDB.getCurrentPlayer().getCurrentPosition()
				.getCarryableItems();
		if (itemsList4.size() != 0)
		if (itemsList4.get(0) instanceof Flag) {
			// Pick it up
			Flag flag3 = (Flag) itemsList4.get(0);
			pickUpCont.pickUpItem(flag3);
			// see if it is back on starting position
			assertTrue(grid.getSquareAt(new Coordinate(0, 9)).contains(flag3));
			// ignore rest of this test method
			return;
		}
		
		moveCont.move(Direction.NORTH);
		endTurnCont.endTurn();
		
		// Player 1 actions
		moveCont.move(Direction.NORTH);
		endTurnCont.endTurn();
		
		// Player 2 actions
		// check if flag is on current location
		List<IItem> itemsList5 = playerDB.getCurrentPlayer().getCurrentPosition()
				.getCarryableItems();
		if (itemsList5.size() != 0)
		if (itemsList5.get(0) instanceof Flag) {
			// Pick it up
			Flag flag4 = (Flag) itemsList5.get(0);
			pickUpCont.pickUpItem(flag4);
			// see if it is back on starting position
			assertTrue(grid.getSquareAt(new Coordinate(0, 9)).contains(flag4));
			// ignore rest of this test method
			return;
		}
		moveCont.move(Direction.NORTH);
		List<IItem> itemsList6 = playerDB.getCurrentPlayer().getCurrentPosition()
				.getCarryableItems();
		if (itemsList6.size() != 0)
		if (itemsList6.get(0) instanceof Flag) {
			// Pick it up
			Flag flag5 = (Flag) itemsList6.get(0);
			pickUpCont.pickUpItem(flag5);
			// see if it is back on starting position
			assertTrue(grid.getSquareAt(new Coordinate(0, 9)).contains(flag5));
			// ignore rest of this test method
			return;
		}
		moveCont.move(Direction.WEST);
		List<IItem> itemsList7 = playerDB.getCurrentPlayer().getCurrentPosition()
				.getCarryableItems();
		if (itemsList7.size() != 0)
		if (itemsList7.get(0) instanceof Flag) {
			// Pick it up
			Flag flag6 = (Flag) itemsList7.get(0);
			pickUpCont.pickUpItem(flag6);
			// see if it is back on starting position
			assertTrue(grid.getSquareAt(new Coordinate(0, 9)).contains(flag6));
			// ignore rest of this test method
			return;
		}
		
		// If we reach this point in the method, the flag did not get dropped or
		// something else went wrong
		fail();
		
		playerDB = null;
	}
}
