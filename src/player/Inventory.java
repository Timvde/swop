package player;

import item.Item;
import java.util.ArrayList;
import java.util.List;
import ObjectronExceptions.InventoryFullException;

/**
 * A class representing an inventory of {@link Item}s that is carried by a
 * {@link Player}.
 */
public class Inventory {
	
	// At this moment the number of items is the same for all inventories.
	private static final int MAX_NUMBER_OF_ITEMS = 6;
	
	private List<Item> contents = new ArrayList<Item>();

	/**
	 * This method adds a given item to this inventory.
	 * 
	 * @param item
	 *            The item to be added to the inventory
	 * 
	 * @throws InventoryFullException
	 *             The inventory can hold a max of
	 *             <code>getMaxNumberOfItems()</code> items.
	 */
	public void addItem(Item item) throws InventoryFullException {
		if (contents.size() >= this.getMaxNumberOfItems())
			throw new InventoryFullException();

		contents.add(item);
	}

	/**
	 * Returns the maximum number of items this inventory can hold.
	 * 
	 * @return the maximum number of items this inventory can hold.
	 */
	public int getMaxNumberOfItems() {
		// At this moment the number of items is the same for all inventories.
		return Inventory.MAX_NUMBER_OF_ITEMS;
	}

	/**
	 * Returns a list of the stored items.
	 * 
	 * @return a list of the stored items.
	 */
	public List<Item> getItems() {
		return contents;
	}

}
