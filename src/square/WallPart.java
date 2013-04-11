package square;

import item.IItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import player.IPlayer;
import player.Player;

/**
 * A class that represents a part of a wall.
 */
public class WallPart extends ASquare {
	
	/**
	 * Create a new wall part 
	 * @param neighbours
	 * 	the neighbours of this square
	 */
	public WallPart(Map<Direction, ASquare> neighbours) {
		super(neighbours);
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
	public void removePlayer() {
		throw new UnsupportedOperationException(
				"Can not remove a player on a wallpart! No players are supposed to be on it.");
	}
	
	@Override
	public boolean hasPowerFailure() {
		return false;
	}
	
	@Override
	void addPowerFailure(PowerFailure powerFailure) {
		throw new UnsupportedOperationException("A WallPart cannot have a power failure.");
	}
	
	@Override
	void removePowerFailure(PowerFailure powerFailure) {
		throw new UnsupportedOperationException("A WallPart does not have any power failures.");
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
}
