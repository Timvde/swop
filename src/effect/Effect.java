package effect;

import square.TronObject;

/**
 * An effect ... TODO
 * 
 */
public interface Effect {
	
	/**
	 * Executes the effect on a given item. If the effect does not modify the
	 * specified object, it should be left in the same state.
	 * 
	 * @param object
	 */
	void execute(TronObject object);
}
