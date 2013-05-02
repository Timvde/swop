package square;

import item.IItem;
import java.util.List;
import player.IPlayer;

/**
 * Square container manages the decorators for square. Because the lifetime of
 * the a square object span almost the entire application time, a container is
 * used to manage the square reference. This allows the square to be decorated
 * by other objects. And thus modifying the properties of a square during the
 * game. Properties can be added by the {@link #addProperty(Porperty)} method.
 * 
 */
public class SquareContainer implements Square {
	
	private Square	square;
	
	/**
	 * Create a new Square container for a specfied square.
	 * 
	 * @param square
	 *        The square this container will hold
	 */
	public SquareContainer(Square square) {
		this.square = square;
	}
	
	public List<IItem> getCarryableItems() {
		return square.getCarryableItems();
	}
	
	public IPlayer getPlayer() {
		return square.getPlayer();
	}
	
	public boolean hasLightTrail() {
		return square.hasLightTrail();
	}
	
	public boolean hasPlayer() {
		return square.hasPlayer();
	}
	
	public boolean hasPowerFailure() {
		return square.hasPowerFailure();
	}
	
	/**
	 * Add a property to this square.
	 * 
	 * @param property
	 *        the property to add
	 */
	public void addProperty(Property property) {
		square = property.getDecorator(square);
	}
	
}
