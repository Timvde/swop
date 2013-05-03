package grid.builder;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import ObjectronExceptions.builderExceptions.GridBuildException;
import ObjectronExceptions.builderExceptions.InvalidGridFileException;

@SuppressWarnings("javadoc")
public class FileDirectorTest {
	
	@Test
	public void testFileDirector() {
		TestGridBuilder builder = new TestGridBuilder();
		GridBuilderDirector director = new FileGridBuilderDirector(builder, "grid.txt");
		director.construct();
		
		assertGridHasValidWallsAndItems(builder);
	}
	
	@Test
	public void testFileDirectorInvalidInput() {
		TestGridBuilder builder = new TestGridBuilder();
		boolean exceptionThrown = false;
		try {
			new FileGridBuilderDirector(null, "grid.txt");
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		exceptionThrown = false;
		try {
			new FileGridBuilderDirector(builder, null);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		exceptionThrown = false;
		GridBuilderDirector director = new FileGridBuilderDirector(builder,
				"file_that_doesn't exist");
		try {
			director.construct();
		}
		catch (GridBuildException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void testFileDirectorInvalidGridFile() {
		TestGridBuilder builder = new TestGridBuilder();
		GridBuilderDirector director = new FileGridBuilderDirector(builder,
				"grid_invalidCharacter.txt");
		
		boolean exceptionThrown = false;
		try {
			director.construct();
		}
		catch (InvalidGridFileException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		exceptionThrown = false;
		director = new FileGridBuilderDirector(builder, "grid_oneStartingPosition.txt");
		try {
			director.construct();
		}
		catch (InvalidGridFileException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		exceptionThrown = false;
		director = new FileGridBuilderDirector(builder, "grid_unreachableIsland.txt");
		try {
			director.construct();
		}
		catch (InvalidGridFileException e) {
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
