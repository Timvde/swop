package item;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import item.lightgrenade.LightGrenade;
import item.lightgrenade.LightGrenade.LightGrenadeState;
import item.teleporter.Teleporter;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import player.DummyPlayer;
import square.ASquare;
import square.Direction;
import square.PowerFailure;
import square.Square;

@SuppressWarnings("javadoc")
public class EffectTest {
	
	private Effect		effect;
	private DummyPlayer	player;
	
	@Before
	public void setUp() {
		player = new DummyPlayer();
		effect = new Effect(player);
	}
	
	@Test
	public void testEffect() {
		LightGrenade lightGrenade = new LightGrenade();
		Effect effect = new Effect(lightGrenade);
		assertEquals(lightGrenade, effect.getObject());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testEffect_nullArgument() {
		// try and make a new effect with null as argument
		new Effect(null);
	}
	
	@Test
	public void testAddPowerFailure() {
		Square square = new Square(Collections.<Direction, ASquare> emptyMap());
		PowerFailure powerFailure = new PowerFailure(square);
		
		effect.addPowerFailure(powerFailure);
		effect.execute();
		assertTrue(player.isDamagedByPowerFailure());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddPowerFailure_nullArgument() {
		effect.addPowerFailure(null);
	}
	
	@Test
	public void testAddItem() {
		LightGrenade lightGrenade = new LightGrenade();
		Teleporter teleporter = new Teleporter(new Square(
				Collections.<Direction, ASquare> emptyMap()));
		
		effect.addItem(teleporter);
		effect.addItem(lightGrenade);
		
		assertEquals(2, effect.getItems().size());
		assertTrue(effect.getItems().contains(lightGrenade));
		assertTrue(effect.getItems().contains(teleporter));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddItem_nullArgument() {
		effect.addItem(null);
	}
	
	@Test
	public void testExecute_withALightGrenade() {
		// add an active light grenade to the effect
		LightGrenade lightGrenade = new LightGrenade();
		explodeLightGrenade(lightGrenade);
		effect.addItem(lightGrenade);
		
		// execute the effect
		effect.execute();
		
		// test if the light grenade has affect the player
		assertEquals(3, player.getNumberOfActionsSkipped());
	}
	
	@Test
	public void testExecute_withLightGrenadeAndPowerFailure() {
		// add an active light grenade to the effect
		LightGrenade lightGrenade = new LightGrenade();
		explodeLightGrenade(lightGrenade);
		effect.addItem(lightGrenade);
		
		// add a power failure to the effect
		Square square = new Square(Collections.<Direction, ASquare> emptyMap());
		PowerFailure powerFailure = new PowerFailure(square);
		effect.addPowerFailure(powerFailure);
		
		// execute the effect
		effect.execute();
		
		// test if the light grenade has affect the player
		assertEquals(4, player.getNumberOfActionsSkipped());
	}
	
	/**
	 * Will set the {@link LightGrenadeState} to EXPLODED.
	 */
	public void explodeLightGrenade(LightGrenade lightGrenade) {
		Square emptySquare = new Square(Collections.<Direction, ASquare> emptyMap());
		
		// Simulate adding the lightgrenade to the square and thus making it active.
		lightGrenade.use(emptySquare);
		
		// Simulate a Player stepping on the light grenade an thus exploding it.
		emptySquare.addPlayer(new DummyPlayer());
	}
}
