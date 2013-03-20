package grid;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class CoordinateTest {
	
	@Before
	public void setUp() throws Exception {}
	
	@Test
	public void testConstructor() {
		assertEquals(0, Coordinate.ORIGIN.getX());
		assertEquals(0, Coordinate.ORIGIN.getY());
		Coordinate coord = new Coordinate(5, -5);
		assertEquals(5, coord.getX());
		assertEquals(-5, coord.getY());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testGetCoordinateInDirection_nullArgument() {
		Coordinate.ORIGIN.getCoordinateInDirection(null);
	}
	
	@Test
	public void testGetCoordinateInDirection() {
		assertEquals(new Coordinate(0, -1), Coordinate.ORIGIN.getCoordinateInDirection(Direction.NORTH));
		assertEquals(new Coordinate(1, -1), Coordinate.ORIGIN.getCoordinateInDirection(Direction.NORTHEAST));
		assertEquals(new Coordinate(1,  0), Coordinate.ORIGIN.getCoordinateInDirection(Direction.EAST));
		assertEquals(new Coordinate(1,  1), Coordinate.ORIGIN.getCoordinateInDirection(Direction.SOUTHEAST));
		assertEquals(new Coordinate(0,  1), Coordinate.ORIGIN.getCoordinateInDirection(Direction.SOUTH));
		assertEquals(new Coordinate(-1, 1), Coordinate.ORIGIN.getCoordinateInDirection(Direction.SOUTHWEST));
		assertEquals(new Coordinate(-1, 0), Coordinate.ORIGIN.getCoordinateInDirection(Direction.WEST));
		assertEquals(new Coordinate(-1,-1), Coordinate.ORIGIN.getCoordinateInDirection(Direction.NORTHWEST));
	}
	
	@Test
	public void testisNeighbour() {
		assertTrue(new Coordinate(2, 3).isNeighbour(new Coordinate(2, 4)));
		assertTrue(new Coordinate(2, 3).isNeighbour(new Coordinate(1, 2)));
		assertFalse(new Coordinate(2, 3).isNeighbour(new Coordinate(3, 1)));
		assertFalse(new Coordinate(2, 3).isNeighbour(new Coordinate(2, 5)));
	}
}
