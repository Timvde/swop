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
		Map<Direction, SquareContainer> neighbours = new HashMap<Direction, SquareContainer>();
		// create a new square
		SquareContainer square = new SquareContainer(neighbours, new NormalSquare());
		// check if there are any neighbours set
		for (Direction direction : Direction.values())
			assertNull(square.getNeighbourIn(direction));
		
		// create a new neighbour
		neighbours.put(Direction.NORTH, square);
		SquareContainer neighbour = new SquareContainer(neighbours, new NormalSquare());
		
		// check if the neighbour is set correctly with both squares
		assertEquals(square, neighbour.getNeighbourIn(Direction.NORTH));
		assertEquals(neighbour, square.getNeighbourIn(Direction.SOUTH));
	}
	
}
