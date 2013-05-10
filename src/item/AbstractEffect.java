package item;

import square.TronObject;

/**
 * This class provides a skeletal implementation of the {@link Effect} interface
 * to minimize the effort required to implement this interface.
 * 
 * <p>
 * To implement an effect, a programmer needs only to implement the
 * {@link #execute(square.TronObject)} method. At the end of this method, the
 * programmer MUST call the execute method of the next effect. This effect is
 * accessible as a protected {@link #next field}.
 * </p>
 */
public abstract class AbstractEffect implements Effect {
	
	/**
	 * The next effect that should be called after the
	 * {@link #execute(square.TronObject)} method.
	 */
	private Effect	next;
	
	@Override
	public void addEffect(Effect effect) {
		if (next == null)
			this.next = effect;
		else
			next.addEffect(effect);
	}
	
	@Override
	public void execute(TronObject object) {
		if (next != null)
			next.execute(object);
	}
}
