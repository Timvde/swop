package item.identitydisk;

import player.TronPlayer;
import square.TronObject;
import effects.AbstractEffect;

/**
 * This class represents the effect which an IdentityDisk has when it hits a
 * player.
 */
public class IdentityDiskEffect extends AbstractEffect {
	
	@Override
	public void preExecutionHook(TronObject object) {
		if (object instanceof TronPlayer) {
			TronPlayer player = (TronPlayer) object;
			player.skipNextTurn();
		}
	}
}
