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
	
	/** the object who will suffer from this effect */
	private TronObject		object;
	/** The list of items that will have an effect on the object */
	private List<Item>		items;
	/** The power failure that will effect the object */
	private PowerFailure	powerFailure;
	/** a boolean representing whether the effect has executed his effect */
	private boolean			executed;
	
	/**
	 * Initializing the Effect.
	 * 
	 * @param object
	 *        The object to receive the effect.
	 * 
	 * @throws IllegalArgumentException
	 *         The specified object must be valid, i.e.
	 *         {@link #isValidObject(TronObject)}.
	 */
	public Effect(TronObject object) throws IllegalArgumentException {
		if (!isValidObject(object))
			throw new IllegalArgumentException("The object was not valid!");
		
		// Initialize variables
		this.executed = false;
		this.object = object;
		items = new ArrayList<Item>();
		
	}
	
	/**
	 * Returns whether the specified object is a valid object for this effect.
	 * 
	 * @param object
	 *        the object to test
	 * @return true if the object is valid for this effect, else false
	 */
	public boolean isValidObject(TronObject object) {
		// test if the object exists
		if (object == null)
			return false;
		// i don't know what else can happen ...
		else
			return true;
	}
	
	/**
	 * return the object that will suffer from this effect (for testing purposes)
	 * 
	 * @return the object of this effect
	 */
	TronObject getObject() {
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
			throw new IllegalArgumentException("Item cannot be null!");
		items.add(item);
	}
	
	/**
	 * Calculate the resulting penalty for the player and execute it.
	 */
	public void execute() {
		if (executed)
			throw new IllegalStateException("The effect has already executed!");
		
		// first, check if the power failure increases any effects that are
		// present
		if (powerFailure != null)
			for (Item item : items)
				powerFailure.modify(item);
		
		// then, execute the effects of each item in the list
		for (Item item : items)
			item.execute(object);
		
		// lastly, let the power failure influence the object
		if (powerFailure != null)
			powerFailure.execute(object);
		
		// set the executed boolean to true
		this.executed = true;
	}
	
	/**
	 * Returns the list of items that will effect the {@link #getObject()
	 * object}.
	 * 
	 * @return list of items for this effect
	 */
	List<IItem> getItems() {
		return new ArrayList<IItem>(items);
	}
}
