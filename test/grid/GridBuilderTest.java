package grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import grid.Coordinate;
import grid.Grid;
import grid.GridBuilder;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import player.Player;
import square.ISquare;
import square.Square;
import square.WallPart;

@SuppressWarnings("javadoc")
public class GridBuilderTest {
	
	List<Player>	players;
	
	@Before
	public void setUp() {
		players = new ArrayList<Player>();
		Player p1 = new Player();
		Player p2 = new Player();
		players.add(p1);
		players.add(p2);
	}
	
	// TODO: Add a test for light grenades, identity disks, teleporters...
	@Test
	public void testConstructor() {
		for (int i = 0; i < 10; i++) {
			Grid grid = new GridBuilder(players).build();
			int numberOfWalls = 2;
			for (ISquare sq : grid.getGrid().values())
				if (sq.getClass() == WallPart.class)
					numberOfWalls++;
			assertTrue(numberOfWalls >= 2); // Minimum wall size
			assertTrue(numberOfWalls <= 20);// 20% of 100 squares filled
			assertTrue(grid.getWidth() >= 10);
			assertTrue(grid.getHeight() >= 10);
			
			// Prepare for next round
			setUp();
		}
	}
	
	@Test
	public void testStaticGrid() {
		
		Grid grid = new GridBuilder(players).getPredefinedTestGrid(false);
		
		assertEquals(grid.toString(), "s s s s s s s s s s \n" + "s s s s s s s s s s \n"
				+ "s s s s s s s l s s \n" + "s s s s s s s s s s \n" + "s s s s s s s s s s \n"
				+ "s s s s w w w w w s \n" + "s s s s s s s l s s \n" + "s s l s s s s s l s \n"
				+ "s s s s s l l l l s \n" + "s s s s s s s s s s \n");
	}
}
