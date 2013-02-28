package item;

import grid.Square;

import java.util.concurrent.atomic.AtomicInteger;

import player.Player;

import com.sun.istack.internal.NotNull;

/**
 * An abstract class representing an Item, an object that can be placed on a {@link Square}. Each item has a
 * unique ID. Some Items can be picked up by a {@link Player}.
 */
public abstract class Item {

	@NotNull
	private int id;
	@NotNull
	private static AtomicInteger nextID = new AtomicInteger();

	/**
	 * Returns the unique ID of this item
	 * 
	 * @return The unique ID of this item
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Return whether or not this item can be picked up by a {@link Player}.
	 * 
	 * @return whether or not this item can be picked up by a {@link Player}.
	 */
	public abstract boolean isCarriable();

	/**
	 * Constructs a new Item and gives it a unique ID.
	 */
	public Item() {
		this.id = nextID.incrementAndGet();
	}

}
