package scenariotests;

import junit.framework.Assert;
import game.Game;
import grid.Coordinate;
import grid.Direction;
import grid.Grid;
import grid.GridBuilder;
import org.junit.Before;
import org.junit.Test;
import player.PlayerDataBase;
import controllers.EndTurnController;
import controllers.MoveController;

/**
 * This class will test scenarios concerning the movement of players.
 * 
 * Tests: - No two players on one square - Cannot move on wall - Cannot leave
 * grid - Cannot cross lightrail - Player must always do a move action in turn
 * 
 * @author Tom
 */
@SuppressWarnings("javadoc")
public class MovePlayerTest {
	
	private static MoveController		moveCont;
	private static EndTurnController	endTurnCont;
	private static Grid					grid;
	private static PlayerDataBase		playerDB;
	
	public void newGame() {
		Game game = new Game();
		playerDB = new PlayerDataBase();
		grid = new GridBuilder().getPredefinedTestGrid();
		
		Coordinate[] startingCoords = new Coordinate[2];
		startingCoords[0] = new Coordinate(grid.getWidth() - 1, 0);
		startingCoords[1] = new Coordinate(0, grid.getHeight() - 1);
		
		playerDB.createNewDB(startingCoords, grid);
		game.setGrid(grid);
		
		game.start();
		
		moveCont = new MoveController(playerDB);
		endTurnCont = new EndTurnController(playerDB);
	}
	
	/*
	 * NOTE: the used coord axis are defined by {@link Coordinate} (origin upper
	 * left)
	 * 
	 * FIXME: @Tom: verbeter dit in alle scenario test (of overleg om het
	 * assenstelsel te veranderen?); de test hieronder is al aangepast, maar nog
	 * errors met lighttrails...
	 */
	@Test
	public void testNoTwoPlayersOnOneSquare() {
		newGame();
		
		// Player 1 actions
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.WEST);
		moveCont.move(Direction.SOUTHWEST);
		// Player 2 actions
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTH);
		moveCont.move(Direction.NORTHEAST);
		// Player 1 actions
		moveCont.move(Direction.SOUTHWEST);
		moveCont.move(Direction.SOUTHWEST);
		moveCont.move(Direction.SOUTHWEST);
		
		// Player 2 actions
		moveCont.move(Direction.NORTHEAST);
		
		boolean exceptionThrown = false;
		try {
			// This causes two players to be on the same square:
			moveCont.move(Direction.NORTHEAST);
		}
		catch (IllegalStateException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
	}
	
	public void testCannotMoveOnWall() {
		newGame();
		
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		// Player 2 actions
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		moveCont.move(Direction.NORTHEAST);
		// Player 1 actions
		moveCont.move(Direction.SOUTH);
		endTurnCont.endTurn();
		
		// Player 2 actions
		boolean exceptionThrown = false;
		try {
			// This is a move on a wall:
			moveCont.move(Direction.NORTHEAST);
		}
		catch (IllegalStateException e) {
			exceptionThrown = true;
		}
		Assert.assertEquals(true, exceptionThrown);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testCannotLeaveGrid() {
		newGame();
		
		// Player 1 actions
		moveCont.move(Direction.NORTH);
	}
	
	// TODO specifieker specifiëren, ook met try catch
	@Test(expected = Exception.class)
	public void testCannotCrossLightrail() {
		newGame();
		
		// Player 1 actions
		moveCont.move(Direction.SOUTHWEST);
		moveCont.move(Direction.EAST);
		moveCont.move(Direction.NORTHWEST);
	}
	
	// TODO specifieker specifiëren
	@Test(expected = Exception.class)
	public void testAlwaysMoveActionInTurn() {
		newGame();
		
		// Player 1 actions
		endTurnCont.endTurn();
	}
}
