package grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import grid.builder.DeterministicGridBuilderDirector;
import grid.builder.TronGridBuilder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import player.PlayerDataBase;
import square.ASquare;
import square.Direction;
import square.Square;

@SuppressWarnings("javadoc")
public class GridTest {
	
	private Grid			grid;
	private PlayerDataBase	playerDb;
	
	@Before
	public void setUp() throws Exception {
		playerDb = new PlayerDataBase();
		TronGridBuilder builder = new TronGridBuilder();
		new DeterministicGridBuilderDirector(builder, false).construct();
		this.grid = builder.getResult();
		playerDb.createNewDB(grid.getAllStartingPositions());
	}
	
	@Test
	public final void testGrid() {
		// create a new map
		Square square = new Square(Collections.<Direction, ASquare> emptyMap());
		Map<Coordinate, ASquare> map = new HashMap<Coordinate, ASquare>();
		map.put(new Coordinate(0, 0), square);
		grid = new Grid(map);
		
		assertEquals(1, grid.size());
		assertTrue(grid.getGrid().containsValue(square));
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public final void testGrid_nullArgument() {
		new Grid(null);
	}
	
	@Test
	public final void testGetGrid() {
		// test whether modifying the returned map influences the internal state
		// of the grid
		grid.getGrid().clear();
		assertFalse(grid.getGrid().isEmpty());
	}
	
	@Test
	public final void testGetItemList() {
		// does this needs testing?
	}
	
	@Test
	public final void testGetSquareAt() {
		fail("Not yet implemented");
	}
	
	@Test
	public final void testGetHeight() {
		fail("Not yet implemented");
	}
	
	@Test
	public final void testGetWidth() {
		fail("Not yet implemented");
	}
	
	@Test
	public final void testGetAllGridCoordinates() {
		fail("Not yet implemented");
	}
	
	@Test
	public final void testCanMoveFromCoordInDirection() {
		fail("Not yet implemented");
	}
	
	@Test
	public final void testUpdatePowerFailures() {
		fail("Not yet implemented");
	}
	
	@Test
	public final void testAddPowerFailureAtCoordinate() {
		fail("Not yet implemented");
	}
	
	@Test
	public final void testEnablePowerFailures() {
		fail("Not yet implemented");
	}
	
}
