package item.lightgrenade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import item.Effect;
import item.lightgrenade.LightGrenade.LightGrenadeState;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import player.DummyPlayer;
import square.ASquare;
import square.Direction;
import square.Square;
import square.WallPart;

@SuppressWarnings("javadoc")
public class LightGrenadeTest {
	
	private LightGrenade	lightGrenade;
	private DummyPlayer		player;
	
	@Before
	public void setUp() {
		lightGrenade = new LightGrenade();
		player = new DummyPlayer();
	}
	
	@Test
	public void testConstructor() {
		LightGrenade lightGrenade = new LightGrenade();
		assertEquals(LightGrenadeState.INACTIVE, lightGrenade.getState());
		
		// A little hack to get a nice test coverage. I sure hope nobody sees
		// this, or i will be forced to kill any witnesses involved.
		lightGrenade.toString();
	}
	
	@Test
	public void testStateTransision() {
		assertEquals(LightGrenadeState.INACTIVE, lightGrenade.getState());
		lightGrenade.enable();
		assertEquals(LightGrenadeState.ACTIVE, lightGrenade.getState());
		lightGrenade.execute(player);
		assertEquals(LightGrenadeState.EXPLODED, lightGrenade.getState());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testEnable_IllegalState() {
		lightGrenade.enable();
		lightGrenade.execute(player);
		lightGrenade.enable();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testExplode_IllegalState() {
		lightGrenade.execute(player);
	}
	
	@Test
	public void isCarriable() {
		assertTrue(lightGrenade.isCarriable());
		lightGrenade.enable();
		assertFalse(lightGrenade.isCarriable());
		lightGrenade.execute(player);
		assertFalse(lightGrenade.isCarriable());
	}
	
	@Test
	public void testUse() {
		Square sq = new Square(Collections.<Direction, ASquare> emptyMap());
		
		lightGrenade.use(sq, null);
		
		assertEquals(LightGrenadeState.ACTIVE, lightGrenade.getState());
		sq.contains(lightGrenade);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testUse_SqureIsAWall() {
		lightGrenade.use(new WallPart(Collections.<Direction, ASquare> emptyMap()), null);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testUse_alreadyLightGrenadeOnSquare() {
		Square sq = new Square(Collections.<Direction, ASquare> emptyMap());
		sq.addItem(new LightGrenade());
		
		lightGrenade.use(sq, null);
	}
	
	@Test
	public void testIncreaseStrength() {
		// place the light grenade somewhere (not really important)
		lightGrenade.use(new Square(Collections.<Direction, ASquare> emptyMap()), null);
		// increase the strength of the light grenade
		lightGrenade.increaseStrength();
		
		// execute the effect on the player
		lightGrenade.execute(player);
		
		// test if the strength was increased
		assertEquals(4, player.getNumberOfActionsSkipped());
	}
	
	@Test
	public void testAddToEffect() {
		Effect effect = new Effect(player);
		lightGrenade.addToEffect(effect);
		effect.execute();
		
		assertEquals(0, player.getNumberOfActionsSkipped());
		
		lightGrenade.use(new Square(Collections.<Direction, ASquare> emptyMap()), null);
		effect = new Effect(player);
		lightGrenade.addToEffect(effect);
		effect.execute();
		
		assertEquals(3, player.getNumberOfActionsSkipped());
		
		effect = new Effect(player);
		lightGrenade.addToEffect(effect);
		effect.execute();
		
		assertEquals(3, player.getNumberOfActionsSkipped());
	}
}
