package scenariotests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import item.IItem;
import item.UseArguments;
import item.identitydisk.DummyIDArgumentsHandler;
import item.identitydisk.UnchargedIdentityDisk;
import java.util.List;
import game.CTFMode;
import grid.Coordinate;
import grid.builder.DeterministicGridBuilderDirector;
import org.junit.Before;
import org.junit.Test;
import player.LightTrail;
import player.Player;
import square.Direction;
import square.Property;
import square.SquareContainer;
import ObjectronExceptions.IllegalMoveException;

@SuppressWarnings("javadoc")
public class TeleportationTest extends SetUpTestGrid {
	
	@Before
	public void setUp() {
		super.setUp(new CTFMode(DeterministicGridBuilderDirector.NUMBER_OF_PLAYERS_ON_TEST_GRID));
	}
	
	private boolean hasLightTrail(SquareContainer square) {
		for (Property property : square.getProperties())
			if (property instanceof LightTrail)
				return true;
		return false;
	}
	
	@Test
	public void testTeleportation() {
		// move player 1 two squares down
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// check whether the destination square of the teleporter contains a
		// player
		assertTrue(null != grid.getSquareAt(new Coordinate(0, 7)).getPlayer());
		
		// end the turn of player 2 asap
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// move player 1 and check whether the light trail travels through the
		// teleporter
		moveCont.move(Direction.SOUTH);
		assertTrue(hasLightTrail(grid.getSquareAt(new Coordinate(0, 7))));
	}
	
	@Test
	public void testTeleportation_PlayerCoversDestination() {
		Player player1 = playerDB.getCurrentPlayer();
		// move player 1
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		assertNotSame(player1, playerDB.getCurrentPlayer());
		
		// move player 2
		
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTH);
		
		// move player 1
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.WEST);
		endTurnCont.endTurn();
		
		// player 2
		assertNotSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		
		// move player 1 into tele
		assertEquals(player1, playerDB.getCurrentPlayer());
				moveCont.move(Direction.SOUTHEAST);
				endTurnCont.endTurn();
		
		// try to move player 2 onto the teleporter
		
		boolean exceptionThrown = false;
		
		try {
			moveCont.move(Direction.NORTHEAST);
			moveCont.move(Direction.NORTHEAST);
		}
		catch (IllegalMoveException e) {
			exceptionThrown = true;
		}
		
		// check whether the teleportation did not happen ..
		assertTrue(exceptionThrown);
	}
	
	/**
	 * Test if an identity disk flies through a teleporter correctly.
	 */
	@Test
	public void testTeleportation_IDFliesThrough() {
		// move player 1
		Player player1 = playerDB.getCurrentPlayer();
		assertEquals(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		List<IItem> itemsList = playerDB.getCurrentPlayer().getCurrentPosition()
				.getCarryableItems();
		
		assertEquals(1, itemsList.size());
		assertTrue(itemsList.get(0) instanceof UnchargedIdentityDisk);
		
		UnchargedIdentityDisk ID = (UnchargedIdentityDisk) itemsList.get(0);
		pickUpCont.pickUpItem(ID);
		
		moveCont.move(Direction.SOUTHEAST);
		
		// Player 2 actions
		assertNotSame(player1, playerDB.getCurrentPlayer());
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTH);
		endTurnCont.endTurn();
		
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		
		// USE THE ID TO THE EAST
		DummyIDArgumentsHandler idHandler = new DummyIDArgumentsHandler();
		idHandler.setChoice(2);
		UseArguments<?> arguments = ID.getUseArguments();
		if (arguments != null)
			idHandler.handleArguments(arguments);
		ID.use(playerDB.getCurrentPlayer().getCurrentPosition(), arguments);
		
		// Check if the ID landed on the correct spot
		assertTrue(grid.getSquareAt(new Coordinate(1,7)).getCarryableItems().contains(ID));

	}
}
