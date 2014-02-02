package square;

import item.IItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import player.Player;
import effects.Effect;

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
	
	private AbstractSquare							square;
	private HashMap<Direction, SquareContainer>		neighbours;
	private Map<Property, AbstractSquareDecorator>	decorators;
	private List<PropertyCreator>					propertyCreators;
	
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
		this.propertyCreators = new ArrayList<PropertyCreator>();
		
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
	 * Returns a set of the properties that affect this square.
	 * 
	 * @return the properties of this square
	 */
	public Set<Property> getProperties() {
		return new HashSet<Property>(decorators.keySet());
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
	 * Add a {@link PropertyCreator} to this square. Each turn, this square will
	 * ask the property creator to affect him.
	 * 
	 * @param creator
	 *        The property creator.
	 */
	public void addPropertyCreator(PropertyCreator creator) {
		this.propertyCreators.add(creator);
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
	
	@Override
	public IItem pickupItem(int ID) throws IllegalArgumentException {
		return square.pickupItem(ID);
	}
	
	@Override
	public List<IItem> getCarryableItems() {
		return square.getCarryableItems();
	}
	
	@Override
	public Player getPlayer() {
		return square.getPlayer();
	}
	
	@Override
	public boolean contains(Object object) {
		return square.contains(object);
	}
	
	@Override
	public boolean hasPlayer() {
		return square.hasPlayer();
	}
	
	@Override
	public void addItem(IItem item) {
		square.addItem(item);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((square == null) ? 0 : square.hashCode());
		return result;
	}
	
	@Override
	public void remove(Object object) {
		square.remove(object);
	}
	
	@Override
	public List<IItem> getAllItems() {
		return square.getAllItems();
	}
	
	@Override
	public boolean canAddItem(IItem item) {
		return square.canAddItem(item);
	}
	
	@Override
	public boolean canAddPlayer() {
		return square.canAddPlayer();
	}
	
	@Override
	public String toString() {
		return square.toString();
	}
	
	@Override
	protected void addPlayer(Player player, Effect effect) {
		square.addPlayer(player, effect);
	}
	
	@Override
	public boolean hasProperty(PropertyType property) {
		return square.hasProperty(property);
	}
	
	@Override
	protected void addItem(IItem item, Effect effect) {
		square.addItem(item, effect);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		square.update(o, arg);
		for (PropertyCreator creator : this.propertyCreators)
			if (arg == creator.getUpdateEvent())
				creator.affect(this);
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SquareContainer))
			return false;
		SquareContainer other = (SquareContainer) obj;
		if (square == null) {
			if (other.square != null)
				return false;
		}
		else if (!square.equals(other.square))
			return false;
		return true;
	}
	
	public Effect getStartTurnEffect(Effect effect) {
		return square.getStartTurnEffect(effect);
	}
	
	/**
	 * Returns a list with all the neigbours of this square.
	 * 
	 * @return a list with all the neigbours of this square.
	 */
	public List<SquareContainer> getAllNeighbours() {
		List<SquareContainer> result = new ArrayList<SquareContainer>();
		for (Direction dir : Direction.values()) {
			if (this.getNeighbourIn(dir) != null)
				result.add(this.getNeighbourIn(dir));
		}
		return result;
	}
	
}
