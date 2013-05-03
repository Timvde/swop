package powerfailure;

import item.Effect;
import square.AbstractSquare;
import square.AbstractSquareDecorator;

/**
 * A decorator to modify a square with a power failure effect.
 */
public class PowerFailureDecorator extends AbstractSquareDecorator {
	
	private PowerFailure	powerfailure;
	
	/**
	 * Create a new power failure decorator for a specified square
	 * 
	 * @param square
	 *        the square who will be decorated
	 * @param powerfailure
	 *        the power failure that this decorator will simulate
	 */
	public PowerFailureDecorator(AbstractSquare square, PowerFailure powerfailure) {
		super(square);
		this.powerfailure = powerfailure;
	}
	
	@Override
	protected Effect effectHook() {
		Effect effect = this.powerfailure.getEffect();
		
		// Let other objects add an effect as well ...
		effect.addEffect(super.effectHook());
		
		return effect;
	}
	
}
