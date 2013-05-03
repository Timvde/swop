package square;

import item.IItem;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import player.IPlayer;

/**
 * Square container manages the decorators for square. Because the lifetime of
 * the a square object span almost the entire application time, a container is
 * used to manage the square reference. This allows the square to be decorated
 * by other objects. And thus modifying the properties of a square during the
 * game. Properties can be added by the {@link #addProperty(Property)} method.
 * 
 */
public class SquareContainer extends AbstractSquare {
	
	private AbstractSquare						square;
	private LinkedList<AbstractSquare>			decorators;
	
	/**
	 * Create a new square container with specified neighbours, after this
	 * method the squares specified in the map will be set as the neighbours for
	 * this square. Also this square will be set as the neighbour for all the
	 * squares in the map. More formally, after the creation of this object the
	 * following will be true for each neighbour in a corresponding direction:
	 * 
	 * <pre>
	 * <code> 
	 *  this == getNeighbourInDirection(direction)
	 *  			.getNeighbourInDirection(direction.getOppositeDirection)
	 *  </code>
	 * </pre>
	 * 
	 * @param neighbours
	 *        the neighbours of this square
	 * @param square
	 *        the square this container will hold
	 */
	public SquareContainer(Map<Direction, AbstractSquare> neighbours, AbstractSquare square) {
		super(neighbours);
		this.square = square;
		this.decorators = new LinkedList<AbstractSquare>();
	}
	
	public void addProperty(Property property) {
		square = property.getDecorator(square);
	}
	
	public void removeProperty(Property property) {
		//TODO
	}
	
	/* ----------- Forwarding Methods ----------------- */
	
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
	
	public int hashCode() {
		return square.hashCode();
	}
	
	public void remove(Object object) {
		square.remove(object);
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
