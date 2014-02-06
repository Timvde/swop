package effects;

import item.teleporter.Teleporter;
import square.AbstractSquare;
import square.SquareContainer;
import square.TronObject;
import ObjectronExceptions.IllegalTeleportException;

/**
 * A Teleportation effect
 * 
 */
public class TeleportationEffect extends AbstractEffect {
	
	private Teleporter	teleporter;
	
	TeleportationEffect(Teleporter teleporter) {
		this.teleporter = teleporter;
	}
	
	@Override
	public void preExecutionHook(TronObject object) {
		// If we can teleport, do it ...
		if (object.asTeleportable() != null) {
			if (teleporter.getSkipNextTeleport() == false) {
				teleporter.getDestination().setSkipNextTeleport(true);
				this.teleport(object, teleporter.getDestination().getSquare());
			}
			// Otherwise we should tell that we have skipped a teleportation
			else if (teleporter.getSkipNextTeleport() == true)
				teleporter.setSkipNextTeleport(false);
		}
	}
	
	private void teleport(TronObject object, SquareContainer destinationSquare) {
		if (destinationSquare == null)
			throw new IllegalArgumentException("Cannot teleport to null!");
		if (teleporter.getSquare() == null)
			throw new IllegalStateException("Teleporter is not placed on a square");
		if (!destinationSquare.canBeAdded(object)) {
				throw new IllegalTeleportException(
					"This TronObject can't be be teleported to the destination.");
		}
		
		AbstractSquare currentSquare = teleporter.getSquare();
		
		currentSquare.remove(object);
		destinationSquare.addTronObject(object);
		object.asTeleportable().setSquare(destinationSquare);
	}
}
