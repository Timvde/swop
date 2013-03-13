package unittests;

import static org.junit.Assert.*;
import grid.Coordinate;
import org.junit.Before;
import org.junit.Test;
import player.LightTrail;

@SuppressWarnings("javadoc")
public class LightTrailTest {
	
	LightTrail	lightTrail;
	
	@Before
	public void setUp() {
		lightTrail = new LightTrail();
	}
	
	@Test
	public void testConstructor() {
		lightTrail = new LightTrail();
		assertEquals(0, lightTrail.getLightTrailCoordinates().size());
	}
	
	@Test
	public void testUpdateLightTrail_validCoordinate() {
		// add a first coordinate to the trail
		lightTrail.updateLightTrail(new Coordinate(0, 0));
		assertEquals(1, lightTrail.getLightTrailCoordinates().size());
		assertTrue(lightTrail.getLightTrailCoordinates().contains(new Coordinate(0, 0)));
		
		// add a second coordinate to the trail
		lightTrail.updateLightTrail(new Coordinate(1, 0));
		assertEquals(2, lightTrail.getLightTrailCoordinates().size());
		assertTrue(lightTrail.getLightTrailCoordinates().contains(new Coordinate(0, 0)));
		assertTrue(lightTrail.getLightTrailCoordinates().contains(new Coordinate(1, 0)));
		
		// add a third coordinate to the trail
		lightTrail.updateLightTrail(new Coordinate(2, 0));
		assertEquals(3, lightTrail.getLightTrailCoordinates().size());
		assertTrue(lightTrail.getLightTrailCoordinates().contains(new Coordinate(0, 0)));
		assertTrue(lightTrail.getLightTrailCoordinates().contains(new Coordinate(1, 0)));
		assertTrue(lightTrail.getLightTrailCoordinates().contains(new Coordinate(2, 0)));
		
		// add a fourth coordinate to the trail
		// trail size should still be three
		lightTrail.updateLightTrail(new Coordinate(3, 0));
		assertEquals(3, lightTrail.getLightTrailCoordinates().size());
		assertFalse(lightTrail.getLightTrailCoordinates().contains(new Coordinate(0, 0)));
		assertTrue(lightTrail.getLightTrailCoordinates().contains(new Coordinate(1, 0)));
		assertTrue(lightTrail.getLightTrailCoordinates().contains(new Coordinate(2, 0)));
		assertTrue(lightTrail.getLightTrailCoordinates().contains(new Coordinate(3, 0)));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateLightTrail_CoordinateIsNotANeighbor() {
		lightTrail.updateLightTrail(new Coordinate(0, 0));
		lightTrail.updateLightTrail(new Coordinate(1, 1));
		// add an invalid lightTrail
		lightTrail.updateLightTrail(new Coordinate(5, 5));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateLightTrail_CoordinateAlreadyInLightTrail() {
		lightTrail.updateLightTrail(new Coordinate(0, 0));
		lightTrail.updateLightTrail(new Coordinate(1, 1));
		// add an invalid lightTrail
		lightTrail.updateLightTrail(new Coordinate(0, 0));
	}
	
	@Test
	public void testUpdateLightTrail() {
		lightTrail.updateLightTrail(new Coordinate(0, 0));
		lightTrail.updateLightTrail(new Coordinate(1, 0));
		lightTrail.updateLightTrail(new Coordinate(2, 0));
		
		assertEquals(3, lightTrail.size());
		lightTrail.updateLightTrail();
		assertEquals(2, lightTrail.size());
		lightTrail.updateLightTrail();
		assertEquals(1, lightTrail.size());
		lightTrail.updateLightTrail();
		assertEquals(0, lightTrail.size());
		lightTrail.updateLightTrail();
		assertEquals(0, lightTrail.size());
	}
}
