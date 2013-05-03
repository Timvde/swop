package player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import square.AbstractSquare;
import square.Direction;
import square.NormalSquare;

@SuppressWarnings("javadoc")
public class LightTrailTest {
	
	private LightTrail	lightTrail;
	private NormalSquare		sq1;
	private NormalSquare		sq2;
	private NormalSquare		sq3;
	private NormalSquare		sq4;
	
	@Before
	public void setUp() {
		// create a new empty neighbour map for the squares
		Map<Direction, AbstractSquare> neighbours = new HashMap<Direction, AbstractSquare>();
		
		lightTrail = new LightTrail();
		
		sq1 = new NormalSquare(neighbours);
		sq2 = new NormalSquare(neighbours);
		sq3 = new NormalSquare(neighbours);
		sq4 = new NormalSquare(neighbours);
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
		assertTrue(sq1.hasLightTrail());
		
		// add a second coordinate to the trail
		lightTrail.updateLightTrail(sq2);
		assertEquals(2, lightTrail.getLightTrailSquares().size());
		assertTrue(lightTrail.getLightTrailSquares().contains(sq1));
		assertTrue(sq1.hasLightTrail());
		assertTrue(lightTrail.getLightTrailSquares().contains(sq2));
		assertTrue(sq2.hasLightTrail());
		
		// add a third coordinate to the trail
		lightTrail.updateLightTrail(sq3);
		assertEquals(3, lightTrail.getLightTrailSquares().size());
		assertTrue(lightTrail.getLightTrailSquares().contains(sq1));
		assertTrue(sq1.hasLightTrail());
		assertTrue(lightTrail.getLightTrailSquares().contains(sq2));
		assertTrue(sq2.hasLightTrail());
		assertTrue(lightTrail.getLightTrailSquares().contains(sq3));
		assertTrue(sq3.hasLightTrail());
		
		// add a fourth coordinate to the trail
		// trail size should still be three
		lightTrail.updateLightTrail(sq4);
		assertEquals(3, lightTrail.getLightTrailSquares().size());
		assertFalse(lightTrail.getLightTrailSquares().contains(sq1));
		assertFalse(sq1.hasLightTrail());
		assertTrue(lightTrail.getLightTrailSquares().contains(sq2));
		assertTrue(sq2.hasLightTrail());
		assertTrue(lightTrail.getLightTrailSquares().contains(sq3));
		assertTrue(sq3.hasLightTrail());
		assertTrue(lightTrail.getLightTrailSquares().contains(sq4));
		assertTrue(sq4.hasLightTrail());
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateLightTrail_CoordinateAlreadyInLightTrail() {
		lightTrail.updateLightTrail(sq1);
		lightTrail.updateLightTrail(sq2);
		// add an invalid lightTrail
		lightTrail.updateLightTrail(sq2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateLightTrail_nullArgument() {
		assertFalse(lightTrail.isValidNewSquare(null));
		lightTrail.updateLightTrail(null);
	}
	
	@Test
	public void testUpdateLightTrail() {
		// create a new empty neighbour map for the squares
		Map<Direction, AbstractSquare> neighbours = new HashMap<Direction, AbstractSquare>();
		
		lightTrail.updateLightTrail(new NormalSquare(neighbours));
		lightTrail.updateLightTrail(new NormalSquare(neighbours));
		lightTrail.updateLightTrail(new NormalSquare(neighbours));
		
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
