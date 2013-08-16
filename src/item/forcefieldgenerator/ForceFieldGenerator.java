package item.forcefieldgenerator;

import item.IItem;
import item.Item;
import item.UseArguments;
import java.util.ArrayList;
import java.util.List;
import ObjectronExceptions.IllegalUseException;
import square.AbstractSquare;
import square.Direction;
import square.PropertyType;
import square.SquareContainer;

/**
 * This class represents a Force Field Generator. When placed, this item looks
 * for other active Force Field Generators in its neighbourhood to create a
 * {@link ForceField}.
 */
public class ForceFieldGenerator extends Item {
	
	@Override
	public void use(SquareContainer square, UseArguments<?> arguments) {
		if (squareContainsFFG(square))
			throw new IllegalUseException(
					"There can only be one force field generator on a square.");
		
		square.addItem(this);
		
		findAndCreateForceFields(square);
	}
	
	private boolean squareContainsFFG(SquareContainer square) {
		for (IItem item : square.getAllItems()) {
			if (item instanceof ForceFieldGenerator)
				return true;
		}
		return false;
	}
	
	/**
	 * This method will look in all directions for other Force Field Generators
	 * and create a {@link ForceField} between them.
	 * 
	 * @param square
	 *        The square this forcefield is on
	 */
	public void findAndCreateForceFields(SquareContainer square) {
		for (Direction direction : Direction.values()) {
			
			List<SquareContainer> listOfSquares = new ArrayList<SquareContainer>();
			SquareContainer neighbour = square;
			listOfSquares.add(neighbour);
			
			for (int i = 0; i < 3; i++) {
				neighbour = neighbour.getNeighbourIn(direction);
				
				if (neighbour != null)
					listOfSquares.add(neighbour);
				
				if (neighbour == null || neighbour.hasProperty(PropertyType.WALL))
					break;
				ForceFieldGenerator neighbouringGenerator = getForceFieldGeneratorOnSquare(neighbour);
				if (neighbouringGenerator != null) {
					if (square.hasPlayer() && !alreadyContainsForceField(listOfSquares)) {
						new ForceField(this, neighbouringGenerator, listOfSquares, true);
					}
					else if (!alreadyContainsForceField(listOfSquares)) {
						new ForceField(this, neighbouringGenerator, listOfSquares, false);
					}
				}
			}
			
		}
	}
	
	private boolean alreadyContainsForceField(List<SquareContainer> listOfSquares) {
		for (SquareContainer square : listOfSquares) {
			if (!square.hasProperty(PropertyType.FORCE_FIELD)) {
				return false;
			}
		}
		return true;
	}
	
	private ForceFieldGenerator getForceFieldGeneratorOnSquare(AbstractSquare neighbour) {
		for (IItem item : neighbour.getAllItems()) {
			if (item instanceof ForceFieldGenerator)
				return (ForceFieldGenerator) item;
		}
		return null;
	}
	
	@Override
	public boolean isCarriable() {
		return true;
	}
	
	@Override
	public String toString() {
		return "ForceFieldGenerator." + getId();
	}
}
