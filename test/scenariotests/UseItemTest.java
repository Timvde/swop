package scenariotests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import game.CTFMode;
import grid.builder.DeterministicGridBuilderDirector;
import gui.GUI;
import item.DummyEffectFactory;
import item.IItem;
import item.lightgrenade.LightGrenade;
import item.lightgrenade.LightGrenadeState;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import square.AbstractSquare;
import square.Direction;
import ObjectronExceptions.IllegalUseException;

/**
 * Tests the "Use An Item From The Inventory" use case.
 * 
 * Tests: - Place maximum one lightgrenade on a square - Cannot place light
 * grenade on start positions
 * 
 * @author Tom
 */
@SuppressWarnings("javadoc")
public class UseItemTest extends SetUpTestGrid {

	@Before
	public void setUp() {
		super.setUp(new CTFMode(DeterministicGridBuilderDirector.NUMBER_OF_PLAYERS_ON_TEST_GRID));
	}
	
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
		LightGrenade LG = (LightGrenade) playerDB.getCurrentPlayer().getCurrentPosition()
				.getCarryableItems().get(0);
		pickUpCont.pickUpItem(LG);
		useItemCont.useItem(LG);
		
		/*
		 * light grenade is not carriable, and therefore I can't replace this
		 * with getCarriableItems(), like the rest
		 */
		assertTrue(((AbstractSquare) playerDB.getCurrentPlayer().getCurrentPosition()).contains(LG));
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
		List<IItem> items1 = playerDB.getCurrentPlayer().getCurrentPosition().getCarryableItems();
		IItem lightGrenade1 = items1.get(0);
		pickUpCont.pickUpItem(lightGrenade1);
		moveCont.move(Direction.EAST);
		List<IItem> items2 = playerDB.getCurrentPlayer().getCurrentPosition().getCarryableItems();
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
	
	@Test(expected = IllegalArgumentException.class)
	public void testArgumentNull() {
		useItemCont.useItem(null);
	}
	
	@Test(expected = IllegalUseException.class)
	public void testUse_NotInInventory() {
		useItemCont.useItem(new LightGrenade(new DummyEffectFactory()));
	}
	
	/**
	 * set a DummyGUI, so we have control over the returned direction by
	 * DummyGUI#getBasicDirection()
	 */
	@SuppressWarnings("unused")
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
