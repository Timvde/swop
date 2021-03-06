package square;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import item.DummyEffectFactory;
import item.lightgrenade.LightGrenade;
import org.junit.Before;
import org.junit.Test;
import player.DummyPlayer;

@SuppressWarnings("javadoc")
public class WallPartTest {
	
	private WallPart	wall;
	
	@Before
	public void setUp() throws Exception {
		wall = new WallPart();
	}
	
	@Test
	public void testGetCarryableItems() {
		assertEquals(0, wall.getCarryableItems().size());
		assertTrue(wall.getCarryableItems().isEmpty());
	}
	
	@Test
	public void testGetCarryableItems_encapsulation() {
		LightGrenade lightGrenade = new LightGrenade(new DummyEffectFactory());
		
		// try and add a light grenade to the wall
		// Illegally i must say but who cares about that, lets just say i'm an
		// ethical hacker or something like that. You have to make sure no one
		// can break the system, right?
		//
		// I'm glad we're on the same page then...
		wall.getCarryableItems().add(lightGrenade);
		
		// test if success
		assertFalse(wall.contains(lightGrenade));
		assertFalse(wall.getCarryableItems().contains(lightGrenade));
	}
	
	@Test
	public void testPlayerStuff() {
		assertNull(wall.getPlayer());
		assertFalse(wall.hasPlayer());
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void addPlayer() {
		wall.addPlayer(new DummyPlayer());
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testPickUpItem() {
		wall.pickupItem(1);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testAddItem() {
		wall.addItem(null);
	}
	
	@Test
	public void testRemove() {
		wall.remove(null);
		// doesn't do much anyway, and i dont think this will be a way to
		// violate any invariants of the wall. Thus i don't check anything for
		// the moment. Maybe i come back later with a stroke of genius ...
	}
	
	@Test
	public void testGetAllItems() {
		assertTrue(wall.getAllItems().isEmpty());
	}
}
