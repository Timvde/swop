package grid.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import effects.RaceFactory;
import grid.Grid;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class RandomDirectorTest {
	
	@Test
	public void testRandomDirector() {
		DummyGridBuilder builder = new DummyGridBuilder();
		RandomGridBuilderDirector director = new RandomGridBuilderDirector(builder);
		
		for (int i = 0; i < 80; i++) {
			director.setWidth(RandomGridBuilderDirector.MINIMUM_GRID_WIDTH + i);
			director.setHeight(RandomGridBuilderDirector.MINIMUM_GRID_HEIGHT + 5 + i);
			director.construct();
			
			builder.assertIsValidGrid();
		}
	}
	
	@Test
	public void testRandomDirectorCreatedGrid() {
		TronGridBuilder builder = new TronGridBuilder(new RaceFactory());
		RandomGridBuilderDirector director = new RandomGridBuilderDirector(builder);
		director.construct();
		
		Grid grid = builder.getResult();
		assertEquals(RandomGridBuilderDirector.MINIMUM_GRID_HEIGHT, grid.getHeight());
		assertEquals(RandomGridBuilderDirector.MINIMUM_GRID_WIDTH, grid.getWidth());
		
		int width = RandomGridBuilderDirector.MINIMUM_GRID_WIDTH + 10;
		int height = RandomGridBuilderDirector.MINIMUM_GRID_HEIGHT + 15;
		director.setHeight(height);
		director.setWidth(width);
		director.construct();
		
		Grid grid2 = builder.getResult();
		assertFalse(grid.equals(grid2));
		assertEquals(width, grid2.getWidth());
		assertEquals(height, grid2.getHeight());
	}
	
	@Test
	public void testRandomDirectorInvalidInput() {
		DummyGridBuilder builder = new DummyGridBuilder();
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
}
