package grid;

import item.IItem;
import java.util.ArrayList;
import java.util.List;
import player.IPlayer;

/**
 * A class that represents a part of a wall.
 */
public class WallPart extends ASquare {
	
	/**
	 * This constructor is package private, because only Walls can call it. This
	 * way, we prevent any walls from being inconsistent with each other.
	 */
	WallPart() {}
	
	@Override
	public List<IItem> getCarryableItems() {
		return new ArrayList<IItem>();
	}
	
	@Override
	public IPlayer getPlayer() {
		return null;
	}
	
	@Override
	public boolean hasLightTrail() {
		return false;
	}
	
	@Override
	public IItem pickupItem(int ID) throws IllegalArgumentException {
		throw new IllegalArgumentException("Walls do not contain items");
	}
	
	@Override
	public boolean hasItemWithID(int ID) {
		return false;
	}
}
