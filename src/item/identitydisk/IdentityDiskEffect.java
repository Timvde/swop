package item.identitydisk;

import item.AbstractEffect;
import player.TronPlayer;
import square.TronObject;

/**
 * This class represents the effect which an IdentityDisk has when it hits a
 * player.
 */
public class IdentityDiskEffect extends AbstractEffect {
	
	@Override
	public void execute(TronObject object) {
		if (object instanceof TronPlayer) {
			TronPlayer player = (TronPlayer) object;
			player.skipNextTurn();
		}
		
	}
}
