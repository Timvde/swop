package item.identitydisk;

import item.AbstractEffect;
import player.Player;
import square.TronObject;

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
