package item.identitydisk;

import player.Player;
import square.TronObject;
import effects.AbstractEffect;

/**
 * This class represents the effect which an IdentityDisk has when it hits a
 * player.
 */
public class IdentityDiskEffect extends AbstractEffect {
	
	@Override
	public void execute(TronObject object) {
		if (object instanceof Player) {
			Player player = (Player) object;
			player.skipNextTurn();
		}
		
	}
}
