package unittests;

import static org.junit.Assert.*;
import item.Item;
import item.LightGrenade;
import grid.Coordinate;
import grid.Square;
import org.junit.BeforeClass;
import org.junit.Test;
import player.Player;


public class SquareTest {
	
	private static Square square;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		square = new Square();
	}
	
	@Test
	public void testItem() {
		Item item = new LightGrenade();
		square.addItem(item);
		assertTrue(square.getCarryableItems().contains(item));
		assertTrue(square.hasItemWithID(item.getId()));
		square.removeItem(item);
		assertEquals(square.getCarryableItems().size(), 0);
	}
	
	@Test
	public void testPlayer() {
		Player player = new Player(new Coordinate(0,0));
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