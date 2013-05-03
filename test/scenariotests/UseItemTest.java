package scenariotests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import grid.Grid;
import grid.builder.DeterministicGridBuilderDirector;
import grid.builder.TronGridBuilder;
import item.IItem;
import item.identitydisk.IdentityDisk;
import item.lightgrenade.LightGrenade;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import player.PlayerDataBase;
import square.Direction;
import square.PlayerStartingPosition;
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
		TronGridBuilder builder = new TronGridBuilder();
		DeterministicGridBuilderDirector director = new DeterministicGridBuilderDirector(builder,
				false);
		director.construct();
		grid = builder.getResult();
		
		//make a set with the startingpostions in a deterministic order
		Set<PlayerStartingPosition> playerstartingpositions = new LinkedHashSet<PlayerStartingPosition>();
		playerstartingpositions.add((PlayerStartingPosition) grid
				.getSquareAt(DeterministicGridBuilderDirector.PLAYER1_START_POS));
		playerstartingpositions.add((PlayerStartingPosition) grid
				.getSquareAt(DeterministicGridBuilderDirector.PLAYER2_START_POS));
		
		playerDB = new PlayerDataBase();
		playerDB.createNewDB(playerstartingpositions);
		assertEquals(1, playerDB.getCurrentPlayer().getID());
		
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
		//useItemCont.setGUI(new DummyGUI());
		
		
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
