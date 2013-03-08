package player;

import item.Item;
import java.util.ArrayList;
import java.util.List;
import item.IItem;

/**
 * A class representing an inventory of {@link Item}s that is carried by a
 * {@link Player}.
 */
public class Inventory implements IInventory {

	// At this moment the number of items is the same for all inventories.
	private static final int MAX_NUMBER_OF_ITEMS = 6;

	private List<IItem> contents = new ArrayList<IItem>();

	public void addItem(Item item) throws IllegalArgumentException {
		if (contents.size() >= this.getMaxNumberOfItems())
			throw new IllegalArgumentException(
					"Trying to add an extra item to an already full inventory");

		contents.add(item);
	}

	public int getMaxNumberOfItems() {
		// At this moment the number of items is the same for all inventories.
		return Inventory.MAX_NUMBER_OF_ITEMS;
	}

	public List<IItem> getItems() {
		return contents;
	}

	/**
	 * TODO
	 */
	@Override
	public void addItem(IItem item) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * TODO
	 */
	@Override
	public void removeItem(IItem item) {
		// TODO Auto-generated method stub
		
	}

}
