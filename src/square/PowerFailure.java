package square;

import item.Item;
import item.lightgrenade.LightGrenade;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a power failure. This is a state a square can be in
 * with the following properties: - If a Square is power failured, its
 * neighbours are, too -
 * 
 */
public class PowerFailure {
	
	private List<ASquare>	squares	= new ArrayList<ASquare>();
	
	private int				timeToLive;
	
	/**
	 * boolean representing whether this power failure has already increased the
	 * strength of an item. If this is the case, the power should not affect the
	 * object when the execute method is called.
	 */
	private boolean			modifiedAnItem;
	
	/**
	 * Create a power failure for a given square. This will also impact the
	 * neigbhours of the specified square.
	 * 
	 * @param square
	 *        The square that is impacted by this power failure.
	 */
	public PowerFailure(ASquare square) {
		squares = new ArrayList<ASquare>();
		
		if (square instanceof Square) {
			square.addPowerFailure(this);
			squares.add(square);
		}
		
		// add a power failure for the neighbours of the square
		for (Direction direction : Direction.values())
			if (square.getNeighbour(direction) != null) {
				if (square.getNeighbour(direction) instanceof Square) {
					square.getNeighbour(direction).addPowerFailure(this);
					squares.add(square.getNeighbour(direction));
				}
			}
		
		timeToLive = 3;
	}
	
	/**
	 * When a turn ends, a PowerFailure has to decrease the number of turns left
	 * it is power failured. When it is set to zero, the power failure will be
	 * released.
	 */
	public void decreaseTimeToLive() {
		if (timeToLive > 0)
			timeToLive--;
		// No else if, timeToLive could be 1 before and 0 now.
		if (timeToLive == 0)
			for (ASquare square : squares)
				square.removePowerFailure(this);
	}
	
	/**
	 * Modify an item so the effect of the item will be that of the original
	 * item accumulated with a power failure. If the item is not influenced by a
	 * power failure, nothing will happen.
	 * 
	 * @param item
	 *        the item of which the effect has to be increased
	 */
	public void modify(Item item) {
		// for now a power failure only increases the strength of a light
		// grenade
		if (item instanceof LightGrenade) {
			((LightGrenade) item).increaseStrength();
			// set the flag, so the power failure does not damage the item twice
			this.modifiedAnItem = true;
		}
	}
	
	/**
	 * inflict damage to an object by power failure. This will only inflict
	 * damage if the item can be {@link AffectedByPowerFailure damaged} by a
	 * power failure.
	 * 
	 * @param object
	 *        the object to be damaged by power failure
	 */
	public void execute(TronObject object) {
		// if this power failure has not yet affected the object by increasing
		// the power of an item and the object can be affected by a power
		// failure
		// damage the object with power failure
		if (!modifiedAnItem && null != object.asAffectedByPowerFailure())
			object.asAffectedByPowerFailure().damageByPowerFailure();
		
		// set the internal state of the item modified flag to false for the
		// next turn
		modifiedAnItem = false;
	}
}
