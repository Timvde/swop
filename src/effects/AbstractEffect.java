package effects;

import square.TronObject;

/**
 * This class provides a skeletal implementation of the {@link Effect} interface
 * to minimize the effort required to implement this interface.
 * 
 * <p>
 * To implement an effect, a programmer needs only to implement the
 * {@link #execute(square.TronObject)} method. At the end of this method, the
 * programmer MUST call the execute method of the next effect. This effect is
 * accessible as a protected field. <br>
 * 
 * </p>
 */
public abstract class AbstractEffect implements Effect {
	
	/**
	 * The next effect that should be called after the
	 * {@link #execute(square.TronObject)} method.
	 */
	protected Effect	next;
	
	@Override
	public void addEffect(Effect effect) {
		if (next == null)
			this.next = effect;
		else
			next.addEffect(effect);
	}
	
	@Override
	public final void execute(TronObject object) {
		preExecutionHook(object);
		if (next != null)
			next.execute(object);
	}
	
	/**
	 * This method allows subclasses to execute their function before the next
	 * effect in the chain is called.
	 * 
	 * @param object
	 */
	protected void preExecutionHook(TronObject object) {}
}
