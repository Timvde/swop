package item;

public class Item implements IItem {
	
	private boolean	isCarriable;
	private int		id;
	
	public int getId() {
		return this.id;
	}
	
	public boolean isCarriable() {
		return isCarriable;
	}
	
}
