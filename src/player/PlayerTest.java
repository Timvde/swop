package player;

import static org.junit.Assert.assertEquals;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
	private Player player;

	@Before
	public void setUp() {
		player = new Player();
	}

	@Test
	public void testConstructor() {
		player = new Player();
		assertEquals(false, player.hasMovedYet());
		assertEquals(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN,
				player.getAllowedNumberOfActions());
		//chk empty inventory
		assertEquals(0, player.getInventory().size());
		Assert.assertNotNull(player.getTargetPosition());
	}

	@Test
	public void testEndTurn() {
		boolean isIllegalStateExceptionThrown = false;
		try {
			player.endTurn();
		} catch (Exception e) {
			isIllegalStateExceptionThrown = true;
		}
		assertEquals(false, isIllegalStateExceptionThrown);
		assertEquals(true, areTurnRelatedFieldsReset());

		// test with no actions left
		isIllegalStateExceptionThrown = false;
		player.skipNumberOfActions(Player.MAX_NUMBER_OF_ACTIONS_PER_TURN);
		try {
			player.endTurn();
		} catch (Exception e) {
			isIllegalStateExceptionThrown = true;
		}
		assertEquals(true, isIllegalStateExceptionThrown);
		assertEquals(false, areTurnRelatedFieldsReset());

		// TODO player.hasMoved --> loses the game
	}

	private boolean areTurnRelatedFieldsReset() {
		return (player.hasMovedYet() == false)
				&& (player.getAllowedNumberOfActions() >= Player.MAX_NUMBER_OF_ACTIONS_PER_TURN);
	}

}
