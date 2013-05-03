package properties.forcefield;

import java.util.Observable;
import item.IItem;
import item.identitydisk.IdentityDisk;
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
	 *        The force field that initiated this decorator
	 */
	public ForceFieldDecorator(AbstractSquare square, ForceField forceField) {
		super(square);
		this.forceField = forceField;
	}
	
	@Override
	public void remove(Object object) {
		if (object instanceof IPlayer)
			throw new IllegalStateException("a player cannot move when there is a forcefield");
		
		super.remove(object);
	}
	
	@Override
	public void addPlayer(IPlayer p) throws IllegalArgumentException {
		throw new IllegalStateException(" a player cannot move onto a force field");
	}
	
	@Override
	public void addItem(IItem item) throws IllegalArgumentException {
		/*
		 * Identity Disks have to be destroyed by a Force Field, so we don't
		 * want to add them
		 */
		if (forceField.getState() == ForceFieldState.INACTIVE || !(item instanceof IdentityDisk))
			super.addItem(item);
	}

	@Override
	public void update(Observable o, Object arg) {
		square.update(o, arg);
	}
	
}
