package square;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class ASquareTest {
	
	@Before
	public void setUp() throws Exception {
		
	}
	
	@Test
	public void testConstructor() {
		// make a new empty map, since there is no square
		Map<Direction, ASquare> neighbours = new HashMap<Direction, ASquare>();
		// create a new square
		Square square = new Square(neighbours);
		// check if there are any neighbours set
		for (Direction direction : Direction.values())
			assertNull(square.getNeighbour(direction));
		
		// create a new neighbour
		neighbours.put(Direction.NORTH, square);
		Square neighbour = new Square(neighbours);
		
		// check if the neighbour is set correctly with both squares
		assertEquals(square, neighbour.getNeighbour(Direction.NORTH));
		assertEquals(neighbour, square.getNeighbour(Direction.SOUTH));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructor_nullArgument() {
		new Square(null);
	}
	
}