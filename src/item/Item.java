package item;

import java.util.concurrent.atomic.AtomicInteger;

import com.sun.istack.internal.NotNull;

public abstract class Item implements IItem {

	@NotNull
	private int id;
	@NotNull
	private static AtomicInteger nextID = new AtomicInteger();

	public int getId() {
		return this.id;
	}

	public Item() {
		this.id = nextID.incrementAndGet();
	}

}
