package unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import grid.Square;
import item.LightGrenade;
import item.LightGrenade.LightGrenadeState;
import org.junit.Before;
import org.junit.Test;
import grid.Coordinate;
import grid.Wall;


@SuppressWarnings("javadoc")
public class LightGrenadeTest {
	
	LightGrenade lightGrenade; 
	
	@Before
	public void setUp() {
		lightGrenade = new LightGrenade();
	}
	
	@Test
	public void testConstructor() {
		LightGrenade lightGrenade = new LightGrenade();
		assertEquals(LightGrenadeState.INACTIVE, lightGrenade.getState());
	}
	
	@Test
	public void testStateTransision() {
		assertEquals(LightGrenadeState.INACTIVE, lightGrenade.getState());
		lightGrenade.enable();
		assertEquals(LightGrenadeState.ACTIVE, lightGrenade.getState());
		lightGrenade.explode();
		assertEquals(LightGrenadeState.EXPLODED, lightGrenade.getState());
	}
	
	@Test (expected = IllegalStateException.class)
	public void testEnable_IllegalState() {
		lightGrenade.enable();
		lightGrenade.explode();
		lightGrenade.enable();
	}
	
	@Test (expected = IllegalStateException.class)
	public void testExplode_IllegalState() {
		lightGrenade.explode();
	}
	
	@Test
	public void isCarriable() {
		assertTrue(lightGrenade.isCarriable());
		lightGrenade.enable();
		assertFalse(lightGrenade.isCarriable());
		lightGrenade.explode();
		assertFalse(lightGrenade.isCarriable());
	}
	
	@Test
	public void testUse() {
		Square sq = new Square();
		
		lightGrenade.use(sq);
		
		assertEquals(LightGrenadeState.ACTIVE, lightGrenade.getState());
		sq.hasItemWithID(lightGrenade.getId());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testUse_SqureIsAWall() {
		Wall wall = new Wall(new Coordinate(0, 0), new Coordinate(4, 0));
		lightGrenade.use(wall.getWallPart());
	}
}
