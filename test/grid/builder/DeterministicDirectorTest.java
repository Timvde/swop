package grid.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import grid.Coordinate;
import grid.Grid;
import org.junit.Before;
import org.junit.Test;
import square.PlayerStartingPosition;

@SuppressWarnings("javadoc")
public class DeterministicDirectorTest {
	
	TronGridBuilder	builder;
	
	@Before
	public void setUp() {
		builder = new TronGridBuilder();
	}
	
	@Test
	public void testDeterministicNoPowerFailures() {
		GridBuilderDirector director = new DeterministicGridBuilderDirector(builder, false);
		director.construct();
		Grid grid = builder.getResult();
		
		assertEquals(grid.toString(), "s s s s s s s u s s \n" + "s s s s s s s s s s \n"
				+ "s s s s s s s l s s \n" + "s s s s s s s s s s \n" + "s s s s s s s s s s \n"
				+ "s s s s w w w w w s \n" + "s s s s s s s l s s \n" + "s s l s s s s s l s \n"
				+ "s s s s s l l l l s \n" + "s s u s s s s s s s \n");
	}
	
	@Test
	public void testDeterministicPowerFailures() {
		fail("not yet implemented");
	}
	
	@Test
	public void testDeterministicPlayerPositions() {
		GridBuilderDirector director = new DeterministicGridBuilderDirector(builder, false);
		director.construct();
		Grid grid = builder.getResult();
		
		assertTrue(grid.getSquareAt(new Coordinate(0, 0)) instanceof PlayerStartingPosition);
		assertTrue(grid.getSquareAt(new Coordinate(
				DeterministicGridBuilderDirector.PREDIFINED_GRID_SIZE - 1,
				DeterministicGridBuilderDirector.PREDIFINED_GRID_SIZE - 1)) instanceof PlayerStartingPosition);
	}
}
