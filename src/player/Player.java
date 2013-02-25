package player;

import grid.Coordinate;
import item.Item;
import java.util.List;
import notnullcheckweaver.NotNull;

public class Player implements IPlayer {
	
	int			id;
	Coordinate	targetPosition;
	Inventory inventory = new Inventory();
	
	public Player(@NotNull int id, @NotNull Coordinate targetPosition) {
		this.id = id;
		this.targetPosition = targetPosition;
	}
	
	@Override
	public int getID() {
		return id;
	}
	
	@Override
	public Coordinate getTargetPosition() {
		return null;
	}
	
	@Override
	public List<Item> getInventory() {
		return inventory.getItems();
	}
	
}
