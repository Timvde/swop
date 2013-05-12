package item.forcefieldgenerator;

import java.util.ArrayList;
import java.util.List;
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
	
	@Override
	public void use(SquareContainer square) throws CannotPlaceLightGrenadeException {
		square.addItem(this);
		
		findAndCreateForceFields(square);
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
				listOfSquares.add(neighbour);
				if (neighbour == null || neighbour.isWall())
					break;
				ForceFieldGenerator neighbouringGenerator = getForceFieldGeneratorOnSquare(neighbour);
				if (neighbouringGenerator != null) {
					new ForceField(this, neighbouringGenerator, listOfSquares);
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
}
