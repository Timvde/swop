package square;

import item.IItem;
import java.util.List;
import player.IPlayer;

/**
 * AbstractSquareDecorator is an abstract implementation of a square decorator. This decorator 
 * wraps a square and forwards all the calls to that square. A concrete implementation
 * of a decorator should override any method whose behavior needs to be changed. 
 * 
 */
public abstract class AbstractSquareDecorator implements Square {
	
	private Square	square;
	
	/**
	 * Create a new abstract square decorator
	 * 
	 * @param square
	 *        the square to decorate
	 */
	public AbstractSquareDecorator(Square square) {
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
}
