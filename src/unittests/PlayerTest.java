package unittests;

import static org.junit.Assert.assertEquals;
import grid.Coordinate;
import grid.GridBuilder;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import player.IPlayer;
import player.Player;

@SuppressWarnings("javadoc")
public class PlayerTest {
	
	private IPlayer		player;
	private Coordinate	randomCoord;
	
	@Before
	public void setUp() {
		randomCoord = GridBuilder.getRandomCoordOnTestGrid();
		player = new Player(randomCoord, new GridBuilder().getPredefinedTestGrid(false));
	}
	
	@Test
	public void testConstructor() {
		assertEquals(player.getCurrentLocation(), randomCoord);
		assertEquals(false, player.hasMovedYet());
		assertEquals(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN, player.getAllowedNumberOfActions());
		// chk empty inventory
		assertEquals(0, player.getInventoryContent().size());
		Assert.assertNotNull(player.getStartingPosition());
		//FIXME maken we PlayerDB verantw vor het bijhouden vd targetpos?
	}
	
	@Test
	public void testEndTurn() {
		boolean isIllegalStateExceptionThrown = false;
		try {
			player.endTurn();
		}
		catch (Exception e) {
			isIllegalStateExceptionThrown = true;
		}
		assertEquals(false, isIllegalStateExceptionThrown);
		assertEquals(true, areTurnRelatedFieldsReset());
		
		// skip a turn
		isIllegalStateExceptionThrown = false;
		player.skipNumberOfActions(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN);
		try {
			player.endTurn();
		}
		catch (Exception e) {
			isIllegalStateExceptionThrown = true;
		}
		assertEquals(false, isIllegalStateExceptionThrown);
		assertEquals(true, areTurnRelatedFieldsReset());
		
		// TODO player.hasMoved --> loses the game
	}
	
	private boolean areTurnRelatedFieldsReset() {
		return (player.hasMovedYet() == false)
				&& (player.getAllowedNumberOfActions() >= Player.MAX_NUMBER_OF_ACTIONS_PER_TURN);
	}
	
}
