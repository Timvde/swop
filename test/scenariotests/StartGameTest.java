package scenariotests;

import game.Game;
import grid.builder.RandomGridBuilderDirector;
import org.junit.Before;
import org.junit.Test;
import ObjectronExceptions.builderExceptions.GridBuildException;
import ObjectronExceptions.builderExceptions.InvalidGridFileException;
import controllers.NewGameController;

/**
 * Tests the "Start New Game", "Choose Generated Grid" and
 * "Choose Grid From File" use cases.
 */
@SuppressWarnings("javadoc")
public class StartGameTest {
	
	private NewGameController	newGameCont;
	
	@Before
	public void setUp() {
		Game game = new Game();
		newGameCont = new NewGameController(game);
	}
	
	@Test
	public void testNewGameCorrectDimensions() {
		newGameCont.newGame(RandomGridBuilderDirector.MINIMUM_GRID_WIDTH,
				RandomGridBuilderDirector.MINIMUM_GRID_HEIGHT);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNewGameInCorrectDimensions() {
		newGameCont.newGame(RandomGridBuilderDirector.MINIMUM_GRID_WIDTH - 1,
				RandomGridBuilderDirector.MINIMUM_GRID_HEIGHT - 1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNewGame_negativeDimensions() {
		newGameCont.newGame(-1, 10);
	}
	
	@Test
	public void testNewGameFromFile() {
		newGameCont.newGame("grid.txt");
	}
	
	@Test(expected = InvalidGridFileException.class)
	public void testInvaldidFile() {
		newGameCont.newGame("grid_invalidCharacter.txt");
	}
	
	@Test(expected = InvalidGridFileException.class)
	public void testInvaldidFile2() {
		newGameCont.newGame("grid_oneStartingPosition.txt");
	}
	
	@Test(expected = InvalidGridFileException.class)
	public void testInvaldidFile3() {
		newGameCont.newGame("grid_unreachableIsland.txt");
	}
	
	@Test(expected = GridBuildException.class)
	public void testInvaldidFile4() {
		newGameCont.newGame("file_that_doesn't exist.txt");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullInput() {
		newGameCont.newGame(null);
	}
}
