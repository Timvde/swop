package scenariotests;

import static org.junit.Assert.*;
import item.IItem;
import java.util.List;
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
import controllers.UseItemController;

/**
 * Test if the number of actions mechanics work correctly.
 * 
 * Tests: - Player enters a power failed square and a light grenade explodes. -
 * Player starts on a square with a power failure. - Player stands on a square
 * and a light grenade explodes.
 * 
 * @author Tom
 */
public class NumberOfActionsTest {
	
	private static GUIDataController	guiDataCont;
	private static PickUpItemController	pickUpCont;
	private static UseItemController	useItemCont;
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
		useItemCont = new UseItemController(playerDB);
		endTurnCont = new EndTurnController(playerDB);
	}
	
	@Test
	public void testPlayerEntersPowerFailedSquareWithLightGrenadeExplosion() {
		
	}
	
	@Test
	public void testPlayerStartsOnPowerFailedSquare() {
		
	}
	
	@Test
	public void testPlayerSquareLightGrenade() {
		// Player 1 actions
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.EAST);
		// Player 2 actions
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		// Player 1 actions
		moveCont.move(Direction.EAST);
		List<IItem> items = grid.getSquareOfPlayer(playerDB.getCurrentPlayer()).getCarryableItems();
		IItem lightGrenade = items.get(0);
		pickUpCont.pickUpItem(lightGrenade);
		moveCont.move(Direction.NORTH);
		// Player 2 actions
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		// Player 1 actions
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		useItemCont.useItem(lightGrenade);
		// Player 2 actions
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		// Player 1 actions
		moveCont.move(Direction.NORTH);
		endTurnCont.endTurn();
		// Player 2 actions
		moveCont.move(Direction.SOUTHWEST);
		// TODO BOOM?
	}
}
