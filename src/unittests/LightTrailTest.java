package unittests;

import static org.junit.Assert.*;
import grid.Coordinate;
import grid.Square;
import org.junit.Before;
import org.junit.Test;
import player.LightTrail;

@SuppressWarnings("javadoc")
public class LightTrailTest {
	
	LightTrail	lightTrail;
	private Square	sq1;
	private Square	sq2;
	private Square	sq3;
	private Square	sq4;
	
	@Before
	public void setUp() {
		lightTrail = new LightTrail();
		
		sq1 = new Square();
		sq2 = new Square();
		sq3 = new Square();
		sq4 = new Square();
	}
	
	@Test
	public void testConstructor() {
		lightTrail = new LightTrail();
		assertEquals(0, lightTrail.getLightTrailSquares().size());
	}
	
	@Test
	public void testUpdateLightTrail_validCoordinate() {
		// add a first coordinate to the trail
		lightTrail.updateLightTrail(sq1);
		assertEquals(1, lightTrail.getLightTrailSquares().size());
		assertTrue(lightTrail.getLightTrailSquares().contains(sq1));
		
		// add a second coordinate to the trail
		lightTrail.updateLightTrail(sq2);
		assertEquals(2, lightTrail.getLightTrailSquares().size());
		assertTrue(lightTrail.getLightTrailSquares().contains(sq1));
		assertTrue(lightTrail.getLightTrailSquares().contains(sq2));
		
		// add a third coordinate to the trail
		lightTrail.updateLightTrail(sq3);
		assertEquals(3, lightTrail.getLightTrailSquares().size());
		assertTrue(lightTrail.getLightTrailSquares().contains(sq1));
		assertTrue(lightTrail.getLightTrailSquares().contains(sq2));
		assertTrue(lightTrail.getLightTrailSquares().contains(sq3));
		
		// add a fourth coordinate to the trail
		// trail size should still be three
		lightTrail.updateLightTrail(sq4);
		assertEquals(3, lightTrail.getLightTrailSquares().size());
		assertFalse(lightTrail.getLightTrailSquares().contains(sq1));
		assertTrue(lightTrail.getLightTrailSquares().contains(sq2));
		assertTrue(lightTrail.getLightTrailSquares().contains(sq3));
		assertTrue(lightTrail.getLightTrailSquares().contains(sq4));
		
		
		// test if the squares where updated as well ...
		assertFalse(sq1.hasLightTrail());
		assertTrue(sq2.hasLightTrail());
		assertTrue(sq3.hasLightTrail());
		assertTrue(sq4.hasLightTrail());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateLightTrail_CoordinateAlreadyInLightTrail() {
		lightTrail.updateLightTrail(sq1);
		lightTrail.updateLightTrail(sq2);
		// add an invalid lightTrail
		lightTrail.updateLightTrail(sq2);
	}
	
	@Test
	public void testUpdateLightTrail() {
		lightTrail.updateLightTrail(new Square());
		lightTrail.updateLightTrail(new Square());
		lightTrail.updateLightTrail(new Square());
		
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
