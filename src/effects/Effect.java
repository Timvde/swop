package effects;

import square.TronObject;

/**
 * An effect which can be applied to a {@link TronObject}. If multiple effects
 * are to be executed on a single TronObject, they can be chained with the
 * {@link #addEffect(Effect)} method.
 * 
 * All implementing subclasses should obey the contract of the effect class.
 * Each effect is responsible for calling the {@link #execute(TronObject)}
 * method on the next effect in the chain of effects. Failing to do so, will
 * result in an incomplete combined effect.
 * 
 */
public interface Effect {
	
	/**
	 * Executes the effect on a given item. If the effect does not modify the
	 * specified object, it should be left in the same state.
	 * 
	 * This method guarantees the whole effect chain will be called.
	 * 
	 * @param object
	 *        the object on which the effect will be executed
	 */
	void execute(TronObject object);
	
	/**
	 * Add an effect to this effect. An effect can only hold one reference to an
	 * other effect. If this effect already holds a reference to an effect, it
	 * should pass on the specified effect.
	 * 
	 * @param effect
	 *        the effect to add
	 */
	void addEffect(Effect effect);
}
