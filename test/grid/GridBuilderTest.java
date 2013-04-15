package grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import grid.Grid;
import grid.AGridBuilder;
import item.IItem;
import item.identitydisk.ChargedIdentityDisk;
import item.identitydisk.UnchargedIdentityDisk;
import item.lightgrenade.LightGrenade;
import item.teleporter.Teleporter;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import player.Player;
import player.PlayerDataBase;
import square.ISquare;
import square.Square;
import square.WallPart;

@SuppressWarnings("javadoc")
public class GridBuilderTest {
	
	private List<Player>	players;
	private PlayerDataBase	playerDb;
	
	@Before
	public void setUp() {
		playerDb = new PlayerDataBase();
		players = playerDb.createNewDB();
	}
	
	// TODO: Add a test for light grenades, identity disks, teleporters...
	@Test
	public void testConstructor() {
		for (int j = 0; j < 100; j++) {
			for (int i = 10; i < 20; i++) {
				Grid grid = new AGridBuilder(players).setGridHeigth(i).setGridWidth(i).build();
				int numberOfSquares = i * i;
				int numberOfWalls = 0;
				int numberOfLightGrenades = 0;
				int numberOfUnchargedIdentityDisks = 0;
				int numberOfChargedIdentityDisks = 0;
				int numberOfTeleporters = 0;
				for (ISquare sq : grid.getGrid().values()) {
					if (sq.getClass() == WallPart.class)
						numberOfWalls++;
					else {
						Square square = (Square) sq;
						List<IItem> items = square.getAllItems();
						for (IItem item : items) {
							if (item instanceof LightGrenade)
								numberOfLightGrenades++;
							else if (item instanceof UnchargedIdentityDisk)
								numberOfUnchargedIdentityDisks++;
							else if (item instanceof ChargedIdentityDisk)
								numberOfChargedIdentityDisks++;
							else if (item instanceof Teleporter)
								numberOfTeleporters++;
						}
					}
				}
				
				assertEquals((int) Math.ceil(numberOfSquares * 0.02), numberOfLightGrenades);
				assertEquals((int) Math.ceil(numberOfSquares * 0.02),
						numberOfUnchargedIdentityDisks);
				assertTrue(numberOfChargedIdentityDisks <= 1);
				assertEquals((int) Math.ceil(numberOfSquares * 0.03), numberOfTeleporters);
				assertTrue(numberOfWalls >= 2); // Minimum wall size
				assertTrue(numberOfWalls <= Math.ceil(numberOfSquares * 0.2));
				assertEquals(i, grid.getWidth());
				assertEquals(i, grid.getHeight());
				
				// Prepare for next round
				setUp();
			}
		}
	}
	
	@Test
	public void testStaticGrid() {
		Grid grid = new AGridBuilder(players).getPredefinedTestGrid(false);
		
		assertEquals(grid.toString(), "s s s s s s s s s s \n" + "s s s s s s s s s s \n"
				+ "s s s s s s s l s s \n" + "s s s s s s s s s s \n" + "s s s s s s s s s s \n"
				+ "s s s s w w w w w s \n" + "s s s s s s s l s s \n" + "s s l s s s s s l s \n"
				+ "s s s s s l l l l s \n" + "s s s s s s s s s s \n");
	}
}
