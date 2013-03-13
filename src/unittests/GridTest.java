package unittests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import game.Game;
import grid.ASquare;
import grid.Coordinate;
import grid.Grid;
import grid.GridBuilder;
import org.junit.Test;
import player.IPlayer;
import player.Player;
import grid.Wall.WallPart;


@SuppressWarnings("javadoc")
public class GridTest {
	
	@Test
	public void testConstructor() {
		Game game = new Game();
		ArrayList<IPlayer> players = new ArrayList<IPlayer>();
		IPlayer p1 = new Player(new Coordinate(9,0));
		IPlayer p2 = new Player(new Coordinate(0,9));
		players.add(p1);
		players.add(p2);
		Grid grid = new GridBuilder(game, players).build();
		int numberOfWalls = 2;
		for (ASquare sq : grid.getGrid().values()) 
			if(sq.getClass() == WallPart.class)
				numberOfWalls++;
		assertTrue(numberOfWalls >= 2); // Minimum wall size
		assertTrue(numberOfWalls <= 20);// 20% of 100 squares filled
		assertTrue(grid.getWidth() >= 10);
		assertTrue(grid.getHeight() >= 10);
	}
	
}
