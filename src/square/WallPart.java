package square;

import item.IItem;
import java.util.ArrayList;
import java.util.List;
import player.IPlayer;
import player.Player;

/**
 * A class that represents a part of a wall.
 */
public class WallPart extends AbstractSquare {
	
	/**
	 * Create a new wall part 
	 */
	public WallPart() {
	}
	
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
	public IItem pickupItem(int ID) {
		throw new UnsupportedOperationException("Walls do not contain items");
	}
	
	/**
	 * Always returns false; A wall cannot hold a {@link Player}.
	 */
	@Override
	public boolean hasPlayer() {
		return false;
	}
	
	@Override
	public boolean hasPowerFailure() {
		return false;
	}

	@Override
	public boolean contains(Object object) {
		return false;
	}

	@Override
	public void addPlayer(IPlayer p) {
		throw new UnsupportedOperationException("Players cannot be added to a wall!");
	}

	@Override
	public void placeLightTrail() {
		throw new UnsupportedOperationException("light trail cannot be placed on wallparts");
	}

	@Override
	public void removeLightTrail() {
		throw new UnsupportedOperationException("light trail cannot be placed on wallparts");
	}

	@Override
	public void addItem(IItem item) {
		throw new UnsupportedOperationException("items cannot be added to light trails");
	}

	@Override
	public void remove(Object object) {
		// there are no items on a wall part, so nothing is to be done
	}

	@Override
	public List<IItem> getAllItems() {
		return new ArrayList<IItem>();
	}
	
	@Override
	public boolean canBeAdded(IItem item) {
		return false;
	}
	
	@Override
	public boolean canAddPlayer() {
		return false;
	}
	
	@Override
	public String toString() {
		return "w ";
	}

	@Override
	public boolean isWall() {
		return true;
	}
}
