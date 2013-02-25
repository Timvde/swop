package player;

import item.Item;
import java.util.ArrayList;
import java.util.List;
import ObjectronExceptions.InventoryFullException;

public class Inventory {
	
	private List<Item>	contents			= new ArrayList<Item>();
	
	private final int	MAX_NUMBER_OF_ITEMS	= 6;
	
	public Inventory() {
		
	}
	
	public void addItem(Item item) throws InventoryFullException {
		if (contents.size() == MAX_NUMBER_OF_ITEMS)
			throw new InventoryFullException();
		
		contents.add(item);
	}
	
	public List<Item> getItems() {
		return contents;
	}
	
}
