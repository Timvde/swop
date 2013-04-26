package item.teleporter;

import square.TronObject;
import effect.Effect;

/**
 * The effect a teleporter has on {@link TronObject}
 * 
 */
public class TeleporterEffect implements Effect {
	
	private Teleporter	teleporter;
	
	TeleporterEffect(Teleporter teleporter) {
		this.teleporter = teleporter;
	}
	
	@Override
	public void execute(TronObject object) {
		// If we can teleport, do it ...
		if (object.asTeleportable() != null) {
			if (teleporter.getSkipNextTeleport() == false) {
				teleporter.setSkipNextTeleport(true);
				object.asTeleportable().teleportTo(teleporter.getDestination().getSquare());
			}
			// Otherwise we should tell that we have skipped a teleportation
			else if (teleporter.getSkipNextTeleport() == true)
				teleporter.setSkipNextTeleport(false);
		}
	}
}
