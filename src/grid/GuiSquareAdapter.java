package grid;

import item.IItem;
import item.forcefieldgenerator.ForceField;
import item.forcefieldgenerator.ForceFieldState;
import java.util.List;
import player.LightTrail;
import player.Player;
import powerfailure.PowerFailure;
import square.Property;
import square.PropertyType;
import square.SquareContainer;

/**
 * An adapter for square to provide methods for the GUI, this adapter 
 * will add methods as {@link #hasForceField()} and {@link #hasLightTrail()}. 
 * This will give the GUI a clean way to draw the grid without disturbing 
 * the internal interfaces. 
 */
public class GuiSquareAdapter implements GuiSquare {
	
	private SquareContainer	square;
	
	/**
	 * Create a new adapter for a specified square
	 * 
	 * @param square
	 *        the square that will be adapted
	 */
	public GuiSquareAdapter(SquareContainer square) {
		if (!isValidSquare(square))
			throw new IllegalArgumentException();
		this.square = square;
	}
	
	private boolean isValidSquare(SquareContainer square) {
		if (square == null)
			return false;
		else 
			return true;
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
	public boolean hasLightTrail() {
		for (Property property : square.getProperties())
			if (property instanceof LightTrail)
				return true;
		return false;
	}
	
	@Override
	public boolean hasPlayer() {
		return square.hasPlayer();
	}
	
	@Override
	public boolean hasPowerFailure() {
		for (Property property : square.getProperties())
			if (property instanceof PowerFailure)
				return true;
		return false;
	}
	
	@Override
	public boolean hasForceField() {
		for (Property property : square.getProperties())
			if (property instanceof ForceField && ((ForceField) property).getState() == ForceFieldState.ACTIVE)
				return true;
		return false;
	}
	
	@Override
	public boolean isWall() {
		return square.hasProperty(PropertyType.WALL);
	}
	
	@Override
	public boolean isStartingPosition() {
		return square.hasProperty(PropertyType.STARTING_POSITION);
	}
	
	@Override
	public boolean contains(Object object) {
		return square.contains(object);
	}

	@Override
	public List<IItem> getItems() {
		return square.getAllItems();
	}
	
}
