package item;

import grid.ASquare;
import grid.PowerFailure;
import grid.TronObject;

/**
 * A class to calculate the consequences of a {@link TronObject} entering a
 * Square, which can for example have an active light grenade, be in power
 * failured state or have both.
 */
public class Effect {
	
	private TronObject	object;
	private boolean		hasLightGrenade;
	private boolean		hasPowerFailure;
	private ASquare		destination;
	
	/**
	 * Initializing the Effect.
	 * 
	 * @param object
	 *        The object to receive the effect.
	 */
	public Effect(TronObject object) {
		this.object = object;
		this.hasLightGrenade = false;
		this.hasPowerFailure = false;
	}
	
	/**
	 * return the object that will suffer from this effect
	 * 
	 * @return the object of this effect
	 */
	public TronObject getObject() {
		return object;
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
	public void addTeleportTo(ASquare destination) {
		this.destination = destination;
	}
	
	/**
	 * Calculate the resulting penalty for the player and execute it.
	 * 
	 * @return True when the effect has ended the player's turn.
	 * @throws IllegalStateException
	 */
	public boolean execute() throws IllegalStateException {
		// teleport the object to the specified destination
		if (destination != null && getObject().asTeleportable() != null) {
			getObject().asTeleportable().teleportTo(destination);
		}
		
		if (!hasLightGrenade) {
			if (hasPowerFailure) {
				getObject().asAffectedByPowerFailure().damageByPowerFailure();
				return true;
			}
		}
		else {
			// The square the player stepped on has a light grenade and should
			// always cause a decrease of at least three actions at this point.
			getObject().asExplodable().skipNumberOfActions(3 + (hasPowerFailure ? 1 : 0));
		}
		return false;
	}
}
