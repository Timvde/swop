package powerfailure;

import item.Effect;
import item.IItem;
import square.AbstractSquare;
import square.AbstractSquareDecorator;
import player.IPlayer;

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
	public void addItem(IItem item) {
		Effect effect = this.powerfailure.getEffect();
		
		super.addItem(item, effect);
	}
	
	@Override 
	public void addPlayer(IPlayer player) {
		Effect effect = this.powerfailure.getEffect();
		super.addPlayer(player, effect);
	}

	@Override
	public boolean hasPowerFailure() {
		return true;
	}
	
	
	
}
