package square;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


@SuppressWarnings("javadoc")
public class DirectionTest {
	
	
	@Test
	public final void testGetPrimeryDirections() {
		// check for north
		assertEquals(1, Direction.NORTH.getPrimeryDirections().size());
		assertTrue(Direction.NORTH.getPrimeryDirections().contains(Direction.NORTH));
		
		// check for south east 
		assertEquals(2, Direction.SOUTHEAST.getPrimeryDirections().size());
		assertTrue(Direction.SOUTHEAST.getPrimeryDirections().contains(Direction.EAST));
		assertTrue(Direction.SOUTHEAST.getPrimeryDirections().contains(Direction.SOUTH));
	}
	
	@Test
	public final void testGetOppositeDirection() {
		// check for north 
		assertEquals(Direction.SOUTH, Direction.NORTH.getOppositeDirection());
		// check for south east
		assertEquals(Direction.NORTHWEST, Direction.SOUTHEAST.getOppositeDirection());
	}
	
}
