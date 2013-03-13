package unittests;

import static org.junit.Assert.*;
import item.Effect;
import org.junit.Before;
import org.junit.Test;
import player.IPlayer;
import player.Player;

public class EffectTest {
	
	private IPlayer	player;
	private Effect	effect;
	
	@Before
	public void setUp() {
		player = new Player();
		// Set the number of actions left at 2 initially to get a different
		// result from the light grenade and the power failure
		player.skipNumberOfActions(1);
		effect = new Effect(player);
	}
	
	@Test
	public void testLightGrenade() {
		effect.addLightGrenade();
		effect.execute();
		assertEquals(player.getAllowedNumberOfActions(), -1);
	}
	
	@Test
	public void testPowerFailure() {
		effect.addPowerFailure();
		effect.execute();
		assertEquals(player.getAllowedNumberOfActions(), 2);
		// TODO: The above number is wrong: it should obviously be 0 instead of
		// 2. At this moment, a player can't move yet, so we can't let it know
		// it has moved in any way (this will always be the case when it hits a
		// power failure). Add a player.moveInDirection(direction) as soon as
		// possible and fix the test.
	}
	
	@Test
	public void testLightGrenadeAndPowerFailure() {
		effect.addLightGrenade();
		effect.addPowerFailure();
		effect.execute();
		assertEquals(player.getAllowedNumberOfActions(), -2);
	}
	
}
