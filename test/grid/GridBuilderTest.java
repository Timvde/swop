package grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import player.PlayerDataBase;
import square.ISquare;
import square.Square;
import square.WallPart;

@SuppressWarnings("javadoc")
public class GridBuilderTest {
	
	@Test
	public void testConstructor() {
		for (int i = 0; i < 10; i++) {
			// create Players, using the PlayerDb and the predefined test grid
			Square[] startingPositions = new Square[2];
			startingPositions[0] = (Square) new GridBuilder().getPredefinedTestGrid(false).getSquareAt(new Coordinate(9, 0));
			startingPositions[1] = (Square) new GridBuilder().getPredefinedTestGrid(false).getSquareAt(new Coordinate(9, 0));
			PlayerDataBase playerDb = new PlayerDataBase();
			playerDb.createNewDB(startingPositions);
			
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
