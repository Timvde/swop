package square;

import item.Effect;
import item.IItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import player.IPlayer;
import powerfailure.PrimaryPowerFailure;

/**
 * Square container manages the decorators for square. Because the lifetime of
 * the a square object span almost the entire application time, a container is
 * used to manage the square reference. This allows the square to be decorated
 * by other objects. And thus modifying the properties of a square during the
 * game. Properties can be added by the {@link #addProperty(Property)} method.
 * 
 */
public class SquareContainer extends AbstractSquare {
	
	private static final boolean					ENABLE_POWER_FAILURE	= false;
	private static final float						POWER_FAILURE_CHANCE	= 0;
	private AbstractSquare							square;
	private HashMap<Direction, SquareContainer>		neighbours;
	private Map<Property, AbstractSquareDecorator>	decorators;
	
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
	public SquareContainer(Map<Direction, SquareContainer> neighbours, AbstractSquare square) {
		
		this.square = square;
		this.decorators = new HashMap<Property, AbstractSquareDecorator>();
		
		// check if the parameter is valid
		if (!canHaveAsNeighbours(neighbours))
			throw new IllegalArgumentException(
					"the specified neighbours could not be set as the neighbours for this square!");
		
		this.neighbours = new HashMap<Direction, SquareContainer>(neighbours);
		
		// set this square as the neighbour
		for (Direction direction : neighbours.keySet())
			neighbours.get(direction).neighbours.put(direction.getOppositeDirection(), this);
	}
	
	/**
	 * Returns whether the specified map can be set as the map of neighbours for
	 * this square. More formally this method returns false if and only if
	 * <code>neighbours == null</code>
	 * 
	 * @param neighbours
	 *        the neighbours to be tested
	 * @return true if the neighours can be set for this square, else false
	 */
	private boolean canHaveAsNeighbours(Map<Direction, SquareContainer> neighbours) {
		if (neighbours == null)
			return false;
		return true;
	}
	
	/**
	 * Returns the neighbour in a specified direction or null if the square has
	 * no mapping for the neighbour in the specified direction.
	 * 
	 * @param direction
	 *        the direction of the neighbour
	 * @return the neighbour in the specified direction
	 */
	public SquareContainer getNeighbourIn(Direction direction) {
		return neighbours.get(direction);
	}
	
	/**
	 * Add a new specified property to this square.
	 * 
	 * @param property
	 *        the property to add
	 */
	public void addProperty(Property property) {
		AbstractSquareDecorator decorator = property.getDecorator(square);
		decorators.put(property, decorator);
		this.square = decorator;
	}
	
	/**
	 * Remove a specified property from this square. After this method the
	 * decorator returned by the {@link Property#getDecorator(AbstractSquare)}
	 * when adding this property to the square, will be removed.
	 * 
	 * @param property
	 *        the property to remove
	 */
	public void removeProperty(Property property) {
		if (!decorators.containsKey(property))
			return; // container does not include the property,
					// the post condition is satisfied
			
		AbstractSquareDecorator squareToRemove = decorators.get(property);
		
		// Special case if the decorator is the outer layer
		if (squareToRemove.equals(square)) {
			square = squareToRemove.getSquare();
			decorators.remove(property);
			return;
		}
		
		AbstractSquareDecorator decorator = (AbstractSquareDecorator) ((AbstractSquareDecorator) square)
				.getSquare();
		AbstractSquareDecorator previousDecorator = (AbstractSquareDecorator) square;
		
		// find the wrapper and it's previous wrapper
		while (!squareToRemove.equals(decorator)) {
			previousDecorator = decorator;
			decorator = (AbstractSquareDecorator) ((AbstractSquareDecorator) decorator).getSquare();
		}
		
		previousDecorator.setSquare(decorator.getSquare());
		decorators.remove(property);
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
	
	@Override
	protected void addPlayer(IPlayer player, Effect effect) {
		square.addPlayer(player, effect);
	}
	
	@Override
	protected void addItem(IItem item, Effect effect) {
		square.addItem(item, effect);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		square.update(o, arg);
		this.updatePowerFailure();
	}
	
	/**
	 * This method updates all power failure related things.
	 * 
	 * <pre>
	 * - This Square has a certain chance of creating a new power failure
	 * </pre>
	 */
	public void updatePowerFailure() {
		if (ENABLE_POWER_FAILURE) {
			Random rand = new Random();
			if (rand.nextFloat() < POWER_FAILURE_CHANCE)
				new PrimaryPowerFailure(this);
		}
	}
	
	/**
	 * Get the direction in which a specific neighbour square is located.
	 * 
	 * @param neighbour
	 *        The neighbour square of which we want the direction to.
	 * @return Returns the direction where a given neighbour is located. Returns
	 *         null if the given square is not a neighbour.
	 */
	public Direction getDirectionOfNeighbour(SquareContainer neighbour) {
		for (Direction dir : Direction.values()) {
			if (getNeighbourIn(dir) == neighbour) {
				return dir;
			}
		}
		
		return null;
	}
	
	@Override
	public boolean isWall() {
		return square.isWall();
	}
	
	@Override
	public boolean isStartingPosition() {
		return square.isStartingPosition();
	}
	
}
