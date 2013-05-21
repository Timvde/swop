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
 * Square container manages the decorators for square. Because the lifetime of a
 * square object spans almost the entire application time, a container is used
 * to manage the square reference. This allows the square to be decorated by
 * other objects. And thus modifying the properties of a square during the game.
 * Properties can be added and removed by the {@link #addProperty(Property)} en
 * {@link #removeProperty(Property)} methods.
 * 
 */
public class SquareContainer extends AbstractSquare {
	
	private static final boolean					ENABLE_POWER_FAILURE	= true;
	private static final float						POWER_FAILURE_CHANCE	= 0.01F;
	private AbstractSquare							square;
	private HashMap<Direction, SquareContainer>		neighbours;
	private Map<Property, AbstractSquareDecorator>	decorators;
	
	/**
	 * Create a new square container with specified neighbours, after this
	 * method the squares specified in the map will be set as the neighbours for
	 * this square. Also this square will be set as the neighbour for all the
	 * squares in the map. More formally, after the creation of this object the
	 * following will be true for each direction where a neighbour can be found:
	 * 
	 * <pre>
	 * <code> 
	 *  this == getNeighbourInDirection(direction)
	 *  			.getNeighbourInDirection(direction.getOppositeDirection())
	 *  </code>
	 * </pre>
	 * 
	 * @param neighbours
	 *        the neighbours of this square
	 * @param square
	 *        the square this container will hold
	 */
	public SquareContainer(Map<Direction, SquareContainer> neighbours, AbstractSquare square) {
		if (square == null)
			throw new IllegalArgumentException("the given square cannot be null");
		if (!canHaveAsNeighbours(neighbours))
			throw new IllegalArgumentException(
					"the specified neighbours could not be set as the neighbours for this square!");
		
		this.square = square;
		this.decorators = new HashMap<Property, AbstractSquareDecorator>();
		this.neighbours = new HashMap<Direction, SquareContainer>(neighbours);
		
		// Make sure the link is bidirectional
		for (Direction direction : neighbours.keySet())
			neighbours.get(direction).setNeighbourInDirection(direction.getOppositeDirection(),
					this);
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
	 * Set a neighbour in a specified direction
	 * 
	 * @param direction
	 *        The direction of the neighbour
	 * @param square
	 *        The new neighbour
	 */
	private void setNeighbourInDirection(Direction direction, SquareContainer square) {
		if (direction == null || square == null)
			throw new IllegalArgumentException("cannot set a null square or a null direction");
		
		this.neighbours.put(direction, square);
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
		/*
		 * The container does not have a decorator of the specified property, so
		 * the post condition is already satisfied.
		 */
		if (!decorators.containsKey(property))
			return;
		
		AbstractSquareDecorator decoratorToRemove = decorators.get(property);
		
		/*
		 * If it is the outer layer, we should adapt the reference of the
		 * container itself, and not iterate the list of decorators.
		 */
		if (decoratorToRemove.equals(square)) {
			square = decoratorToRemove.getSquare();
			decorators.remove(property);
		}
		else {
			AbstractSquareDecorator decorator = (AbstractSquareDecorator) ((AbstractSquareDecorator) square)
					.getSquare();
			AbstractSquareDecorator previousDecorator = (AbstractSquareDecorator) square;
			
			// find the wrapper and its previous wrapper
			while (!decoratorToRemove.equals(decorator)) {
				previousDecorator = decorator;
				decorator = (AbstractSquareDecorator) ((AbstractSquareDecorator) decorator)
						.getSquare();
			}
			
			previousDecorator.setSquare(decorator.getSquare());
			decorators.remove(property);
		}
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
	
	@Override
	public boolean hasForceField() {
		return square.hasForceField();
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
	 * This method has a chance of {@value #POWER_FAILURE_CHANCE} to initiate a
	 * new power failure on this square.
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
		for (Direction dir : Direction.values())
			if (getNeighbourIn(dir) == neighbour)
				return dir;
		
		return null;
	}
	
	@Override
	public boolean isWall() {
		return square.isWall();
	}
	
	@Override
	public int getStartingPosition() {
		return square.getStartingPosition();
	}
	
}
