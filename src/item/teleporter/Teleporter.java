package item.teleporter;

import item.Effect;
import item.Item;
import square.ASquare;
import square.TronObject;

/**
 * A teleporter can teleport objects from the square this teleporter is placed
 * on to an other square. This other square is constant during the game.
 * 
 * @author Bavo Mees
 */
public class Teleporter extends Item {
	
	/** The destination of the teleporter */
	private Teleporter	destination;
	/** The square where this teleporter is placed on */
	private ASquare		square;
	/**
	 * A boolean that indicates whether this teleporter should skip his next
	 * teleport. This prevents an infinite loop when we teleport to an other
	 * teleporter.
	 */
	private boolean		skipNextTeleport;
	
	/**
	 * Create a new teleporter that can teleport objects to the specified
	 * destination. The destination of the teleporter can be set as null, but
	 * then the method {@link #setDestination(Teleporter)} must be called before
	 * this teleporter is used. If this teleporter is used without setting a
	 * destination the behaviour of this object is unspecified.
	 * 
	 * @param destination
	 *        the destination of this teleporter
	 * @param square
	 *        the square where this teleporter is placed on
	 */
	public Teleporter(Teleporter destination, ASquare square) {
		this.destination = destination;
		this.square = square;
	}
	
	@Override
	public void use(ASquare square) {
		throw new UnsupportedOperationException(
				"A teleporter cannot be pickup and thus be placed on an item by a player !!");
	}
	
	@Override
	public boolean isCarriable() {
		return false;
	}
	
	@Override
	public String toString() {
		return "Teleporter." + getId();
	}
	
	/**
	 * Teleport the specified object to the destination square. If the object
	 * cannot be {@link Teleportable teleported}, nothing will happen.
	 * 
	 * @param object
	 *        the object that will be teleported
	 */
	public void execute(TronObject object) {
		// If we can teleport, do it ...
		if (null != object.asTeleportable() && skipNextTeleport == false) {
			destination.skipNextTeleport = true;
			object.asTeleportable().teleportTo(destination.square);
		}
		// Otherwise we should tell that we have skipped a teleportation
		else if (null != object.asTeleportable() && skipNextTeleport == true)
			this.skipNextTeleport = false;
	}
	
	@Override
	public void addToEffect(Effect effect) {
		effect.addItem(this);
	}
	
	/**
	 * Set the destination of this teleporter to a specified destination. This
	 * operation can only be executed when the destination of the teleporter was
	 * left blank at initialization. This ensures that the destination of the
	 * teleporter is final during the lifetime of this teleporter.
	 * 
	 * @param destination
	 *        The destination for this teleporter
	 */
	public void setDestination(Teleporter destination) {
		if (this.destination != null)
			throw new IllegalStateException("The destination of a teleporter can only be set once!");
		
		this.destination = destination;
	}
	
	/**
	 * Returns the teleporter a TronObject will be teleported to when it passes
	 * through this teleporter.
	 * 
	 * @return The destination teleporter
	 */
	public Teleporter getDestination() {
		return destination;
	}
	
	@Override
	public char toChar() {
		return 't';
	}
}
