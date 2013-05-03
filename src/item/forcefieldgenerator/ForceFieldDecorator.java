package item.forcefieldgenerator;

import item.IItem;
import item.identitydisk.IdentityDisk;
import java.util.Observable;
import player.IPlayer;
import square.AbstractSquare;
import square.AbstractSquareDecorator;

/**
 * A force field decorator modifies the square to simulate a square that is
 * covered by a force field
 */
public class ForceFieldDecorator extends AbstractSquareDecorator {
	
	private ForceField	forceField;
	
	/**
	 * Create a new force field decorator on a specified square
	 * 
	 * @param square
	 *        the square that will be modified
	 * @param forceField
	 *        the force field that this decorator will simulate
	 */
	public ForceFieldDecorator(AbstractSquare square, ForceField forceField) {
		super(square);
		this.forceField = forceField;
	}
	
	@Override
	public void remove(Object object) {
		if (forceField.getState() == ForceFieldState.ACTIVE && object instanceof IPlayer)
			throw new IllegalStateException("a player cannot move when there is a forcefield");
		
		super.remove(object);
	}
	
	@Override
	public void addPlayer(IPlayer p) throws IllegalArgumentException {
		if (forceField.getState() == ForceFieldState.ACTIVE)
			throw new IllegalArgumentException(" a player cannot move onto a force field");
		else 
			super.addPlayer(p);
	}
	
	@Override
	public void addItem(IItem item) throws IllegalArgumentException {
		if (forceField.getState() == ForceFieldState.ACTIVE && item instanceof IdentityDisk)
			; // Destroy the disc 
		else 
			super.addItem(item);
	}
	
	@Override
	public boolean hasForceField() {
		if (forceField.getState() == ForceFieldState.ACTIVE)
			return true;
		else 
			return super.hasForceField();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((forceField == null) ? 0 : forceField.hashCode());
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
		ForceFieldDecorator other = (ForceFieldDecorator) obj;
		if (forceField == null) {
			if (other.forceField != null)
				return false;
		}
		else if (!forceField.equals(other.forceField))
			return false;
		return true;
	}

	@Override
	public void update(Observable o, Object arg) {
		square.update(o, arg);
		forceField.update();
		
		super.update(o, arg);
	}
	
}
