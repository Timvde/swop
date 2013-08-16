package effects;

import ObjectronExceptions.IllegalTeleportException;
import item.teleporter.Teleporter;
import player.TronPlayer;
import square.AbstractSquare;
import square.SquareContainer;
import square.TronObject;

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
	public void execute(TronObject object) {
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
		
		super.execute(object);
	}
	
	private void teleport(TronObject object, SquareContainer destinationSquare) {
		if (destinationSquare == null)
			throw new IllegalArgumentException("Cannot teleport to null!");
		if (teleporter.getSquare() == null)
			throw new IllegalStateException("Teleporter is not placed on a square");
		if (!destinationSquare.canBeAdded(object)) {
			if (object instanceof TronPlayer) {
				throw new IllegalTeleportException(
						"The player can't be be teleported to the destination.");
			}
			else {
				throw new IllegalStateException(
						"This object can't be teleported to the destination square");
			}
		}
		
		AbstractSquare currentSquare = teleporter.getSquare();
		
		currentSquare.remove(object);
		destinationSquare.addTronObject(object);
		object.asTeleportable().setSquare(destinationSquare);
	}
}
