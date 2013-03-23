package item.teleporter;

import item.Effect;
import item.Item;
import square.ASquare;
import square.Direction;
import square.TronObject;

/**
 * A teleporter can teleport objects from the square this teleporter is placed
 * on to an other square. This other square is constant during the game.
 * 
 * @author Bavo Mees
 */
public class Teleporter extends Item {
	
	private ASquare	destination;
	
	/**
	 * Create a new teleporter that can teleport objects to the specified
	 * destination.
	 * 
	 * @param destination
	 *        the destination of this teleporter
	 */
	public Teleporter(ASquare destination) {
		this.destination = destination;
	}
	
	@Override
	public void use(ASquare square, Direction direction) {
		throw new UnsupportedOperationException(
				"A teleporter cannot be pickup and thus be placed on an item by a player !!");
	}
	
	@Override
	public boolean isCarriable() {
		// a teleporter is not carriable
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
		if (null != object.asTeleportable())
			object.asTeleportable().teleportTo(destination);
	}

	@Override
	public void addToEffect(Effect effect) {
		effect.addItem(this);
	}
	
}
