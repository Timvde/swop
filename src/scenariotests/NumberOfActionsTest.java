package scenariotests;

import static org.junit.Assert.*;
import item.IItem;
import java.util.List;
import game.Game;
import grid.Direction;
import grid.Grid;
import grid.GridBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import player.IPlayer;
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
	
	private static PickUpItemController	pickUpCont;
	private static UseItemController	useItemCont;
	private static MoveController		moveCont;
	private static EndTurnController	endTurnCont;
	private static Grid					grid;
	private static PlayerDataBase		playerDB;
	
	private void newGame() {
		Game game = new Game();
		
		playerDB = new PlayerDataBase();
		List<IPlayer> playerList = playerDB.createNewDB();
		
		
		GridBuilder builder = new GridBuilder(playerList);
		grid = builder.getPredefinedTestGrid(playerList);
		
		for (IPlayer p : playerList) {
			p.setGrid(grid);
		}
		
		game.setGrid(grid);
		
		game.start();

		moveCont = new MoveController(playerDB);
		endTurnCont = new EndTurnController(playerDB);
		pickUpCont = new PickUpItemController(playerDB);
		useItemCont = new UseItemController(playerDB);
	}
	
	@Test
	public void testPlayerEntersPowerFailedSquareWithLightGrenadeExplosion() {
		newGame();
		
	}
	
	@Test
	public void testPlayerStartsOnPowerFailedSquare() {
		newGame();
		
	}
	
	@Test
	public void testPlayerSquareLightGrenade() {
		newGame();
		
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
