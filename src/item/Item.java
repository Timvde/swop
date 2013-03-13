package item;

import java.util.concurrent.atomic.AtomicInteger;
import com.sun.istack.internal.NotNull;

public abstract class Item implements IItem {
	
	@NotNull
	private int						id;
	@NotNull
	private static AtomicInteger	nextID	= new AtomicInteger();
	
	public int getId() {
		return this.id;
	}
	
	/**
	 * Constructs a new Item and gives it a unique ID.
	 */
	public Item() {
		this.id = nextID.incrementAndGet();
	}
	
	/**
	 * Return a string representation of this item.
	 */
	public abstract String toString();
	
	public void addToEffect(Effect effect) {
		// Do nothing by default. This shouldn't be abstract though, some items
		// really don't have to do anything.
	}
	
}
