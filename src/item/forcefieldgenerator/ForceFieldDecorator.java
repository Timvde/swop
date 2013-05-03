package item.forcefieldgenerator;

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
	
	/**
	 * Create a new force field decorator on a specified square
	 * 
	 * @param square
	 *        the square that will be modified
	 */
	public ForceFieldDecorator(AbstractSquare square) {
		super(square);
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
		if (item instanceof IdentityDisk)
			; // do nothing
		super.addItem(item);
	}
	
}
