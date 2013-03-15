package grid;

import item.IItem;
import java.util.ArrayList;
import java.util.List;
import player.IPlayer;
import player.Player;

/**
 * A class that represents a part of a wall.
 */
public class WallPart extends ASquare {
	
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
	
	/**
	 * Always returns false; A wall cannot hold a {@link Player}.
	 */
	@Override
	public boolean hasPlayer() {
		return false;
	}
	
	@Override
	public boolean setPlayer(IPlayer p) {
		throw new IllegalStateException("Can not set a player on a wallpart!");
	}
	
	@Override
	public void removePlayer() {
		throw new IllegalStateException(
				"Can not remove a player on a wallpart! No players are supposed to be on it.");
	}
	
	@Override
	public boolean hasPowerFailure() {
		return false;
	}
	
	@Override
	void addPowerFailure(PowerFailure powerFailure) {
		throw new IllegalStateException("A WallPart cannot have a power failure.");
	}
	
	@Override
	void removePowerFailure(PowerFailure powerFailure) {
		throw new IllegalStateException("A WallPart does not have any power failures.");
	}
}
