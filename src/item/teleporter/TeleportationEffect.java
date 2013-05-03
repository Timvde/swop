package item.teleporter;

import item.AbstractEffect;
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
				object.asTeleportable().teleportTo(teleporter.getDestination().getSquare());
			}
			// Otherwise we should tell that we have skipped a teleportation
			else if (teleporter.getSkipNextTeleport() == true)
				teleporter.setSkipNextTeleport(false);
		}
		
		super.execute(object);
	}
}
