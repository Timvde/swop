package player;

import item.Item;

import java.util.List;

/**
 * An inventory of {@link Item}s is carried by a {@link Player}.
 */
public interface IInventory {

	/**
	 * This method adds a given item to this inventory.
	 * 
	 * @param item
	 *            The item to be added to the inventory
	 * 
	 * @throws IllegalArgumentException
	 *             The inventory can hold a max of
	 *             <code>getMaxNumberOfItems()</code> items.
	 */
	public void addItem(Item item) throws IllegalArgumentException;

	/**
	 * Returns the maximum number of items this inventory can hold.
	 * 
	 * @return the maximum number of items this inventory can hold.
	 */
	public int getMaxNumberOfItems();

	/**
	 * Returns a list of the stored items.
	 * 
	 * @return a list of the stored items.
	 */
	public List<Item> getItems();

}
