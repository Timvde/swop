package scenariotests;

import game.CTFMode;
import game.GameRunner;
import grid.builder.RandomGridBuilderDirector;
import org.junit.Before;
import org.junit.Test;
import ObjectronExceptions.builderExceptions.GridBuildException;
import ObjectronExceptions.builderExceptions.InvalidGridFileException;
import controllers.NewGameController;

/**
 * Tests the "Start New Game", "Choose Generated Grid", "Choose Grid From File"
 * and "Choose Capture The Flag Mode" use cases.
 */
@SuppressWarnings("javadoc")
public class StartGameTest {
	
	private NewGameController	newGameCont;
	
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
				RandomGridBuilderDirector.MINIMUM_GRID_HEIGHT, CTFMode.MINIMUM_NUMBER_OF_PLAYERS);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNewCTFGameInCorrectDimensions() {
		newGameCont.newCTFGame(RandomGridBuilderDirector.MINIMUM_GRID_WIDTH - 1,
				RandomGridBuilderDirector.MINIMUM_GRID_HEIGHT - 1,
				CTFMode.MINIMUM_NUMBER_OF_PLAYERS);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNewCTFGame_negativeDimensions() {
		newGameCont.newCTFGame(-1, 10, CTFMode.MINIMUM_NUMBER_OF_PLAYERS);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNewCTFGameInCorrectNumberOfPlayers() {
		newGameCont.newCTFGame(RandomGridBuilderDirector.MINIMUM_GRID_WIDTH,
				RandomGridBuilderDirector.MINIMUM_GRID_HEIGHT,
				CTFMode.MINIMUM_NUMBER_OF_PLAYERS - 1);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testNewCTFGameIllegalNumberOfPlayers() {
		newGameCont.newCTFGame(RandomGridBuilderDirector.MINIMUM_GRID_WIDTH,
				RandomGridBuilderDirector.MINIMUM_GRID_HEIGHT,
				RandomGridBuilderDirector.NUMBER_OF_PLAYER_STARTS + 1);
	}
	
	// ############## test newRaceGame(String) ################
	@Test
	public void testNewRaceGameFromFile() {
		newGameCont.newRaceGame("grid.txt");
	}
	
	@Test(expected = InvalidGridFileException.class)
	public void testInvaldidFileRace() {
		newGameCont.newRaceGame("grid_invalidCharacter.txt");
	}
	
	@Test(expected = InvalidGridFileException.class)
	public void testInvaldidFile2Race() {
		newGameCont.newRaceGame("grid_oneStartingPosition.txt");
	}
	
	@Test(expected = InvalidGridFileException.class)
	public void testInvaldidFile3Race() {
		newGameCont.newRaceGame("grid_unreachableIsland.txt");
	}
	
	@Test(expected = GridBuildException.class)
	public void testInvaldidFile4Race() {
		newGameCont.newRaceGame("file_that_doesn't exist.txt");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullInputRace() {
		newGameCont.newRaceGame(null);
	}
	
	// ############## test newCTFGame(String, int) ################
	@Test
	public void testNewCTFGameFromFile() {
		newGameCont.newCTFGame("grid.txt", CTFMode.MINIMUM_NUMBER_OF_PLAYERS);
	}
	
	@Test(expected = InvalidGridFileException.class)
	public void testInvaldidFileCTF() {
		newGameCont.newCTFGame("grid_invalidCharacter.txt", CTFMode.MINIMUM_NUMBER_OF_PLAYERS);
	}
	
	@Test(expected = InvalidGridFileException.class)
	public void testInvaldidFile2CTF() {
		newGameCont.newCTFGame("grid_oneStartingPosition.txt", CTFMode.MINIMUM_NUMBER_OF_PLAYERS);
	}
	
	@Test(expected = InvalidGridFileException.class)
	public void testInvaldidFile3CTF() {
		newGameCont.newCTFGame("grid_unreachableIsland.txt", CTFMode.MINIMUM_NUMBER_OF_PLAYERS);
	}
	
	@Test(expected = GridBuildException.class)
	public void testInvaldidFile4CTF() {
		newGameCont.newCTFGame("file_that_doesn't exist.txt", CTFMode.MINIMUM_NUMBER_OF_PLAYERS);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullInputCTF() {
		newGameCont.newCTFGame(null, CTFMode.MINIMUM_NUMBER_OF_PLAYERS);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCTFInCorrectNumberOfPlayers() {
		newGameCont.newCTFGame("grid.txt", CTFMode.MINIMUM_NUMBER_OF_PLAYERS - 1);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testCTFIllegalNumberOfPlayers() {
		newGameCont.newCTFGame("grid.txt", RandomGridBuilderDirector.NUMBER_OF_PLAYER_STARTS + 1);
	}
}
