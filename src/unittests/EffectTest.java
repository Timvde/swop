package unittests;

import static org.junit.Assert.assertEquals;
import grid.Direction;
import grid.GridBuilder;
import grid.PowerFailure;
import item.Effect;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import player.IPlayer;
import player.Player;

@SuppressWarnings("javadoc")
public class EffectTest {
	
	private IPlayer	player;
	private Effect	effect;
	
	@Before
	public void setUp() {
		Random randomgen = new Random();
		player = new Player(GridBuilder.getRandomCoordOnTestGrid(), new GridBuilder().getPredefinedTestGrid());
		// Set the number of actions left at 2 initially to get a different
		// result from the light grenade and the power failure
		player.skipNumberOfActions(1);
		effect = new Effect(player);
	}
	
	@Test
	public void testLightGrenade() {
		effect.addLightGrenade();
		effect.execute();
		// this will result in a negative number of actions left (i.e. the
		// penalty for his next turn) The player will see he has no actions left
		// and notify the database (to end his turn) and give himself again 3
		// actions for his next
		// turn.
		assertEquals(player.getAllowedNumberOfActions(), -1 + Player.MAX_NUMBER_OF_ACTIONS_PER_TURN);
	}
	
	@Test
	public void testPowerFailure() {
		// a player always has already done a move-action when it hits a
		// powerfailure. This is necessary to do a sucessfull endTurn
		player.moveInDirection(Direction.NORTH);
		
		effect.addPowerFailure(new PowerFailure(null));
		effect.execute();
		assertEquals(player.getAllowedNumberOfActions(), 0 + Player.MAX_NUMBER_OF_ACTIONS_PER_TURN);
		
		// TODO: The above number is wrong: it should obviously be 0 instead of
		// 2. At this moment, a player can't move yet, so we can't let it know
		// it has moved in any way (this will always be the case when it hits a
		// power failure). Add a player.moveInDirection(direction) as soon as
		// possible and fix the test.
	}
	
	@Test
	public void testLightGrenadeAndPowerFailure() {
		effect.addLightGrenade();
		effect.addPowerFailure(new PowerFailure(null));
		effect.execute();
		assertEquals(player.getAllowedNumberOfActions(), -2 + Player.MAX_NUMBER_OF_ACTIONS_PER_TURN);
	}
	
}
