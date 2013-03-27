package grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import grid.Coordinate;
import grid.Grid;
import grid.GridBuilder;
import java.util.ArrayList;
import org.junit.Test;
import player.IPlayer;
import player.Player;
import square.ISquare;
import square.WallPart;

@SuppressWarnings("javadoc")
public class GridTest {
	
	@Test
	public void testConstructor() {
		for (int i = 0; i < 10; i++) {
			ArrayList<IPlayer> players = new ArrayList<IPlayer>();
			IPlayer p1 = new Player(new Coordinate(9, 0), new GridBuilder().getPredefinedTestGrid(false));
			IPlayer p2 = new Player(new Coordinate(0, 9), new GridBuilder().getPredefinedTestGrid(false));
			players.add(p1);
			players.add(p2);
			Grid grid = new GridBuilder().build();
			int numberOfWalls = 2;
			for (ISquare sq : grid.getGrid().values())
				if (sq.getClass() == WallPart.class)
					numberOfWalls++;
			assertTrue(numberOfWalls >= 2); // Minimum wall size
			assertTrue(numberOfWalls <= 20);// 20% of 100 squares filled
			assertTrue(grid.getWidth() >= 10);
			assertTrue(grid.getHeight() >= 10);
		}
	}
	
	@Test
	public void testStaticGrid() {

		Grid grid = new GridBuilder().getPredefinedTestGrid(false);
				
		assertEquals(grid.toString(), "s s s s s s s s s s \n" + "s s s s s s s s s s \n"
				+ "s s s s s s s l s s \n" + "s s s s s s s s s s \n" + "s s s s s s s s s s \n"
				+ "s s s s w w w w w s \n" + "s s s s s s s l s s \n" + "s s l s s s s s l s \n"
				+ "s s s s s l l l l s \n" + "s s s s s s s s s s \n");
	}
}
