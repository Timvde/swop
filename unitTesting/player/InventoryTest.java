package player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import item.Item;
import item.lightgrenade.LightGrenade;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class InventoryTest {
	
	private static Inventory	inventory;
	
	@Before
	public void setUp() {
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
	
	@Test(expected = IllegalArgumentException.class)
	public void testFullInventory() {
		// try and add seven items to the inventory
		for (int i = 0; i < 7; i++) {
			inventory.addItem(new LightGrenade());
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddNullItemToInventory() {
		// test if null can be added
		assertFalse(inventory.canAddItem(null));
		// try and add null
		inventory.addItem(null);
	}
	
	@Test
	public void testRemoveItem() {
		// put six items in the inventory
		for (int i = 0; i < inventory.getMaxNumberOfItems(); i++)
			inventory.addItem(new LightGrenade());
		
		// test the size
		assertEquals(6, inventory.size());
		// try removing null
		inventory.removeItem(null);
		assertEquals(6, inventory.size());
		// try removing a new light grenade
		inventory.removeItem(new LightGrenade());
		assertEquals(6, inventory.size());
		// remove an item from the inventory
		for (int i = 1; i <= inventory.getMaxNumberOfItems(); i++) {
			inventory.removeItem(inventory.getItems().get(0));
			assertEquals(inventory.getMaxNumberOfItems() - i, inventory.size());
		}
	}
	
	@Test
	public void testHasItem() {
		LightGrenade lg = new LightGrenade();
		assertFalse(inventory.hasItem(lg));
		inventory.addItem(lg);
		assertTrue(inventory.hasItem(lg));
		assertFalse(inventory.hasItem(new LightGrenade()));
		assertFalse(inventory.hasItem(null));
		inventory.removeItem(lg);
		assertFalse(inventory.hasItem(lg));
	}
	
	@Test
	public void testEncapsulation() {
		// put six items in the inventory
		for (int i = 0; i < inventory.getMaxNumberOfItems(); i++)
			inventory.addItem(new LightGrenade());
		
		// empty the returned list
		inventory.getItems().clear();
		
		// the size should be unchanged
		assertEquals(inventory.getMaxNumberOfItems(), inventory.size());
	}
	
}
