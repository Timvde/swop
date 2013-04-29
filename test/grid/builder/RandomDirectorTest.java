package grid.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import grid.Grid;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class RandomDirectorTest {
	
	@Test
	public void testRandomDirector() {
		TestGridBuilder builder = new TestGridBuilder();
		RandomGridBuilderDirector director = new RandomGridBuilderDirector(builder);
		
		for (int i = 0; i < 50; i++) {
			director.setWidth(RandomGridBuilderDirector.MINIMUM_GRID_WIDTH + i);
			director.setHeight(RandomGridBuilderDirector.MINIMUM_GRID_HEIGHT + 5 + i);
			director.construct();
			assertGridHasValidWallsAndItems(builder);
		}
	}
	
	@Test
	public void testRandomDirectorCreatedGrid() {
		TronGridBuilder builder = new TronGridBuilder();
		RandomGridBuilderDirector director = new RandomGridBuilderDirector(builder);
		director.construct();
		
		Grid grid = builder.getResult();
		assertEquals(RandomGridBuilderDirector.MINIMUM_GRID_HEIGHT, grid.getHeight() - 1);
		assertEquals(RandomGridBuilderDirector.MINIMUM_GRID_WIDTH, grid.getWidth() - 1);
		
		int width = RandomGridBuilderDirector.MINIMUM_GRID_WIDTH + 10;
		int height = RandomGridBuilderDirector.MINIMUM_GRID_HEIGHT + 15;
		director.setHeight(height);
		director.setWidth(width);
		director.construct();
		
		Grid grid2 = builder.getResult();
		assertFalse(grid.equals(grid2));
		assertEquals(width, grid2.getWidth() - 1);
		assertEquals(height, grid2.getHeight() - 1);
	}
	
	@Test
	public void testRandomDirectorInvalidInput() {
		TestGridBuilder builder = new TestGridBuilder();
		boolean exceptionThrown = false;
		try {
			new RandomGridBuilderDirector(null);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		RandomGridBuilderDirector director = new RandomGridBuilderDirector(builder);
		exceptionThrown = false;
		try {
			director.setWidth(RandomGridBuilderDirector.MINIMUM_GRID_WIDTH - 1);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		exceptionThrown = false;
		try {
			director.setHeight(RandomGridBuilderDirector.MINIMUM_GRID_HEIGHT - 1);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
	}
	
	private void assertGridHasValidWallsAndItems(TestGridBuilder builder) {
		assertTrue(builder.hasValidLightGrenades());
		assertTrue(builder.hasValidIDdisks());
		assertTrue(builder.hasValidTeleporters());
		assertTrue(builder.hasValidWalls());
		
		assertTrue(builder.gridHasValidWallsAndItems());
	}
}
