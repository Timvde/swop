package scenariotests;

import static org.junit.Assert.*;
import item.IItem;
import item.Item;
import game.Game;
import grid.Coordinate;
import grid.Direction;
import grid.Grid;
import grid.GridBuilder;
import org.junit.Before;
import org.junit.Test;
import ObjectronExceptions.IllegalMoveException;
import player.PlayerDataBase;
import controllers.EndTurnController;
import controllers.MoveController;
import controllers.PickUpItemController;
import controllers.UseItemController;

@SuppressWarnings("javadoc")
public class IdentityDiskTest {
	
	private MoveController			moveCont;
	private EndTurnController		endTurnCont;
	private UseItemController		useItemCont;
	private PickUpItemController	pickUpItemCont;
	private Grid					grid;
	private PlayerDataBase			playerDB;
	
	@Before
	public void setUp() throws Exception {
		Game game = new Game();
		grid = new GridBuilder().getPredefinedTestGrid(false);
		playerDB = new PlayerDataBase(grid);
		
		Coordinate[] startingCoords = new Coordinate[2];
		startingCoords[0] = new Coordinate(grid.getWidth() - 1, 0);
		startingCoords[1] = new Coordinate(0, grid.getHeight() - 1);
		
		playerDB.createNewDB(startingCoords, grid);
		game.setGrid(grid);
		
		game.start();
		
		moveCont = new MoveController(playerDB);
		endTurnCont = new EndTurnController(playerDB);
	}
	
	@Test
	public void testUseIdentityDisk() throws IllegalStateException, IllegalArgumentException,
			IllegalMoveException {
		
		// move player 1 to the identity disk
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		IItem disk = grid.getSquareAt(playerDB.getCurrentPlayer().getCurrentLocation())
				.getCarryableItems().get(0);
		pickUpItemCont.pickUpItem(disk);
		
		// end the turn of player 2 asap
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// shoot the identity disk south
		useItemCont.useItem(disk, Direction.SOUTH);
		
		assertTrue(grid.getSquareAt(new Coordinate(7, 4)).contains(disk));
	}
	
	@Test
	public void testUseIdentityDisk_withPowerFailure() throws IllegalStateException,
			IllegalArgumentException, IllegalMoveException {
		// move player 1 to the identity disk
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		IItem disk = grid.getSquareAt(playerDB.getCurrentPlayer().getCurrentLocation())
				.getCarryableItems().get(0);
		pickUpItemCont.pickUpItem(disk);
		
		// end the turn of player 2 asap
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// shoot the identity disk west ( i think there are power failures there ;) )
		useItemCont.useItem(disk, Direction.WEST);
		
		assertTrue(grid.getSquareAt(new Coordinate(4, 0)).contains(disk));
	}
	
	@Test
	public void testUseIdentityDisk_shootAgainstWall() throws IllegalStateException, IllegalArgumentException, IllegalMoveException {
		// move player 1 to the identity disk
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		IItem disk = grid.getSquareAt(playerDB.getCurrentPlayer().getCurrentLocation())
				.getCarryableItems().get(0);
		pickUpItemCont.pickUpItem(disk);
		
		// end the turn of player 2 asap
		moveCont.move(Direction.EAST);
		endTurnCont.endTurn();
		
		// move south and shoot the identity disk against a wall
		moveCont.move(Direction.SOUTH);
		moveCont.move(Direction.SOUTH);
		useItemCont.useItem(disk, Direction.SOUTH);
		
		assertTrue(grid.getSquareAt(new Coordinate(7, 4)).contains(disk));
	}
}
