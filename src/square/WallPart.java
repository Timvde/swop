package square;

import item.IItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import player.Player;
import player.TronPlayer;
import effects.Effect;
import effects.EmptyEffect;

/**
 * A class that represents a part of a wall.
 */
public class WallPart extends AbstractSquare {
	
	/**
	 * Create a new wall part
	 */
	public WallPart() {}
	
	@Override
	public List<IItem> getCarryableItems() {
		return new ArrayList<IItem>();
	}
	
	@Override
	public Player getPlayer() {
		return null;
	}
	
	@Override
	public IItem pickupItem(int ID) {
		throw new UnsupportedOperationException("Walls do not contain items");
	}
	
	/**
	 * Always returns false; A wall cannot hold a {@link TronPlayer}.
	 */
	@Override
	public boolean hasPlayer() {
		return false;
	}
	
	@Override
	public boolean contains(Object object) {
		return false;
	}
	
	@Override
	public void addPlayer(Player p) {
		throw new UnsupportedOperationException("Players cannot be added to a wall!");
	}
	
	@Override
	public void addItem(IItem item) {
		throw new UnsupportedOperationException("items cannot be added to wall parts");
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
	protected void addPlayer(Player player, Effect effect) {
		this.addPlayer(player);
	}
	
	@Override
	protected void addItem(IItem item, Effect effect) {
		this.addItem(item);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// Wallparts don't have to do anything
	}

	@Override
	public boolean hasProperty(PropertyType property) {
		return property == PropertyType.WALL;
	}

	@Override
	protected Effect getStartTurnEffect(Effect effect) {
		return new EmptyEffect();
	} 
}
