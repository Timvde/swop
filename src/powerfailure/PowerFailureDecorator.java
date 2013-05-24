package powerfailure;

import item.Effect;
import item.IItem;
import player.Player;
import square.AbstractSquare;
import square.AbstractSquareDecorator;
import square.PropertyType;

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
	public void addPlayer(Player player) {
		Effect effect = this.powerfailure.getEffect();
		super.addPlayer(player, effect);
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
		if (!(obj instanceof PowerFailureDecorator))
			return false;
		
		PowerFailureDecorator other = (PowerFailureDecorator) obj;
		
		if (!powerfailure.equals(other.powerfailure))
			return false;
		return true;
	}
	
	@Override
	public boolean hasProperty(PropertyType property) {
		return property == PropertyType.POWER_FAILURE ? true : square.hasProperty(property);
	}
}
