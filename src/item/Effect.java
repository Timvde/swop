package item;

import grid.PowerFailure;
import player.IPlayer;
import grid.ASquare;

/**
 * A class to calculate the consequences of a Player entering a Square, which
 * can at the moment have an active light grenade, be in power failured state or
 * have both.
 */
public class Effect {
	
	private IPlayer	player;
	private boolean	hasLightGrenade;
	private boolean	hasPowerFailure;
	private ASquare	destination;
	
	/**
	 * Initializing the Effect.
	 * 
	 * @param player
	 *        The player to receive the effect.
	 */
	public Effect(IPlayer player) {
		this.player = player;
		this.hasLightGrenade = false;
		this.hasPowerFailure = false;
	}
	
	/**
	 * Tell the Effect to take a light grenade into calculation.
	 */
	public void addLightGrenade() {
		this.hasLightGrenade = true;
	}
	
	/**
	 * Tell the Effect to take the specified power failure into calculation.
	 * 
	 * @param powerFailure
	 *        the power failure to be added
	 */
	public void addPowerFailure(PowerFailure powerFailure) {
		if (powerFailure != null)
			this.hasPowerFailure = true;
	}
	
	/**
	 * Tell the Effect to take the teleporter into calculation.
	 * 
	 * @param destination
	 *        the destination of the teleporter
	 */
	public void teleportTo(ASquare destination) {
		this.destination = destination;
	}
	
	/**
	 * Calculate the resulting penalty for the player and execute it.
	 * 
	 * @return True when the effect has ended the player's turn.
	 * @throws IllegalStateException
	 */
	public boolean execute() throws IllegalStateException {
		// teleport the player to the specified destination
		if (destination != null) {
			//teleport 
		}
		
		if (!hasLightGrenade) {
			if (hasPowerFailure) {
				player.endTurn();
				return true;
			}
		}
		else {
			// The square the player stepped on has a light grenade and should
			// always cause a decrease of at least three actions at this point.
			player.skipNumberOfActions(3 + (hasPowerFailure ? 1 : 0));
		}
		return false;
	}
}
