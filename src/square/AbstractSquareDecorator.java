package square;

import item.IItem;
import java.util.List;
import player.IPlayer;

/**
 * AbstractSquareDecorator is an abstract implementation of a square decorator.
 * This decorator wraps a square and forwards all the calls to that square. A
 * concrete implementation of a decorator should override any method whose
 * behavior needs to be changed.
 * 
 */
public abstract class AbstractSquareDecorator extends AbstractSquare {
	
	private AbstractSquare	square;
	
	/**
	 * Create a new abstract square decorator
	 * 
	 * @param square
	 *        the square to decorate
	 */
	public AbstractSquareDecorator(AbstractSquare square) {
		this.square = square;
	}
	
	final AbstractSquare getSquare() {
		return square;
	}
	
	public IItem pickupItem(int ID) throws IllegalArgumentException {
		return square.pickupItem(ID);
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

	public boolean contains(Object object) {
		return square.contains(object);
	}

	public boolean hasPlayer() {
		return square.hasPlayer();
	}

	public void addPlayer(IPlayer p) throws IllegalArgumentException {
		square.addPlayer(p);
	}

	public boolean hasPowerFailure() {
		return square.hasPowerFailure();
	}

	public void placeLightTrail() {
		square.placeLightTrail();
	}

	public void removeLightTrail() {
		square.removeLightTrail();
	}

	public void addItem(IItem item) throws IllegalArgumentException {
		square.addItem(item);
	}

	public void remove(Object object) {
		square.remove(object);
	}

	public int hashCode() {
		return square.hashCode();
	}

	public List<IItem> getAllItems() {
		return square.getAllItems();
	}

	public boolean canBeAdded(IItem item) {
		return square.canBeAdded(item);
	}

	public boolean canAddPlayer() {
		return square.canAddPlayer();
	}

	public boolean equals(Object obj) {
		return square.equals(obj);
	}

	public String toString() {
		return square.toString();
	}	
}