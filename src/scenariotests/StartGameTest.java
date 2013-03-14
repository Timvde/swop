package scenariotests;

import static org.junit.Assert.*;
import game.Game;
import org.junit.BeforeClass;
import org.junit.Test;
import controllers.NewGameController;

/**
 * Test if the game starts correctly, and all preconditions are respected.
 * 
 * Tests:
 * - Minimum grid size: 10x10
 * 
 * @author tom
 *
 */
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
		try {
			newGameCont.newGame(10, 10);
		}
		catch (Exception e) {
			fail();
		}
	}
	
	// TODO juiste exception definiëren
	@Test(expected = Exception.class)
	public void testNewGameFalseDimensions() {
		newGameCont.newGame(6, 6);
	}
}
