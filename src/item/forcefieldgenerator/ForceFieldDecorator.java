package item.forcefieldgenerator;

import item.IItem;
import item.identitydisk.IdentityDisk;
import java.util.Observable;
import player.Player;
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
		if (forceField == null)
			throw new IllegalArgumentException(
					"A ForceFieldDecorator without a ForceField is like a car with square wheels."
							+ "Sure, we could create it, but no-one wants it, so let's not do that.");
		this.forceField = forceField;
	}
	
	@Override
	public void remove(Object object) {
		if (forceField.getState() == ForceFieldState.ACTIVE && object instanceof Player)
			throw new IllegalStateException("a player cannot move when there is a forcefield");
		
		super.remove(object);
	}
	
	@Override
	public void addPlayer(Player p) {
		if (forceField.getState() == ForceFieldState.ACTIVE)
			throw new IllegalStateException(" a player cannot move onto a force field");
		
		super.addPlayer(p);
	}
	
	@Override
	public void addItem(IItem item) {
		/*
		 * Identity Disks have to be destroyed by a Force Field, so we don't
		 * want to add them anymore
		 */
		if (forceField.getState() == ForceFieldState.INACTIVE || !(item instanceof IdentityDisk))
			super.addItem(item);
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
		if (!(obj instanceof ForceFieldDecorator))
			return false;
		
		ForceFieldDecorator other = (ForceFieldDecorator) obj;
		
		return forceField.equals(other.forceField);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		square.update(o, arg);
		forceField.update();
		
		super.update(o, arg);
	}
	
}
