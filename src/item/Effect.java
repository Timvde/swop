package item;

import java.util.ArrayList;
import java.util.List;
import square.PowerFailure;
import square.TronObject;

/**
 * A class to calculate the consequences of a {@link TronObject} entering a
 * Square, which can for example have an active light grenade, be in power
 * failured state or have both.
 */
public class Effect {
	
	private TronObject		object;
	private List<Item>		items;
	private PowerFailure	powerFailure;
	
	/**
	 * Initializing the Effect.
	 * 
	 * @param object
	 *        The object to receive the effect.
	 */
	public Effect(TronObject object) {
		this.object = object;
		items = new ArrayList<Item>();
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
	 * Tell the Effect to take the specified power failure into calculation.
	 * 
	 * @param powerFailure
	 *        the power failure to be added
	 */
	public void addPowerFailure(PowerFailure powerFailure) {
		if (powerFailure == null)
			throw new IllegalArgumentException("power failure cannot be null!");
		this.powerFailure = powerFailure;
	}
	
	/**
	 * Tell the Effect to take the specified item into calculation.
	 * 
	 * @param item
	 *        the item that has an effect on the object
	 */
	public void addItem(Item item) {
		if (item == null) 
			throw new IllegalArgumentException("teleporter cannot be null!");
		items.add(item);
	}
	
	/**
	 * Calculate the resulting penalty for the player and execute it.
	 */
	public void execute() {
		// first, check if the power failure increases any effects that are present
		if (powerFailure != null)
			for (Item item : items) 
				powerFailure.modify(item);
		// then, execute the effects of each item in the list
		for (Item item : items)
			item.execute(object);
		// lastly, let the powerfailure influence the object
		if (powerFailure != null)
			powerFailure.execute(object);
	}
}
