package scenariotests;

import game.CTFMode;
import game.GameRunner;
import grid.builder.RandomGridBuilderDirector;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.Test;
import ObjectronExceptions.builderExceptions.InvalidGridFileException;
import controllers.NewGameController;

/**
 * Tests the "Start New Game", "Choose Generated Grid", "Choose Grid From File"
 * and "Choose Capture The Flag Mode" use cases.
 */
@SuppressWarnings("javadoc")
public class StartGameTest {
	
	private NewGameController	newGameCont;
	private static final int	NUMBER_OF_PLAYERS	= 4;
	
	@Before
	public void setUp() {
		GameRunner game = new GameRunner();
		newGameCont = new NewGameController(game);
	}
	
	// ############## test newRaceGame(int, int) ################
	@Test
	public void testNewRaceGameCorrectDimensions() {
		newGameCont.newRaceGame(RandomGridBuilderDirector.MINIMUM_GRID_WIDTH,
				RandomGridBuilderDirector.MINIMUM_GRID_HEIGHT);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNewRaceGameInCorrectDimensions() {
		newGameCont.newRaceGame(RandomGridBuilderDirector.MINIMUM_GRID_WIDTH - 1,
				RandomGridBuilderDirector.MINIMUM_GRID_HEIGHT - 1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNewRaceGame_negativeDimensions() {
		newGameCont.newRaceGame(-1, 10);
	}
	
	// ############## test newCTFGame(int, int) ################
	@Test
	public void testNewCTFGameCorrectDimensions() {
		newGameCont.newCTFGame(RandomGridBuilderDirector.MINIMUM_GRID_WIDTH,
				RandomGridBuilderDirector.MINIMUM_GRID_HEIGHT, NUMBER_OF_PLAYERS);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNewCTFGameInCorrectDimensions() {
		newGameCont.newCTFGame(RandomGridBuilderDirector.MINIMUM_GRID_WIDTH - 1,
				RandomGridBuilderDirector.MINIMUM_GRID_HEIGHT - 1, NUMBER_OF_PLAYERS);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNewCTFGame_negativeDimensions() {
		newGameCont.newCTFGame(-1, 10, NUMBER_OF_PLAYERS);
	}
	
	// ############## test newRaceGame(String) ################
	@Test
	public void testNewRaceGameFromFile() throws FileNotFoundException {
		newGameCont.newRaceGame("grid.txt");
	}
	
	@Test(expected = InvalidGridFileException.class)
	public void testInvaldidFileRace() throws FileNotFoundException {
		newGameCont.newRaceGame("grid_invalidCharacter.txt");
	}
	
	@Test(expected = InvalidGridFileException.class)
	public void testInvaldidFile2Race() throws FileNotFoundException {
		newGameCont.newRaceGame("grid_oneStartingPosition.txt");
	}
	
	@Test(expected = InvalidGridFileException.class)
	public void testInvaldidFile3Race() throws FileNotFoundException {
		newGameCont.newRaceGame("grid_unreachableIsland.txt");
	}
	
	@Test(expected = FileNotFoundException.class)
	public void testInvaldidFile4Race() throws FileNotFoundException {
		newGameCont.newRaceGame("file_that_doesn't exist.txt");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullInputRace() throws FileNotFoundException {
		newGameCont.newRaceGame(null);
	}
	
	// ############## test newCTFGame(String, int) ################
	@Test
	public void testNewCTFGameFromFile() throws FileNotFoundException {
		newGameCont.newCTFGame("grid.txt", CTFMode.MINIMUM_NUMBER_OF_PLAYERS);
	}
	
	@Test(expected = InvalidGridFileException.class)
	public void testInvaldidFileCTF() throws FileNotFoundException {
		newGameCont.newCTFGame("grid_invalidCharacter.txt", CTFMode.MINIMUM_NUMBER_OF_PLAYERS);
	}
	
	@Test(expected = InvalidGridFileException.class)
	public void testInvaldidFile2CTF() throws FileNotFoundException {
		newGameCont.newCTFGame("grid_oneStartingPosition.txt", CTFMode.MINIMUM_NUMBER_OF_PLAYERS);
	}
	
	@Test(expected = InvalidGridFileException.class)
	public void testInvaldidFile3CTF() throws FileNotFoundException {
		newGameCont.newCTFGame("grid_unreachableIsland.txt", CTFMode.MINIMUM_NUMBER_OF_PLAYERS);
	}
	
	@Test(expected = FileNotFoundException.class)
	public void testInvaldidFile4CTF() throws FileNotFoundException {
		newGameCont.newCTFGame("file_that_doesn't exist.txt", CTFMode.MINIMUM_NUMBER_OF_PLAYERS);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullInputCTF() throws FileNotFoundException {
		newGameCont.newCTFGame(null, CTFMode.MINIMUM_NUMBER_OF_PLAYERS);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCTFInCorrectNumberOfPlayers() throws FileNotFoundException {
		newGameCont.newCTFGame("grid.txt", CTFMode.MINIMUM_NUMBER_OF_PLAYERS - 1);
	}
}
