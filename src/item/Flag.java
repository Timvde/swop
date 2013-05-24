package item;

import game.CTFMode;
import square.AbstractSquare;
import square.PropertyType;
import square.SquareContainer;
import square.StartingPositionProperty;

/**
 * A Flag-item for the {@link CTFMode Capture the flag game}.
 */
public class Flag extends Item {
	
	private AbstractSquare	home;
	
	/**
	 * Creates a new Flag with a specified home.
	 * 
	 * @param homeSquare
	 *        The homeSquare of this flag.
	 */
	public Flag(AbstractSquare homeSquare) {
		this.home = homeSquare;
	}
	
	/**
	 * Uses this flag, i.e. teleport it back to its home square.
	 */
	@Override
	public void use(SquareContainer square, UseArguments<?> arguments) {
		if (square.hasProperty(PropertyType.STARTING_POSITION))
			this.teleportBack();
	}
	
	@Override
	public boolean isCarriable() {
		return true;
	}
	
	@Override
	public String toString() {
		return "Flag " + home;
	}
	
	private void teleportBack() {
		this.home.addItem(this);
	}
	
}
