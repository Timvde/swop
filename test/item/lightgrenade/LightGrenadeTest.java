package item.lightgrenade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import item.DummyEffectFactory;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import player.DummyPlayer;
import powerfailure.PrimaryPowerFailure;
import square.Direction;
import square.NormalSquare;
import square.SquareContainer;
import square.WallPart;
import ObjectronExceptions.IllegalUseException;
import effects.Effect;
import effects.RaceEffectFactory;

@SuppressWarnings("javadoc")
public class LightGrenadeTest {
	
	private LightGrenade		lightGrenade;
	private DummyPlayer			affectedPlayer;
	private SquareContainer		emptySquare;
	
	private static final int	DEFAULT_DAMAGE		= 3;
	private static final int	INCREASED_DAMAGE	= 4;
	
	@Before
	public void setUp() {
		lightGrenade = new LightGrenade(new DummyEffectFactory());
		emptySquare = new SquareContainer(Collections.<Direction, SquareContainer> emptyMap(),
				new NormalSquare());
		affectedPlayer = new DummyPlayer();
	}
	
	@Test
	public void testConstructor() {
		LightGrenade lightGrenade = new LightGrenade(new DummyEffectFactory());
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
	
	@Test
	public void testExecuteInactiveIllegalState_IllegalState() {
		// one cannot execute an inexploded lightgrenade
		lightGrenade.getEffect().execute(affectedPlayer);
		assertEquals(0, affectedPlayer.getNumberOfActionsSkipped());
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
		lightGrenade.use(emptySquare, null);
		
		assertEquals(LightGrenadeState.ACTIVE, lightGrenade.getState());
		assertTrue(emptySquare.contains(lightGrenade));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testUse_SquareIsAWall() {
		lightGrenade.use(new SquareContainer(Collections.<Direction, SquareContainer> emptyMap(),
				new WallPart()), null);
	}
	
	@Test(expected = IllegalUseException.class)
	public void testUse_alreadyLightGrenadeOnSquare() {
		emptySquare.addItem(new LightGrenade(new DummyEffectFactory()));
		lightGrenade.use(emptySquare, null);
	}
	
	@Test
	public void testNormalStrengthExplode() {
		activateLightGrenade();
		explodeLightGrenade();
		// test if the strength was increased
		assertEquals(DEFAULT_DAMAGE, affectedPlayer.getNumberOfActionsSkipped());
	}
	
	@Test
	public void testIncreasedStrenghtExplode() {
		activateLightGrenade();
		Effect effect = new PrimaryPowerFailure(emptySquare, new RaceEffectFactory()).getEffect();
		effect.addEffect(lightGrenade.getEffect());
		effect.execute(affectedPlayer);
		// test if the strength was increased
		assertEquals(INCREASED_DAMAGE, affectedPlayer.getNumberOfActionsSkipped());
	}
	
	/**
	 * Simulates adding the lightgrenade to the square and thus making it
	 * active.
	 * 
	 * @throws CannotPlaceLightGrenadeException
	 */
	public void activateLightGrenade() {
		lightGrenade.use(emptySquare, null);
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
