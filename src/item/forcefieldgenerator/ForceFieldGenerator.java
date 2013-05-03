package item.forcefieldgenerator;

import square.AbstractSquare;
import ObjectronExceptions.CannotPlaceLightGrenadeException;
import item.Effect;
import item.EmptyEffect;
import item.Item;


public class ForceFieldGenerator extends Item {
	
	@Override
	public void use(AbstractSquare square) throws CannotPlaceLightGrenadeException {
		
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
