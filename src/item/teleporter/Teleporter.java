package item.teleporter;

import item.Item;
import square.SquareContainer;
import effects.Effect;
import effects.EffectFactory;
import effects.TeleportationEffect;

/**
 * A teleporter can teleport objects from the square this teleporter is placed
 * on to an other square. This other square is constant during the game.
 * 
 * @author Bavo Mees
 */
public class Teleporter extends Item {
	
	/** The destination of the teleporter */
	private Teleporter		destination;
	/** The square where this teleporter is placed on */
	private SquareContainer	square;
	/**
	 * A boolean that indicates whether this teleporter should skip his next
	 * teleport. This prevents an infinite loop when we teleport to an other
	 * teleporter.
	 */
	private boolean			skipNextTeleport;
	
	/**
	 * Create a new teleporter that can teleport objects to the specified
	 * destination. The destination of the teleporter can be set as null, but
	 * then the method {@link #setDestination(Teleporter)} must be called before
	 * this teleporter is used. If this teleporter is used without setting a
	 * destination the behaviour of this object is unspecified.
	 * 
	 * @param destination
	 *        the destination of this teleporter
	 * @param square
	 *        the square where this teleporter is placed on
	 * @param effectFactory
	 *        The effect factory this item will use to get its needed effect.
	 */
	public Teleporter(Teleporter destination, SquareContainer square, EffectFactory effectFactory) {
		super(effectFactory);
		this.destination = destination;
		this.square = square;
	}
	
	@Override
	public void use(SquareContainer square) {
		throw new UnsupportedOperationException(
				"A teleporter cannot be pickup and thus be placed on an item by a player !!");
	}
	
	@Override
	public boolean isCarriable() {
		return false;
	}
	
	@Override
	public String toString() {
		return "Teleporter." + getId();
	}
	
	/**
	 * Set the destination of this teleporter to a specified destination. This
	 * operation can only be executed when the destination of the teleporter was
	 * left blank at initialization. This ensures that the destination of the
	 * teleporter is final during the lifetime of this teleporter.
	 * 
	 * @param destination
	 *        The destination for this teleporter
	 */
	public void setDestination(Teleporter destination) {
		if (this.destination != null)
			throw new IllegalStateException("The destination of a teleporter can only be set once!");
		
		this.destination = destination;
	}
	
	/**
	 * Returns the teleporter a TronObject will be teleported to when it passes
	 * through this teleporter.
	 * 
	 * @return The destination teleporter
	 */
	public Teleporter getDestination() {
		return destination;
	}
	
	/**
	 * Returns the square where this teleporter is placed on.
	 * 
	 * @return the current square
	 */
	public SquareContainer getSquare() {
		return square;
	}
	
	/**
	 * Returns whether or not the next teleport should be skipped.
	 * 
	 * (this is used in the {@link TeleportationEffect}.)
	 * 
	 * @return whether or not the next teleport should be skipped.
	 */
	public boolean getSkipNextTeleport() {
		return skipNextTeleport;
	}
	
	/**
	 * Sets whether or not the next teleport should be skipped.<br>
	 * 
	 * <br>
	 * <b>Do NOT use this method. This is used by the
	 * {@link TeleportationEffect}!</b>
	 * 
	 * @param skipNextTurn
	 *        the value to set
	 */
	public void setSkipNextTeleport(boolean skipNextTurn) {
		this.skipNextTeleport = skipNextTurn;
	}
	
	@Override
	public char toChar() {
		return 't';
	}
	
	@Override
	public Effect getEffect() {
		return effectFactory.getTeleportationEffect(this);
	}
}
