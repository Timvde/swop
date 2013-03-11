package player;

import item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing an inventory of {@link Item}s that is carried by a
 * {@link Player}.
 */
public class Inventory implements IInventory {

	// At this moment the number of items is the same for all inventories.
	private static final int MAX_NUMBER_OF_ITEMS = 6;

	private List<Item> contents = new ArrayList<Item>();

	@Override
	public void addItem(Item item) throws IllegalArgumentException {
		if (contents.size() >= this.getMaxNumberOfItems())
			throw new IllegalArgumentException(
					"Trying to add an extra item to an already full inventory");

		contents.add(item);
	}

	@Override
	public int getMaxNumberOfItems() {
		// At this moment the number of items is the same for all inventories.
		return MAX_NUMBER_OF_ITEMS;
	}

	@Override
	public List<Item> getItems() {
		return contents;
	}

}
