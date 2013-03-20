package item;

import grid.square.AffectedByPowerFailure;
import grid.square.TronObject;
import item.lightgrenade.Explodable;
import item.teleporter.Teleportable;
import java.util.concurrent.atomic.AtomicInteger;
import com.sun.istack.internal.NotNull;

/**
 * an abstract implementation of the item interface. This class offers some
 * basic functionality for most Items (e.g. ID).
 * 
 * @author Bavo Mees
 */
public abstract class Item extends TronObject implements IItem {
	
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
	
	@Override
	public void addToEffect(Effect effect) {
		// Do nothing by default. This shouldn't be abstract though, some items
		// really don't have to do anything.
	}
	
	/**
	 * By default an item is not teleportable, therefore this method returns
	 * null. Subclasses implementing the teleportable interface should override 
	 * this method with the specification described {@link TronObject#asTeleportable() here}.
	 */
	@Override
	public Teleportable asTeleportable() {
		return null;
	}
	
	/**
	 * By default an item is not explodable, therefore this method returns null.
	 * Subclasses implementing the explodable interface should override 
	 * this method with the specifications described {@link TronObject#asExplodable() here}.
	 */
	@Override
	public Explodable asExplodable() {
		return null;
	}
	
	/**
	 * By default an item is not affected by a power failure, therefore this method returns
	 * null. Subclasses implementing the {@link AffectedByPowerFailure} interface should override
	 * this method with the specifications described {@link TronObject#asAffectedByPowerFailure() here}.
	 */
	@Override 
	public AffectedByPowerFailure asAffectedByPowerFailure() {
		return null;
	}
}
