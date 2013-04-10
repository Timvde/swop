package scenariotests;

import game.Game;
import org.junit.BeforeClass;
import org.junit.Test;
import controllers.NewGameController;

/**
 * Test if the game starts correctly, and all preconditions are respected.
 * 
 * Tests: - Minimum grid size: 10x10
 * 
 * @author tom
 * 
 */
@SuppressWarnings("javadoc")
public class StartGameTest {
	
	private static NewGameController	newGameCont;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		Game game = new Game();
		game.start();
		newGameCont = new NewGameController(game);
	}
	
	@Test
	public void testNewGameCorrectDimensions() {
		newGameCont.newGame(10, 10);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNewGame_falseDimensions() {
		newGameCont.newGame(6, 6);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testNewGame_negativeDimensions() {
		newGameCont.newGame(-1, 10);
	}
}
