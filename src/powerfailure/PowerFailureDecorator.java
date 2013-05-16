package powerfailure;

import java.util.Observable;
import item.Effect;
import item.IItem;
import square.AbstractSquare;
import square.AbstractSquareDecorator;
import player.IPlayer;
import player.TurnEvent;

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
		if (powerfailure == null) 
			throw new IllegalArgumentException("powerfailure cannot be null");
		
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
	
	@Override 
	public void update(Observable o, Object arg) {
		square.update(o, arg);
		
		if (arg instanceof TurnEvent) {
			switch ((TurnEvent) arg) {
				case END_ACTION:
					if (hasPowerFailure()) {
						powerfailure.updateStatus(TurnEvent.END_ACTION);
					}
					break;
				case END_TURN:
					if (hasPowerFailure()) {
						powerfailure.updateStatus(TurnEvent.END_TURN);
					}
					break;
				default:
					break;
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((powerfailure == null) ? 0 : powerfailure.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PowerFailureDecorator other = (PowerFailureDecorator) obj;
		if (powerfailure == null) {
			if (other.powerfailure != null)
				return false;
		}
		else if (!powerfailure.equals(other.powerfailure))
			return false;
		return true;
	}
}
