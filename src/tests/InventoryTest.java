package tests;

import static org.junit.Assert.*;
import item.Item;
import item.LightGrenade;
import org.junit.BeforeClass;
import org.junit.Test;
import player.Inventory;


public class InventoryTest {
	
	private static Inventory inventory;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		inventory = new Inventory();
	}
	
	@Test
	public void testMaxInventorySize() {
		assertEquals(inventory.getMaxNumberOfItems(), 6);
	}
	
	@Test
	public void testInventory() {
		Item item = new LightGrenade();
		assertEquals(inventory.getItems().size(), 0);
		inventory.addItem(item);
		assertEquals(inventory.getItems().size(), 1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testFullInventory() {
		for (int i = 0; i < 7; i++) {
			inventory.addItem(new LightGrenade());
		}
	}
	
}
