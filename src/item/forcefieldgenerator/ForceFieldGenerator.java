package item.forcefieldgenerator;

import java.util.ArrayList;
import java.util.List;
import properties.forcefield.ForceField;
import square.AbstractSquare;
import square.Direction;
import square.SquareContainer;
import ObjectronExceptions.CannotPlaceLightGrenadeException;
import item.Effect;
import item.EmptyEffect;
import item.IItem;
import item.Item;

/**
 * This class represents a Force Field Generator. When placed, this item looks
 * for other active Force Field Generators in its neighbourhood to create a
 * {@link ForceField}.
 */
public class ForceFieldGenerator extends Item {
	
	private List<ForceField> forceFields;
	
	/**
	 * Creates a Force Field Generator.
	 */
	public ForceFieldGenerator() {
		forceFields = new ArrayList<ForceField>();
	}
	
	@Override
	public void use(SquareContainer square) throws CannotPlaceLightGrenadeException {
		// Look for another force field generator in other squares nearby
		for (Direction direction : Direction.values()) {
			SquareContainer neighbour = square;
			List<SquareContainer> listOfSquares = new ArrayList<SquareContainer>();
			for (int i = 0; i < 3; i++) {
				neighbour = neighbour.getNeighbourIn(direction);
				listOfSquares.add(neighbour);
				if (neighbour.isWall())
					break;
				ForceFieldGenerator neighbouringGenerator = getForceFieldGeneratorOnSquare(neighbour);
				if (neighbouringGenerator != null) {
					
				}
			}
		}
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
	
	@Override
	public Effect getEffect() {
		return new EmptyEffect();
	}
	
	private void addForceField(ForceField forceField) {
		forceFields.add(forceField);
	}
	
}
