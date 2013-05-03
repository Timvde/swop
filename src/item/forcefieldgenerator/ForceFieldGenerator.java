package item.forcefieldgenerator;

import square.AbstractSquare;
import square.Direction;
import square.SquareContainer;
import square.WallPart;
import ObjectronExceptions.CannotPlaceLightGrenadeException;
import item.Effect;
import item.EmptyEffect;
import item.IItem;
import item.Item;


public class ForceFieldGenerator extends Item {
	
	@Override
	public void use(AbstractSquare square) throws CannotPlaceLightGrenadeException {
		// Look for another force field generator in other squares nearby
		for (Direction direction : Direction.values()) {
			AbstractSquare neighbour = square;
			for (int i = 0; i < 3; i++) {
				neighbour = neighbour.getNeighbourIn(direction);
				if (neighbour instanceof WallPart)
					break;
				if (getForceFieldGeneratorOnSquare(neighbour) != null) {
					
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
