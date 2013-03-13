package item;

import player.IPlayer;

/**
 * A class to calculate the consequences of a Player entering a Square, which
 * can at the moment have an active light grenade, be in power failured state or
 * have both.
 */
public class Effect {
	
	private IPlayer	player;
	private boolean	hasLightGrenade	= false;
	private boolean	hasPowerFailure	= false;
	
	/**
	 * Initializing the Effect.
	 * 
	 * @param player
	 *        The player to receive the effect.
	 */
	public Effect(IPlayer player) {
		this.player = player;
	}
	
	/**
	 * Tell the Effect to take a light grenade into calculation.
	 */
	public void addLightGrenade() {
		this.hasLightGrenade = true;
	}
	
	/**
	 * Tell the Effect to take a power failure into calculation.
	 */
	public void addPowerFailure() {
		this.hasPowerFailure = true;
	}
	
	/**
	 * Calculate the resulting penalty for the player and execute it.
	 */
	public void execute() {
		if (!hasLightGrenade) {
			if (hasPowerFailure)
				player.endTurn();
		}
		else {
			// The square the player stepped on has a light grenade and should
			// always cause a decrease of at least three actions at this point.
			player.skipNumberOfActions(3 + (hasPowerFailure ? 1 : 0));
		}
	}
}
