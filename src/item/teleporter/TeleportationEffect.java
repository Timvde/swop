package item.teleporter;

import item.AbstractEffect;
import item.IItem;
import player.IPlayer;
import square.TronObject;
import square.AbstractSquare;

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

	private void teleport(TronObject object, AbstractSquare destinationSquare) {
		if (destinationSquare == null)
			throw new IllegalArgumentException("Cannot teleport to null!");
		if (teleporter.getSquare() == null)
			throw new IllegalStateException("Teleporter is not placed on a squaer");
		
		AbstractSquare currentSquare  = teleporter.getSquare();
		
		currentSquare.remove(this);
		
		if (object instanceof IItem)
			destinationSquare.addItem((IItem) this);
		else if (object instanceof IPlayer)
			destinationSquare.addPlayer((IPlayer) object);
	}
}
