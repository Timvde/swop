package player;

import item.Item;

import java.util.List;

/**
 * An inventory of {@link Item items} is carried by a {@link Player player}. The inventory cannot 
 * hold more then a {@link #getMaxNumberOfItems() maximum} number of items.
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
	 * @return the maximum number of items
	 */
	public int getMaxNumberOfItems();

	/**
	 * Returns a list of the stored items.
	 * 
	 * @return a list of the stored items.
	 */
	public List<Item> getItems();

}
