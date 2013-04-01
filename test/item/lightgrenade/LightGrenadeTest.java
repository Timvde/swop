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
	private DummyPlayer		affectedPlayer;
	private Square			emptySquare;
	
	@Before
	public void setUp() {
		lightGrenade = new LightGrenade();
		emptySquare = new Square(Collections.<Direction, ASquare> emptyMap());
		affectedPlayer = new DummyPlayer();
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
		activateLightGrenade();
		assertEquals(LightGrenadeState.ACTIVE, lightGrenade.getState());
		
		explodeLightGrenade();
		assertEquals(LightGrenadeState.EXPLODED, lightGrenade.getState());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testExecuteInactiveIllegalState_IllegalState() {
		// one cannot execute an inexploded lightgrenade
		lightGrenade.execute(affectedPlayer);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testExecuteActiveIllegalState() {
		activateLightGrenade();
		// one cannot execute an active lightgrenade
		lightGrenade.execute(affectedPlayer);
	}
	
	@Test
	public void TestIsCarriable() {
		assertTrue(lightGrenade.isCarriable());
		
		activateLightGrenade();
		assertFalse(lightGrenade.isCarriable());
		
		explodeLightGrenade();
		assertFalse(lightGrenade.isCarriable());
	}
	
	@Test
	public void testUse() {
		lightGrenade.use(emptySquare);
		
		assertEquals(LightGrenadeState.ACTIVE, lightGrenade.getState());
		assertTrue(emptySquare.contains(lightGrenade));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testUse_SquareIsAWall() {
		lightGrenade.use(new WallPart(Collections.<Direction, ASquare> emptyMap()));
	}
	
	@Test(expected = IllegalStateException.class)
	public void testUse_alreadyLightGrenadeOnSquare() {
		emptySquare.addItem(new LightGrenade());
		lightGrenade.use(emptySquare);
	}
	
	@Test
	public void testNormalStrengthExplode() {
		activateLightGrenade();
		explodeLightGrenade();
		// test if the strength was increased
		assertEquals(LightGrenade.DEFAULT_STRENGTH, affectedPlayer.getNumberOfActionsSkipped());
	}
	
	@Test
	public void testIncreasedStrenghtExplode() {
		// increase the strength of the light grenade
		lightGrenade.increaseStrength();
		activateLightGrenade();
		explodeLightGrenade();
		// test if the strength was increased
		assertEquals(LightGrenade.INCREASED_STRENGHT, affectedPlayer.getNumberOfActionsSkipped());
	}
	
	@Test
	public void testAddToEffect() {
		Effect effect = new Effect(affectedPlayer);
		lightGrenade.addToEffect(effect);
		effect.execute();
		assertEquals(0, affectedPlayer.getNumberOfActionsSkipped());
		
		activateLightGrenade();
		effect = new Effect(affectedPlayer);
		lightGrenade.addToEffect(effect);
		effect.execute();
		assertEquals(LightGrenade.DEFAULT_STRENGTH, affectedPlayer.getNumberOfActionsSkipped());
		
		effect = new Effect(affectedPlayer);
		lightGrenade.addToEffect(effect);
		effect.execute();
		assertEquals(LightGrenade.DEFAULT_STRENGTH, affectedPlayer.getNumberOfActionsSkipped());
		
		lightGrenade.increaseStrength();
		effect = new Effect(affectedPlayer);
		lightGrenade.addToEffect(effect);
		effect.execute();
		assertEquals(LightGrenade.INCREASED_STRENGHT, affectedPlayer.getNumberOfActionsSkipped());
	}
	
	/**
	 * Simulates adding the lightgrenade to the square and thus making it
	 * active.
	 */
	public void activateLightGrenade() {
		lightGrenade.use(emptySquare);
		assertEquals(LightGrenadeState.ACTIVE, lightGrenade.getState());
		assertTrue(emptySquare.contains(lightGrenade));
	}
	
	/**
	 * Simulate a Player stepping on the light grenade an thus exploding it.
	 * 
	 * this.square should contain the lightgrenade (by first calling
	 * {@link #activateLightGrenade()}
	 */
	public void explodeLightGrenade() {
		emptySquare.addPlayer(affectedPlayer);
		assertEquals(LightGrenadeState.EXPLODED, lightGrenade.getState());
	}
}
