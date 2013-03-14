package scenariotests;

import static org.junit.Assert.*;
import game.Game;
import grid.Direction;
import grid.Grid;
import org.junit.BeforeClass;
import org.junit.Test;
import player.PlayerDataBase;
import controllers.EndTurnController;
import controllers.GUIDataController;
import controllers.MoveController;
import controllers.PickUpItemController;

/**
 * Test if a player loses the game in correct circumstances.
 * 
 * Tests: - Player trapped (not by item effect): lose - Player's opponent wins:
 * lose
 * 
 * @author Tom
 */
public class LosingGameTest {
	
	private static GUIDataController	guiDataCont;
	private static PickUpItemController	pickUpCont;
	private static MoveController		moveCont;
	private static EndTurnController	endTurnCont;
	private static Grid					grid;
	private static PlayerDataBase		playerDB;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		// TODO set up playerDB en predefined grid, en bij Game doe setGrid
		Game game = new Game();
		game.start();
		guiDataCont = new GUIDataController(playerDB, grid);
		pickUpCont = new PickUpItemController(playerDB);
		moveCont = new MoveController(playerDB);
		endTurnCont = new EndTurnController(playerDB);
	}
	
	@Test
	public void testPlayerTrappedButNotLosing() {
		
	}
	
	@Test
	public void testPlayerTrappedAndLosing() {
		// Player 1 actions
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		// Player 2 actions
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		// Player 1 actions
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.EAST);
		// Player 2 actions
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 1 actions
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		// TODO test if player 2 lost
	}
}
