package item;

import item.lightgrenade.Explodable;
import item.teleporter.Teleportable;
import java.util.concurrent.atomic.AtomicInteger;
import powerfailure.AffectedByPowerFailure;
import square.TronObject;

/**
 * an abstract implementation of the item interface. This class offers some
 * basic functionality for most Items (e.g. ID).
 * 
 * @author Bavo Mees
 */
public abstract class Item implements IItem {
	
	private int						id;
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
	
	/**
	 * By default an item is not teleportable, therefore this method returns
	 * null. Subclasses implementing the teleportable interface should override
	 * this method with the specification described
	 * {@link TronObject#asTeleportable() here}.
	 */
	@Override
	public Teleportable asTeleportable() {
		return null;
	}
	
	/**
	 * By default an item is not explodable, therefore this method returns null.
	 * Subclasses implementing the explodable interface should override this
	 * method with the specifications described
	 * {@link TronObject#asExplodable() here}.
	 */
	@Override
	public Explodable asExplodable() {
		return null;
	}
	
	/**
	 * By default an item is not affected by a power failure, therefore this
	 * method returns null. Subclasses implementing the
	 * {@link AffectedByPowerFailure} interface should override this method with
	 * the specifications described
	 * {@link TronObject#asAffectedByPowerFailure() here}.
	 */
	@Override
	public AffectedByPowerFailure asAffectedByPowerFailure() {
		return null;
	}
	
	/**
	 * Returns the effect this item has on an {@link TronObject object}.
	 * 
	 * @return the effect of the item
	 */
	public abstract Effect getEffect();
	
	@Override
	public char toChar() {
		return 'i';
	}
	
	/**
	 * By default an item does not require addition parameters from the user
	 */
	@Override
	public UseArguments<?> getUseArguments() {
		return null;
	}
}
