package grid.builder;

import static org.junit.Assert.assertTrue;
import java.io.FileNotFoundException;
import org.junit.Test;
import ObjectronExceptions.builderExceptions.InvalidGridFileException;

@SuppressWarnings("javadoc")
public class FileDirectorTest {
	
	@Test
	public void testFileDirector() throws FileNotFoundException {
		DummyGridBuilder builder = new DummyGridBuilder();
		GridBuilderDirector director = new FileGridBuilderDirector(builder, "grid.txt");
		director.construct();
		
		builder.assertIsValidGrid();
	}
	
	@Test
	public void testFileDirectorInvalidInput() throws FileNotFoundException {
		DummyGridBuilder builder = new DummyGridBuilder();
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
		try {
			new FileGridBuilderDirector(builder, "file_that_doesn't exist");
		}
		catch (FileNotFoundException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void testFileDirectorInvalidGridFile() throws FileNotFoundException {
		DummyGridBuilder builder = new DummyGridBuilder();
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
}
