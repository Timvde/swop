package square;

import item.IItem;
import java.util.List;
import java.util.Observable;
import player.Player;
import effects.Effect;

/**
 * AbstractSquareDecorator is an abstract implementation of a square decorator.
 * This decorator wraps a square and forwards all the calls to that square. A
 * concrete implementation of a decorator should override any method whose
 * behavior needs to be changed.
 * 
 */
public abstract class AbstractSquareDecorator extends AbstractSquare {
	
	protected AbstractSquare	square;
	
	/**
	 * Create a new abstract square decorator
	 * 
	 * @param square
	 *        the square to decorate
	 */
	public AbstractSquareDecorator(AbstractSquare square) {
		if (square == null)
			throw new IllegalArgumentException("Null square found while creating a decorator");
		this.square = square;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		square.update(o, arg);
	}
	
	final AbstractSquare getSquare() {
		return square;
	}
	
	final void setSquare(AbstractSquare square) {
		if (square == null)
			throw new IllegalArgumentException();
		this.square = square;
	}
	
	public IItem pickupItem(int ID) throws IllegalArgumentException {
		return square.pickupItem(ID);
	}
	
	public List<IItem> getCarryableItems() {
		return square.getCarryableItems();
	}
	
	public Player getPlayer() {
		return square.getPlayer();
	}
	
	public boolean contains(Object object) {
		return square.contains(object);
	}
	
	public boolean hasPlayer() {
		return square.hasPlayer();
	}
	
	public void addPlayer(Player p) throws IllegalArgumentException {
		square.addPlayer(p);
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
	
	@Override
	public boolean equals(Object obj) {
		return square.equals(obj);
	}
	
	@Override
	public String toString() {
		return square.toString();
	}
	
	protected void addItem(IItem item, Effect effect) {
		square.addItem(item, effect);
	}
	
	protected void addPlayer(Player player, Effect effect) {
		square.addPlayer(player, effect);
	}
	
	public boolean hasProperty(PropertyType property) {
		return square.hasProperty(property);
	}
	
	@Override
	protected Effect getStartTurnEffect(Effect effect) {
		return square.getStartTurnEffect(effect);
	}
}
