package grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import lightgrenade.LightGrenade;
import grid.GridBuilder;
import item.Item;
import org.junit.Before;
import org.junit.Test;
import player.Player;
import square.Square;

@SuppressWarnings("javadoc")
public class SquareTest {
	
	private static Square	square;
	
	@Before
	public void setUp() {
		square = new Square();
	}
	
	@Test
	public void testItem() {
		Item item = new LightGrenade();
		square.addItem(item);
		assertTrue(square.getCarryableItems().contains(item));
		assertTrue(square.contains(item));
		square.removeItem(item);
		assertEquals(square.getCarryableItems().size(), 0);
	}
	
	@Test
	public void testPlayer() {
		Player player = new Player(GridBuilder.getRandomCoordOnTestGrid(),
				new GridBuilder().getPredefinedTestGrid(false));
		square.setPlayer(player);
		assertEquals(square.getPlayer(), player);
		square.removePlayer();
		assertEquals(square.getPlayer(), null);
	}
	
	@Test
	public void testLightTrail() {
		assertFalse(square.hasLightTrail());
		square.placeLightTrail();
		assertTrue(square.hasLightTrail());
	}
	
}
