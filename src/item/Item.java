package item;

import grid.Placeable;

public class Item implements Placeable {
	
	private boolean	isCarriable;
	private int		id;
	
	public int getId() {
		return this.id;
	}
	
}
