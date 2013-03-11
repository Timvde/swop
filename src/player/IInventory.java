package player;

import item.IItem;
import item.Item;
import java.util.List;

/**
 * An inventory of {@link Item items} is carried by a {@link Player player}. The
 * inventory cannot hold more then a {@link #getMaxNumberOfItems() maximum}
 * number of items.
 */
public interface IInventory {
	
	/**
	 * This method adds a given item to this inventory.
	 * 
	 * @param item
	 *        The item to be added to the inventory
	 * 
	 * @throws IllegalArgumentException
	 *         The inventory can hold a max of
	 *         <code>getMaxNumberOfItems()</code> items.
	 */
	public void addItem(IItem item) throws IllegalArgumentException;
	
	/**
	 * Remove an item for this inventory.
	 * 
	 * @param item
	 *        the item to be removed
	 */
	public void removeItem(IItem item);
	
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
	public List<IItem> getItems();
	
}
