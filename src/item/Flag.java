package item;

import game.CTFMode;
import square.AbstractSquare;
import square.SquareContainer;

/**
 * A Flag-item for the {@link CTFMode Capture the flag game}.
 */
public class Flag extends Item {
	
	private AbstractSquare	home;
	private int ownerID;
	
	/**
	 * Creates a new Flag with a specified home.
	 * 
	 * @param homeSquare
	 *        The homeSquare of this flag.
	 * @param ownerID
	 * 			The ID of the player whose flag this is.
	 */
	public Flag(AbstractSquare homeSquare, int ownerID) {
		this.home = homeSquare;
		this.ownerID = ownerID;
	}
	
	/**
	 * Uses this flag, i.e. teleport it back to its home square.
	 */
	@Override
	public void use(SquareContainer square, UseArguments<?> arguments) {
			this.teleportBack();
	}
	
	/**
	 * Sends the flag back to its home square.
	 */
	public void sendHome() {
		this.teleportBack();
	}
	
	@Override
	public boolean isCarriable() {
		return true;
	}
	
	@Override
	public String toString() {
		return "Flag " + ownerID;
	}
	
	private void teleportBack() {
		this.home.addItem(this);
	}
	
	/**
	 * Return the ID of the player whose flag this is. Use this to identify different flags.
	 * @return The owner ID.
	 */
	public int getOwnerID() {
		return this.ownerID;
	}
	
}
