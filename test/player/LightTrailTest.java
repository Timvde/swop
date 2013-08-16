package player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import square.Direction;
import square.NormalSquare;
import square.Property;
import square.SquareContainer;

@SuppressWarnings("javadoc")
public class LightTrailTest {
	
	private LightTrail		lightTrail;
	private SquareContainer	sq1;
	private SquareContainer	sq2;
	private SquareContainer	sq3;
	private SquareContainer	sq4;
	
	@Before
	public void setUp() {
		sq1 = new SquareContainer(Collections.<Direction, SquareContainer> emptyMap(),
				new NormalSquare());
		sq2 = new SquareContainer(Collections.<Direction, SquareContainer> emptyMap(),
				new NormalSquare());
		sq3 = new SquareContainer(Collections.<Direction, SquareContainer> emptyMap(),
				new NormalSquare());
		sq4 = new SquareContainer(Collections.<Direction, SquareContainer> emptyMap(),
				new NormalSquare());
		
		lightTrail = new LightTrail(sq1);
	}
	
	private boolean hasLightTrail(SquareContainer square) {
		for (Property property : square.getProperties())
			if (property instanceof LightTrail)
				return true;
		return false;
	}
	
	@Test
	public void testUpdateLightTrail_validCoordinate() {
		// add a second coordinate to the trail
		lightTrail.updateLightTrail(sq2);
		assertEquals(2, lightTrail.getLightTrailSquares().size());
		assertTrue(lightTrail.getLightTrailSquares().contains(sq1));
		assertTrue(hasLightTrail(sq1));
		assertTrue(lightTrail.getLightTrailSquares().contains(sq2));
		
		// add a third coordinate to the trail
		lightTrail.updateLightTrail(sq3);
		assertEquals(3, lightTrail.getLightTrailSquares().size());
		assertTrue(lightTrail.getLightTrailSquares().contains(sq1));
		assertTrue(hasLightTrail(sq1));
		assertTrue(lightTrail.getLightTrailSquares().contains(sq2));
		assertTrue(hasLightTrail(sq2));
		assertTrue(lightTrail.getLightTrailSquares().contains(sq3));
		
		// add a fourth coordinate to the trail
		lightTrail.updateLightTrail(sq4);
		assertEquals(4, lightTrail.getLightTrailSquares().size());
		assertTrue(lightTrail.getLightTrailSquares().contains(sq1));
		assertTrue(hasLightTrail(sq1));
		assertTrue(lightTrail.getLightTrailSquares().contains(sq2));
		assertTrue(hasLightTrail(sq2));
		assertTrue(lightTrail.getLightTrailSquares().contains(sq3));
		assertTrue(hasLightTrail(sq3));
		assertTrue(lightTrail.getLightTrailSquares().contains(sq4));
		
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
		lightTrail.updateLightTrail(new SquareContainer(Collections
				.<Direction, SquareContainer> emptyMap(), new NormalSquare()));
		lightTrail.updateLightTrail(new SquareContainer(Collections
				.<Direction, SquareContainer> emptyMap(), new NormalSquare()));
		lightTrail.updateLightTrail(new SquareContainer(Collections
				.<Direction, SquareContainer> emptyMap(), new NormalSquare()));
		
		assertEquals(4, lightTrail.size());
		lightTrail.updateLightTrail();
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
