package player;

import item.Item;
import java.util.ArrayList;
import java.util.List;
import item.IItem;

/**
 * An inventory of {@link Item}s that is carried by a
 * {@link TronPlayer}.
 */
public class Inventory implements IInventory {
	
	// At this moment the number of items is the same for all inventories.
	private static final int	MAX_NUMBER_OF_ITEMS	= 6;
	
	private List<IItem>			contents;
	
	/**
	 * Creates a new empty inventory
	 */
	public Inventory() {
		this.contents = new ArrayList<IItem>(MAX_NUMBER_OF_ITEMS);
	}
	
	/**
	 * add an item to this inventory
	 * 
	 * @param item
	 *        the item to be added to the inventory
	 * @throws IllegalArgumentException
	 *         when an item {@link #canAddItem(IItem) cannot} be added to this
	 *         inventory
	 */
	public void addItem(IItem item) throws IllegalArgumentException {
		if (!canAddItem(item))
			throw new IllegalArgumentException(
					"The specified item could not be added to this inventory");
		
		contents.add(item);
	}
	
	/**
	 * returns whether a specified item can be added to the inventory. An item
	 * can be added when the when the size of this inventory is less then the
	 * maximum number of items in the inventory and the specified item is not
	 * null.
	 * 
	 * @param item
	 *        the item to test
	 * @return true if the item can be added, else false
	 */
	public boolean canAddItem(IItem item) {
		if (contents.size() >= this.getMaxNumberOfItems())
			return false;
		else if (item == null)
			return false;
		else
			return true;
	}
	
	@Override
	public int getMaxNumberOfItems() {
		// At this moment the number of items is the same for all inventories.
		return Inventory.MAX_NUMBER_OF_ITEMS;
	}
	
	@Override
	public List<IItem> getItems() {
		return new ArrayList<IItem>(contents);
	}
	
	@Override
	public void removeItem(IItem item) {
		contents.remove(item);
	}
	
	/**
	 * returns the number of elements in this inventory. The number of items is
	 * always less than or equal to the {@link #getMaxNumberOfItems() maximum}
	 * number of items in this inventory
	 * 
	 * @return the number of items in this inventory
	 */
	public int size() {
		return contents.size();
	}
	
	/**
	 * returns whether this inventory contains the specified element. More
	 * formally this method returns <tt>true</tt> if and only if this inventory
	 * contains at least one item <tt>i</tt> such that
	 * <tt> item.equals(i) </tt>
	 * 
	 * @param item
	 *        the item whose presence in this inventory is to be tested
	 * @return true if this inventory contains the specified item, else false
	 */
	public boolean hasItem(IItem item) {
		return contents.contains(item);
	}
	
}
